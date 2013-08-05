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

package org.geomajas.smartgwt.client.spatial.geometry.operation;

import org.geomajas.geometry.Coordinate;
import org.geomajas.smartgwt.client.spatial.geometry.Geometry;
import org.geomajas.smartgwt.client.spatial.geometry.LineString;
import org.geomajas.smartgwt.client.spatial.geometry.LinearRing;

/**
 * This operation translates a single <code>Coordinate</code> from the coordinates array of a {@link org.geomajas
 * .smartgwt.client.spatial.geometry.LineString} or {@link org.geomajas.smartgwt.client.spatial.geometry.LinearRing}
 * geometry to another location.
 *
 * @author Pieter De Graef
 */
public class TranslateCoordinateOperation implements GeometryOperation {

	/**
	 * The integer index in the coordinates array. If this index is smaller then 0, then 0 is used instead. If this
	 * index is too large, then the last coordinate of the {@link org.geomajas.smartgwt.client.spatial.geometry
	 * .LineString} is used. (in case of a {@link org.geomajas.smartgwt.client.spatial.geometry.LinearRing},
	 * the last position before the closing coordinate is used)
	 */
	private int coordIndex;

	/**
	 * Translation value along the X-axis.
	 */
	private double translateX;

	/**
	 * Translation value along the Y-axis.
	 */
	private double translateY;

	/**
	 * This constructor sets all the necessary parameter values.
	 *
	 * @param coordIndex
	 *            The integer index in the coordinates array. If this index is smaller then 0, then 0 is used instead.
	 *            If this index is too large, then the last coordinate of the {@link org.geomajas.smartgwt.client
	 *            .spatial.geometry.LineString} is used. (in case of a {@link org.geomajas.smartgwt.client.spatial
	 *            .geometry.LinearRing}, the last position before the closing coordinate is used)
	 * @param translateX
	 *            Translation value along the X-axis.
	 * @param translateY
	 *            Translation value along the Y-axis.
	 */
	public TranslateCoordinateOperation(int coordIndex, double translateX, double translateY) {
		this.coordIndex = coordIndex;
		this.translateX = translateX;
		this.translateY = translateY;
	}

	/**
	 * Execute the operation!
	 *
	 * @return Returns a new geometry. If the given geometry is null, or the given geometry is not a {@link org
	 * .geomajas.smartgwt.client.spatial.geometry.LineString} or {@link org.geomajas.smartgwt.client.spatial.geometry
	 * .LinearRing}, then null is returned.
	 */
	public Geometry execute(Geometry geometry) {
		if (geometry == null) {
			return null;
		}
		if (geometry instanceof LinearRing) {
			if (coordIndex <= 0) {
				coordIndex = 0;
			} else if (coordIndex > geometry.getNumPoints() - 2) {
				coordIndex = geometry.getNumPoints() - 2;
			}
			Coordinate[] coordinates = new Coordinate[geometry.getNumPoints()];
			for (int i = 0; i < coordinates.length; i++) {
				Coordinate coordinate = geometry.getCoordinates()[i];
				if (i == coordIndex) {
					coordinates[i] = new Coordinate(coordinate.getX() + translateX, coordinate.getY() + translateY);
				} else if (i == geometry.getNumPoints() - 1) {
					coordinates[i] = coordinates[0];
				} else {
					coordinates[i] = coordinate;
				}
			}
			return geometry.getGeometryFactory().createLinearRing(coordinates);
		} else if (geometry instanceof LineString) {
			if (coordIndex <= 0) {
				coordIndex = 0;
			} else if (coordIndex > geometry.getNumPoints() - 1) {
				coordIndex = geometry.getNumPoints() - 1;
			}
			Coordinate[] coordinates = new Coordinate[geometry.getNumPoints()];
			for (int i = 0; i < coordinates.length; i++) {
				Coordinate coordinate = geometry.getCoordinates()[i];
				if (i == coordIndex) {
					coordinates[i] = new Coordinate(coordinate.getX() + translateX, coordinate.getY() + translateY);
				} else {
					coordinates[i] = geometry.getCoordinates()[i];
				}
			}
			return geometry.getGeometryFactory().createLineString(coordinates);
		}
		return null;
	}
}
