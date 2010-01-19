package hr.chus.cchat.helper;

import hr.chus.cchat.model.db.jpa.Operator;


public interface UserAware {
	public void setUser(Operator user);
}
