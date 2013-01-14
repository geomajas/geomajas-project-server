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

import java.util.Map;

import org.geomajas.command.CommandResponse;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;

/**
 * Result object for getting centroids.
 * 
 * @author Pieter De Graef
 */
public class GetCentroidResponse extends CommandResponse {

	private static final long serialVersionUID = 100L;

	private Map<Geometry, Coordinate> centroids;

	public GetCentroidResponse() {
	}

	public Map<Geometry, Coordinate> getCentroids() {
		return centroids;
	}

	public void setCentroids(Map<Geometry, Coordinate> centroids) {
		this.centroids = centroids;
	}
}