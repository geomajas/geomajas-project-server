package org.geomajas.puregwt.client.map.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent.Type;

public interface LayerOrderChangedHandler  extends EventHandler {

	Type<LayerOrderChangedHandler> TYPE = new Type<LayerOrderChangedHandler>();

	/**
	 * Called when labels are shown on the layer.
	 * 
	 * @param event
	 *            event
	 */
	void onLayerOrderChanged(LayerOrderChangedEvent event);
}
