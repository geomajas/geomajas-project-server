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
package org.geomajas.command.dto;

import org.geomajas.annotation.Api;
import org.geomajas.command.EmptyCommandRequest;
import org.geomajas.geometry.Geometry;

/**
 * Request object for {@link org.geomajas.command.geometry.GeometrySplitCommand}.
 * 
 * @author Pieter De Graef
 * @since 1.11.0
 */
@Api(allMethods = true)
public class GeometrySplitRequest extends EmptyCommandRequest {

	private static final long serialVersionUID = 1110L;

	/** Command name for this request. */
	public static final String COMMAND = "command.geometry.Split";

	private Geometry geometry;

	private Geometry splitLine;

	/**
	 * Main constructor.
	 * 
	 * @param geometry
	 * @param splitLine
	 */
	public GeometrySplitRequest(Geometry geometry, Geometry splitLine) {
		this.geometry = geometry;
		this.splitLine = splitLine;
	}

	/**
	 * Get geometry.
	 * 
	 * @return geometry
	 */
	public Geometry getGeometry() {
		return geometry;
	}

	/**
	 * Get split line.
	 * 
	 * @return split line
	 */
	public Geometry getSplitLine() {
		return splitLine;
	}

	/**
	 * Set geometry.
	 * 
	 * @param geometry
	 */
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	/**
	 * Set split line.
	 * 
	 * @param splitLine
	 */
	public void setSplitLine(Geometry splitLine) {
		this.splitLine = splitLine;
	}

	/**
	 * Get the string representation of this request.
	 * 
	 * @return string representation of this request
	 */
	@Override
	public String toString() {
		return "GeometrySplitRequest{" + "geometry=" + geometry + ", splitLine=" + splitLine + '}';
	}
}