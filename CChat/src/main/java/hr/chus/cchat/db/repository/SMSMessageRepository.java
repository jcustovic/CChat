package hr.chus.cchat.db.repository;

import hr.chus.cchat.model.db.jpa.SMSMessage;
import hr.chus.cchat.model.db.jpa.SMSMessage.Direction;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@Transactional(readOnly = true)
public interface SMSMessageRepository extends JpaRepository<SMSMessage, Integer> {

    @Query("SELECT sms FROM SMSMessage sms WHERE sms.user.id = ?1 AND sms.direction = ?2 ORDER BY sms.time DESC")
    List<SMSMessage> findByUserAndDirection(Integer p_userId, Direction p_direction, Pageable p_page);

}
