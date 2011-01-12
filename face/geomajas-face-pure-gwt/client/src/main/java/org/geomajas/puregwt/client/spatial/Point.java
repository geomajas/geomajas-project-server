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

package org.geomajas.puregwt.client.spatial;

import org.geomajas.global.Api;

/**
 * GWT client side implementation of a Point.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface Point extends Geometry {

	/**
	 * Return the X-ordinate for this point.
	 * 
	 * @return Returns the X-ordinate.
	 */
	double getX();

	/**
	 * Return the Y-ordinate for this point.
	 * 
	 * @return Returns the Y-ordinate.
	 */
	double getY();
}