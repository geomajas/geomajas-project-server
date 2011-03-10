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

import javax.swing.JComponent;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.plugin.rasterizing.dto.LegendRasterizingInfo;
import org.geomajas.plugin.rasterizing.dto.VectorLayerRasterizingInfo;

/**
 * Builder class for legend.
 * 
 * @author Jan De Moerloose
 * 
 */
public class LegendBuilder {

	public JComponent buildComponentTree(LegendRasterizingInfo legendRasterizingInfo) {
		LegendPanel legendPanel = new LegendPanel(legendRasterizingInfo.getTitle());
		for (ClientLayerInfo layer : legendRasterizingInfo.getMapInfo().getLayers()) {
			if (layer instanceof ClientVectorLayerInfo) {
				ClientVectorLayerInfo vectorLayer = (ClientVectorLayerInfo) layer;
				VectorLayerRasterizingInfo vectorRasterizingInfo = (VectorLayerRasterizingInfo) vectorLayer
						.getWidgetInfo(VectorLayerRasterizingInfo.WIDGET_KEY);
				for (FeatureStyleInfo style : vectorRasterizingInfo.getStyle().getFeatureStyles()) {
					legendPanel.addLayer(vectorLayer, style);
				}
			} else if (layer instanceof ClientRasterLayerInfo) {
				legendPanel.addLayer((ClientRasterLayerInfo) layer);
			} else {
				legendPanel.addLayer(layer);
			}
		}
		legendPanel.pack();
		return legendPanel;
	}

}
