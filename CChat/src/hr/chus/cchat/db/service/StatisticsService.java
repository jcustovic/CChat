package hr.chus.cchat.db.service;

import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.helper.db.StatisticsPerOperator;
import hr.chus.cchat.model.helper.db.StatisticsPerServiceProvider;

import java.util.Date;
import java.util.List;

/**
 * Statistic services that DAO needs to implement.
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public interface StatisticsService {
	
	public List<StatisticsPerServiceProvider> getStatisticsPerServiceProvider(Date from, Date to);
	public List<StatisticsPerOperator> getStatisticsPerOperator(Date from, Date to, Operator operator);

}
