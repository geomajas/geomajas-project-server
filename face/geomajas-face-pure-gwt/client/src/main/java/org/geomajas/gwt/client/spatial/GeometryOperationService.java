package org.geomajas.gwt.client.spatial;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;


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