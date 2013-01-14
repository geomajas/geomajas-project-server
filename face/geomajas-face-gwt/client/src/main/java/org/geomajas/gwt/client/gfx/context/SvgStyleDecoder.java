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
