package hr.chus.cchat.service.impl;

import hr.chus.cchat.db.repository.RobotRepository;
import hr.chus.cchat.db.repository.UserRepository;
import hr.chus.cchat.model.db.jpa.Robot;
import hr.chus.cchat.model.db.jpa.User;
import hr.chus.cchat.service.RobotService;

import java.util.List;

import org.aitools.programd.Core;
import org.aitools.programd.bot.Bot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@Service
public class RobotServiceImpl implements RobotService {

    private static final Logger       LOG = LoggerFactory.getLogger(RobotServiceImpl.class);

    @Autowired
    private transient RobotRepository robotRepository;

    @Autowired
    private transient Core            core;

    @Autowired
    private transient UserRepository  userRepository;

    @Override
    public final Robot findByName(final String p_name) {
        return robotRepository.findByName(p_name);
    }

    @Override
    public final List<Robot> findAll() {
        return robotRepository.findAll();
    }

    @Override
    public final List<Robot> findByOnline(final Boolean p_online) {
        return robotRepository.findByOnline(p_online);
    }

    @Override
    public final Robot findOne(final Integer p_id) {
        return robotRepository.findOne(p_id);
    }

    @Override
    public final String responde(final String p_text, Integer p_userId) {
        final User user = userRepository.findOne(p_userId);
        if (user.getBot() != null && user.getBot().getOnline()) {
            final Bot bot = core.getBot(user.getBot().getName());
            if (bot == null) {
                LOG.warn("Bot with name {} not registered!", user.getBot().getName());
            } else {
                return core.getResponse(p_text, user.getId().toString(), bot.getID());
            }
        }

        return "";
    }

}
