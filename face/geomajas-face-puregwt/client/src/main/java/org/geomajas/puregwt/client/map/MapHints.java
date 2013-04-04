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

package org.geomajas.puregwt.client.map;

import org.geomajas.annotation.Api;

/**
 * Map specific hints.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public enum MapHints {

	/**
	 * Parameter used to determine how long the animations should take during navigation (zooming). The value should be
	 * expressed in milliseconds. It's value should be of type <code>Long</code>.
	 * 
	 * @since 1.0.0
	 */
	ANIMATION_TIME;
}