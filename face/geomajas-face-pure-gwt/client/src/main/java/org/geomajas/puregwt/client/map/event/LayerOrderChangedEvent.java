package org.geomajas.puregwt.client.map.event;

import com.google.gwt.event.shared.GwtEvent;

public class LayerOrderChangedEvent extends GwtEvent<LayerOrderChangedHandler> {

	private int minIndex;

	private int maxIndex;


	public LayerOrderChangedEvent(int minIndex, int maxIndex) {
		this.minIndex = minIndex;
		this.maxIndex = maxIndex;
	}

	@Override
	public Type<LayerOrderChangedHandler> getAssociatedType() {
		return LayerOrderChangedHandler.TYPE;
	}

	@Override
	protected void dispatch(LayerOrderChangedHandler layerOrderChangedHandler) {
		layerOrderChangedHandler.onLayerOrderChanged(this);
	}

	public int getMinIndex() {
		return minIndex;
	}

	public int getMaxIndex() {
		return maxIndex;
	}
	
	
}
