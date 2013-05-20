package hr.chus.cchat.db.service;

import hr.chus.cchat.model.db.jpa.Operator;

import java.util.List;

/**
 * Operator services that DAO needs to implement.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public interface OperatorService {

    void addOperator(Operator p_operator);

    void removeOperator(Operator p_operator);

    Operator updateOperator(Operator p_operator);

    Operator authenticateUser(String p_username, String p_password);

    Operator getOperatorByUsername(String p_username);

    List<Operator> getAllOperators();

    List<Operator> getAllActiveOperators();

    boolean checkIfUsernameExists(Operator p_operator);

    Operator getOperatorById(Integer p_id);

    Operator save(Operator p_operator, List<Integer> p_languages);

}
