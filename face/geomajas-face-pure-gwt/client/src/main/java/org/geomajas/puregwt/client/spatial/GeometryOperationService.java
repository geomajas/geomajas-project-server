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
import org.geomajas.geometry.Geometry;

/**
 * to do....
 * 
 * @author Pieter De Graef
 */
public interface GeometryOperationService {

	Geometry translate(Geometry original, double deltaX, double deltaY);
	
	Geometry insertCoordinate(Geometry original, Coordinate coordinate, int index);

	Geometry removeCoordinate(Geometry original, int index);

	Geometry updateCoordinate(Geometry original, Coordinate coordinate, int index);

	Geometry translateCoordinate(Geometry original, int index, double deltaX, double deltaY);

	Geometry addInnerGeometry(Geometry original, Geometry inner);

	Geometry insertInnerGeometry(Geometry original, Geometry inner, int index);

	Geometry removeInnerGeometry(Geometry original, int index);
}