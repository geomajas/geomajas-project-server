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

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.internal.rendering.writers.GraphicsWriter;
import org.geomajas.layer.LayerType;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.rendering.GraphicsDocument;
import org.geomajas.rendering.RenderException;

/**
 * This class writes the features of a feature tile as styled VML. Note that there is no surrounding group element for
 * the following reasons: - the result can be immediately assigned to innerHTML in IE - the position of the tile in
 * screen space is not known yet - the result is always cachable.
 * 
 * @author Jan De Moerloose
 */
public class VmlTileWriter implements GraphicsWriter {

	private int coordWidth;

	private int coordHeight;

	public VmlTileWriter(int coordWidth, int coordHeight) {
		this.coordWidth = coordWidth;
		this.coordHeight = coordHeight;
	}

	public void writeObject(Object object, GraphicsDocument document, boolean asChild) throws RenderException {
		InternalTile tile = (InternalTile) object;
		String style = null;
		for (InternalFeature feature : tile.getFeatures()) {
			String nextStyle = feature.getStyleInfo().getIndex() + "";
			if (style == null || !style.equals(nextStyle)) {
				if (style != null) {
					document.closeElement();
					document.writeElement("vml:group", false);
					document.writeAttribute("coordsize", coordWidth + "," + coordHeight);
					document.writeAttribute("style", "WIDTH: " + coordWidth + "; HEIGHT: " + coordHeight);
				} else {
					document.writeElement("vml:group", true);
					document.writeAttribute("coordsize", coordWidth + "," + coordHeight);
					document.writeAttribute("style", "WIDTH: " + coordWidth + "; HEIGHT: " + coordHeight);
				}
				style = nextStyle;

				VectorLayerInfo layerInfo = feature.getLayer().getLayerInfo();
				if (layerInfo.getLayerType() != LayerType.POINT && layerInfo.getLayerType() != LayerType.MULTIPOINT) {

					// the shapetype
					FeatureStyleInfo info = feature.getStyleInfo();
					document.writeElement("vml:shapetype", true);
					document.writeAttribute("id", info.getStyleId());
					document.writeAttribute("style", "WIDTH: 100%; HEIGHT: 100%");
					document.writeAttribute("style", "VISIBILITY: hidden");
					document.writeAttribute("coordsize", coordWidth + "," + coordHeight);
					document.writeAttribute("fillcolor", info.getFillColor());
					document.writeAttribute("strokecolor", info.getStrokeColor());
					document.writeAttribute("strokeweight", info.getStrokeWidth() + "px");

					// Tile-fill element:
					document.writeElement("vml:fill", true);
					document.writeAttribute("opacity", Float.toString(info.getFillOpacity()));
					document.closeElement();

					// Tile-stroke element:
					document.writeElement("vml:stroke", true);
					document.writeAttribute("opacity", Float.toString(info.getStrokeOpacity()));
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