/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.rendering.writer.vml;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.internal.rendering.writer.GraphicsWriter;
import org.geomajas.layer.LayerType;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.rendering.GraphicsDocument;
import org.geomajas.rendering.RenderException;

/**
 * This class writes the features of a feature tile as styled VML. Note that there is no surrounding group element for
 * the following reasons: - the result can be immediately assigned to innerHTML in IE - the position of the tile in
 * screen space is not known yet - the result is always cacheable.
 * 
 * @author Jan De Moerloose
 */
public class VmlTileWriter implements GraphicsWriter {

	private final int coordWidth;

	private final int coordHeight;

	public VmlTileWriter(int coordWidth, int coordHeight) {
		this.coordWidth = coordWidth;
		this.coordHeight = coordHeight;
	}

	public void writeObject(Object object, GraphicsDocument document, boolean asChild) throws RenderException {
		InternalTile tile = (InternalTile) object;
		String style = null;
		for (InternalFeature feature : tile.getFeatures()) {
			FeatureStyleInfo featureStyle = feature.getStyleInfo();
			if (null != featureStyle) {
				String nextStyle = Integer.toString(featureStyle.getIndex());
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
					if (layerInfo.getLayerType() != LayerType.POINT &&
							layerInfo.getLayerType() != LayerType.MULTIPOINT) {

						// the shapetype
						document.writeElement("vml:shapetype", true);
						document.writeAttribute("id", featureStyle.getStyleId());
						document.writeAttribute("style", "WIDTH: 100%; HEIGHT: 100%");
						document.writeAttribute("style", "VISIBILITY: hidden");
						document.writeAttribute("coordsize", coordWidth + "," + coordHeight);
						document.writeAttribute("fillcolor", featureStyle.getFillColor());
						document.writeAttribute("strokecolor", featureStyle.getStrokeColor());
						document.writeAttribute("strokeweight", featureStyle.getStrokeWidth() + "px");

						// Tile-fill element:
						document.writeElement("vml:fill", true);
						document.writeAttribute("opacity", Float.toString(featureStyle.getFillOpacity()));
						document.closeElement();

						// Tile-stroke element:
						document.writeElement("vml:stroke", true);
						document.writeAttribute("opacity", Float.toString(featureStyle.getStrokeOpacity()));
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
		if (style != null) {
			document.closeElement();
		}
		document.closeElement();

	}

}