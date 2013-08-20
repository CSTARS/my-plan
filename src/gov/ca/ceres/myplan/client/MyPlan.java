package gov.ca.ceres.myplan.client;

import edu.ucdavis.gwt.gis.client.Debugger;
import edu.ucdavis.gwt.gis.client.GisClient;
import edu.ucdavis.gwt.gis.client.GisClient.GisClientLoadHandler;
import edu.ucdavis.gwt.gis.client.config.GadgetConfig;
import edu.ucdavis.gwt.gis.client.extras.InfoPopup;
import edu.ucdavis.gwt.gis.client.toolbar.Toolbar;
import edu.ucdavis.gwt.gis.client.toolbar.button.ToolbarItem;
import edu.ucdavis.gwt.gis.client.toolbar.menu.AddLayerMenu;
import edu.ucdavis.gwt.gis.client.toolbar.menu.ExportMenu;
import edu.ucdavis.gwt.gis.client.toolbar.menu.HelpMenu;
import edu.ucdavis.gwt.gis.client.resources.GadgetResources;
import gov.ca.ceres.myplan.client.resources.MyPlanResources;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;


public class MyPlan implements EntryPoint {
	
	private GisClient mapClient;
	
	public void onModuleLoad() {
		
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler(){
			@Override
			public void onUncaughtException(Throwable e) {
				Debugger.INSTANCE.catchException(e, "MyPlan", "Uncaught exception in MyPlan");
			}
		});
		
		injectMobileMetaTag();
		
		mapClient = new GisClient();
		mapClient.setPrintPage(new MyPlanPrintPage());
		mapClient.load(new GisClientLoadHandler(){
			@Override
			public void onLoad() {
				onClientReady();
			}
		});
		
	}
	
	public void onClientReady() {
		
		mapClient.getToolbar().addToolbarMenu(new ExportMenu());
		mapClient.getToolbar().addSpacer();
		
		mapClient.getToolbar().addToolbarMenu(new AddLayerMenu());
		mapClient.getToolbar().addSpacer();
		
		HelpMenu helpMenu = new HelpMenu();
		InfoPopup popup = helpMenu.getInfoPopup();
		popup.alignDescriptionLeft();
		popup.setWidth("600px");
		
		String customSplash = getCustomSplash();
		if( !customSplash.isEmpty() ) popup.setDescription(customSplash);
		else popup.setDescription(createInfoHtml());
		
		popup.center();
		helpMenu.addItem(new HelpIcon());
		mapClient.getToolbar().addToolbarMenu(helpMenu);
	}
	
	private final native String getCustomSplash() /*-{
		var conf = null;
		
		if( typeof mapConfig != 'undefined' ) {
			conf =  mapConfig;
		} else if ( $wnd.mapConfig ) {
			conf = $wnd.mapConfig;
		}
		
		if( conf == null ) return "";
		
		if( conf.customSplashHtml != null ) 
			return conf.customSplashHtml;
		
		return "";
	}-*/;
	
	public static class HelpIcon extends ToolbarItem {

		Image icon = new Image(GadgetResources.INSTANCE.help());
		
		public HelpIcon() {			
			icon.setTitle("Quick Start Guide");
		}
		
		
		@Override
		public Image getIcon() {
			return icon;
		}

		@Override
		public void onAdd(Toolbar toolbar) {}

		@Override
		public void onClick(ClickEvent event) {
			Window.open("http://myplan.calema.ca.gov/myplan_quick_start_guide.pdf", "_blank", "");
		}


		@Override
		public String getText() {
			return "Quick Start Guide";
		}
		
	}
	
	public String createInfoHtml() {
		String info =
				"<div style='height: 80px; margin: 5px 0 15px 0; border-bottom: 1px solid #cccccc'><div style='float:left'><a href='http://www.oes.ca.gov/' border='0'  target='_blank'>"+createStringIcon(MyPlanResources.INSTANCE.calema())+"</a></div>" +
				"<div style='float:right'><a href='http://www.conservation.ca.gov/cgs/Pages/Index.aspx' border='0' target='_blank'>"+createStringIcon(MyPlanResources.INSTANCE.cgs())+"</a></div>" +
				"<div style='float:right'><a href='http://www.water.ca.gov/' border='0' target='_blank'>"+createStringIcon(MyPlanResources.INSTANCE.dwr())+"</a></div>" +
				"<div style='float:right'><a href='http://www.fire.ca.gov/' border='0' target='_blank'>"+createStringIcon(MyPlanResources.INSTANCE.calfire())+"</a></div></div>" +
			
				"<div style='font-size: 16px; padding-bottom: 10px; color: #333333; text-align: center; font-weight: bold;'>" +
				"<a href='http://hazardmitigation.calema.ca.gov/myplan' target='_blank' >Cal EMA's MyPlan</a></div>" +
				"<div style='color: #444444'>" + 
				
				"<p>MyPlan is a map service designed to be a simple interface to California natural hazard data products produced by the California Natural " +
				"Resources Agency departments and other government agencies. This Web site is provided by Cal EMA to allow users to easily make hazard " +
				"maps for mitigation planning, report generation, and other tasks.</p>" +
				
				"<p>When using the application, browse to your area of interest, or use the search box to locate an address, city, or other feature. " +
				"Use the print button ( "+createStringIcon(GadgetResources.INSTANCE.printer())+" ), to produce a report.  Alternatively use the export map and export legend button " +
				"( "+createStringIcon(GadgetResources.INSTANCE.exportMap())+" ) ( "+createStringIcon(GadgetResources.INSTANCE.exportLegend())+" ), to " +
						"generate images for use in any custom reporting.</p>" +
		
				"<p>For more information, view our <a href='http://myplan.calema.ca.gov/myplan_quick_start_guide.pdf' target='_blank'>Quick Start Guide</a>.</p>";
		
		info += "<p>MyPlan is a collaborative effort between <a href='http://www.calema.ca.gov/' target='_blank'>Cal EMA</a>, The California Natural Resources Agency's " +
				"<a href='http://www.ceres.ca.gov/' target='_blank'>CERES</a> program, and <a href='http://www.fema.gov/' target='_blank'>FEMA</a>.  Please read Cal EMA's " +
				"<a href='http://myplan.calema.ca.gov/disclaimer' target='_blank'>disclaimer</a>.</p>";
				
			//	+"<br><a href='data:text/plain;attachment;base64,"+Base64.encode("<html><head></head><body>This is a saved webpage</body></html>")+"' target='_blank'>Save</a>";
		
		info += "</div>";
		return info;
	}	
	
	public String createStringIcon(ImageResource resource) {
		Image img = new Image(resource);
		img.getElement().getStyle().setVerticalAlign(VerticalAlign.BOTTOM);
		return img.toString();
	}
	
	private native void injectMobileMetaTag() /*-{
		try {
			var head = $wnd.document.getElementsByTagName('head')[0];
			
			var meta = $wnd.document.createElement('meta');
			meta.setAttribute('name', 'viewport');
			meta.setAttribute('content', 'width=device-width, initial-scale=1.0, maximum-scale=1.0; user-scalable=0;');
			head.appendChild(meta);
			
			meta = $wnd.document.createElement('meta');
			meta.setAttribute('name', 'viewport');
			meta.setAttribute('content', 'width=device-width');
			head.appendChild(meta);
			
			meta = $wnd.document.createElement('meta');
			meta.setAttribute('name', 'HandheldFriendly');
			meta.setAttribute('content', 'True');
			head.appendChild(meta);
		} catch (e) {}
	}-*/;

	
	
}
