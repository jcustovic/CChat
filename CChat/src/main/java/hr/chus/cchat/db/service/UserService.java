package hr.chus.cchat.db.service;

import java.util.Date;
import java.util.List;

import hr.chus.cchat.model.db.jpa.Nick;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.Picture;
import hr.chus.cchat.model.db.jpa.ServiceProvider;
import hr.chus.cchat.model.db.jpa.User;

/**
 * User services that DAO needs to implement.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public interface UserService {

    void addUser(User p_user);

    User editUser(User p_user);

    User editUserAdmin(User p_user);

    User editUserOperator(User p_user);

    void deleteUser(User p_user);

    User getUserById(Integer p_id, boolean p_loadSentPictures);

    User getByMsisdnAndServiceName(String p_msisdn, String p_serviceName, boolean p_loadSentPictures);

    Object[] searchUsers(Nick p_nick, Operator p_operator, ServiceProvider p_serviceProvider, String p_msisdn, Integer p_id, String p_name, String p_surname,
                         Boolean p_deleted, int p_start, int p_limit);

    Long getCount();

    List<User> getByOperator(Operator p_operator);

    List<User> getRandom(Date p_lastMsgDate, int p_count);

    List<User> getNewest(Date p_lastMsgDate, int p_count);

    void clearOperatorField(Operator p_operator);

    void clearOperatorField(Operator p_operator, Date p_date);

    void assignUsersWithNewMsgToOperator(Operator p_operator);

    void updateAllMessagesRead(Integer p_userId);

    List<Picture> getSentPictureList(Integer p_userId);

    void addPicture(Integer p_userId, Picture p_picture);

    Long countByOperatorAndUnread(Operator p_operator);

    List<User> findUnassigned(int p_count);

}
