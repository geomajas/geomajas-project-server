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
package org.geomajas.command.geometry;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.command.Command;
import org.geomajas.command.dto.GeometryMergeRequest;
import org.geomajas.command.dto.GeometryMergeResponse;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.service.DtoConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * <p>
 * This command merges multiple geometries into a single geometry.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.11.0
 */
@Component
@Api
public class GeometryMergeCommand implements Command<GeometryMergeRequest, GeometryMergeResponse> {

	@Autowired
	private DtoConverterService converter;

	/** {@inheritDoc} */
	public GeometryMergeResponse getEmptyCommandResponse() {
		return new GeometryMergeResponse();
	}

	/** {@inheritDoc} */
	public void execute(GeometryMergeRequest request, GeometryMergeResponse response) throws Exception {
		List<org.geomajas.geometry.Geometry> clientGeometries = request.getGeometries();
		if (clientGeometries == null || clientGeometries.size() == 0) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "request");
		}
		if (clientGeometries.size() == 1) {
			response.setGeometry(clientGeometries.get(0));
			return;
		}
		int precision = request.getPrecision();

		List<Geometry> geometries = new ArrayList<Geometry>();
		for (org.geomajas.geometry.Geometry geometry : clientGeometries) {
			geometry.setPrecision(precision);
			geometries.add(converter.toInternal(geometry));
		}
		PrecisionModel precisionModel;
		if (precision == -1) {
			precisionModel = new PrecisionModel(PrecisionModel.FLOATING);
			precision = 20;
		} else {
			precisionModel = new PrecisionModel(Math.pow(10.0, precision));
		}
		GeometryFactory factory = new GeometryFactory(precisionModel, geometries.get(0).getSRID());

		// Calculate the union:
		double buffer = 0;
		if (request.usePrecisionAsBuffer()) {
			buffer = Math.pow(10.0, -(precision - 1));
		}
		GeometryCollection geometryCollection = (GeometryCollection) factory.buildGeometry(geometries);

		Geometry union = geometryCollection.buffer(buffer);
		response.setGeometry(converter.toDto(union));
	}
}