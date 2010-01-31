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

import java.io.Serializable;

/**
 * <p>
 * Definition of a DTO geometry object. This geometry type is used for client-server communication. Internally on the
 * server, these geometries are converted into JTS geometries.
 * </p>
 * 
 * @author Pieter De Graef
 */
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

	public Geometry() {
	}

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

	public int getPrecision() {
		return precision;
	}

	public String getGeometryType() {
		return geometryType;
	}

	public void setGeometryType(String geometryType) {
		this.geometryType = geometryType;
	}

	public void setSrid(int srid) {
		this.srid = srid;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public Coordinate[] getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Coordinate[] coordinates) {
		this.coordinates = coordinates;
	}

	public Geometry[] getGeometries() {
		return geometries;
	}

	public void setGeometries(Geometry[] geometries) {
		this.geometries = geometries;
	}
}