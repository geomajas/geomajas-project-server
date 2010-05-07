/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.gwt.client.map.layer;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.event.HasLayerChangedHandlers;
import org.geomajas.gwt.client.map.event.LayerChangedHandler;
import org.geomajas.gwt.client.map.event.LayerLabeledEvent;
import org.geomajas.gwt.client.map.event.LayerShownEvent;
import org.geomajas.gwt.client.spatial.Bbox;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * <p>
 * An abstract layer implements common behavior and data for all layers. Since we will want to paint layers on the map,
 * it must implement the <code>Paintable</code> interface.
 * </p>
 * 
 * @param <T>
 *            layer info {@link ClientLayerInfo}
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 * @author Frank Wynants
 */
public abstract class AbstractLayer<T extends ClientLayerInfo> implements Layer<T>, HasLayerChangedHandlers {

	private T layerInfo;

	/** The model behind a map. Every layer belongs to such a model. */
	protected MapModel mapModel;

	private boolean showing;

	private boolean selected;

	private boolean labeled;

	private boolean visible;

	private HandlerManager handlerManager;
	
	//-------------------------------------------------------------------------
	// Constructor:
	//-------------------------------------------------------------------------

	protected AbstractLayer(MapModel mapModel, T layerInfo) {
		this.mapModel = mapModel;
		this.layerInfo = layerInfo;
		this.visible = layerInfo.isVisible();
		this.updateShowing();
		handlerManager = new HandlerManager(this);
	}
	
	//-------------------------------------------------------------------------
	// Public methods:
	//-------------------------------------------------------------------------

	/**
	 * Return the layer information.
	 * 
	 * @return the layer info
	 */
	public T getLayerInfo() {
		return layerInfo;
	}

	/**
	 * The PainterVisitor accept function, that determines how this object should be rendered.
	 */
	public abstract void accept(PainterVisitor visitor, Object group, Bbox bounds, boolean recursive);

	/**
	 * Is this layer currently selected or not?
	 * 
	 * @return true if selected, false otherwise
	 */
	public boolean isSelected() {
		return selected;
	}

	public void updateShowing() {
		double scale = mapModel.getMapView().getCurrentScale();
		if (visible) {
			if (scale >= layerInfo.getViewScaleMin() && scale <= layerInfo.getViewScaleMax()) {
				showing = true;
			} else {
				showing = false;
			}
		} else {
			showing = false;
		}
	}

	//-------------------------------------------------------------------------
	// Getters and setters:
	//-------------------------------------------------------------------------

	/**
	 * Return this layer's client ID as defined in the configuration.
	 *
	 * @return id of the client layer
	 */
	public String getId() {
		return layerInfo.getId();
	}
	
	/**
	 * Return this layer's server ID as defined in the configuration.
	 *
	 * @return id of the server layer
	 */
	public String getServerLayerId() {
		return layerInfo.getServerLayerId();
	}

	/**
	 * Returns a nice name for the group to use in the DOM, not necessarily unique.
	 * 
	 * @return name
	 */
	public String getGroupName() {
		return layerInfo.getId();
	}

	public MapModel getMapModel() {
		return mapModel;
	}

	public String getLabel() {
		return layerInfo.getLabel();
	}

	public boolean isLabeled() {
		return showing && labeled;
	}

	public double getMinViewScale() {
		return layerInfo.getViewScaleMin();
	}

	public double getMaxViewScale() {
		return layerInfo.getViewScaleMax();
	}

	public void setLabeled(boolean labeled) {
		this.labeled = labeled;
		handlerManager.fireEvent(new LayerLabeledEvent(this));
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isShowing() {
		return showing;
	}

	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param visible
	 *            the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
		updateShowing();
		handlerManager.fireEvent(new LayerShownEvent(this));
	}

	public HandlerRegistration addLayerChangedHandler(LayerChangedHandler handler) {
		return handlerManager.addHandler(LayerChangedHandler.TYPE, handler);
	}
}