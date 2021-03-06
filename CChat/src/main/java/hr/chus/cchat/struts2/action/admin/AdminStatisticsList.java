package hr.chus.cchat.struts2.action.admin;

import hr.chus.cchat.db.service.StatisticsService;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.helper.db.StatisticsPerOperator;
import hr.chus.cchat.model.helper.db.StatisticsPerServiceProvider;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that calculates statistics based upon different parameters.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class AdminStatisticsList extends ActionSupport {

    private static final Logger                LOG = LoggerFactory.getLogger(AdminStatisticsList.class);

    @Autowired
    private StatisticsService                  statisticsService;

    private Date                               fromDate;
    private Date                               toDate;
    private Operator                           operator;
    private String                             statisticsType;
    private List<StatisticsPerServiceProvider> statisticsPerServiceProviders;
    private List<StatisticsPerOperator>        statisticsPerOperators;

    public String execute() throws Exception {
        if (statisticsType == null) {
            return INPUT;
        }

        final Calendar todayMax = Calendar.getInstance();
        todayMax.set(Calendar.HOUR_OF_DAY, 23);
        todayMax.set(Calendar.MINUTE, 59);
        todayMax.set(Calendar.SECOND, 59);
        todayMax.set(Calendar.MILLISECOND, 999);

        final Calendar todayMin = Calendar.getInstance();
        todayMin.set(Calendar.HOUR_OF_DAY, 0);
        todayMin.set(Calendar.MINUTE, 0);
        todayMin.set(Calendar.SECOND, 0);
        todayMin.set(Calendar.MILLISECOND, 0);

        if (fromDate == null) fromDate = todayMin.getTime();
        if (toDate == null) toDate = todayMax.getTime();

        LOG.debug("Getting statistics of type " + statisticsType + " from " + fromDate + " to " + toDate + " for " + operator);
        if (statisticsType.equals("StatisticsPerServiceProvider")) {
            statisticsPerServiceProviders = statisticsService.getStatisticsPerServiceProvider(fromDate, toDate);
        } else if (statisticsType.equals("StatisticsPerOperator")) {
            statisticsPerOperators = statisticsService.getStatisticsPerOperator(fromDate, toDate, operator);
        } else if (statisticsType.equals("LiveStatistics")) {
            statisticsPerServiceProviders = statisticsService.getStatisticsPerServiceProvider(todayMin.getTime(), todayMax.getTime());
        } else {
            LOG.warn("Statistics type {} in not known!", statisticsType);
        }
        LOG.debug("Statistics processed, returning result...");

        return SUCCESS;
    }

    // Getters & setters

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public void setStatisticsType(String statisticsType) {
        this.statisticsType = statisticsType;
    }

    public List<StatisticsPerServiceProvider> getStatisticsPerServiceProviders() {
        return statisticsPerServiceProviders;
    }

    public List<StatisticsPerOperator> getStatisticsPerOperators() {
        return statisticsPerOperators;
    }

}
