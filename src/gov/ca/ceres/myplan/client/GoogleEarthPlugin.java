package gov.ca.ceres.myplan.client;

import java.util.LinkedList;

import com.esri.gwt.client.layers.Layer;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.event.EarthInstanceHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class GoogleEarthPlugin {
	
	private JavaScriptObject ge;
	private LinkedList<Layer> layers = null;
	private RootPanel panel = null;
	private GoogleEarthPlugin ref = this;
	
	private MapWidget map = new MapWidget();
	private Widget pp = null;
	
	public GoogleEarthPlugin(LinkedList<Layer> layers, Widget parentpanel) {
		this.layers = layers;
		pp = parentpanel;
		
		panel = RootPanel.get("map3d");
		
		panel.getElement().getStyle().setPosition(Position.ABSOLUTE);
		panel.getElement().getStyle().setTop(Window.getClientHeight()*2, Unit.PX);
		panel.getElement().getStyle().setLeft(parentpanel.getAbsoluteLeft(), Unit.PX);
		panel.getElement().getStyle().setZIndex(5000);
		panel.setSize(parentpanel.getOffsetWidth()+"px", (parentpanel.getOffsetHeight()-4)+"px");
		RootPanel.get().add(panel);
		
		panel.add(map);
		
		

		map.getEarthInstance(new EarthInstanceHandler() {
			@Override
			public void onEarthInstance(EarthInstanceEvent event) {
				map.onResize();
				
				setInstance(ref, event.getEarthInstance());
				map.setCurrentMapType(MapType.getEarthMap());
				onLoadComplete();
				

				panel.getElement().getStyle().setTop(pp.getAbsoluteTop(), Unit.PX);

			}
		});
		
		//init(this);
	}
	
	@SuppressWarnings("unused")
	private void onLoadComplete() {
		for( Layer l: layers ) {
			try {
				addOverlay(this, l.getUrl()+"/kml/mapImage.kmz");
			} catch (Exception e) {}
		}
	}
	
	private native void setInstance(GoogleEarthPlugin plugin, JavaScriptObject ge) /*-{
		plugin.@gov.ca.ceres.myplan.client.GoogleEarthPlugin::ge = ge;
	}-*/;
	
	private final native void addOverlay(GoogleEarthPlugin plugin, String url) /*-{
		var ge = plugin.@gov.ca.ceres.myplan.client.GoogleEarthPlugin::ge;
		
		var link = ge.createLink('');
		link.setHref(url);
		
		var networkLink = ge.createNetworkLink('');
		$wnd["__networklink"] = networkLink;
		networkLink.set(link, true, true); // Sets the link, refreshVisibility, and flyToView
		
		ge.getFeatures().appendChild(networkLink);
	}-*/;
	
	private final native void init(GoogleEarthPlugin plugin) /*-{

		$wnd.google.load("earth", "1");
		
		function initCB(instance) {
			plugin.@gov.ca.ceres.myplan.client.GoogleEarthPlugin::ge = instance;
			instance.getWindow().setVisible(true);
			plugin.@gov.ca.ceres.myplan.client.GoogleEarthPlugin::onLoadComplete();
		}
		
		
		function failure (errorCode) {
			$wnd.alert("error loading google earth");
		}
		
		$wnd.alert("1");
		$wnd.google.setOnLoadCallback(function() {
			$wnd.alert("3");
			$wnd.google.earth.createInstance("map3d", initCB, failureCB);
			$wnd.alert("4");
		});
		$wnd.alert("2");
		

	}-*/;
	

}
