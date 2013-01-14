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
package org.geomajas.plugin.editing.jsapi.example.server.command;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.command.Command;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.editing.jsapi.example.dto.GetCentroidRequest;
import org.geomajas.plugin.editing.jsapi.example.dto.GetCentroidResponse;
import org.geomajas.service.DtoConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Point;

/**
 * <p>
 * This command calculates the centroid of the given geometries.
 * </p>
 * 
 * @author Pieter De Graef
 */
@Component
public class GetCentroidCommand implements Command<GetCentroidRequest, GetCentroidResponse> {

	@Autowired
	private DtoConverterService converter;

	public GetCentroidResponse getEmptyCommandResponse() {
		return new GetCentroidResponse();
	}

	public void execute(GetCentroidRequest request, GetCentroidResponse response) throws Exception {
		if (request.getGeometries() == null || request.getGeometries().size() == 0) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "request");
		}

		Map<Geometry, Coordinate> centroids = new HashMap<Geometry, Coordinate>(request.getGeometries().size());
		for (Geometry geometry : request.getGeometries()) {
			com.vividsolutions.jts.geom.Geometry jtsGeom = converter.toInternal(geometry);
			Point centroid = jtsGeom.getCentroid();
			centroids.put(geometry, new Coordinate(centroid.getX(), centroid.getY()));
		}

		response.setCentroids(centroids);
	}
}