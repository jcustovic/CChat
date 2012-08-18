package hr.chus.cchat.model.helper.db;

import hr.chus.cchat.model.db.jpa.SMSMessage.Direction;

import java.io.Serializable;
import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

/**
 * This class is a helper class for SMS messages and are just a different conversation view of SMS messages
 * with mobile user.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class Conversation implements Serializable {

    private Integer   messageId;
    private Date      time;
    private String    text;
    private String    operatorUsername;
    private Direction direction;

    public Conversation(Integer messageId, Date time, String text, String operatorUsername, Direction direction) {
        this.messageId = messageId;
        this.time = time;
        this.text = text;
        this.operatorUsername = operatorUsername;
        this.direction = direction;
    }

    // Getters & setters

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    @JSON(format = "yyyy-MM-dd'T'HH:mm:ssZ")
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getOperatorUsername() {
        return operatorUsername;
    }

    public void setOperatorUsername(String operatorUsername) {
        this.operatorUsername = operatorUsername;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

}
