package hr.chus.client.smartgwt.client.admin;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.PieChart;
import com.google.gwt.visualization.client.visualizations.PieChart.Options;

public class Graphic extends AbsolutePanel {
	
	public Graphic() {
		setHeight("550px");
		setWidth("550px");
		Runnable onLoadCallback = new Runnable() {
			public void run() {
				add(new PieChart(createTable(), createOptions()));
			}
		};
		VisualizationUtils.loadVisualizationApi(onLoadCallback, PieChart.PACKAGE);
	}

	private Options createOptions() {
		Options options = Options.create();
		options.setWidth(400);
		options.setHeight(240);
		options.set3D(true);
		options.setTitle("My Daily Activities");
		return options;
	}

	private AbstractDataTable createTable() {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "Task");
		data.addColumn(ColumnType.NUMBER, "Hours per Day");
		data.addRows(2);
		data.setValue(0, 0, "Work");
		data.setValue(0, 1, 14);
		data.setValue(1, 0, "Sleep");
		data.setValue(1, 1, 10);
		return data;
	}

}