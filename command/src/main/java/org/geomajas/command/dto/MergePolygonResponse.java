/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.command.dto;

import org.geomajas.command.CommandResponse;
import org.geomajas.geometry.Geometry;

/**
 * Result object for {@link org.geomajas.command.geometry.MergePolygonCommand}.
 * 
 * @author Pieter De Graef
 * @deprecated use {@link org.geomajas.command.geometry.GeometryMergeCommand}
 */
@Deprecated
public class MergePolygonResponse extends CommandResponse {

	private static final long serialVersionUID = 151L;

	private Geometry geometry;

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	@Override
	public String toString() {
		return "MergePolygonResponse{" +
				"geometry=" + geometry +
				'}';
	}
}