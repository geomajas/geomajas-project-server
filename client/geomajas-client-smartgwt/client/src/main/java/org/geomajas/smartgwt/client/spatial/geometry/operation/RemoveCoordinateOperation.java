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
 * This operation removes a <code>Coordinate</code> from the coordinates array of a {@link LineString} or
 * {@link org.geomajas.smartgwt.client.spatial.geometry.LinearRing} geometry.
 *
 * @author Pieter De Graef
 */
public class RemoveCoordinateOperation implements GeometryOperation {

	/**
	 * The integer index in the coordinates array. If this index is smaller then 0, then the first coordinate is
	 * removed. If this index is too large, then the last coordinate is removed (in the case of a {@link org
	 * .geomajas.smartgwt.client.spatial.geometry.LinearRing},
	 * the last coordinate before the closing coordinate is removed).
	 */
	private int coordIndex;

	/**
	 * Initialize the operation with the correct parameters.
	 *
	 * @param coordIndex
	 *            The integer index in the coordinates array. If this index is smaller then 0, then the first coordinate
	 *            is removed. If this index is too large, then the last coordinate is removed (in the case of a
	 *            {@link org.geomajas.smartgwt.client.spatial.geometry.LinearRing},
	 *            the last coordinate before the  closing coordinate is removed).
	 */
	public RemoveCoordinateOperation(int coordIndex) {
		this.coordIndex = coordIndex;
	}

	/**
	 * Execute the operation!
	 *
	 * @return Returns a new geometry. If the given geometry is null, or the given geometry is not a {@link LineString}
	 *         or {@link org.geomajas.smartgwt.client.spatial.geometry.LinearRing}, then null is returned.
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
			Coordinate[] coordinates = new Coordinate[geometry.getNumPoints() - 1];
			int count = 0;
			for (int i = 0; i < geometry.getNumPoints(); i++) {
				if (i == coordinates.length) {
					coordinates[count++] = coordinates[0];
				} else if (i != coordIndex) {
					coordinates[count++] = geometry.getCoordinates()[i];
				}
			}
			return geometry.getGeometryFactory().createLinearRing(coordinates);
		} else if (geometry instanceof LineString) {
			if (coordIndex <= 0) {
				coordIndex = 0;
			} else if (coordIndex > geometry.getNumPoints() - 1) {
				coordIndex = geometry.getNumPoints() - 1;
			}
			Coordinate[] coordinates = new Coordinate[geometry.getNumPoints() - 1];
			int count = 0;
			for (int i = 0; i < geometry.getNumPoints(); i++) {
				if (i != coordIndex) {
					coordinates[count++] = geometry.getCoordinates()[i];
				}
			}
			return geometry.getGeometryFactory().createLineString(coordinates);
		}
		return null;
	}

}
