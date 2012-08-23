package hr.chus.cchat.struts2.action.admin;

import hr.chus.cchat.db.service.RoleService;
import hr.chus.cchat.model.db.jpa.Role;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that returns all roles.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class AdminRoleList extends ActionSupport {

    @Autowired
    private RoleService roleService;

    private List<Role>  roleList;

    @Override
    public String execute() throws Exception {
        roleList = roleService.getAll();

        return SUCCESS;
    }

    // Getters & setters

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

}
