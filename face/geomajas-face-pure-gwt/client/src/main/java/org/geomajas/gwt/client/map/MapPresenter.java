package org.geomajas.gwt.client.map;

import java.util.Collection;

import org.geomajas.gwt.client.controller.Controller;
import org.geomajas.gwt.client.controller.Listener;

public interface MapPresenter {

	VectorContainer getWorldContainer(String id);

	VectorContainer getScreenContainer(String id);

	MapModel getMapModel();

	void addMapGadget(MapGadget mapGadget);

	void removeMapGadget(MapGadget mapGadget);

	void setFallbackController(Controller fallbackController);

	void setController(Controller controller);

	Controller getController();

	boolean addListener(Listener listener);

	boolean removeListener(Listener listener);
	
	Collection<Listener> getListeners();

}
