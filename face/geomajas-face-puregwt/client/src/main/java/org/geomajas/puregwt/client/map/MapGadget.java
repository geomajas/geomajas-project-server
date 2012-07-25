/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client.map;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.layout.client.Layout.Alignment;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * <p>
 * Definition for an autonomous widgets which can be displayed on the map. Examples are the scale bar, a north arrow,
 * the panning and zooming buttons, ...
 * </p>
 * <p>
 * The goal of this interface is to provide an easy way to have the gadget positioned.
 * </p>
 * <p>
 * The position of a MapGadget is maintained by the face, but you have to make sure that the container has a
 * "position: absolute" style.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@UserImplemented
@Api(allMethods = true)
public interface MapGadget extends IsWidget {

	/**
	 * Method executed before the gadget is actually attached to the map's DOM tree.
	 * 
	 * @param mapPresenter The map upon which this gadget is to be attached.
	 */
	void beforeDraw(MapPresenter mapPresenter);

	/**
	 * Should the gadget be left or right aligned, or should it fill the map area? This value is read upon attaching the
	 * gadget to the map.
	 * 
	 * @return The horizontal alignment (begin=left, end=right, or stretch=fill).
	 */
	Alignment getHorizontalAlignment();

	/**
	 * Should the gadget be top or bottom aligned, or should it fill the map area? This value is read upon attaching the
	 * gadget to the map.
	 * 
	 * @return The horizontal alignment (begin=top, end=bottom, or stretch=fill).
	 */
	Alignment getVerticalAlignment();

	/**
	 * What's the horizontal margin? How much space should be kept between the gadget and the map edge (left or right)?
	 * This value is ignored in case of stretch alignment.
	 * 
	 * @return The number of pixels to keep between the gadget and the map edge.
	 */
	int getHorizontalMargin();

	/**
	 * What's the vertical margin? How much space should be kept between the gadget and the map edge (top or bottom)?
	 * This value is ignored in case of stretch alignment.
	 * 
	 * @return The number of pixels to keep between the gadget and the map edge.
	 */
	int getVerticalMargin();

	/**
	 * Get the width of the gadget.
	 * 
	 * @return width in pixels
	 */
	int getWidth();

	/**
	 * Get the height of the gadget.
	 * 
	 * @return height in pixels
	 */
	int getHeight();

	/**
	 * Set the width of the gadget.
	 * 
	 * @param width width in pixels
	 */
	void setWidth(int width);

	/**
	 * Set the height of the gadget.
	 * 
	 * @param height height in pixels
	 */
	void setHeight(int height);

	/**
	 * Set the top css property.
	 * 
	 * @param top top
	 */
	void setTop(int top);

	/**
	 * Set the left css property.
	 * 
	 * @param left left
	 */
	void setLeft(int left);

	/**
	 * Add a handler for resize events.
	 * 
	 * @param resizeHandler the handler
	 */
	void addResizeHandler(ResizeHandler resizeHandler);

}