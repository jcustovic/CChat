package hr.chus.cchat.service;

import java.util.List;

import hr.chus.cchat.model.db.jpa.Robot;

/**
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public interface RobotService {

    Robot findByName(String p_name);
    
    List<Robot> findAll();
    
    List<Robot> findByOnline(Boolean p_online);

    Robot findOne(Integer p_id);

    String responde(String text, Integer userId);

}
