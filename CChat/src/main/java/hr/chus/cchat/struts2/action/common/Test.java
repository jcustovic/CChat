package hr.chus.cchat.struts2.action.common;

import java.util.Date;

import hr.chus.cchat.db.service.SMSMessageService;
import hr.chus.cchat.db.service.ServiceProviderService;
import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.model.db.jpa.SMSMessage;
import hr.chus.cchat.model.db.jpa.SMSMessage.Direction;
import hr.chus.cchat.model.db.jpa.ServiceProvider;
import hr.chus.cchat.model.db.jpa.User;

import javax.print.attribute.standard.DateTimeAtCompleted;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action used for testing.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class Test extends ActionSupport implements ServletRequestAware {

    private static final Logger    LOG = LoggerFactory.getLogger(Test.class);

    @Autowired
    private UserService            userService;

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private SMSMessageService      smsMessageService;

    private HttpServletRequest     request;
    private String                 test;

    @Override
    public String execute() throws Exception {
        LOG.info(request.getCharacterEncoding());
        LOG.info(test);
        
//        final ServiceProvider serviceProvider = new ServiceProvider("00000", "testProvider", "testService", "", false);
//        serviceProviderService.addServiceProvider(serviceProvider);
//        
//        for (int i = 0; i < 1000; i++) {
//            final User user = new User(String.valueOf(i), serviceProvider);
//            userService.addUser(user);
//            
//            LOG.info("Test user " + i);
//            for (int j = 0; j < 100; j++) {
//                final SMSMessage sms = new SMSMessage(user, null, new Date(), "User " + i + " msg " + j, "00000", serviceProvider, Direction.IN);
//                smsMessageService.addSMSMessage(sms);
//            }
//        }

        return SUCCESS;
    }

    // Getters & setters

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

}
