package org.geomajas.plugin.editing.client.snapping;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.spatial.MathService;

/**
 * Snapping algorithm that snaps to the first coordinate of the list of geometries (if within allowed snapping
 * distance).
 * 
 * @author Pieter De Graef
 */
public class FirstCoordinateSnappingAlgorithm implements SnappingAlgorithm {

	private Geometry[] geometries;

	private double calculatedDistance;

	private boolean hasSnapped;

	public Coordinate snap(Coordinate coordinate, double distance) {
		// Initialization:
		hasSnapped = false;
		calculatedDistance = distance;
		Coordinate result = coordinate;

		for (Geometry geometry : geometries) {
			double d = MathService.distance(getFirstCoordinate(geometry), coordinate);
			if (d < calculatedDistance) {
				calculatedDistance = d;
				result = getFirstCoordinate(geometry);
			}
		}

		return result;
	}

	public void setGeometries(Geometry[] geometries) {
		this.geometries = geometries;
	}

	public double getCalculatedDistance() {
		return calculatedDistance;
	}

	public boolean hasSnapped() {
		return hasSnapped;
	}

	private Coordinate getFirstCoordinate(Geometry geometry) {
		if (geometry == null) {
			return null;
		}
		if (geometry.getCoordinates() == null || geometry.getCoordinates().length == 0) {
			return getFirstCoordinate(geometry.getGeometries()[0]);
		}
		return geometry.getCoordinates()[0];
	}
}