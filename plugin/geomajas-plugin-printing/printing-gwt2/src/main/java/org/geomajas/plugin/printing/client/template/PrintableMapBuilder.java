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

package org.geomajas.plugin.printing.client.template;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.plugin.rasterizing.command.dto.LegendRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.MapRasterizingInfo;

/**
 * Builds a printable version of a live map.
 * 
 * @author Jan De Moerloose
 * @author An Buyle (support for extra layer with e.g. selected geometries)
 */
public class PrintableMapBuilder {

	private List<PrintableLayerBuilder> layerBuilders = new ArrayList<PrintableLayerBuilder>();

	public PrintableMapBuilder() {
		layerBuilders.add(new RasterServerLayerBuilder());
		layerBuilders.add(new VectorServerLayerBuilder());
	}

	public void registerLayerBuilder(PrintableLayerBuilder layerBuilder) {
		layerBuilders.add(layerBuilder);
	}

	protected MapRasterizingInfo buildMap(MapPresenter mapPresenter) {
		MapRasterizingInfo mapRasterizingInfo = new MapRasterizingInfo();
		ViewPort viewPort = mapPresenter.getViewPort();
		mapRasterizingInfo.setBounds(viewPort.getBounds());
		mapRasterizingInfo.setScale(viewPort.getScale());
		mapRasterizingInfo.setTransparent(true);
		LegendRasterizingInfo legendRasterizingInfo = new LegendRasterizingInfo();
		legendRasterizingInfo.setTitle("Legend");
		FontStyleInfo font = new FontStyleInfo();
		font.applyDefaults();
		legendRasterizingInfo.setFont(font);

		mapRasterizingInfo.setLegendRasterizingInfo(legendRasterizingInfo);

		// Support for selection of layer object : create container for info on selected features;
		// store the selections layer per layer
		List<ClientLayerInfo> selectedLayers = new ArrayList<ClientLayerInfo>();
		mapRasterizingInfo.setExtraLayers(selectedLayers);

		ClientMapInfo mapInfo = mapPresenter.getConfiguration().getServerConfiguration();
		mapInfo.getWidgetInfo().put(MapRasterizingInfo.WIDGET_KEY, mapRasterizingInfo);
		// Note: mapRasterizingInfo at this time is pretty empty (rastering info for
		// layers not yet filled in)
		return mapRasterizingInfo;
	}

	public void build(MapPresenter mapPresenter, Bbox worldBounds, double rasterScale) {
		buildMap(mapPresenter);
		List<ClientLayerInfo> clientLayers = new ArrayList<ClientLayerInfo>();
		for (int i = 0; i < mapPresenter.getLayersModel().getLayerCount(); i++) {
			Layer layer = mapPresenter.getLayersModel().getLayer(i);
			for (PrintableLayerBuilder layerBuilder : layerBuilders) {
				if (layerBuilder.supports(layer)) {
					clientLayers.add(layerBuilder.build(mapPresenter, layer, worldBounds, rasterScale));
				}
			}
		}
		mapPresenter.getConfiguration().getServerConfiguration().setLayers(clientLayers);
	}
}