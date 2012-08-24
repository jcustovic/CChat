package hr.chus.cchat.service.impl;

import hr.chus.cchat.db.service.SMSMessageService;
import hr.chus.cchat.db.service.ServiceProviderKeywordService;
import hr.chus.cchat.db.service.ServiceProviderService;
import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.gateway.SendMessageService;
import hr.chus.cchat.model.db.jpa.SMSMessage;
import hr.chus.cchat.model.db.jpa.SMSMessage.DeliveryStatus;
import hr.chus.cchat.model.db.jpa.SMSMessage.Direction;
import hr.chus.cchat.model.db.jpa.ServiceProvider;
import hr.chus.cchat.model.db.jpa.ServiceProviderKeyword;
import hr.chus.cchat.model.db.jpa.User;
import hr.chus.cchat.service.MessageService;

import java.util.Date;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link ReceiveSmsService}
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    private static final Logger           LOG = LoggerFactory.getLogger(MessageServiceImpl.class);

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
    private SendMessageService            defaultSendMessageService;

    @Override
    public final Integer receiveSms(final String p_serviceProviderName, final String p_sc, final String p_serviceProviderKeyword, final String p_msisdn,
                                    final String p_text, final Date p_date, final String p_gatewayId) {
        final ServiceProvider serviceProvider = serviceProviderService.getByProviderNameAndShortCode(p_serviceProviderName, p_sc);
        if (serviceProvider == null) {
            throw new EntityNotFoundException("ServiceProvider not found for provider " + p_serviceProviderName + " and sc " + p_sc);
        }

        ServiceProviderKeyword providerKeyword = null;
        if (p_serviceProviderKeyword != null && !p_serviceProviderKeyword.isEmpty()) {
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

        User user = userService.getByMsisdnAndServiceName(p_msisdn, serviceProvider.getServiceName(), false);
        if (user == null) {
            user = new User(p_msisdn, serviceProvider);
            user.setUnreadMsgCount(1);
            assignOperator(user);

            user = userService.editUser(user);
            LOG.info("New user ({}) registred to service {}", user, serviceProvider.getServiceName());
        } else {
            user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);
            user.setLastMsg(new Date());

            if (!user.getServiceProvider().equals(serviceProvider)) {
                LOG.info("User (" + user + ") changed service provider from " + user.getServiceProvider() + " to " + serviceProvider);
                user.setServiceProvider(serviceProvider);
            }
            assignOperator(user);

            userService.editUser(user);
        }

        final SMSMessage message = new SMSMessage(user, null, p_date, p_text, p_sc, serviceProvider, Direction.IN);
        message.setDeliveryStatus(DeliveryStatus.RECEIVED);
        message.setGatewayId(p_gatewayId);
        message.setServiceProviderKeyword(providerKeyword);

        return smsMessageService.updateSMSMessage(message).getId();
    }

    @Override
    public final SMSMessage sendMessage(final SMSMessage p_smsMessage, final User p_user, final String p_msgType) throws Exception {
        String gatewayId = null;
        final SendMessageService sendMessageService;
        if (p_user.getServiceProvider().getSendServiceBeanName() != null && !p_user.getServiceProvider().getSendServiceBeanName().isEmpty()) {
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

        return smsMessageService.updateSMSMessage(p_smsMessage);
    }

    private void assignOperator(final User p_user) {
        // NOTE: UnreadMsgAssignerScheduler will do this for us.
        //        final Operator operator = p_user.getOperator();
        //        if (operator == null || !operator.getIsActive()) {
        //            p_user.setOperator(operatorChooser.chooseOperator());
        //        }
    }

}
