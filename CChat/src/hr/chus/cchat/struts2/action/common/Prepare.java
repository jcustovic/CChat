package hr.chus.cchat.struts2.action.common;

import hr.chus.cchat.db.service.OperatorService;
import hr.chus.cchat.db.service.RoleService;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.Role;
import hr.chus.cchat.util.StringUtil;

import com.opensymphony.xwork2.ActionSupport;

public class Prepare extends ActionSupport  {
	
	private static final long serialVersionUID = 1L;

	private OperatorService operatorService;
	private RoleService roleService;
	private String message;
	
	@Override
	public String execute() {
		try {
	//		developPrepare();
			productionPrepare();
		} catch (Exception e) {
			message = e.getMessage();
		}
		if (message == null) {
			message = getText("prepare.success");
		}
		return SUCCESS;
	}
	

	private void productionPrepare() {		
		Role admin = new Role();
		admin.setName("admin");
		admin.setDescription("Tip korisnika za administratore");
		roleService.addRole(admin);
		
		Role moderator = new Role();
		moderator.setName("moderator");
		moderator.setDescription("Tip korisnika za moderatore");
		roleService.addRole(moderator);
		
		Role operator = new Role();
		operator.setName("operator");
		operator.setDescription("Tip korisnika za operatore");
		roleService.addRole(operator);
		
		Operator adminUser = new Operator();
		adminUser.setUsername("admin");
		adminUser.setPassword(StringUtil.encodePassword("admin", "SHA"));
		adminUser.setRole(admin);
		adminUser.setIsActive(false);
		adminUser.setDisabled(false);
		operatorService.addOperator(adminUser);
		
		Operator user = new Operator();
		user.setUsername("user");
		user.setPassword(StringUtil.encodePassword("user", "SHA"));
		user.setRole(operator);
		user.setIsActive(false);
		user.setDisabled(false);
		operatorService.addOperator(user);
	}
	
	
	// Getters & setters
	
	public void setOperatorService(OperatorService operatorService) { this.operatorService = operatorService; }
	
	public void setRoleService(RoleService roleService) { this.roleService = roleService; }

	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }	
		
}
