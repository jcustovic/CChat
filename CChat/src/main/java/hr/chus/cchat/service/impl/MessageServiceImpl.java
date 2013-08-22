package hr.chus.cchat.service.impl;

import hr.chus.cchat.db.service.SMSMessageService;
import hr.chus.cchat.db.service.ServiceProviderKeywordService;
import hr.chus.cchat.db.service.ServiceProviderService;
import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.exception.LanguageNotFound;
import hr.chus.cchat.gateway.GatewayResponseError;
import hr.chus.cchat.gateway.SendMessageService;
import hr.chus.cchat.model.db.jpa.Language;
import hr.chus.cchat.model.db.jpa.LanguageProvider;
import hr.chus.cchat.model.db.jpa.Robot;
import hr.chus.cchat.model.db.jpa.SMSMessage;
import hr.chus.cchat.model.db.jpa.SMSMessage.DeliveryStatus;
import hr.chus.cchat.model.db.jpa.SMSMessage.Direction;
import hr.chus.cchat.model.db.jpa.ServiceProvider;
import hr.chus.cchat.model.db.jpa.ServiceProviderKeyword;
import hr.chus.cchat.model.db.jpa.User;
import hr.chus.cchat.service.LanguageProviderService;
import hr.chus.cchat.service.MessageService;
import hr.chus.cchat.service.RobotService;

import java.io.IOException;
import java.util.Date;
import java.util.Set;

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

    private static final Logger           LOG                    = LoggerFactory.getLogger(MessageServiceImpl.class);

    private static final Object           USER_LOCK              = new Object();
    private static final Object           SP_LOCK                = new Object();

    public static final int               SMS_UNICODE_MAX_LENGTH = 70;
    public static final int               SMS_ASCII_MAX_LENGTH   = 160;

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

    @Autowired
    private LanguageProviderService       languageProviderService;

    @Override
    public final Integer[] receiveSms(final String p_serviceProviderName, final String p_sc, final String p_serviceProviderKeyword, final String p_msisdn,
                                      final String p_text, final Date p_date, final String p_gatewayId) {
        return receiveSms(p_serviceProviderName, p_sc, p_serviceProviderKeyword, p_msisdn, null, p_text, p_date, p_gatewayId, null, null);
    }

    @Override
    public Integer[] receiveSms(final String p_serviceProviderName, final String p_sc, final String p_serviceProviderKeyword, final String p_msisdn,
                                final String p_mccMnc, final String p_text, final Date p_date, final String p_gatewayId, final Float p_price,
                                final String p_currency) {
        final String msisdn = p_msisdn.trim();
        ServiceProvider serviceProvider;

        // 1. Try to find service provider
        LOG.debug("Searching for service provider with name {} and sc {}.", p_serviceProviderName, p_sc);
        serviceProvider = serviceProviderService.getByProviderNameAndShortCode(p_serviceProviderName, p_sc);

        // 2. Match language
        final LanguageProvider matchedLanguageProvider = languageProviderService.findBestMatchByPrefix(msisdn);
        if (matchedLanguageProvider == null) {
            throw new LanguageNotFound("Language not found for msisdn " + msisdn + " --> provider " + p_serviceProviderName + " and sc " + p_sc);
        }
        LOG.debug("MSISDN {} matched {} language provider", msisdn, matchedLanguageProvider.getLanguage().getName());

        // 3. If service provider not found create new service provider with specific language
        if (serviceProvider == null) {
            LOG.debug("Service provider not found. Auto creating new service provider.");
            serviceProvider = getProviderByMatchedLanguageProvider(p_serviceProviderName, p_sc, msisdn, matchedLanguageProvider);
        }

        LOG.debug("Using service provider --> {}", serviceProvider);

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

        final int smsCount;
        if (smsText.isEmpty()) {
            smsCount = 1;
        } else {
            // TODO: Maybe respect ASCII or UNITCODE length
            smsCount = (int) Math.ceil(smsText.length() * 1f / SMS_ASCII_MAX_LENGTH);
        }
        User user;
        synchronized (USER_LOCK) {
            user = getOrCreateUser(msisdn, serviceProvider, matchedLanguageProvider, p_mccMnc, smsCount);
        }

        // We will split message if its length is more than 160 chars.
        if (smsCount > 1) {
            LOG.debug("Received sms with length {} (max {}). Will split message ({} parts)...",
                    new Object[] { smsText.length(), SMS_ASCII_MAX_LENGTH, smsCount });
        }
        
        final SMSMessage responseTo = smsMessageService.getLastSentMessage(user);

        final Integer[] msgIds = new Integer[smsCount];
        for (int i = 0; i < smsCount; i++) {
            final int currentIndex = SMS_ASCII_MAX_LENGTH * i;
            final String textToSave = smsText.substring(currentIndex, currentIndex + Math.min(SMS_ASCII_MAX_LENGTH, smsText.length() - currentIndex));
            final SMSMessage message = new SMSMessage(user, null, p_date, textToSave, p_sc, serviceProvider, Direction.IN);
            message.setDeliveryStatus(DeliveryStatus.RECEIVED);
            message.setGatewayId(p_gatewayId);
            message.setServiceProviderKeyword(providerKeyword);
            message.setEndUserPrice(p_price);
            message.setEndUserPriceCurrency(p_currency);
            message.setResponseTo(responseTo);

            msgIds[i] = smsMessageService.updateSMSMessage(message).getId();
        }

        return msgIds;
    }

    private ServiceProvider getProviderByMatchedLanguageProvider(final String p_serviceProviderName, final String p_sc, final String p_msisdn,
                                                                 final LanguageProvider p_matchedLanguageProvider) {
        final Language language = p_matchedLanguageProvider.getLanguage();
        LOG.debug("Best matched language for msisdn {} is {} (prefix: {}, sendBean: {})", new Object[] { p_msisdn, language.getShortCode(),
                p_matchedLanguageProvider.getPrefix(), p_matchedLanguageProvider.getSendServiceBeanName() });

        final String serviceProviderName = p_serviceProviderName + "_" + p_matchedLanguageProvider.getId();
        synchronized (SP_LOCK) {
            ServiceProvider serviceProvider = serviceProviderService.findByProviderNameAndShortCodeAndProviderLanguage(serviceProviderName, p_sc,
                    p_matchedLanguageProvider);
            if (serviceProvider == null) {
                LOG.debug("Service provider not found for provider name {}, sc {} and language {}. Will automatically create...", new Object[] {
                        serviceProviderName, p_sc, language.getShortCode() });
                // TODO: Is this the correct way to provide serviceName?
                serviceProvider = new ServiceProvider(p_sc, serviceProviderName, "LANG_PROVIDER_" + p_matchedLanguageProvider.getId(), "Language provider "
                        + language.getShortCode(), false);
                serviceProvider.setAutoCreated(true);
                serviceProvider.setLanguageProvider(p_matchedLanguageProvider);

                serviceProviderService.addServiceProvider(serviceProvider);
            }

            return serviceProvider;
        }
    }

    private User getOrCreateUser(final String p_msisdn, final ServiceProvider p_serviceProvider, final LanguageProvider p_languageProvider,
                                 final String p_mccMnc, final int p_smsCount) {
        // Find or create user. Must be thread safe.
        User user = userService.getByMsisdnAndServiceName(p_msisdn, p_serviceProvider.getServiceName(), false);

        if (user == null) {
            user = new User(p_msisdn, p_serviceProvider);
            user.setLanguageProvider(p_languageProvider);
            user.setMccMnc(p_mccMnc);
            user.setUnreadMsgCount(1);
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
            // We set language provider if it changes
            user.setLanguageProvider(p_languageProvider);

            if (!user.getServiceProvider().equals(p_serviceProvider)) {
                LOG.info("User (" + user + ") changed service provider from " + user.getServiceProvider() + " to " + p_serviceProvider);
                user.setServiceProvider(p_serviceProvider);
            }

            userService.editUser(user);
        }

        return user;
    }

    @Override
    public final SMSMessage sendMessage(final SMSMessage p_smsMessage, final Boolean p_botResponse, final User p_user, final String p_msgType)
            throws HttpException, IOException, GatewayResponseError {
        final ServiceProvider serviceProvider = p_user.getServiceProvider();
        
        String senderBeanName = null;
        if (serviceProvider.getLanguageProvider() != null && serviceProvider.getLanguageProvider().getSendServiceBeanName() != null) {
            senderBeanName = serviceProvider.getLanguageProvider().getSendServiceBeanName();
        } else if (StringUtils.hasText(serviceProvider.getSendServiceBeanName())) {
            senderBeanName = serviceProvider.getSendServiceBeanName();
        }
        
        final SendMessageService sendMessageService;
        if (senderBeanName == null) {
            sendMessageService = defaultSendMessageService;
        } else {
            sendMessageService = (SendMessageService) applicationContext.getBean(senderBeanName);
        }

        LOG.debug("Sending message: {}", p_smsMessage);
        String gatewayId = null;
        if ("wapPush".equals(p_msgType)) {
            gatewayId = sendMessageService.sendWapPushMessage(p_smsMessage);
        } else {
            gatewayId = sendMessageService.sendSmsMessage(p_smsMessage);
        }

        LOG.debug("Sending done. Message gateway id: {}", gatewayId);
        p_smsMessage.setUsedBean(senderBeanName);
        p_smsMessage.setGatewayId(gatewayId);
        p_smsMessage.setDeliveryStatus(DeliveryStatus.SENT_TO_GATEWAY);
        p_smsMessage.setBotResponse(Boolean.TRUE.equals(p_botResponse));

        return smsMessageService.updateSMSMessage(p_smsMessage);
    }

}
