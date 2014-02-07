/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.command.geometry;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.command.Command;
import org.geomajas.command.dto.GeometryConvexHullRequest;
import org.geomajas.command.dto.GeometryConvexHullResponse;
import org.geomajas.geometry.Geometry;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.service.DtoConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.algorithm.ConvexHull;

/**
 * <p>
 * This command returns the convex hull of the given geometries.
 * </p>
 * 
 * @author Emiel Ackermann
 * @since 1.11.0
 */
@Component
@Api
public class GeometryConvexHullCommand implements Command<GeometryConvexHullRequest, GeometryConvexHullResponse> {

	@Autowired
	private DtoConverterService converter;

	@Override
	public GeometryConvexHullResponse getEmptyCommandResponse() {
		return new GeometryConvexHullResponse();
	}

	@Override
	public void execute(GeometryConvexHullRequest request, GeometryConvexHullResponse response) throws Exception {
		List<org.geomajas.geometry.Geometry> clientGeometries = request.getGeometries();
		if (clientGeometries == null || clientGeometries.size() == 0) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "request");
		}
		
		// Convert to internal, apply ConvexHull and convert back to DTO
		List<org.geomajas.geometry.Geometry> result = new ArrayList<org.geomajas.geometry.Geometry>();
		for (Geometry clientGeometry : clientGeometries) {
			result.add(converter.toDto(new ConvexHull(converter.toInternal(clientGeometry)).getConvexHull()));
		}
		response.setGeometries(result);
	}
}