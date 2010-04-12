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
import org.geomajas.gwt.client.util.DOM;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.smartgwt.client.util.SC;

/**
 * <p>
 * Decoder for style objects in Internet Explorer (VML). Is able to decode styles, but also to apply styles on DOM
 * elements and to retrieve them again (from DOM elements).
 * </p>
 * TODO: we could use a visitor pattern here for the different style implementations.
 *
 * @author Pieter De Graef
 */
public final class VmlStyleUtil {

	private VmlStyleUtil() {
	}

	/**
	 * Apply the given style onto a DOM element. This decoder will decide how to handle all the details.
	 *
	 * @param element
	 *            The target element onto whom we apply a style object.
	 * @param style
	 *            The style object to be applied.
	 */
	public static void applyStyle(Element element, Style style) {
		if (style != null && element != null) {
			if (style instanceof ShapeStyle) {
				applyShapeStyle(element, (ShapeStyle) style);
			} else if (style instanceof FontStyle) {
				FontStyle fontStyle = (FontStyle) style;
				element.getStyle().setProperty("fontSize", "" + fontStyle.getFontSize());
				element.getStyle().setProperty("color", fontStyle.getFillColor());
				element.getStyle().setProperty("fontFamily", fontStyle.getFontFamily());
				element.getStyle().setProperty("fontWeight", fontStyle.getFontWeight());
				element.getStyle().setProperty("fontStyle", fontStyle.getFontStyle());
			} else if (style instanceof PictureStyle) {
				PictureStyle picturStyle = (PictureStyle) style;
				if (SC.isIE()) { //TODO: it's a VML decoder, why make this check?
					element.getStyle().setProperty("filter", "alpha(opacity=" + 100 * picturStyle.getOpacity() + ")");
				} else {
					element.setAttribute("style", "opacity:" + picturStyle.getOpacity() + ";");
				}
			}
		}
	}

	/**
	 * Retrieve a ShapeStyle object from a DOM element. Note that this function will always return a shapestyle object,
	 * even if nothing is in it.
	 *
	 * @param element
	 *            The element to retrieve the style from.
	 * @return The ShapeStyle object retrieved from the element.
	 */
	public static ShapeStyle retrieveShapeStyle(Element element) {
		ShapeStyle style = new ShapeStyle();

		// Check the "fill" child-element:
		String filled = element.getAttribute("filled");
		if ("true".equals(filled)) {
			Element fill = element.getElementsByTagName("fill").getItem(0);
			style.setFillColor(fill.getAttribute("color"));
			style.setFillOpacity(Float.parseFloat(fill.getAttribute("opacity")));
		}

		// Check the "stroke" child-element:
		String stroked = element.getAttribute("stroked");
		if ("true".equals(stroked)) {
			Element stroke = element.getElementsByTagName("stroke").getItem(0);
			style.setFillColor(stroke.getAttribute("color"));
			style.setFillOpacity(Float.parseFloat(stroke.getAttribute("opacity")));
			style.setStrokeWidth(Integer.parseInt(stroke.getAttribute("strokeweight")));
		}
		return style;
	}

	// -------------------------------------------------------------------------
	// Private functions for DOM element manipulation:
	// -------------------------------------------------------------------------

	/**
	 * When applying ShapeStyles, we create child elements for fill and stroke. According to Microsoft, this is the
	 * proper way to go.
	 */
	private static void applyShapeStyle(Element element, ShapeStyle style) {
		// First check the presence of the fill and stroke elements:
		NodeList<Element> fills = element.getElementsByTagName("fill");
		if (fills.getLength() == 0) {
			Element stroke = DOM.createElementNS(DOM.NS_VML, "stroke");
			element.appendChild(stroke);
			Element fill = DOM.createElementNS(DOM.NS_VML, "fill");
			element.appendChild(fill);
			fills = element.getElementsByTagName("fill");
		}

		// Then if fill-color then filled=true:
		if (style.getFillColor() != null) {
			element.setAttribute("filled", "true");
			fills.getItem(0).setAttribute("color", style.getFillColor());
			fills.getItem(0).setAttribute("opacity", style.getFillOpacity() + "");
		} else {
			element.setAttribute("filled", "false");
		}

		// Then if stroke-color then stroke=true:
		if (style.getStrokeColor() != null) {
			element.setAttribute("stroked", "true");
			NodeList<Element> strokes = element.getElementsByTagName("stroke");
			strokes.getItem(0).setAttribute("color", style.getStrokeColor());
			strokes.getItem(0).setAttribute("opacity", style.getStrokeOpacity() + "");
			element.setAttribute("strokeweight", style.getStrokeWidth() + "");
		} else {
			element.setAttribute("stroked", "false");
		}
	}
}