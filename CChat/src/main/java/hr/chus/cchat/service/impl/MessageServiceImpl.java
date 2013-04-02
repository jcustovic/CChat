package hr.chus.cchat.service.impl;

import hr.chus.cchat.db.service.SMSMessageService;
import hr.chus.cchat.db.service.ServiceProviderKeywordService;
import hr.chus.cchat.db.service.ServiceProviderService;
import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.gateway.GatewayResponseError;
import hr.chus.cchat.gateway.SendMessageService;
import hr.chus.cchat.model.db.jpa.Robot;
import hr.chus.cchat.model.db.jpa.SMSMessage;
import hr.chus.cchat.model.db.jpa.SMSMessage.DeliveryStatus;
import hr.chus.cchat.model.db.jpa.SMSMessage.Direction;
import hr.chus.cchat.model.db.jpa.ServiceProvider;
import hr.chus.cchat.model.db.jpa.ServiceProviderKeyword;
import hr.chus.cchat.model.db.jpa.User;
import hr.chus.cchat.service.MessageService;
import hr.chus.cchat.service.RobotService;

import java.io.IOException;
import java.util.Date;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Default implementation of {@link ReceiveSmsService}
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@Service
public class MessageServiceImpl implements MessageService {

    private static final Logger           LOG                  = LoggerFactory.getLogger(MessageServiceImpl.class);

    private static final Object           LOCK                 = new Object();

    private static final int              MAX_RECEIVE_SMS_SIZE = 160;

    @Autowired
    private ApplicationContext            applicationContext;

    @Autowired
    private ServiceProviderService        serviceProviderService;

    @Autowired
    private UserService                   userService;

    @Autowired
    private SMSMessageService             smsMessageService;

    @Autowired
    private ServiceProviderKeywordService serviceProviderKeywordService;

    @Autowired
    private RobotService                  robotService;

    @Qualifier("defaultSendMessageService")
    @Autowired
    private SendMessageService            defaultSendMessageService;

    @Override
    public final Integer[] receiveSms(final String p_serviceProviderName, final String p_sc, final String p_serviceProviderKeyword, final String p_msisdn,
                                      final String p_text, final Date p_date, final String p_gatewayId) {
        return receiveSms(p_serviceProviderName, p_sc, p_serviceProviderKeyword, p_msisdn, null, p_text, p_date, p_gatewayId, null, null);
    }

    @Override
    public Integer[] receiveSms(final String p_serviceProviderName, final String p_sc, final String p_serviceProviderKeyword, final String p_msisdn,
                                final String p_mccMnc, final String p_text, final Date p_date, final String p_gatewayId, final Float p_price,
                                final String p_currency) {
        final ServiceProvider serviceProvider = serviceProviderService.getByProviderNameAndShortCode(p_serviceProviderName, p_sc);
        if (serviceProvider == null) {
            throw new EntityNotFoundException("ServiceProvider not found for provider " + p_serviceProviderName + " and sc " + p_sc);
        }
        LOG.debug("Found service provider --> {}", serviceProvider);

        ServiceProviderKeyword providerKeyword = null;
        if (StringUtils.hasText(p_serviceProviderKeyword)) {
            Set<ServiceProviderKeyword> keywords = serviceProvider.getServiceProviderKeywords();
            if (keywords != null && !keywords.isEmpty()) {
                for (ServiceProviderKeyword keyword : keywords) {
                    if (p_serviceProviderKeyword.equalsIgnoreCase(keyword.getKeyword())) {
                        providerKeyword = keyword;
                    }
                }
            }
            if (providerKeyword == null) {
                providerKeyword = serviceProviderKeywordService.addOrEditServiceProviderKeyword(new ServiceProviderKeyword(serviceProvider,
                        p_serviceProviderKeyword, null));
                serviceProvider.getServiceProviderKeywords().add(providerKeyword);
                serviceProviderService.updateServiceProvider(serviceProvider);
            }
        }

        final String smsText;
        if (p_text == null) {
            smsText = "";
        } else {
            smsText = p_text;
        }

        final int smsCount = (int) Math.ceil(smsText.length() * 1f / MAX_RECEIVE_SMS_SIZE);
        User user;
        synchronized (LOCK) {
            user = getOrCreateUser(p_msisdn, serviceProvider, p_mccMnc, smsCount);
        }

        // We will split message if its length is more than 160 chars.
        if (smsCount > 1) {
            LOG.debug("Received sms with length {} (max {}). Will split message ({} parts)...",
                    new Object[] { smsText.length(), MAX_RECEIVE_SMS_SIZE, smsCount });
        }

        final Integer[] msgIds = new Integer[smsCount];
        for (int i = 0; i < smsCount; i++) {
            final int currentIndex = MAX_RECEIVE_SMS_SIZE * i;
            final String textToSave = smsText.substring(currentIndex, currentIndex + Math.min(MAX_RECEIVE_SMS_SIZE, smsText.length() - currentIndex));
            final SMSMessage message = new SMSMessage(user, null, p_date, textToSave, p_sc, serviceProvider, Direction.IN);
            message.setDeliveryStatus(DeliveryStatus.RECEIVED);
            message.setGatewayId(p_gatewayId);
            message.setServiceProviderKeyword(providerKeyword);
            message.setEndUserPrice(p_price);
            message.setEndUserPriceCurrency(p_currency);

            msgIds[i] = smsMessageService.updateSMSMessage(message).getId();
        }

        return msgIds;
    }

    private User getOrCreateUser(final String p_msisdn, final ServiceProvider p_serviceProvider, final String p_mccMnc, final int p_smsCount) {
        // Find or create user. Must be thread safe.
        User user = userService.getByMsisdnAndServiceName(p_msisdn, p_serviceProvider.getServiceName(), false);

        if (user == null) {
            user = new User(p_msisdn, p_serviceProvider);
            user.setMccMnc(p_mccMnc);
            user.setUnreadMsgCount(1);
            assignOperator(user);
            // Default bot should have ID 1
            final Robot bot = robotService.findOne(1);
            if (bot != null) {
                LOG.debug("Assigning default bot {} to new user {}", bot.getName(), user.getMsisdn());
                user.setBot(bot);
            } else {
                LOG.debug("No default bot with ID 1 in database");
            }

            user = userService.editUser(user);
            LOG.info("New user ({}) registred to service {}", user, p_serviceProvider.getServiceName());
        } else {
            user.setUnreadMsgCount(user.getUnreadMsgCount() + p_smsCount);
            user.setLastMsg(new Date());

            if (!user.getServiceProvider().equals(p_serviceProvider)) {
                LOG.info("User (" + user + ") changed service provider from " + user.getServiceProvider() + " to " + p_serviceProvider);
                user.setServiceProvider(p_serviceProvider);
            }
            assignOperator(user);

            userService.editUser(user);
        }

        return user;
    }

    @Override
    public final SMSMessage sendMessage(final SMSMessage p_smsMessage, final Boolean p_botResponse, final User p_user, final String p_msgType)
            throws HttpException, IOException, GatewayResponseError {
        String gatewayId = null;
        final SendMessageService sendMessageService;
        if (StringUtils.hasText(p_user.getServiceProvider().getSendServiceBeanName())) {
            sendMessageService = (SendMessageService) applicationContext.getBean(p_user.getServiceProvider().getSendServiceBeanName());
        } else {
            sendMessageService = defaultSendMessageService;
        }

        LOG.debug("Sending message: {}", p_smsMessage);
        if ("wapPush".equals(p_msgType)) {
            gatewayId = sendMessageService.sendWapPushMessage(p_smsMessage);
        } else {
            gatewayId = sendMessageService.sendSmsMessage(p_smsMessage);
        }

        LOG.debug("Sending done. Message gateway id: {}", gatewayId);
        p_smsMessage.setGatewayId(gatewayId);
        p_smsMessage.setDeliveryStatus(DeliveryStatus.SENT_TO_GATEWAY);
        p_smsMessage.setBotResponse(Boolean.TRUE.equals(p_botResponse));

        return smsMessageService.updateSMSMessage(p_smsMessage);
    }

    private void assignOperator(final User p_user) {
        // NOTE: UnreadMsgAssignerScheduler will do this for us.
        // final Operator operator = p_user.getOperator();
        // if (operator == null || !operator.getIsActive()) {
        //     p_user.setOperator(operatorChooser.chooseOperator());
        // }
    }

}
