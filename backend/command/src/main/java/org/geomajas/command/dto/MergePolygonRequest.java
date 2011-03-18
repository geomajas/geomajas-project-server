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
package org.geomajas.command.dto;

import org.geomajas.command.CommandRequest;
import org.geomajas.geometry.Geometry;

/**
 * Request object for {@link org.geomajas.command.geometry.MergePolygonCommand}.
 *
 * @author Joachim Van der Auwera
 */
public class MergePolygonRequest implements CommandRequest {

	private static final long serialVersionUID = 151L;

	/**
	 * Command name for this request.
	 *
	 * @since 1.9.0
	 * */
	public static final String COMMAND = "command.geometry.MergePolygon";

	private Geometry[] polygons;

	private boolean allowMultiPolygon;

	public Geometry[] getPolygons() {
		return polygons;
	}

	public void setPolygons(Geometry[] polygons) {
		this.polygons = polygons;
	}

	public boolean isAllowMultiPolygon() {
		return allowMultiPolygon;
	}

	public void setAllowMultiPolygon(boolean allowMultiPolygon) {
		this.allowMultiPolygon = allowMultiPolygon;
	}
}
