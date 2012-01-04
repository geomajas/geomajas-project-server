/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.geometry;

import java.io.Serializable;

import org.geomajas.annotation.Api;

/**
 * <p>
 * Definition of a mutable geometry object. This type of geometry is meant as a Data Transfer Object (DTO) within Java
 * environments, and especially within GWT environments.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class Geometry implements Serializable {

	private static final long serialVersionUID = 100L;

	/** Point type geometry. */
	public static final String POINT = "Point";

	/** Multi-point type geometry. */
	public static final String MULTI_POINT = "MultiPoint";

	/** Linestring type geometry. */
	public static final String LINE_STRING = "LineString";

	/** Multi-linestring type geometry. */
	public static final String MULTI_LINE_STRING = "MultiLineString";

	/** Linear ring type geometry. */
	public static final String LINEAR_RING = "LinearRing";

	/** Polygon type geometry. */
	public static final String POLYGON = "Polygon";

	/** Multi-polygon type geometry. */
	public static final String MULTI_POLYGON = "MultiPolygon";

	private String geometryType;

	private int srid;

	private int precision;

	private Coordinate[] coordinates;

	private Geometry[] geometries;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/** No argument constructor. */
	public Geometry() {
	}

	/**
	 * Create a new geometry.
	 * 
	 * @param geometryType
	 *            The type of geometry
	 * @param srid
	 *            The spatial reference ID for this geometry.
	 * @param precision
	 *            The precision at which manipulation on the geometry should occur.
	 */
	public Geometry(String geometryType, int srid, int precision) {
		this.geometryType = geometryType;
		this.srid = srid;
		this.precision = precision;
	}

	// -------------------------------------------------------------------------
	// General getters and setters
	// -------------------------------------------------------------------------

	/**
	 * Return the spatial reference ID.
	 * 
	 * @return Returns the srid as an integer.
	 */
	public int getSrid() {
		return srid;
	}

	/**
	 * Get the precision for the geometry.
	 * 
	 * @return geometry precision
	 */
	public int getPrecision() {
		return precision;
	}

	/**
	 * Get the geometry type.
	 * 
	 * @return geometry type
	 */
	public String getGeometryType() {
		return geometryType;
	}

	/**
	 * Get the geometry type.
	 * 
	 * @param geometryType
	 *            geometry type
	 */
	public void setGeometryType(String geometryType) {
		this.geometryType = geometryType;
	}

	/**
	 * Set the SRID (the "x" in a 'EPSG:x" CRS code) for the geometry.
	 * 
	 * @param srid
	 *            spatial reference id
	 */
	public void setSrid(int srid) {
		this.srid = srid;
	}

	/**
	 * Set the precision for the geometry.
	 * 
	 * @param precision
	 *            precision
	 */
	public void setPrecision(int precision) {
		this.precision = precision;
	}

	/**
	 * Get the coordinates for this geometry.
	 * 
	 * @return coordinates for geometry
	 */
	public Coordinate[] getCoordinates() {
		return coordinates;
	}

	/**
	 * Set the coordinates for this geometry.
	 * 
	 * @param coordinates
	 *            coordinates for geometry
	 */
	public void setCoordinates(Coordinate[] coordinates) {
		this.coordinates = coordinates;
	}

	/**
	 * Get the contained geometries for this geometry.
	 * 
	 * @return contained geometries
	 */
	public Geometry[] getGeometries() {
		return geometries;
	}

	/**
	 * Set the contained geometries.
	 * 
	 * @param geometries
	 *            contained geometries
	 */
	public void setGeometries(Geometry[] geometries) {
		this.geometries = geometries;
	}

	/**
	 * Returns whether the geometry is empty.
	 * 
	 * @return true if empty, false otherwise
	 */
	public boolean isEmpty() {
		return getCoordinates() == null && getGeometries() == null;
	}
}