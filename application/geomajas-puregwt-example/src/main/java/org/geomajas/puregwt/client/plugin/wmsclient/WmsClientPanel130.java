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

package org.geomajas.puregwt.client.plugin.wmsclient;

import org.geomajas.geometry.Coordinate;
import org.geomajas.plugin.wmsclient.client.layer.WmsLayer;
import org.geomajas.plugin.wmsclient.client.layer.WmsLayerConfiguration;
import org.geomajas.plugin.wmsclient.client.layer.WmsTileConfiguration;
import org.geomajas.plugin.wmsclient.client.service.WmsService.WmsVersion;
import org.geomajas.puregwt.client.ContentPanel;
import org.geomajas.puregwt.client.Showcase;
import org.geomajas.puregwt.client.event.MapInitializationEvent;
import org.geomajas.puregwt.client.event.MapInitializationHandler;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.widget.MapLayoutPanel;

import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public class WmsClientPanel130 extends ContentPanel {

	public WmsClientPanel130(MapPresenter mapPresenter) {
		super(mapPresenter);
	}

	@Override
	public String getTitle() {
		return "WMS 1.3.0 - Client layer";
	}

	@Override
	public String getDescription() {
		return "Showcase that shows a client side WMS layer.";
	}

	@Override
	public Widget getContentWidget() {
		// Define the whole layout:
		MapLayoutPanel mapLayoutPanel = new MapLayoutPanel(mapPresenter);

		// Initialize the map, and return the layout:
		mapPresenter.initialize("puregwt-app", "mapEmpty");
		mapPresenter.getEventBus().addMapInitializationHandler(new MapInitializationHandler() {

			public void onMapInitialized(MapInitializationEvent event) {
				WmsLayerConfiguration wmsConfig = new WmsLayerConfiguration();
				wmsConfig.setFormat("image/jpeg");
				wmsConfig.setLayers("bluemarble");
				wmsConfig.setVersion(WmsVersion.V1_3_0);
				wmsConfig.setBaseUrl("http://apps.geomajas.org/geoserver/wms");

				Coordinate tileOrigin = new Coordinate(mapPresenter.getViewPort().getMaximumBounds().getX(),
						mapPresenter.getViewPort().getMaximumBounds().getY());
				WmsTileConfiguration tileConfig = new WmsTileConfiguration(256, 256, tileOrigin);

				WmsLayer wmsLayer = Showcase.GEOMAJASINJECTOR.getWmsLayerFactory().createWmsLayer("bluemarble",
						wmsConfig, tileConfig);
				mapPresenter.getLayersModel().addLayer(wmsLayer);
			}
		});
		ResizeLayoutPanel resizeLayoutPanel = new ResizeLayoutPanel();
		resizeLayoutPanel.setWidget(mapLayoutPanel);
		resizeLayoutPanel.setSize("100%", "100%");
		return resizeLayoutPanel;
	}
}