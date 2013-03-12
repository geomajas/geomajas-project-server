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
package org.geomajas.plugin.printing.client.widget;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.plugin.rasterizing.command.dto.LegendRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.MapRasterizingInfo;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.ViewPort;
import org.geomajas.puregwt.client.map.layer.Layer;

/**
 * Builds a printable version of a live map.
 * 
 * @author Jan De Moerloose
 * 
 */
public class PrintableMapBuilder {

	private List<PrintableLayerBuilder> layerBuilders = new ArrayList<PrintableLayerBuilder>();
	
	
	public PrintableMapBuilder() {
		layerBuilders.add(new RasterServerLayerBuilder());
		layerBuilders.add(new VectorServerLayerBuilder());
	}

	public void addLayerBuilder(PrintableLayerBuilder layerBuilder) {
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
		ClientMapInfo mapInfo = mapPresenter.getConfiguration();
		mapRasterizingInfo.setLegendRasterizingInfo(legendRasterizingInfo);
		mapInfo.getWidgetInfo().put(MapRasterizingInfo.WIDGET_KEY, mapRasterizingInfo);
		return mapRasterizingInfo;
	}

	public void build(MapPresenter mapPresenter) {
		MapRasterizingInfo mapRasterizingInfo = buildMap(mapPresenter);
		for (int i = 0; i < mapPresenter.getLayersModel().getLayerCount(); i++) {
			Layer layer = mapPresenter.getLayersModel().getLayer(i);
			for (PrintableLayerBuilder layerBuilder : layerBuilders) {
				layerBuilder.build(mapRasterizingInfo, mapPresenter, layer);
			}
		}
	}

}
