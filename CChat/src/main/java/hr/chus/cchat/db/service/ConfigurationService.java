package hr.chus.cchat.db.service;

import java.util.List;

import hr.chus.cchat.model.db.jpa.Configuration;

/**
 * Configuration services that DAO needs to implement.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public interface ConfigurationService {

    public void addConfiguration(Configuration configuration);

    public void removeConfiguration(Configuration configuration);

    public Configuration editConfiguration(Configuration configuration);

    public List<Configuration> getAll();

    public Configuration getByName(String name);

}
