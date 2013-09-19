/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.graphics.client.util;

/**
 * flip around X-axis, Y-axis, none or both.
 * 
 * @author Jan De Moerloose
 */
public enum FlipState {
	/**
	 * Flip around x-axis.
	 */
	FLIP_X,
	/**
	 * Flip around y-axis.
	 */
	FLIP_Y,
	/**
	 * Flip around x-axis, y-axis.
	 */
	FLIP_XY,
	/**
	 * No flipping.
	 */
	NONE
}
