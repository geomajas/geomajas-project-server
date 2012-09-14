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
package org.geomajas.plugin.deskmanager.command.common;

import org.geomajas.command.Command;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.deskmanager.command.common.dto.BufferGeometriesRequest;
import org.geomajas.plugin.deskmanager.command.common.dto.BufferGeometriesResponse;
import org.geomajas.service.DtoConverterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Geometry;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 * 
 */
@Component(BufferGeometriesRequest.COMMAND)
public class BufferGeometriesCommand implements Command<BufferGeometriesRequest, BufferGeometriesResponse> {

	private final Logger logger = LoggerFactory.getLogger(BufferGeometriesCommand.class);

	private static final int BUFFER_QUADRANT_SEGS = 4;

	@Autowired
	private DtoConverterService converter;

	public void execute(final BufferGeometriesRequest request, final BufferGeometriesResponse response)
			throws GeomajasException {
		if (request.getGeometries() == null || request.getGeometries().length < 1) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "geometries");
		}

		// Convert geometries and add buffer
		logger.debug("BufferGeometriesCommand for " + request.getGeometries().length + " geometries.");
		Geometry resultGeom = null;
		for (final org.geomajas.geometry.Geometry g : request.getGeometries()) {
			Geometry geometry = converter.toInternal(g);
			geometry = geometry.buffer(0.01);
			if (resultGeom == null) {
				resultGeom = geometry;
			} else {
				resultGeom = resultGeom.union(geometry);
			}
		}
		response.setGeometryCenter(converter.toDto(resultGeom));
		if (request.getBuffer() > 0) {
			resultGeom = resultGeom.buffer(request.getBuffer(), BUFFER_QUADRANT_SEGS);
		}
		response.setGeometryBuffer(converter.toDto(resultGeom));
	}

	public BufferGeometriesResponse getEmptyCommandResponse() {
		return new BufferGeometriesResponse();
	}

	public void setConverter(DtoConverterService converter) {
		this.converter = converter;
	}
}
