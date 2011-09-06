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

import org.geomajas.annotation.FutureApi;

/**
 * Polygon client-side GWT object.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@FutureApi(allMethods = true)
public interface Polygon extends Geometry {

	/**
	 * Get the outermost LinearRing of the polygon.
	 */
	LinearRing getExteriorRing();

	/**
	 * Get one of the interior LinearRing geometries. i.e. one of the holes.
	 * 
	 * @param n
	 *            Index in the geometry's interior rings.
	 * @return Returns the hole if it exists, null otherwise.
	 */
	LinearRing getInteriorRingN(int n);

	/**
	 * Get the total number of interior rings (holes) in the polygon.
	 * 
	 * @return
	 */
	int getNumInteriorRing();
}