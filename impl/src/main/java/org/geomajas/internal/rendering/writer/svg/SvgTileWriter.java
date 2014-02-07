/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.rendering.writer.svg;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.internal.rendering.writer.GraphicsWriter;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.rendering.GraphicsDocument;
import org.geomajas.rendering.RenderException;

/**
 * Writer for tiles.
 * 
 * @author Jan De Moerloose
 * @author Pieter De Graef
 */
public class SvgTileWriter implements GraphicsWriter {

	public void writeObject(Object object, GraphicsDocument document, boolean asChild) throws RenderException {
		InternalTile tile = (InternalTile) object;
		document.writeElement("g", true);
		document.writeId("features." + tile.getCode().toString());
		String style = null;
		for (InternalFeature feature : tile.getFeatures()) {
			FeatureStyleInfo featureStyle = feature.getStyleInfo();
			if (null != featureStyle) {
				String nextStyle = Integer.toString(featureStyle.getIndex());
				if (style == null || !style.equals(nextStyle)) {
					if (style != null) {
						document.closeElement();
						document.writeElement("g", false);
					} else {
						document.writeElement("g", true);
					}
					style = nextStyle;
					document.writeAttribute("style", parseStyle(featureStyle));
					document.writeId(nextStyle);
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

	private String parseStyle(FeatureStyleInfo style) {
		if (!(style.getIndex() == FeatureStyleInfo.DEFAULT_STYLE_INDEX)) {
			StringBuilder css = new StringBuilder();
			addToCss(css, "fill", style.getFillColor());
			addToCss(css, "fill-opacity", style.getFillOpacity());
			addToCss(css, "stroke", style.getStrokeColor());
			addToCss(css, "stroke-opacity", style.getStrokeOpacity());
			addToCss(css, "stroke-width", style.getStrokeWidth() + "px");
			addToCss(css, "stroke-dasharray", style.getDashArray());
			return css.toString();
		}
		return "display:none;";
	}

	private void addToCss(StringBuilder css, String name, float value) {
		addToCss(css, name, Float.toString(value));
	}

	private void addToCss(StringBuilder css, String name, String value) {
		if (value != null && value.length() > 0) {
			css.append(name);
			css.append(":");
			css.append(value);
			css.append(";");
		}
	}
}