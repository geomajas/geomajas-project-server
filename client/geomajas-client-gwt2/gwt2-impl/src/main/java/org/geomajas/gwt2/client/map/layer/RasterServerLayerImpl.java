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
package org.geomajas.gwt2.client.map.layer;

import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.gwt.client.util.UrlBuilder;
import org.geomajas.gwt2.client.event.LayerStyleChangedEvent;
import org.geomajas.gwt2.client.map.MapEventBus;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.service.EndPointService;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * The client side representation of a raster layer defined on the backend.
 * 
 * @author Pieter De Graef
 */
public class RasterServerLayerImpl extends AbstractServerLayer<ClientRasterLayerInfo> implements RasterServerLayer {

	private EndPointService endPointService;

	/** The only constructor. */
	@Inject
	public RasterServerLayerImpl(@Assisted ClientRasterLayerInfo layerInfo, @Assisted final ViewPort viewPort,
			@Assisted final MapEventBus eventBus, final EndPointService endPointService) {
		super(layerInfo, viewPort, eventBus);
		this.endPointService = endPointService;
	}

	@Override
	public IsWidget buildLegendWidget() {
		UrlBuilder url = new UrlBuilder(endPointService.getLegendServiceUrl());
		url.addPath(getServerLayerId() + LEGEND_ICON_EXTENSION);
		return new ServerLayerStyleWidget(url.toString(), getTitle(), null);
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