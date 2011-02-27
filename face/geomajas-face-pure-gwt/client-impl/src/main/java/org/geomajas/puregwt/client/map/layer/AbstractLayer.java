/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client.map.layer;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.puregwt.client.map.MapModel;
import org.geomajas.puregwt.client.map.event.LayerDeselectedEvent;
import org.geomajas.puregwt.client.map.event.LayerHideEvent;
import org.geomajas.puregwt.client.map.event.LayerSelectedEvent;
import org.geomajas.puregwt.client.map.event.LayerShowEvent;

/**
 * Abstraction of the basic layer interface. Specific layer implementations should use this as a base.
 * 
 * @param <T>
 *            The layer meta-data. Some extension of {@link ClientLayerInfo}.
 * @author Pieter De Graef
 */
public abstract class AbstractLayer<T extends ClientLayerInfo> implements Layer<T> {

	protected MapModel mapModel;

	protected T layerInfo;

	private boolean selected;

	private boolean markedAsVisible;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create a new layer that belongs to the given map model, using the given meta-data.
	 * 
	 * @param mapModel
	 *            The model this layer belongs to.
	 * @param layerInfo
	 *            The layer configuration from which to create the layer.
	 */
	public AbstractLayer(MapModel mapModel, T layerInfo) {
		this.mapModel = mapModel;
		this.layerInfo = layerInfo;
	}

	// ------------------------------------------------------------------------
	// Layer implementation:
	// ------------------------------------------------------------------------

	public String getId() {
		return layerInfo.getId();
	}

	public String getServerLayerId() {
		return layerInfo.getServerLayerId();
	}

	public String getTitle() {
		return layerInfo.getLabel();
	}

	public T getLayerInfo() {
		return layerInfo;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		if (selected) {
			mapModel.getEventBus().fireEvent(new LayerSelectedEvent(this));
		} else {
			mapModel.getEventBus().fireEvent(new LayerDeselectedEvent(this));
		}
	}

	public boolean isSelected() {
		return selected;
	}

	public void setMarkedAsVisible(boolean markedAsVisible) {
		this.markedAsVisible = markedAsVisible;
		if (isShowing()) {
			mapModel.getEventBus().fireEvent(new LayerShowEvent(this));
		} else {
			mapModel.getEventBus().fireEvent(new LayerHideEvent(this));
		}
	}

	public boolean isMarkedAsVisible() {
		return markedAsVisible;
	}

	public boolean isShowing() {
		if (markedAsVisible) {
			double scale = mapModel.getViewPort().getScale();
			if (scale >= layerInfo.getMinimumScale().getPixelPerUnit()
					&& scale <= layerInfo.getMaximumScale().getPixelPerUnit()) {
				return true;
			}
		}
		return false;
	}

	public MapModel getMapModel() {
		return mapModel;
	}
}