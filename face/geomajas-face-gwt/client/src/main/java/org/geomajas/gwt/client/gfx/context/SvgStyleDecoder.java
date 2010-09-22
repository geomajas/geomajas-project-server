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

package org.geomajas.gwt.client.gfx.context;

import org.geomajas.gwt.client.gfx.style.FontStyle;
import org.geomajas.gwt.client.gfx.style.PictureStyle;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.gfx.style.Style;

/**
 * Decoder for Style objects in an SVG environment. SVG uses standard CSS as styling language.
 *
 * @author Pieter De Graef
 */
public final class SvgStyleDecoder {

	private SvgStyleDecoder() {
	}

	/**
	 * Return the CSS equivalent of the Style object.
	 * @param style
	 * @return
	 */
	public static String decode(Style style) {
		if (style != null) {
			if (style instanceof ShapeStyle) {
				return decode((ShapeStyle) style);
			} else if (style instanceof FontStyle) {
				return decode((FontStyle) style);
			} else if (style instanceof PictureStyle) {
				return decode((PictureStyle) style);
			}
		}
		return "";
	}

	// -------------------------------------------------------------------------
	// Private decode methods for each Style class:
	// -------------------------------------------------------------------------

	private static String decode(ShapeStyle style) {
		String css = "";
		if (style.getFillColor() != null && !"".equals(style.getFillColor())) {
			css += "fill:" + style.getFillColor() + ";";
		}
		css += "fill-opacity:" + style.getFillOpacity() + ";";
		if (style.getStrokeColor() != null && !"".equals(style.getStrokeColor())) {
			css += "stroke:" + style.getStrokeColor() + ";";
		}
		css += "stroke-opacity:" + style.getStrokeOpacity() + ";";
		if (style.getStrokeWidth() >= 0) {
			css += "stroke-width:" + style.getStrokeWidth() + ";";
		}
		return css;
	}

	private static String decode(FontStyle style) {
		String css = "";
		if (style.getFillColor() != null && !"".equals(style.getFillColor())) {
			css += "fill:" + style.getFillColor() + ";";
		}
		if (style.getFontFamily() != null && !"".equals(style.getFontFamily())) {
			css += "font-family:" + style.getFontFamily() + ";";
		}
		if (style.getFontStyle() != null && !"".equals(style.getFontStyle())) {
			css += "font-style:" + style.getFontStyle() + ";";
		}
		if (style.getFontWeight() != null && !"".equals(style.getFontWeight())) {
			css += "font-weight:" + style.getFontWeight() + ";";
		}
		if (style.getFontSize() >= 0) {
			css += "stroke-width:" + style.getFontSize() + ";";
		}
		return css;
	}

	private static String decode(PictureStyle style) {
		return "opacity:" + style.getOpacity() + ";";
	}
}
