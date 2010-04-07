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
package org.geomajas.command.geometry;

import org.geomajas.command.Command;
import org.geomajas.command.dto.SplitPolygonRequest;
import org.geomajas.command.dto.SplitPolygonResponse;
import org.geomajas.geometry.Geometry;
import org.geomajas.service.DtoConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

/**
 * <p>
 * This command splits a polygon or multipolygon by a linestring, and returns an array of resulting
 * polygons/multipolygons.
 * </p>
 * 
 * @author Pieter De Graef
 */
@Component()
public class SplitPolygonCommand implements Command<SplitPolygonRequest, SplitPolygonResponse> {

	@Autowired
	private DtoConverterService converter;

	public SplitPolygonResponse getEmptyCommandResponse() {
		return new SplitPolygonResponse();
	}

	public void execute(SplitPolygonRequest request, SplitPolygonResponse response) throws Exception {
		// convert to most accurate precision model
		Polygon polygon = null;
		polygon = (Polygon) converter.toInternal(request.getPolygon());

		// Convert to the polygons precision model:
		LineString preciseLine = (LineString) polygon.getFactory().createGeometry(
				converter.toInternal(request.getLineString()));
		int precision = polygon.getPrecisionModel().getMaximumSignificantDigits() - 1;
		com.vividsolutions.jts.geom.Geometry bufferedLine = preciseLine.buffer(Math.pow(10.0, -precision));
		com.vividsolutions.jts.geom.Geometry diff = polygon.difference(bufferedLine);

		if (diff instanceof Polygon) {
			response.setPolygons(new Geometry[] { converter.toDto(diff) });
		} else if (diff instanceof MultiPolygon) {
			Geometry[] polygons = new Geometry[diff.getNumGeometries()];
			for (int i = 0; i < diff.getNumGeometries(); i++) {
				polygons[i] = converter.toDto(diff.getGeometryN(i));
				// makePrecise(polygon.getPrecisionModel(), polygons[i]);
			}
			response.setPolygons(polygons);
		}
	}
}