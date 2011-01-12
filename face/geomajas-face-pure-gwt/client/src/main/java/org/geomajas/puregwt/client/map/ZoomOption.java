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
 * Zoom options. These express the different ways to zoom in and out on a map.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api
public enum ZoomOption {

	/** Zoom exactly to the new scale. This is only possible if no resolutions have been defined on the map. */
	EXACT,

	/**
	 * Zoom to a scale level that is different from the current (lower or higher according to the new scale, only if
	 * allowed of course).
	 */
	LEVEL_CHANGE,

	/** Zoom to a scale level that is as close as possible to the new scale. */
	LEVEL_CLOSEST,

	/** Zoom to a scale level that makes the bounds fit inside our view. */
	LEVEL_FIT
}