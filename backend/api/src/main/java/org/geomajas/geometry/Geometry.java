/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
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
 * Definition of a DTO geometry object. This geometry type is used for client-server communication. Internally on the
 * server, these geometries are converted into JTS geometries.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
public class Geometry implements Serializable {

	private static final long serialVersionUID = -6330507241114727324L;

	/**
	 * Point type geometry.
	 *
	 * @since 1.7.0
	 */
	public static final String POINT = "Point";

	/**
	 * Multi-point type geometry.
	 *
	 * @since 1.7.0
	 */
	public static final String MULTI_POINT = "MultiPoint";

	/**
	 * Linestring type geometry.
	 *
	 * @since 1.7.0
	 */
	public static final String LINE_STRING = "LineString";

	/**
	 * Multi-linestring type geometry.
	 *
	 * @since 1.7.0
	 */
	public static final String MULTI_LINE_STRING = "MultiLineString";

	/**
	 * Linear ring type geometry.
	 *
	 * @since 1.7.0
	 */
	public static final String LINEAR_RING = "LinearRing";

	/**
	 * Polygon type geometry.
	 *
	 * @since 1.7.0
	 */
	public static final String POLYGON = "Polygon";

	/**
	 * Multi-polygon type geometry.
	 *
	 * @since 1.7.0
	 */
	public static final String MULTI_POLYGON = "MultiPolygon";

	private String geometryType;

	private int srid;

	private int precision;

	private Coordinate[] coordinates;

	private Geometry[] geometries;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * No-args creator, available for GWT.
	 */
	public Geometry() {
	}

	/**
	 * Create geometry.
	 *
	 * @param geometryType geometry type
	 * @param srid srid
	 * @param precision precision
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
	 * @param geometryType geometry type
	 */
	public void setGeometryType(String geometryType) {
		this.geometryType = geometryType;
	}

	/**
	 * Set the SRID (the "x" in a 'EPSG:x" CRS code) for the geometry.
	 *
	 * @param srid spatial reference id
	 */
	public void setSrid(int srid) {
		this.srid = srid;
	}

	/**
	 * Set the precision for the geometry.
	 *
	 * @param precision precision
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
	 * @param coordinates coordinates for geometry
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
	 * @param geometries contained geometries
	 */
	public void setGeometries(Geometry[] geometries) {
		this.geometries = geometries;
	}
	
	/**
	 * Returns whether the geometry is empty.
	 * 
	 * @return true if empty, false otherwise
	 * @since 1.10.0
	 */
	public boolean isEmpty() {
		return getCoordinates() == null && getGeometries() == null;
	}
}