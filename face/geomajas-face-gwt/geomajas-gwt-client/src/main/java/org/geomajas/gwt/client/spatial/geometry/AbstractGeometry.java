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

package org.geomajas.gwt.client.spatial.geometry;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.spatial.Mathlib;

/**
 * ???
 *
 * @author Pieter De Graef
 */
public abstract class AbstractGeometry implements Geometry {

	private static final long serialVersionUID = -6330507241114727324L;

	private int srid;

	private int precision;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * This constructor exists only to make it GWT serializable.
	 */
	public AbstractGeometry() {
	}

	/**
	 * Package visible constructor, used by the GeometryFactory.
	 * @param srid
	 * @param precision
	 */
	AbstractGeometry(int srid, int precision) {
		this.srid = srid;
		this.precision = precision;
	}

	// -------------------------------------------------------------------------
	// Partial Geometry implementation:
	// -------------------------------------------------------------------------

	public Object clone() {
		return null;
	}

	/**
	 * Return the spatial reference ID.
	 *
	 * @return Returns the srid as an integer.
	 */
	public int getSrid() {
		return srid;
	}

	public int getPrecision() {
		return precision;
	}

	/**
	 * Return the {@link GeometryFactory} object that corresponds to this geometry.
	 */
	public GeometryFactory getGeometryFactory() {
		return new GeometryFactory(srid, precision);
	}

	/**
	 * Return 0.
	 */
	public double getArea() {
		return 0;
	}

	/**
	 * Return the number of coordinates.
	 */
	public int getNumPoints() {
		return 0;
	}

	/**
	 * Return this. Complex geometry implementations (MultiPoint, MultiLineString and MultiPolygon) should override
	 * this.
	 *
	 * @param n
	 *            Index in the geometry. This can be an integer value or an array of values.
	 * @return A geometry object.
	 */
	public Geometry getGeometryN(int n) {
		return this;
	}

	/**
	 * Return the number of direct geometries that this geometry object holds.
	 */
	public int getNumGeometries() {
		return 1;
	}

	/**
	 * Return the geometry type. Can be one of the following:
	 * <ul>
	 * <li>POINT</li>
	 * <li>LINESTRING</li>
	 * <li>LINEARRING</li>
	 * <li>POLYGON</li>
	 * <li>MULTILINESTRING</li>
	 * <li>MULTIPOLYGON</li>
	 * </ul>
	 */
	public int getGeometryType() {
		return 0;
	}

	/**
	 * Basically this function checks if the geometry is self-intersecting or not.
	 *
	 * @return True or false. True if there are no self-intersections in the geometry.
	 */
	public boolean isSimple() {
		if (isEmpty()) {
			return true;
		}

		if (getNumGeometries() > 1) {
			for (int n = 0; n < getNumGeometries(); n++) {
				if (!getGeometryN(n).isSimple()) {
					return false;
				}
			}
		} else {
			final Coordinate[] coords1 = getCoordinates();
			final Coordinate[] coords2 = getCoordinates();
			if (coords1.length > 1 && coords2.length > 1) {
				for (int i = 0; i < coords2.length - 1; i++) {
					for (int j = 0; j < coords1.length - 1; j++) {
						if (Mathlib.lineIntersects(coords2[i], coords2[i + 1], coords1[j], coords1[j + 1])) {
							return false;
						}
					}
				}
			} else {
				// TODO implement me
			}
		}

		return true;
	}

	/**
	 * Is the geometry a valid one? Different rules apply to different geometry types. Each geometry class should
	 * override this!
	 */
	public boolean isValid() {
		return false;
	}

	/**
	 * Calculate whether or not this geometry intersects with another.
	 *
	 * @param geometry
	 *            The other geometry to check for intersection.
	 * @return Returns true or false.
	 */
	public boolean intersects(Geometry geometry) {
		if (geometry == null) {
			return false;
		}
		Coordinate[] arr1 = getCoordinates();
		Coordinate[] arr2 = geometry.getCoordinates();
		for (int i = 0; i < arr1.length - 1; i++) {
			for (int j = 0; j < arr2.length - 1; j++) {
				if (Mathlib.lineIntersects(arr1[i], arr1[i + 1], arr2[j], arr2[j + 1])) {
					return true;
				}
			}
		}
		return false;
	}

}