/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.puregwt.client.spatial;

import org.geomajas.geometry.Coordinate;
import org.geomajas.puregwt.client.spatial.LinearRing;

/**
 * LinearRing client-side GWT object.
 * 
 * @author Pieter De Graef
 */
public class LinearRingImpl extends LineStringImpl implements LinearRing {

	private static final long serialVersionUID = 4314580899552986922L;

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------

	LinearRingImpl(GeometryFactory factory) {
		super(factory, null, null);
	}

	LinearRingImpl(GeometryFactory factory, SpatialService service, Coordinate[] coordinates) {
		super(factory, service, coordinates);
	}

	// -------------------------------------------------------------------------
	// Geometry implementation:
	// -------------------------------------------------------------------------

	/**
	 * Create a copy of this geometry and return it.
	 */
	public Object clone() {
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
		for (int i = 0; i < coordinates.length - 1; i++) {
			for (int j = 0; j < coordinates.length - 1; j++) {
				if (service.lineIntersects(coordinates[i], coordinates[i + 1], coordinates[j], coordinates[j + 1])) {
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
		return Math.abs(area / 2);
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
		double area = getArea();
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
}
