package gov.ca.ceres.myplan.client;

import com.esri.gwt.client.MapWidget;
import com.esri.gwt.client.geometry.Point;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.ucdavis.gwt.gis.client.print.PrintPage;
import edu.ucdavis.gwt.gis.client.state.overlays.MapStateOverlay;

public class MyPlanPrintPage extends PrintPage {

	@Override
	public void onPrintPageReady(MapWidget map, SimplePanel rootPanel,
			MapStateOverlay mapState) {
		
		VerticalPanel printModePanel = new VerticalPanel();
		printModePanel.setWidth("100%");
		rootPanel.add(printModePanel);
		
		HTML print = new HTML("<div style='text-align:right'><a style='cursor:pointer;text-decoration:underline' onclick='window.print()'>Print</a></div>");
		printModePanel.add(print);
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setWidth("100%");
		
		HTML html = new HTML("<h2>MyPlan Report</h2>");
		hp.add(html);
		hp.setCellWidth(html, "180px");
		
		html = new HTML("<div></div>");
		hp.add(html);
		hp.setCellWidth(html, "150px");
		
		html = new HTML("Notes: ");
		hp.add(html);
		hp.setCellWidth(html, "20px");
		
		TextArea ta = new TextArea();
		ta.setWidth("100%");
		ta.setHeight("50px");
		hp.add(ta);
		
		printModePanel.add(hp);
		
		printModePanel.add(map);
		map.setSize("700px", "700px");
		

		addVisibleLayers();
				
		// add legends
		printModePanel.add(addVisibleLegends());
		
		map.centerAndZoom(
			Point.create(
				mapState.getCenterX(),
				mapState.getCenterY(),
				map.getSpatialReference()
			), mapState.getLevel());
	}



}
