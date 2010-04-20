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

package org.geomajas.geometry;

import org.geomajas.global.Api;

import java.io.Serializable;

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
}