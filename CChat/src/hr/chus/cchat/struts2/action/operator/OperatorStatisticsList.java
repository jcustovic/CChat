package hr.chus.cchat.struts2.action.operator;

import hr.chus.cchat.db.service.StatisticsService;
import hr.chus.cchat.helper.UserAware;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.helper.db.StatisticsPerOperator;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that return operator statistics from some date to some date.
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class OperatorStatisticsList extends ActionSupport implements UserAware {
	
	private static final long serialVersionUID = 1L;
	
	private Log log = LogFactory.getLog(getClass());
	
	private StatisticsService statisticsService;
	private Date fromDate;
	private Date toDate;
	private Operator operator;
	private List<StatisticsPerOperator> statistics;
	
	
	public String execute() throws Exception {
		Calendar todayMax = Calendar.getInstance();
		todayMax.set(Calendar.HOUR_OF_DAY, 23);
		todayMax.set(Calendar.MINUTE, 59);
		todayMax.set(Calendar.SECOND, 59);
		todayMax.set(Calendar.MILLISECOND, 999);
		
		Calendar todayMin = Calendar.getInstance();
		todayMin.set(Calendar.HOUR_OF_DAY, 0);
		todayMin.set(Calendar.MINUTE, 0);
		todayMin.set(Calendar.SECOND, 0);
		todayMin.set(Calendar.MILLISECOND, 0);
		
		if (fromDate == null) fromDate = todayMin.getTime();
		if (toDate == null)	toDate = todayMax.getTime();
		
		log.info("Getting statistics for operator " + operator.getUsername() + " from " + fromDate + " to " + toDate);
		statistics = statisticsService.getStatisticsPerOperator(fromDate, toDate, operator);
		return SUCCESS;
	}


	// Getters & setters
	
	@Override
	public void setAuthenticatedUser(Operator user) { this.operator = user; }
	
	public void setStatisticsService(StatisticsService statisticsService) { this.statisticsService = statisticsService; }
	
	public void setFromDate(Date fromDate) { this.fromDate = fromDate; }

	public void setToDate(Date toDate) { this.toDate = toDate; }

	public List<StatisticsPerOperator> getStatistics() { return statistics; }
	
}
