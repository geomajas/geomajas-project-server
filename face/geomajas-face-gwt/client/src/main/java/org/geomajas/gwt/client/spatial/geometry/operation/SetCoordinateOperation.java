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

package org.geomajas.gwt.client.spatial.geometry.operation;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.spatial.geometry.LinearRing;

/**
 * This operation set new values for a <code>Coordinate</code> in the coordinates array of a {@link LineString} or
 * {@link LinearRing} geometry.
 *
 * @author Pieter De Graef
 */
public class SetCoordinateOperation implements GeometryOperation {

	/**
	 * The integer index in the coordinates array. If this index is smaller then 0, then 0 is used instead. If this
	 * index is too large, then the last coordinate of the {@link LineString} is updated (in the case of a
	 * {@link LinearRing}, it will be the last coordinate before the closing coordinate).
	 */
	private int coordIndex;

	/**
	 * The new coordinate value.
	 */
	private Coordinate coordinate;

	/**
	 * Initialize the operation with the correct parameters.
	 *
	 * @param coordIndex
	 *            The integer index in the coordinates array. If this index is smaller then 0, then 0 is used instead.
	 *            If this index is too large, then the last coordinate of the {@link LineString} is updated (in the case
	 *            of a {@link LinearRing}, it will be the last coordinate before the closing coordinate).
	 * @param coordinate
	 *            The new coordinate value.
	 */
	public SetCoordinateOperation(int coordIndex, Coordinate coordinate) {
		this.coordIndex = coordIndex;
		this.coordinate = coordinate;
	}

	/**
	 * Execute the operation!
	 *
	 * @return Returns a new geometry. If the given geometry is null, or the given geometry is not a {@link LineString}
	 *         or {@link LinearRing}, then null is returned.
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
			for (int i = 0; i < geometry.getNumPoints(); i++) {
				if (i == coordIndex) {
					coordinates[i] = coordinate;
				} else if (i == geometry.getNumPoints() - 1) {
					coordinates[i] = coordinates[0];
				} else {
					coordinates[i] = geometry.getCoordinates()[i];
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
			for (int i = 0; i < geometry.getNumPoints(); i++) {
				if (i == coordIndex) {
					coordinates[i] = coordinate;
				} else {
					coordinates[i] = geometry.getCoordinates()[i];
				}
			}
			return geometry.getGeometryFactory().createLineString(coordinates);
		}
		return null;
	}

}
