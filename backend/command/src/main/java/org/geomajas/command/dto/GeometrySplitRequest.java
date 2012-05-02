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
 * Request for splitting a geometry on the back-end.
 * 
 * @author Pieter De Graef
 * @since 1.11.0
 */
@Api(allMethods = true)
public class GeometrySplitRequest extends EmptyCommandRequest {

	private static final long serialVersionUID = 100L;

	/** Command name for this request. */
	public static final String COMMAND = "command.geometry.Split";

	private Geometry geometry;

	private Geometry splitLine;

	public GeometrySplitRequest() {
	}

	public GeometrySplitRequest(Geometry geometry, Geometry splitLine) {
		this.geometry = geometry;
		this.splitLine = splitLine;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public Geometry getSplitLine() {
		return splitLine;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public void setSplitLine(Geometry splitLine) {
		this.splitLine = splitLine;
	}
}