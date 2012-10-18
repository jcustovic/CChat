package hr.chus.cchat.struts2.action.admin;

import hr.chus.cchat.helper.UserAware;
import hr.chus.cchat.model.db.jpa.Operator;

import org.aitools.programd.Core;
import org.aitools.programd.bot.Bot;
import org.aitools.programd.util.DeveloperError;
import org.aitools.programd.util.XMLKit;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class TalkToBot extends ActionSupport implements UserAware {

    @Autowired
    private transient Core core;

    private Operator       currentUser;

    private String         message;
    private String         botId;
    private String         response;

    @Override
    public final String execute() throws Exception {
        try {
            final Bot bot = core.getBot(botId);
            response = core.getResponse(message, currentUser.getUsername(), bot.getID());
            response = XMLKit.filterWhitespace(XMLKit.removeMarkup(response));
        } catch (DeveloperError e) {
            response = "WARN --> Bot with id " + botId + " not found!";
        }

        return SUCCESS;
    }

    // Getters & setters

    public final String getResponse() {
        return response;
    }

    public final void setMessage(final String p_message) {
        message = p_message;
    }

    public final void setBotId(String p_botId) {
        botId = p_botId;
    }

    @Override
    public void setAuthenticatedUser(final Operator p_user) {
        currentUser = p_user;
    }

}
