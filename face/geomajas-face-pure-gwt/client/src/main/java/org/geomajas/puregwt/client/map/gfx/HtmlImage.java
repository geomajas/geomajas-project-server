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

package org.geomajas.puregwt.client.map.gfx;

import org.geomajas.puregwt.client.spatial.Bbox;

import com.google.gwt.user.client.DOM;

/**
 * <p>
 * Extension of an HtmlObject that represents a single HTML image (IMG tag). Next to the default values provided by the
 * HtmlObject, an extra 'href' field is provided. This string value should point to the actual image.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class HtmlImage extends HtmlObject {

	/**
	 * Create an HtmlImage widget that represents an HTML IMG element.
	 * 
	 * @param href
	 *            Pointer to the actual image.
	 * @param width
	 *            The width for this image, expressed in pixels.
	 * @param height
	 *            The height for this image, expressed in pixels.
	 * @param top
	 *            How many pixels should this image be placed from the top (relative to the parent origin).
	 * @param left
	 *            How many pixels should this image be placed from the left (relative to the parent origin).
	 */
	public HtmlImage(String href, int width, int height, int top, int left) {
		super("img", width, height, top, left);
		DOM.setStyleAttribute(getElement(), "border", "0px");
		DOM.setElementProperty(getElement(), "src", href);
	}

	/**
	 * Create an HtmlImage widget that represents an HTML IMG element.
	 * 
	 * @param href
	 *            Pointer to the actual image.
	 * @param bounds
	 *            Bounding box that determines the actual size and position for this image.
	 */
	public HtmlImage(String href, Bbox bounds) {
		this(href, (int) Math.round(bounds.getWidth()), (int) Math.round(bounds.getHeight()), (int) Math.round(bounds
				.getY()), (int) Math.round(bounds.getX()));
	}

	/**
	 * Get the pointer to the actual image. In HTML this is represented by the 'src' attribute in an IMG element.
	 * 
	 * @return The pointer to the actual image.
	 */
	public String getHref() {
		return DOM.getElementProperty(getElement(), "src");
	}

	/**
	 * Set the pointer to the actual image. In HTML this is represented by the 'src' attribute in an IMG element.
	 * 
	 * @param href
	 *            The new image pointer.
	 */
	public void setHref(String href) {
		DOM.setElementProperty(getElement(), "src", href);
	}
}