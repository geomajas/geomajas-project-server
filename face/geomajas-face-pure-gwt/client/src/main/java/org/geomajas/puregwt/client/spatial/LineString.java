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

import org.geomajas.geometry.Coordinate;
import org.geomajas.global.FutureApi;

/**
 * LineString client-side GWT object.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@FutureApi(allMethods = true)
public interface LineString extends Geometry {

	/**
	 * Is this line string closed or not? Closed means that the last point equals the first one. Typically
	 * {@link LineString} geometries are not closed, while {@link LinearRing} geometries are. For a {@link LinearRing}
	 * it is a necessity.
	 * 
	 * @return Returns true or false.
	 */
	boolean isClosed();

	/**
	 * Return the coordinate at the given index.
	 * 
	 * @param index
	 *            The index to search.
	 * @return Returns the coordinate, or null if no coordinate could be found at the given index.
	 */
	Coordinate getCoordinateN(int index);
}