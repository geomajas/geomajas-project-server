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
package org.geomajas.plugin.editing.server.command;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.command.Command;
import org.geomajas.geometry.Geometry;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.editing.dto.GeometryBufferRequest;
import org.geomajas.plugin.editing.dto.GeometryBufferResponse;
import org.geomajas.service.DtoConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * This command applies a buffer on multiple geometries.
 * </p>
 * 
 * @author Emiel Ackermann
 * @since 1.0.0
 */
@Component
@Api
public class GeometryBufferCommand implements Command<GeometryBufferRequest, GeometryBufferResponse> {

	@Autowired
	private DtoConverterService converter;

	/** {@inheritDoc} */
	public GeometryBufferResponse getEmptyCommandResponse() {
		return new GeometryBufferResponse();
	}

	/** {@inheritDoc} */
	public void execute(GeometryBufferRequest request, GeometryBufferResponse response) throws Exception {
		List<org.geomajas.geometry.Geometry> clientGeometries = request.getGeometries();
		if (clientGeometries == null || clientGeometries.size() == 0) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "request");
		}

		// Convert to internal, apply buffer and convert back to DTO
		List<org.geomajas.geometry.Geometry> result = new ArrayList<org.geomajas.geometry.Geometry>();
		double buffer = request.getBufferDistance();
		int quadrantSegments = request.getQuadrantSegments();
		for (Geometry clientGeometry : clientGeometries) {
			result.add(converter.toDto(converter.toInternal(clientGeometry).buffer(buffer, quadrantSegments)));
		}
		response.setGeometries(result);
	}
}