package hr.chus.cchat.struts2.action.admin;

import hr.chus.cchat.db.service.StatisticsService;
import hr.chus.cchat.model.helper.db.StatisticsPerServiceProvider;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class AdminStatisticsList extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	
	private Log log = LogFactory.getLog(getClass());
	
	private StatisticsService statisticsService;
	private Date fromDate;
	private Date toDate;
	private List<StatisticsPerServiceProvider> statisticsPerServiceProviders;
	
	
	public String execute() throws Exception {
		if (fromDate == null) fromDate = new Date();
		if (toDate == null) toDate = new Date();
		log.info("Getting statistics from " + fromDate + " to " + toDate);
		statisticsPerServiceProviders = statisticsService.getStatisticsPerServiceProvider(fromDate, toDate);
		return SUCCESS;
	}


	// Getters & setters
	
	public void setStatisticsService(StatisticsService statisticsService) { this.statisticsService = statisticsService; }
	
	public void setFromDate(Date fromDate) { this.fromDate = fromDate; }
	
	public void setToDate(Date toDate) { this.toDate = toDate; }

	public List<StatisticsPerServiceProvider> getStatisticsPerServiceProviders() { return statisticsPerServiceProviders; }
		
}
