package hr.chus.cchat.db.repository;

import java.util.List;

import hr.chus.cchat.model.db.jpa.Robot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@Transactional(readOnly = true)
public interface RobotRepository extends JpaRepository<Robot, Integer> {

    Robot findByName(String p_name);
    
    List<Robot> findByOnline(Boolean p_online);

}
