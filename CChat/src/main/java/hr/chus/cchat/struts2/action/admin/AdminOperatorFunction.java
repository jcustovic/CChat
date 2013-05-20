package hr.chus.cchat.struts2.action.admin;

import hr.chus.cchat.db.service.OperatorService;
import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.listener.SessionListener;
import hr.chus.cchat.model.db.jpa.Operator;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.util.StringUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that implements operator functions. Thru this web service operators can be created,
 * updated or deleted (dao services are invoked).
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class AdminOperatorFunction extends ActionSupport {

    private static final Logger          LOG = LoggerFactory.getLogger(AdminOperatorFunction.class);

    @Autowired
    private OperatorService              operatorService;

    @Autowired
    private UserService                  userService;

    @Autowired
    private transient ShaPasswordEncoder shaPasswordEncoder;

    private Operator                     operator;
    private List<Integer>                languages;
    private String                       operation;
    private Map<String, String>          errorFields;
    private String                       status;

    @Override
    public String execute() throws Exception {
        if ("save/edit".equals(operation)) {
            if (operator.getId() != null) {
                // Update user
                if (operator.getDisabled()) {
                    operator.setIsActive(false);
                    SessionListener.removeSessionWithUser(operator);
                    userService.clearOperatorField(operator);
                } else {
                    SessionListener.updateSessionWithUser(operator);
                }
            }
            operator = operatorService.save(operator, languages);
        } else if ("delete".equals(operation)) {
            if ("admin".equals(operator.getUsername())) {
                errorFields = new LinkedHashMap<String, String>();
                errorFields.put("operator.username", getText("operator.canNotBeDeleted"));
                status = "validation_error";

                return INPUT;
            }
            SessionListener.removeSessionWithUser(operator);
            operator = operatorService.getOperatorById(operator.getId());
            try {
                operatorService.removeOperator(operator);
            } catch (DataIntegrityViolationException e) {
                errorFields = new LinkedHashMap<String, String>();
                errorFields.put("operator.username", getText("operator.deleteNotAllowed.linkedToMessages"));
                status = "validation_error";

                return INPUT;
            }
        }

        return SUCCESS;
    }

    @Override
    public void validate() {
        errorFields = new LinkedHashMap<String, String>();
        if (operator == null) {
            errorFields.put("operator", getText("operator.null"));
        } else if (!StringUtils.hasText(operation)) {
            LOG.warn("Operation must not be null.");
            errorFields.put("operation", getText("operation.empty"));
        } else if (!("save/edit".equals(operation) || "delete".equals(operation))) {
            LOG.warn("Unsupported operation: " + operation);
            errorFields.put("operation", getText("operation.notSupported"));
        } else if ("save/edit".equals(operation)) {
            if (operator.getDisabled() == null) operator.setDisabled(false);
            if (operator.getIsActive() == null) operator.setIsActive(false);

            if (!StringUtils.hasText(operator.getUsername())) {
                errorFields.put("operator.username", getText("operator.username.empty"));
            } else if (operator.getUsername().length() > 30) {
                errorFields.put("operator.username", getText("operator.username.toLong", new String[] { "30" }));
            } else if (operatorService.checkIfUsernameExists(operator)) {
                errorFields.put("operator.username", getText("operator.username.exists"));
            } else if (operator.getUsername().equals("admin") && operator.getDisabled()) {
                errorFields.put("operator.disabled", getText("operator.admin.cantBeDisabled"));
            }

            if (operator.getRole() == null) {
                errorFields.put("operator.role", getText("operator.role.empty"));
            }

            if (operator.getId() == null && operator.getPassword() != null && operator.getPassword().length() > 15) {
                errorFields.put("operator.password", getText("operator.password.toLong", new String[] { "15" }));
            }

            if (operator.getName() != null && operator.getName().length() > 20) {
                errorFields.put("operator.name", getText("operator.name.toLong", new String[] { "20" }));
            }

            if (operator.getSurname() != null && operator.getSurname().length() > 30) {
                errorFields.put("operator.surname", getText("operator.surname.toLong", new String[] { "30" }));
            }

            if (operator.getEmail() != null && operator.getEmail().length() > 50) {
                errorFields.put("operator.email", getText("operator.email.toLong", new String[] { "50" }));
            }
        }

        if (operator.getId() != null) {
            String currentPassword = operatorService.getOperatorById(operator.getId()).getPassword();
            if (operator.getPassword() != null && !operator.getPassword().equals(currentPassword)) {
                operator.setPassword(shaPasswordEncoder.encodePassword(operator.getPassword(), null));
            } else {
                operator.setPassword(currentPassword);
            }
        } else {
            if (operator.getPassword() == null || operator.getPassword().length() == 0) {
                errorFields.put("operator.password", getText("operator.password.empty"));
            } else {
                operator.setPassword(shaPasswordEncoder.encodePassword(operator.getPassword(), null));
            }
        }

        if (errorFields.size() == 0) {
            errorFields = null;
            status = "validation_ok";
        } else {
            addActionError(errorFields.size() + " errors found!");
            status = "validation_error";
        }
    }

    // Getters & setters

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(final Operator p_operator) {
        operator = p_operator;
    }

    public final void setLanguages(final List<Integer> p_languages) {
        languages = p_languages;
    }

    public void setOperation(final String p_operation) {
        operation = p_operation;
    }

    public Map<String, String> getErrorFields() {
        return errorFields;
    }

    public void setErrorFields(final Map<String, String> p_errorFields) {
        errorFields = p_errorFields;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String p_status) {
        status = p_status;
    }

}
