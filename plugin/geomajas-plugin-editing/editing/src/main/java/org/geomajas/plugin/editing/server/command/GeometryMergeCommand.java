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

import org.geomajas.annotation.Api;
import org.geomajas.command.Command;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.editing.dto.GeometryMergeRequest;
import org.geomajas.plugin.editing.dto.GeometryMergeResponse;
import org.geomajas.service.DtoConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.precision.EnhancedPrecisionOp;

/**
 * <p>
 * This command merges multiple geometries into a single geometry.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Component
@Api(allMethods = true)
public class GeometryMergeCommand implements Command<GeometryMergeRequest, GeometryMergeResponse> {

	@Autowired
	private DtoConverterService converter;

	public GeometryMergeResponse getEmptyCommandResponse() {
		return new GeometryMergeResponse();
	}

	public void execute(GeometryMergeRequest request, GeometryMergeResponse response) throws Exception {
		if (request.getGeometries() == null || request.getGeometries().size() == 0) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "request");
		}
		double buffer = request.getBuffer();
		double boundaries = 0.0000001d;
		boolean bufferIsZero = buffer < boundaries && buffer > 0 - boundaries;
		if (request.getGeometries().size() == 1 && bufferIsZero) {
			response.setGeometry(request.getGeometries().get(0));
			return;
		}
		int precision = request.getPrecision();

		Geometry[] geometries = new Geometry[request.getGeometries().size()];
		for (int i = 0; i < request.getGeometries().size(); i++) {
			request.getGeometries().get(i).setPrecision(precision);
			geometries[i] = converter.toInternal(request.getGeometries().get(i));
		}
		PrecisionModel precisionModel;
		if (precision == -1) {
			precisionModel = new PrecisionModel(PrecisionModel.FLOATING);
			precision = 20;
		} else {
			precisionModel = new PrecisionModel(Math.pow(10.0, precision));
		}
		GeometryFactory factory = new GeometryFactory(precisionModel, geometries[0].getSRID());

		// Calculate the union:
		Geometry temp = factory.createGeometry(geometries[0]);
		if (bufferIsZero) {
			buffer = Math.pow(10.0, -(precision - 1));
		}
		temp = temp.buffer(buffer);
		for (int i = 1; i < geometries.length; i++) {
			Geometry geometry = factory.createGeometry(geometries[i]);
			// Buffer to make sure that after a split, the merging the same geometries would work:
			geometry = geometry.buffer(buffer);

			// Use EnhancedPrecisionOp to reduce likeliness of robustness problems:
			temp = EnhancedPrecisionOp.union(temp, geometry);
		}
		response.setGeometry(converter.toDto(temp));
	}
}