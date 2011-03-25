/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.rasterizing.legend;

import java.util.LinkedHashMap;

import javax.swing.JComponent;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.plugin.rasterizing.api.LayerFactory;
import org.geomajas.plugin.rasterizing.command.dto.LegendRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.MapRasterizingInfo;
import org.geomajas.plugin.rasterizing.layer.GeometryDirectLayer;
import org.geomajas.plugin.rasterizing.layer.RasterDirectLayer;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContext;

/**
 * Builder class for legend.
 * 
 * @author Jan De Moerloose
 * 
 */
public class LegendBuilder {

	@SuppressWarnings("unchecked")
	public JComponent buildComponentTree(MapContext mapContext) {
		MapRasterizingInfo mapRasterizingInfo = (MapRasterizingInfo) mapContext.getUserData().get(
				LayerFactory.USERDATA_RASTERIZING_INFO);
		LegendRasterizingInfo legendRasterizingInfo = mapRasterizingInfo.getLegendRasterizingInfo();
		LegendPanel legendPanel = new LegendPanel(legendRasterizingInfo.getTitle());
		for (Layer layer : mapContext.layers()) {
			if (layer instanceof RasterDirectLayer) {
				RasterDirectLayer rasterLayer = (RasterDirectLayer) layer;
				legendPanel.addLayer(rasterLayer);
			} else if (layer instanceof FeatureLayer) {
				FeatureLayer featureLayer = (FeatureLayer) layer;
				LinkedHashMap<String, FeatureStyleInfo> styles = (LinkedHashMap<String, FeatureStyleInfo>) layer
						.getUserData().get(LayerFactory.USERDATA_KEY_STYLES);
				for (FeatureStyleInfo style : styles.values()) {
					legendPanel.addLayer(featureLayer, style);
				}
			} else if (layer instanceof GeometryDirectLayer) {
				GeometryDirectLayer geometryLayer = (GeometryDirectLayer) layer;
				legendPanel.addLayer(geometryLayer);
			}
		}
		legendPanel.pack();
		return legendPanel;
	}

}
