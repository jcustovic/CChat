package hr.chus.cchat.db.service;

import hr.chus.cchat.model.db.jpa.Operator;

import java.util.List;

/**
 * Operator services that DAO needs to implement.
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public interface OperatorService {
	
	public void addOperator(Operator operator);
	public void removeOperator(Operator operator);
	public Operator updateOperator(Operator operator);
	public Operator authenticateUser(String username, String password);
	public Operator getOperatorByUsername(String username);
	public List<Operator> getAllOperators();
	public List<Operator> getAllActiveOperators();
	public boolean checkIfUsernameExists(Operator operator);
	public Operator getOperatorById(Integer id);

}
