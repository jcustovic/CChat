package hr.chus.cchat.db.service;

import java.util.Date;
import java.util.List;

import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.SMSMessage;
import hr.chus.cchat.model.db.jpa.SMSMessage.Direction;
import hr.chus.cchat.model.db.jpa.ServiceProvider;
import hr.chus.cchat.model.helper.db.Conversation;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public interface SMSMessageService {
	
	public void addSMSMessage(SMSMessage smsMessage);
	public void removeSMSMessage(SMSMessage smsMessage);
	public SMSMessage updateSMSMessage(SMSMessage smsMessage);
	public SMSMessage getById(Integer id);
	public Object[] search(Operator operator, ServiceProvider serviceProvider, Direction direction, Integer userId, String userName, String userSurname, String msisdn, Date startDate, Date endDate, String text, int start, int limit);
	public List<Conversation> getConversationByUserId(Integer userId, int start, int limit);

}
