package hr.chus.cchat.struts2.action.admin;

import hr.chus.cchat.db.service.RoleService;
import hr.chus.cchat.model.db.jpa.Role;

import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class AdminRoleList extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	
	private RoleService roleService;
	private List<Role> roleList;
	
	@Override
	public String execute() throws Exception {
		roleList = roleService.getAll();
		return SUCCESS;
	}

	// Getters & setters
	
	public void setRoleService(RoleService roleService) { this.roleService = roleService; }
	
	public List<Role> getRoleList() { return roleList; }
	public void setRoleList(List<Role> roleList) { this.roleList = roleList; }
		
}
