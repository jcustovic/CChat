package hr.chus.cchat.db.service;

import java.util.List;

import hr.chus.cchat.model.db.jpa.Nick;

/**
 * Nick services that DAO needs to implement.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public interface NickService {

    public void addNick(Nick nick);

    public void removeNick(Nick nick);

    public Nick updateNick(Nick nick);

    public List<Nick> getAllNicks();

    public Nick getNickByName(String name);

    public boolean checkIfNameExists(Nick nick);

    public Nick getNickById(Integer id);

}
