package hr.chus.cchat.db.service;

import java.util.List;

import hr.chus.cchat.model.db.jpa.Role;

/**
 * 
 * @author chus
 *
 */
public interface RoleService {
	
	public void addRole(Role role);
	public void removeRole(Role role) ;
	public Role updateRole(Role role);
	public List<Role> getAll();
	public Role getRoleById(Integer valueOf);

}
