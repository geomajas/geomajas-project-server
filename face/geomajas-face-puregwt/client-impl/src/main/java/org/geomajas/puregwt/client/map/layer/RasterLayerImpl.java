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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.gwt.client.util.UrlBuilder;
import org.geomajas.puregwt.client.event.LayerStyleChangedEvent;
import org.geomajas.puregwt.client.map.MapEventBus;
import org.geomajas.puregwt.client.map.ViewPort;
import org.geomajas.puregwt.client.service.EndPointService;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;


/**
 * <p>
 * The client side representation of a raster layer.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class RasterLayerImpl extends AbstractLayer<ClientRasterLayerInfo> implements RasterLayer {
	
	private EndPointService endPointService;
	
	/**
	 * The only constructor! Set the MapModel and the layer info.
	 * 
	 */
	@Inject
	public RasterLayerImpl(@Assisted ClientRasterLayerInfo layerInfo, @Assisted final ViewPort viewPort,
			@Assisted final MapEventBus eventBus, final EndPointService endPointService) {
		super(layerInfo, viewPort, eventBus);
		this.endPointService = endPointService;
	}

	/** {@inheritDoc} */
	public List<LayerStylePresenter> getStylePresenters() {
		List<LayerStylePresenter> stylePresenters = new ArrayList<LayerStylePresenter>();
		UrlBuilder url = new UrlBuilder(endPointService.getLegendServiceUrl());
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