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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.gwt.client.util.UrlBuilder;
import org.geomajas.puregwt.client.Geomajas;
import org.geomajas.puregwt.client.event.LayerStyleChangedEvent;
import org.geomajas.puregwt.client.map.ViewPort;

import com.google.web.bindery.event.shared.EventBus;


/**
 * <p>
 * The client side representation of a raster layer.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class RasterLayer extends AbstractLayer<ClientRasterLayerInfo> implements OpacitySupported {

	/**
	 * The only constructor! Set the MapModel and the layer info.
	 * 
	 */
	public RasterLayer(ClientRasterLayerInfo layerInfo, ViewPort viewPort, EventBus eventBus) {
		super(layerInfo, viewPort, eventBus);
	}

	/** {@inheritDoc} */
	public List<LayerStylePresenter> getStylePresenters() {
		List<LayerStylePresenter> stylePresenters = new ArrayList<LayerStylePresenter>();
		UrlBuilder url = new UrlBuilder(Geomajas.getLegendServiceUrl());
		url.addPath(getServerLayerId() + LEGEND_ICON_EXTENSION);
		stylePresenters.add(new LayerStylePresenter(0, url.toString(), getTitle()));
		return stylePresenters;
	}

	/**
	 * Apply a new opacity on the entire raster layer.
	 * 
	 * @param opacity
	 *            The new opacity value. Must be a value between 0 and 1, where 0 means invisible and 1 is totally
	 *            visible.
	 */
	public void setOpacity(double opacity) {
		getLayerInfo().setStyle(Double.toString(opacity));
		eventBus.fireEvent(new LayerStyleChangedEvent(this));
	}

	public double getOpacity() {
		return Double.parseDouble(getLayerInfo().getStyle());
	}
}