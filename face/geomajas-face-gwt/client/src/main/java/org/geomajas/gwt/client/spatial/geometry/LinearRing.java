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
import org.geomajas.gwt.client.spatial.Mathlib;

/**
 * LinearRing client-side GWT object.
 *
 * @author Pieter De Graef
 */
public class LinearRing extends LineString {

	private static final long serialVersionUID = 4314580899552986922L;

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------

	LinearRing(int srid, int precision) {
		super(srid, precision);
	}

	LinearRing(int srid, int precision, Coordinate[] coordinates) {
		super(srid, precision, coordinates);
	}

	// -------------------------------------------------------------------------
	// Geometry implementation:
	// -------------------------------------------------------------------------

	/** {@inheritDoc} */
	public Object clone() { // NOSONAR super.clone() not supported by GWT
		return getGeometryFactory().createLinearRing(getCoordinates());
	}

	/**
	 * Return true.
	 */
	public boolean isSimple() {
		return true;
	}

	/**
	 * Checks to see if the last coordinate equals the first. Should always be the case!
	 */
	public boolean isClosed() {
		if (isEmpty()) {
			return false;
		}
		if (getNumPoints() == 1) {
			return false;
		}
		Coordinate[] coordinates = getCoordinates();
		return coordinates[0].equals(coordinates[coordinates.length - 1]);
	}

	/**
	 * An empty LinearRing is valid. Furthermore this object must be closed and must not self-intersect.
	 */
	public boolean isValid() {
		if (isEmpty()) {
			return true;
		}
		if (!isClosed()) {
			return false;
		}
		Coordinate[] coordinates = getCoordinates();

		if (coordinates.length < 4) {
			return false;
		}

		for (int i = 0; i < coordinates.length - 1; i++) {
			for (int j = 0; j < coordinates.length - 1; j++) {
				if (Mathlib.lineIntersects(coordinates[i], coordinates[i + 1], coordinates[j], coordinates[j + 1])) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Since a LinearRing is closed, it has an area.
	 */
	public double getArea() {
		return Math.abs(getSignedArea());
	}

	/**
	 * The centroid is also known as the "center of gravity" or the "center of mass".
	 *
	 * @return Return the center point.
	 */
	public Coordinate getCentroid() {
		if (isEmpty()) {
			return null;
		}
		double area = getSignedArea();
		double x = 0;
		double y = 0;
		Coordinate[] coordinates = getCoordinates();
		for (int i = 1; i < coordinates.length; i++) {
			double x1 = coordinates[i - 1].getX();
			double y1 = coordinates[i - 1].getY();
			double x2 = coordinates[i].getX();
			double y2 = coordinates[i].getY();
			x += (x1 + x2) * (x1 * y2 - x2 * y1);
			y += (y1 + y2) * (x1 * y2 - x2 * y1);
		}
		x = x / (6 * area);
		y = y / (6 * area);
		return new Coordinate(x, y);
	}

	private double getSignedArea() {
		if (isEmpty()) {
			return 0;
		}
		double area = 0;
		Coordinate[] coordinates = getCoordinates();
		for (int i = 1; i < coordinates.length; i++) {
			double x1 = coordinates[i - 1].getX();
			double y1 = coordinates[i - 1].getY();
			double x2 = coordinates[i].getX();
			double y2 = coordinates[i].getY();
			area += x1 * y2 - x2 * y1;
		}
		return area / 2;
	}
}