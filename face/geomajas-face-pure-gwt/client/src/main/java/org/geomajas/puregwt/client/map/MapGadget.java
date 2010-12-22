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

package org.geomajas.puregwt.client.map;

/**
 * Definition for an autonomous gadget which can be displayed on the map. These gadgets receive some events from the map
 * and should take care of there own rendering and cleanup.<br/>
 * Examples are the scale bar, a north arrow, the panning and zooming buttons, ...
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 */
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
	void onDraw(ViewPort viewPort, VectorContainer container);

	/** This method is automatically called when panning has occurred on the map. */
	void onPan();

	/** This method is automatically called when scaling has occurred on the map. */
	void onScale();

	/** This method is automatically called when the map has been resized. */
	void onResize();

	/** Called when the gadget is removed from the map. In this method, the gadget should clean itself up. */
	void onDestroy();
}