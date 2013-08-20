package gov.ca.ceres.myplan.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface MyPlanResources  extends ClientBundle {
	
	public static final MyPlanResources INSTANCE = GWT.create(MyPlanResources.class);
	
	@Source("images/googleearth.png")
	public ImageResource googleearth();
	
	@Source("images/calema.png")
	public ImageResource calema();

	@Source("images/calfire.png")
	public ImageResource calfire();

	@Source("images/cgs.png")
	public ImageResource cgs();

	@Source("images/dwr.png")
	public ImageResource dwr();

}
