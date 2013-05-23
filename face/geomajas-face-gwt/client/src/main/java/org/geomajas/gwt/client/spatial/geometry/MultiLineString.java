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

package org.geomajas.gwt.client.spatial.geometry;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.layer.LayerType;

/**
 * MultiLineString client-side GWT object.
 *
 * @author Pieter De Graef
 */
public class MultiLineString extends AbstractGeometry {

	private static final long serialVersionUID = 5319457800593690343L;

	private LineString[] lineStrings;

	// -------------------------------------------------------------------------
	// Constructor (package visibility)
	// -------------------------------------------------------------------------

	/**
	 * Constructor using srid and precision.
	 *
	 * @param srid srid
	 * @param precision precision
	 */
	MultiLineString(int srid, int precision) {
		super(srid, precision);
	}

	/**
	 * Constructor using srid, precision and the linestrings to combine.
	 *
	 * @param srid srid
	 * @param precision precision
	 * @param lineStrings linestrings to combine
	 */
	MultiLineString(int srid, int precision, LineString[] lineStrings) {
		super(srid, precision);
		this.lineStrings = lineStrings;
	}

	// -------------------------------------------------------------------------
	// Geometry implementation:
	// -------------------------------------------------------------------------

	@Override
	public Object clone() { // NOSONAR super.clone() not supported by GWT
		return getGeometryFactory().createMultiLineString(lineStrings);
	}

	@Override
	public Geometry getGeometryN(int n) {
		if (!isEmpty()) {
			if (n >= 0 && n < lineStrings.length) {
				return lineStrings[n];
			}
		}
		return this;
	}

	@Override
	public int getNumGeometries() {
		if (isEmpty()) {
			return 0;
		}
		return lineStrings.length;
	}

	@Override
	public int getNumPoints() {
		if (isEmpty()) {
			return 0;
		}
		int sum = 0;
		for (LineString lineString : lineStrings) {
			sum += lineString.getNumPoints();
		}
		return sum;
	}

	LineString[] getLineStrings() {
		return lineStrings;
	}

	void setLineStrings(LineString[] lineStrings) {
		this.lineStrings = lineStrings;
	}

	@Override
	public double getLength() {
		double len = 0;
		if (!isEmpty()) {
			for (LineString lineString : lineStrings) {
				len += lineString.getLength();
			}
		}
		return len;
	}

	@Override
	public Coordinate getCentroid() {
		if (isEmpty()) {
			return null;
		}
		double sumX = 0;
		double sumY = 0;
		double totalLength = 0;
		Coordinate[] coordinates = new Coordinate[lineStrings.length];
		for (int i = 0; i < lineStrings.length; i++) {
			coordinates[i] = lineStrings[i].getCentroid();
		}
		for (int i = 0; i < coordinates.length - 1; i++) {
			double length = coordinates[i].distance(coordinates[i + 1]);
			totalLength += length;
			double midx = (coordinates[i].getX() + coordinates[i + 1].getX()) / 2;
			sumX += length * midx;
			double midy = (coordinates[i].getY() + coordinates[i + 1].getY()) / 2;
			sumY += length * midy;
		}
		return new Coordinate(sumX / totalLength, sumY / totalLength);
	}

	/**
	 * Return the minimal distance between this coordinate and any vertex of the geometry.
	 *
	 * @return Return the minimal distance
	 */
	public double getDistance(Coordinate coordinate) {
		double distance = Double.MAX_VALUE;
		if (!isEmpty()) {
			for (LineString lineString : lineStrings) {
				double d = lineString.getDistance(coordinate);
				if (d < distance) {
					distance = d;
				}
			}
		}
		return distance;
	}

	@Override
	public boolean isEmpty() {
		return (lineStrings == null || lineStrings.length == 0);
	}

	@Override
	public boolean isValid() {
		if (isEmpty()) {
			return true;
		}
		for (LineString lineString : lineStrings) {
			if (!lineString.isValid()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isSimple() {
		if (isEmpty()) {
			return true;
		}
		for (int i = 0; i < lineStrings.length - 1; i++) {
			for (int j = i + 1; j < lineStrings.length; j++) {
				if (lineStrings[i].intersects(lineStrings[j])) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public Bbox getBounds() {
		Bbox bounds = null;
		if (!isEmpty()) {
			for (LineString lineString : lineStrings) {
				if (bounds == null) {
					bounds = lineString.getBounds();
				} else {
					bounds = bounds.union(lineString.getBounds());
				}
			}
		}
		return bounds;
	}

	@Override
	public Coordinate getCoordinate() {
		if (isEmpty()) {
			return null;
		}
		return lineStrings[0].getCoordinate();
	}

	/**
	 * Get the full concatenated list of coordinates of all the LineStrings in this MultiLineString geometry.
	 */
	public Coordinate[] getCoordinates() {
		if (isEmpty()) {
			return null;
		}
		Coordinate[] coordinates = new Coordinate[getNumPoints()];
		int count = 0;
		for (LineString lineString : lineStrings) {
			for (int j = 0; j < lineString.getNumPoints(); j++) {
				coordinates[count++] = lineString.getCoordinateN(j);
			}
		}
		return coordinates;
	}

	@Override
	public LayerType getLayerType() {
		return LayerType.MULTILINESTRING;
	}

	/**
	 * Check the LineStrings one by one for intersections with the given geometry.
	 */
	public boolean intersects(Geometry geometry) {
		if (isEmpty()) {
			return false;
		}
		for (LineString lineString : lineStrings) {
			if (lineString.intersects(geometry)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String toWkt() {
		if (isEmpty()) {
			return "MULTILINESTRING EMPTY";
		}
		String wkt = "MULTILINESTRING (";
		for (int i = 0; i < lineStrings.length; i++) {
			String lineWkt = lineStrings[i].toWkt();
			if (i > 0) {
				wkt += ",";
			}
			wkt += lineWkt.substring(lineWkt.indexOf("("));
		}
		return wkt + ")";
	}
}
