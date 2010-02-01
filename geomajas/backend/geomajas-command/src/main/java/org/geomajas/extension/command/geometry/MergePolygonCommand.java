/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.extension.command.geometry;

import org.geomajas.command.Command;
import org.geomajas.extension.command.dto.MergePolygonRequest;
import org.geomajas.extension.command.dto.MergePolygonResponse;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.service.DtoConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * <p>
 * This command splits a polygon or multipolygon by a linestring, and returns an
 * array of resulting polygons/multipolygons.
 * </p>
 *
 * @author Pieter De Graef
 */
@Component()
public class MergePolygonCommand implements Command<MergePolygonRequest, MergePolygonResponse> {

	@Autowired
	private DtoConverterService converter;

	public MergePolygonResponse getEmptyCommandResponse() {
		return new MergePolygonResponse();
	}

	public void execute(MergePolygonRequest request, MergePolygonResponse response) throws Exception {
		Polygon[] polygons = new Polygon[request.getPolygons().length];
		for (int i = 0; i < request.getPolygons().length; i++) {
			try {
				polygons[i] = (Polygon) converter.toJts(request.getPolygons()[i]);
			} catch (Exception e) {
				throw new GeomajasException(ExceptionCode.MERGE_NO_POLYGON);
			}
		}
		int precision = polygons[0].getPrecisionModel().getMaximumSignificantDigits() - 1;
		PrecisionModel precisionModel = new PrecisionModel(Math.pow(10.0, precision));
		GeometryFactory factory = new GeometryFactory(precisionModel, polygons[0].getSRID());

		Geometry temp = factory.createGeometry(polygons[0]);
		for (int i = 1; i < polygons.length; i++) {
			Geometry polygon = factory.createGeometry(polygons[i]);
			temp = temp.union(polygon.buffer(Math.pow(10.0, -(precision - 1))));
		}
		if (temp instanceof Polygon) {
			MultiPolygon mp = factory.createMultiPolygon(new Polygon[] {(Polygon) temp});
			response.setMultiPolygon(converter.toDto(mp));
		} else if (temp instanceof MultiPolygon && temp.getNumGeometries() != 0
				&& (request.isAllowMultiPolygon() || temp.getNumGeometries() == 1)) {
			response.setMultiPolygon(converter.toDto(temp));
		} else {
			throw new GeomajasException(ExceptionCode.MERGE_NO_POLYGON);
		}
	}
}