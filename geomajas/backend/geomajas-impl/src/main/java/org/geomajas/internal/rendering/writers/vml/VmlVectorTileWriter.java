/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.internal.rendering.writers.vml;

import org.geomajas.configuration.StyleInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.internal.layer.feature.ClippedInternalFeature;
import org.geomajas.internal.layer.tile.InternalVectorTile;
import org.geomajas.internal.rendering.writers.GraphicsWriter;
import org.geomajas.layer.LayerType;
import org.geomajas.rendering.GraphicsDocument;
import org.geomajas.rendering.RenderException;

/**
 * This class writes the features of a feature tile as styled VML. Note that
 * there is no surrounding group element for the following reasons: - the result
 * can be immediately assigned to innerHTML in IE - the position of the tile in
 * screen space is not known yet - the result is always cacheable
 *
 * @author Jan De Moerloose
 */
public class VmlVectorTileWriter implements GraphicsWriter {

	private int coordWidth;

	private int coordHeight;

	public VmlVectorTileWriter(int coordWidth, int coordHeight) {
		this.coordWidth = coordWidth;
		this.coordHeight = coordHeight;
	}

	public void writeObject(Object object, GraphicsDocument document,
			boolean asChild) throws RenderException {
		InternalVectorTile tile = (InternalVectorTile) object;
		String style = null;
		for (org.geomajas.layer.feature.InternalFeature f : tile.getFeatures()) {
			ClippedInternalFeature feature = (ClippedInternalFeature) f;
			String nextStyle = feature.getStyleInfo().getId() + "";
			if (style == null || !style.equals(nextStyle)) {
				if (style != null) {
					document.closeElement();
					document.writeElement("vml:group", false);
					document.writeAttribute("coordsize", coordWidth + ","
							+ coordHeight);
					document.writeAttribute("style",
							"WIDTH: 100%; HEIGHT: 100%");
				} else {
					document.writeElement("vml:group", true);
					document.writeAttribute("coordsize", coordWidth + ","
							+ coordHeight);
					document.writeAttribute("style",
							"WIDTH: 100%; HEIGHT: 100%");
				}
				style = nextStyle;

				VectorLayerInfo layerInfo = (VectorLayerInfo) feature
						.getLayer().getLayerInfo();
				if (layerInfo.getLayerType() != LayerType.POINT
						&& layerInfo.getLayerType() != LayerType.MULTIPOINT) {
					// the shapetype
					document.writeElement("vml:shapetype", true);
					document.writeAttribute("id", feature.getLayer().getLayerInfo().getId()
							+ "." + style + ".style");
					document.writeAttribute("style",
							"WIDTH: 100%; HEIGHT: 100%");
					document.writeAttribute("style", "VISIBILITY: hidden");
					document.writeAttribute("filled", "t");
					document.writeAttribute("stroked", "t");
					document.writeAttribute("coordsize", coordWidth + "," + coordHeight);
					StyleInfo info = feature.getStyleInfo();
					document.writeAttribute("fillcolor", info.getFillColor());
					document.writeAttribute("strokecolor", info.getStrokeColor());
					document.writeAttribute("strokeweight", info.getStrokeWidth() + "px");
					// fill element
					document.writeElement("vml:stroke", true);
					document.writeAttribute("opacity", info.getStrokeOpacity());
					// style element
					document.writeElement("vml:fill", false);
					document.writeAttribute("opacity", info.getFillOpacity());
					// up to shapetype
					document.closeElement();
					// up to style group
					document.closeElement();
				}
				// now the feature
				document.writeObject(feature, true);
			} else {
				document.writeObject(feature, false);
			}
		}
	}

}