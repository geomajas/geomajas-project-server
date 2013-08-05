/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.smartgwt.client.map.layer;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.smartgwt.client.gfx.PainterVisitor;
import org.geomajas.smartgwt.client.map.MapModel;
import org.geomajas.smartgwt.client.map.event.HasLayerChangedHandlers;
import org.geomajas.smartgwt.client.map.event.LayerChangedHandler;
import org.geomajas.smartgwt.client.map.event.LayerLabeledEvent;
import org.geomajas.smartgwt.client.map.event.LayerShownEvent;
import org.geomajas.smartgwt.client.map.event.LayerStyleChangedHandler;
import org.geomajas.smartgwt.client.spatial.Bbox;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * <p>
 * An abstract layer implements common behavior and data for all layers. Since we will want to paint layers on the map,
 * it must implement the {@link org.geomajas.smartgwt.client.gfx.Paintable} interface.
 * </p>
 * 
 * @param <T>
 *            layer info {@link ClientLayerInfo}
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 * @author Frank Wynants
 * @since 1.10.0
 */
@Api
public abstract class AbstractLayer<T extends ClientLayerInfo> implements Layer<T>, HasLayerChangedHandlers {

	private T layerInfo;

	/** The model behind a map. Every layer belongs to such a model. */
	protected MapModel mapModel;

	private boolean showing;

	private boolean selected;

	private boolean labeled;

	private boolean visible;

	protected HandlerManager handlerManager;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	protected AbstractLayer(MapModel mapModel, T layerInfo) {
		this.mapModel = mapModel;
		this.layerInfo = layerInfo;
		this.visible = layerInfo.isVisible();
		this.updateShowing(false);
		handlerManager = new HandlerManager(this);
	}

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	/**
	 * Add a handler that registers changes in layer status.
	 * 
	 * @param handler
	 *            The new handler to be added.
	 */
	public HandlerRegistration addLayerChangedHandler(LayerChangedHandler handler) {
		return handlerManager.addHandler(LayerChangedHandler.TYPE, handler);
	}

	/**
	 * Add a handler that registers changes in layer style.
	 * 
	 * @param handler
	 *            The new handler to be added.
	 */
	public HandlerRegistration addLayerStyleChangedHandler(LayerStyleChangedHandler handler) {
		return handlerManager.addHandler(LayerStyleChangedHandler.TYPE, handler);
	}

	/**
	 * Return the layer information.
	 * 
	 * @return the layer info
	 * @since 1.10.0
	 */
	@Api
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

	@Override
	public void updateShowing() {
		updateShowing(true);
	}

	/**
	 * Update showing state.
	 * 
	 * @param fireEvents Should events be fired if state changes?
	 */
	protected void updateShowing(boolean fireEvents) {
		double scale = mapModel.getMapView().getCurrentScale();
		if (visible) {
			boolean oldShowing = showing;
			showing = scale >= layerInfo.getMinimumScale().getPixelPerUnit()
					&& scale <= layerInfo.getMaximumScale().getPixelPerUnit();
			if (oldShowing != showing && fireEvents) {
				handlerManager.fireEvent(new LayerShownEvent(this, true));
			}
		} else {
			showing = false;
		}
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	/**
	 * Return this layer's client ID as defined in the configuration.
	 * 
	 * @return id of the client layer
	 * @since 1.10.0
	 */
	@Api
	public String getId() {
		return layerInfo.getId();
	}

	/**
	 * Return this layer's server ID as defined in the configuration.
	 * 
	 * @return id of the server layer
	 * @since 1.10.0
	 */
	@Api
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

	@Override
	public MapModel getMapModel() {
		return mapModel;
	}

	/**
	 * Get the layer label as defined in the configuration.
	 *
	 * @return layer label
	 * @since 1.10.0
	 */
	@Api
	public String getLabel() {
		return layerInfo.getLabel();
	}

	/**
	 * Returns true if labels are visible (This does not necessarily mean that
	 * the labels are currently showing on the map!).
	 * 
	 * @return true when labels are enable
	 * @since 1.10.0
	 */
	@Api
	public boolean isLabelsVisible() {
		return labeled;
	}

	/**
	 * Returns true if labels are currently showing (eg. isShowing == true && isLabelsVisible == true).
	 *
	 * @return true when labels are enabled and the layer is showing
	 * @since 1.10.0
	 */
	@Api
	public boolean isLabelsShowing() {
		return isLabelsVisible() && isShowing();
	}

	/**
	 * @deprecated replaced by {@link #isLabelsShowing()}.
	 */
	@Deprecated
	public boolean isLabeled() {
		return showing && labeled;
	}

	public double getMinimumScale() {
		return layerInfo.getMinimumScale().getPixelPerUnit();
	}

	public double getMaximumScale() {
		return layerInfo.getMaximumScale().getPixelPerUnit();
	}

	@Override
	public void setLabeled(boolean labeled) {
		this.labeled = labeled;
		handlerManager.fireEvent(new LayerLabeledEvent(this));
	}

	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public boolean isShowing() {
		return showing;
	}

	/**
	 * Return whether the layer is visible.
	 *
	 * @return true when layer is visible
	 * @since 1.10.0
	 */
	@Api
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Make the layer visible or invisible.
	 *
	 * @param visible
	 *            the visible to set
	 * @since 1.10.0
	 */
	@Api
	public void setVisible(boolean visible) {
		if (visible != this.visible) {
			this.visible = visible;
			updateShowing(false);
			handlerManager.fireEvent(new LayerShownEvent(this));
		}
	}
}