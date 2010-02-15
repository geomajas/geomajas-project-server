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

package org.geomajas.internal.rendering.writers.svg;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.internal.layer.feature.InternalFeatureImpl;
import org.geomajas.internal.layer.tile.InternalTileImpl;
import org.geomajas.internal.rendering.writers.GraphicsWriter;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.rendering.GraphicsDocument;
import org.geomajas.rendering.RenderException;

/**
 * ???
 * 
 * @author check subversion
 */
public class SvgFeatureTileWriter implements GraphicsWriter {

	public void writeObject(Object object, GraphicsDocument document, boolean asChild) throws RenderException {
		InternalTileImpl tile = (InternalTileImpl) object;
		document.writeElement("g", true);
		document.writeId("features." + tile.getCode().toString());
		String style = null;
		for (InternalFeature f : tile.getFeatures()) {
			InternalFeatureImpl feature = (InternalFeatureImpl) f;
			String nextStyle = feature.getStyleInfo().getIndex() + "";
			if (style == null || !style.equals(nextStyle)) {
				if (style != null) {
					document.closeElement();
					document.writeElement("g", false);
				} else {
					document.writeElement("g", true);
				}
				style = nextStyle;
				document.writeAttribute("style", parseStyle(feature.getStyleInfo()));
				document.writeId(feature.getStyleInfo().getIndex() + "");
				document.writeObject(feature, true);
			} else {
				document.writeObject(feature, false);
			}
		}
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