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

package org.geomajas.puregwt.client.map;

import org.geomajas.global.Api;

/**
 * Definition for an autonomous gadget which can be displayed on the map. These gadgets receive some events from the map
 * and should take care of there own rendering and cleanup.<br/>
 * Examples are the scale bar, a north arrow, the panning and zooming buttons, ...
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api
public interface MapGadget {

	/**
	 * The initial drawing method, which is called when the gadget is initialized on the map. In this method, the gadget
	 * should draw and initialize itself.
	 * 
	 * @param viewPort
	 *            The view port of the map.
	 * @param container
	 *            A vector container in screen space into which this gadget can draw itself.
	 */
	void onDraw(ViewPort viewPort, ScreenContainer container);

	/** This method is automatically called when panning has occurred on the map. */
	void onPan();

	/** This method is automatically called when scaling has occurred on the map. */
	void onScale();

	/** This method is automatically called when the map has been resized. */
	void onResize();

	/** Called when the gadget is removed from the map. In this method, the gadget should clean itself up. */
	void onDestroy();
}