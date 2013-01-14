/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.editing.jsapi.example.dto;

import java.util.List;

import org.geomajas.command.CommandRequest;
import org.geomajas.geometry.Geometry;

/**
 * Request for getting centroids.
 * 
 * @author Pieter De Graef
 */
public class GetCentroidRequest implements CommandRequest {

	private static final long serialVersionUID = 100L;

	/** Command name for this request. **/
	public static final String COMMAND = "command.GetCentroid";

	private List<Geometry> geometries;

	public GetCentroidRequest() {
	}

	public List<Geometry> getGeometries() {
		return geometries;
	}

	public void setGeometries(List<Geometry> geometries) {
		this.geometries = geometries;
	}
}