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
	 * @param mapPresenter
	 *            The map upon which this gadget is to be attached.
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
}