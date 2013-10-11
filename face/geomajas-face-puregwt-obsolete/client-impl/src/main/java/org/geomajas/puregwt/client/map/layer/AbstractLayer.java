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

package org.geomajas.puregwt.client.map.layer;

import org.geomajas.puregwt.client.event.LayerDeselectedEvent;
import org.geomajas.puregwt.client.event.LayerHideEvent;
import org.geomajas.puregwt.client.event.LayerRefreshedEvent;
import org.geomajas.puregwt.client.event.LayerSelectedEvent;
import org.geomajas.puregwt.client.event.LayerShowEvent;
import org.geomajas.puregwt.client.event.LayerVisibilityMarkedEvent;
import org.geomajas.puregwt.client.event.ViewPortChangedEvent;
import org.geomajas.puregwt.client.event.ViewPortChangedHandler;
import org.geomajas.puregwt.client.event.ViewPortScaledEvent;
import org.geomajas.puregwt.client.event.ViewPortTranslatedEvent;
import org.geomajas.puregwt.client.map.MapEventBus;
import org.geomajas.puregwt.client.map.ViewPort;

import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Abstraction of the basic layer interface. Specific layer implementations can use this as a base.
 * 
 * @author Pieter De Graef
 */
public abstract class AbstractLayer implements Layer {

	protected final String id;

	protected ViewPort viewPort;

	protected MapEventBus eventBus;

	protected boolean selected;

	protected boolean markedAsVisible;

	protected boolean visibleAtPreviousScale;

	protected String title;

	private HandlerRegistration visibilityReg;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create a new layer that belongs to the given map model, using the given meta-data.
	 * 
	 * @param id
	 *            The unique ID for this layer.
	 */
	public AbstractLayer(String id) {
		this.id = id;
		markedAsVisible = true;
	}

	// ------------------------------------------------------------------------
	// Layer implementation:
	// ------------------------------------------------------------------------

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
		if (selected) {
			eventBus.fireEvent(new LayerSelectedEvent(this));
		} else {
			eventBus.fireEvent(new LayerDeselectedEvent(this));
		}
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public void setMarkedAsVisible(boolean markedAsVisible) {
		this.markedAsVisible = markedAsVisible;
		eventBus.fireEvent(new LayerVisibilityMarkedEvent(this));
		if (isShowing()) {
			visibleAtPreviousScale = true;
			eventBus.fireEvent(new LayerShowEvent(this));
		} else {
			visibleAtPreviousScale = false;
			eventBus.fireEvent(new LayerHideEvent(this));
		}
	}

	@Override
	public boolean isMarkedAsVisible() {
		return markedAsVisible;
	}

	@Override
	public boolean isShowing() {
		return markedAsVisible;
	}

	@Override
	public void refresh() {
		eventBus.fireEvent(new LayerRefreshedEvent(this));
	}

	// ------------------------------------------------------------------------
	// Protected methods:
	// ------------------------------------------------------------------------

	protected void setViewPort(ViewPort viewPort) {
		this.viewPort = viewPort;
	}

	protected void setEventBus(MapEventBus eventBus) {
		this.eventBus = eventBus;
		if (visibilityReg != null) {
			visibilityReg.removeHandler();
		}
		visibilityReg = eventBus.addViewPortChangedHandler(new LayerScaleVisibilityHandler());
	}

	// ------------------------------------------------------------------------
	// Protected classes:
	// ------------------------------------------------------------------------

	/**
	 * Every time the scale on the map changes, this handler checks to see if the layer should become visible or not.
	 * 
	 * @author Pieter De Graef
	 */
	protected class LayerScaleVisibilityHandler implements ViewPortChangedHandler {

		public void onViewPortChanged(ViewPortChangedEvent event) {
			onViewPortScaled(null);
		}

		public void onViewPortScaled(ViewPortScaledEvent event) {
			if (!visibleAtPreviousScale && isShowing()) {
				visibleAtPreviousScale = true;
				eventBus.fireEvent(new LayerShowEvent(AbstractLayer.this));
			} else if (visibleAtPreviousScale && !isShowing()) {
				visibleAtPreviousScale = false;
				eventBus.fireEvent(new LayerHideEvent(AbstractLayer.this));
			}
		}

		public void onViewPortTranslated(ViewPortTranslatedEvent event) {
		}
	}
}