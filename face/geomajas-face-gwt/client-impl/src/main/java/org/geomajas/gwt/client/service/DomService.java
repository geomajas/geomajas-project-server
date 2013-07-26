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

package org.geomajas.gwt.client.service;

import org.geomajas.gwt.client.util.Dom;

import com.google.gwt.user.client.Element;

/**
 * General service for DOM operations.
 * 
 * @author Pieter De Graef
 */
public final class DomService {

	private DomService() {
	}

	/**
	 * Apply the "top" style attribute on the given element.
	 * 
	 * @param element
	 *            The DOM element.
	 * @param top
	 *            The top value.
	 */
	public static void setTop(Element element, String top) {
		setTop(element, sizeToInt(top));
	}

	/**
	 * Apply the "top" style attribute on the given element.
	 * 
	 * @param element
	 *            The DOM element.
	 * @param top
	 *            The top value.
	 */
	public static void setTop(Element element, int top) {
		if (Dom.isIE()) { // Limitation in IE8...
			while (top > 1000000) {
				top -= 1000000;
			}
			while (top < -1000000) {
				top += 1000000;
			}
		}
		Dom.setStyleAttribute(element, "top", top + "px");
	}

	/**
	 * Apply the "left" style attribute on the given element.
	 * 
	 * @param element
	 *            The DOM element.
	 * @param left
	 *            The left value.
	 */
	public static void setLeft(Element element, String left) {
		setLeft(element, sizeToInt(left));
	}

	/**
	 * Apply the "left" style attribute on the given element.
	 * 
	 * @param element
	 *            The DOM element.
	 * @param left
	 *            The left value.
	 */
	public static void setLeft(Element element, int left) {
		if (Dom.isIE()) { // Limitation in IE8...
			while (left > 1000000) {
				left -= 1000000;
			}
			while (left < -1000000) {
				left += 1000000;
			}
		}
		Dom.setStyleAttribute(element, "left", left + "px");
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private static int sizeToInt(String size) {
		int position = size.indexOf('p');
		if (position < 0) {
			return Integer.parseInt(size);
		}
		return Integer.parseInt(size.substring(0, position));
	}
}