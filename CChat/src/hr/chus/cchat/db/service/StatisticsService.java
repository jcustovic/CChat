package hr.chus.cchat.db.service;

import hr.chus.cchat.model.helper.db.StatisticsPerServiceProvider;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public interface StatisticsService {
	
	public List<StatisticsPerServiceProvider> getStatisticsPerServiceProvider(Date from, Date to);

}
