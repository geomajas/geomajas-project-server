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
package org.geomajas.command.geometry;

import org.geomajas.annotation.Api;
import org.geomajas.command.CommandHasRequest;
import org.geomajas.command.dto.TransformGeometryRequest;
import org.geomajas.command.dto.TransformGeometryResponse;
import org.geomajas.geometry.Crs;
import org.geomajas.geometry.CrsTransform;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Command to perform CRS transformations on various geometry types: bounding box, geometry, geometry collection.
 * 
 * @author Jan De Moerloose
 * @since 1.9.0.
 */
@Api
@Component()
public class TransformGeometryCommand
		implements CommandHasRequest<TransformGeometryRequest, TransformGeometryResponse> {

	@Autowired
	private DtoConverterService converter;

	@Autowired
	private GeoService geoService;

	@Override
	public TransformGeometryRequest getEmptyCommandRequest() {
		return new TransformGeometryRequest();
	}

	@Override
	public TransformGeometryResponse getEmptyCommandResponse() {
		return new TransformGeometryResponse();
	}

	@Override
	public void execute(TransformGeometryRequest request, TransformGeometryResponse response) throws Exception {
		Crs sourceCrs = geoService.getCrs2(request.getSourceCrs());
		Crs targetCrs = geoService.getCrs2(request.getTargetCrs());
		CrsTransform transform = geoService.getCrsTransform(sourceCrs, targetCrs);

		if (request.getBounds() != null) {
			Envelope source = converter.toInternal(request.getBounds());
			Envelope target = geoService.transform(source, transform);
			response.setBounds(converter.toDto(target));
		}

		if (request.getGeometry() != null) {
			Geometry source = converter.toInternal(request.getGeometry());
			Geometry target = geoService.transform(source, transform);
			response.setGeometry(converter.toDto(target));
		}

		if (request.getGeometryCollection() != null) {
			for (org.geomajas.geometry.Geometry geometry : request.getGeometryCollection()) {
				Geometry source = converter.toInternal(geometry);
				Geometry target = geoService.transform(source, transform);
				response.getGeometryCollection().add(converter.toDto(target));
			}
		}
	}
}