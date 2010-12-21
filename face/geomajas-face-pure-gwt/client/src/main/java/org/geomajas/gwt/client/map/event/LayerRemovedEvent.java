package org.geomajas.gwt.client.map.event;

import org.geomajas.gwt.client.map.layer.Layer;

import com.google.gwt.event.shared.GwtEvent;

public class LayerRemovedEvent extends GwtEvent<MapCompositionHandler> {

	private Layer<?> layer;

	public LayerRemovedEvent(Layer<?> layer) {
		this.layer = layer;
	}

	public Type<MapCompositionHandler> getAssociatedType() {
		return MapCompositionHandler.TYPE;
	}

	protected void dispatch(MapCompositionHandler mapCompositionHandler) {
		mapCompositionHandler.onLayerRemoved(this);
	}

	public Layer<?> getLayer() {
		return layer;
	}
}

