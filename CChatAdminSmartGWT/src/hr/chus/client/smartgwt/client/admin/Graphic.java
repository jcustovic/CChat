package hr.chus.client.smartgwt.client.admin;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.PieChart;
import com.google.gwt.visualization.client.visualizations.PieChart.Options;
import com.google.gwt.visualization.client.visualizations.Table;

public class Graphic extends AbsolutePanel {
	
	private PieChart pieChart;
	private int workH;
	private int sleepH;
	
	public Graphic() {
		setHeight("550px");
		setWidth("550px");
		Runnable onLoadCallback = new Runnable() {
			public void run() {
				workH = 10;
				sleepH = 14;
				HorizontalPanel hp = new HorizontalPanel();
				pieChart = new PieChart(createTable(workH, sleepH), createOptions());
				hp.add(pieChart);
				hp.add(new Table(createTable(workH, sleepH), createTOptions()));
				add(hp);
			}
		};
		VisualizationUtils.loadVisualizationApi(onLoadCallback, PieChart.PACKAGE, Table.PACKAGE);
		Timer refreshTimer = new Timer() {
	    	@Override
	    	public void run() {
	    		pieChart.draw(createTable(++workH, ++sleepH), createOptions());
	    	}
	    };
	    refreshTimer.scheduleRepeating(1000 * 10); // 10 seconds
	}
	
	private static Table.Options createTOptions() {
		Table.Options options = Table.Options.create();
		return options;
	}

	private static Options createOptions() {
		Options options = Options.create();
		options.setWidth(400);
		options.setHeight(240);
		options.set3D(true);
		options.setTitle("My Daily Activities");
		return options;
	}

	private static AbstractDataTable createTable(int workH, int sleepH) {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "Task");
		data.addColumn(ColumnType.NUMBER, "Hours per Day");
		data.addRows(2);
		data.setValue(0, 0, "Work");
		data.setValue(0, 1, workH);
		data.setValue(1, 0, "Sleep");
		data.setValue(1, 1, sleepH);
		
//		DataView result = DataView.create(data);
//	    result.setColumns(new int[] {0, 1});
//	    return result;
		return data;
	}

}