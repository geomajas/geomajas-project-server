/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.command.dto;

import org.geomajas.command.CommandResponse;
import org.geomajas.geometry.Geometry;

import java.util.Arrays;

/**
 * Result object for {@link org.geomajas.command.geometry.SplitPolygonCommand}.
 * 
 * @author Pieter De Graef
 * @deprecated use {@link org.geomajas.command.geometry.GeometrySplitCommand}
 */
@Deprecated
public class SplitPolygonResponse extends CommandResponse {

	private static final long serialVersionUID = 151L;

	private Geometry[] geometries;

	public SplitPolygonResponse() {
	}

	public Geometry[] getGeometries() {
		return geometries;
	}

	public void setGeometries(Geometry[] geometries) {
		this.geometries = geometries;
	}

	@Override
	public String toString() {
		return "SplitPolygonResponse{" +
				"geometries=" + (geometries == null ? null : Arrays.asList(geometries)) +
				'}';
	}
}