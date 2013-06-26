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
package org.geomajas.puregwt.client.gfx;

import org.geomajas.annotation.Api;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * <p>
 * Basic HTML element definition used in map rendering. Note that all HtmlObjects are actually GWT widgets.This class
 * will create an HTML element in the constructor and set as many style info as is provided. Also all HtmlObjects will
 * have an absolute positioning.
 * </p>
 * <p>
 * All getters and setters work immediately on the underlying HTML element for performance reasons. Extensions of this
 * class can represent individual HTML tags such as images, but also groups (DIV). In this case of a group (see
 * HtmlContainer) it is possible to attach multiple other HtmlObjects to create a tree structure.
 * </p>
 * <p>
 * Note that this interface extends the {@link IsWidget} interface, so DOM manipulation is always possible.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface HtmlObject extends IsWidget {

	/**
	 * Get the parent widget for this HtmlObject.
	 * 
	 * @return The parent widget.
	 */
	Widget getParent();

	/**
	 * Sets the object's width pixels. This width does not include decorations such as border, margin, and padding.
	 * 
	 * @return The current width for this object.
	 */
	int getWidth();

	/**
	 * Set a new width (in pixels) onto this object.
	 * 
	 * @param width
	 *            The new value.
	 */
	void setWidth(int width);

	/**
	 * Sets the object's height in pixels. This width does not include decorations such as border, margin, and padding.
	 * 
	 * @return The current height for this object.
	 */
	int getHeight();

	/**
	 * Set a new height (in pixels) onto this object.
	 * 
	 * @param height
	 *            The new value.
	 */
	void setHeight(int height);

	/**
	 * Get the number of pixels this object is shifted to the right relative to the parent widget.
	 * 
	 * @return The CSS style value "left".
	 */
	int getLeft();

	/**
	 * Set the number of pixels this object is shifted to the right relative to the parent widget.
	 * 
	 * @param left
	 *            The CSS style value "left".
	 */
	void setLeft(int left);
	
	/**
	 * Get the number of pixels this object is shifted to the bottom relative to the parent widget.
	 * 
	 * @return The CSS style value "top".
	 */
	int getTop();

	/**
	 * Set the number of pixels this object is shifted to the bottom relative to the parent widget.
	 * 
	 * @param left
	 *            The CSS style value "top".
	 */
	void setTop(int top);
	
	/**
	 * Get the current opacity on this object.
	 * 
	 * @return The current opacity value.
	 */
	double getOpacity();

	/**
	 * Set the opacity value onto this object.
	 * 
	 * @param opacity
	 *            The new value.
	 */
	void setOpacity(double opacity);

	/**
	 * Determine whether or not this object should be visible.
	 * 
	 * @param visible
	 *            The visible parameter.
	 */
	void setVisible(boolean visible);

	/**
	 * Is this object currently visible or not?
	 * 
	 * @return
	 */
	boolean isVisible();
}