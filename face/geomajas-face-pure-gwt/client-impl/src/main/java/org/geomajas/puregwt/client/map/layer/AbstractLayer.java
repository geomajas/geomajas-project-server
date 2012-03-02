/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client.map.layer;

import org.geomajas.configuration.client.ClientLayerInfo;
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
import org.geomajas.puregwt.client.map.ViewPort;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;

/**
 * Abstraction of the basic layer interface. Specific layer implementations should use this as a base.
 * 
 * @param <T>
 *            The layer meta-data. Some extension of {@link ClientLayerInfo}.
 * @author Pieter De Graef
 */
public abstract class AbstractLayer<T extends ClientLayerInfo> implements Layer<T> {

	protected static final String LEGEND_ICONS_PATH = "legendgraphic/";

	protected static final String LEGEND_ICONS_TYPE = ".png";

	protected ViewPort viewPort;

	protected T layerInfo;

	protected EventBus eventBus;

	private boolean selected;

	private boolean markedAsVisible;

	private boolean visibleAtPreviousScale;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create a new layer that belongs to the given map model, using the given meta-data.
	 * 
	 * @param layerInfo
	 *            The layer configuration from which to create the layer.
	 * @param viewPort
	 *            The view port of the map.
	 * @param eventBus
	 *            The map centric event bus.
	 */
	public AbstractLayer(T layerInfo, ViewPort viewPort, EventBus eventBus) {
		this.layerInfo = layerInfo;
		this.viewPort = viewPort;
		this.eventBus = eventBus;
		markedAsVisible = layerInfo.isVisible();
		eventBus.addHandler(ViewPortChangedHandler.TYPE, new LayerScaleVisibilityHandler());
	}

	// ------------------------------------------------------------------------
	// Layer implementation:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public String getId() {
		return layerInfo.getId();
	}

	/** {@inheritDoc} */
	public String getServerLayerId() {
		return layerInfo.getServerLayerId();
	}

	/** {@inheritDoc} */
	public String getTitle() {
		return layerInfo.getLabel();
	}

	/** {@inheritDoc} */
	public T getLayerInfo() {
		return layerInfo;
	}

	/** {@inheritDoc} */
	public void setSelected(boolean selected) {
		this.selected = selected;
		if (selected) {
			eventBus.fireEvent(new LayerSelectedEvent(this));
		} else {
			eventBus.fireEvent(new LayerDeselectedEvent(this));
		}
	}

	/** {@inheritDoc} */
	public boolean isSelected() {
		return selected;
	}

	/** {@inheritDoc} */
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

	/** {@inheritDoc} */
	public boolean isMarkedAsVisible() {
		return markedAsVisible;
	}

	/** {@inheritDoc} */
	public boolean isShowing() {
		if (markedAsVisible) {
			if (viewPort.getScale() >= layerInfo.getMinimumScale().getPixelPerUnit()
					&& viewPort.getScale() <= layerInfo.getMaximumScale().getPixelPerUnit()) {
				return true;
			}
		}
		return false;
	}

	/** {@inheritDoc} */
	public void refresh() {
		eventBus.fireEvent(new LayerRefreshedEvent(this));
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	protected static String getLegendServiceUrl() {
		String moduleBaseUrl = GWT.getModuleBaseURL();
		// remove last slash
		moduleBaseUrl = moduleBaseUrl.substring(0, moduleBaseUrl.length() - 1);
		// replace module part by /d
		int contextEndIndex = moduleBaseUrl.lastIndexOf("/");
		if (contextEndIndex > 6) {
			return moduleBaseUrl.substring(0, contextEndIndex) + "/d/" + LEGEND_ICONS_PATH;
		} else {
			// fall back to module base URL
			return GWT.getModuleBaseURL() + LEGEND_ICONS_PATH;
		}
	}

	// ------------------------------------------------------------------------
	// Private classes:
	// ------------------------------------------------------------------------

	/**
	 * Every time the scale on the map changes, this handler checks to see if the layer should become visible or not.
	 * 
	 * @author Pieter De Graef
	 */
	private class LayerScaleVisibilityHandler implements ViewPortChangedHandler {

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