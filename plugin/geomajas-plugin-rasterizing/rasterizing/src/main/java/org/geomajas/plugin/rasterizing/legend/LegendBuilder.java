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
import org.geomajas.plugin.rasterizing.dto.LayerMetadata;
import org.geomajas.plugin.rasterizing.dto.LegendMetadata;
import org.geomajas.plugin.rasterizing.dto.RasterLayerMetadata;
import org.geomajas.plugin.rasterizing.dto.VectorLayerMetadata;

/**
 * Builder class for legend.
 * 
 * @author Jan De Moerloose
 * 
 */
public class LegendBuilder {


	public JComponent buildComponentTree(LegendMetadata legendMetadata) {
		LegendPanel legendPanel = new LegendPanel(legendMetadata.getTitle());
		for (LayerMetadata layer : legendMetadata.getMapMetadata().getLayers()) {
			if (layer instanceof VectorLayerMetadata) {
				VectorLayerMetadata vectorLayer = (VectorLayerMetadata) layer;
				for (FeatureStyleInfo style : vectorLayer.getStyle().getFeatureStyles()) {
					legendPanel.addLayer(vectorLayer, style);
				}
			} else if (layer instanceof RasterLayerMetadata) {
				legendPanel.addLayer((RasterLayerMetadata) layer);
			} else {
				legendPanel.addLayer(layer);
			}
		}
		legendPanel.pack();
		return legendPanel;
	}

}
