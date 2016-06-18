/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.command.geometry;

import org.geomajas.command.CommandHasRequest;
import org.geomajas.command.dto.SplitPolygonRequest;
import org.geomajas.command.dto.SplitPolygonResponse;
import org.geomajas.geometry.Geometry;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
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
 * @deprecated use {@link org.geomajas.command.geometry.GeometrySplitCommand}
 */
@Deprecated
@Component()
public class SplitPolygonCommand implements CommandHasRequest<SplitPolygonRequest, SplitPolygonResponse> {

	@Autowired
	private DtoConverterService converter;

	@Override
	public SplitPolygonRequest getEmptyCommandRequest() {
		return new SplitPolygonRequest();
	}

	@Override
	public SplitPolygonResponse getEmptyCommandResponse() {
		return new SplitPolygonResponse();
	}

	@Override
	public void execute(SplitPolygonRequest request, SplitPolygonResponse response) throws Exception {
		// convert to most accurate precision model
		com.vividsolutions.jts.geom.Geometry jtsGeometry = converter.toInternal(request.getGeometry());
		if (!(jtsGeometry instanceof Polygon)) {
			throw new GeomajasException(ExceptionCode.UNEXPECTED_PROBLEM, "geometry has to be a Polygon");
		}
		Polygon polygon = (Polygon) converter.toInternal(request.getGeometry());

		// Convert to the polygons precision model:
		jtsGeometry = converter.toInternal(request.getSplitter());
		if (!(jtsGeometry instanceof LineString)) {
			throw new GeomajasException(ExceptionCode.UNEXPECTED_PROBLEM, "splitter has to be a LineString");
		}
		LineString preciseLine = (LineString) jtsGeometry;
		int precision = polygon.getPrecisionModel().getMaximumSignificantDigits() - 1;
		com.vividsolutions.jts.geom.Geometry bufferedLine = preciseLine.buffer(Math.pow(10.0, -precision));
		com.vividsolutions.jts.geom.Geometry diff = polygon.difference(bufferedLine);

		if (diff instanceof Polygon) {
			response.setGeometries(new Geometry[] { converter.toDto(diff) });
		} else if (diff instanceof MultiPolygon) {
			Geometry[] polygons = new Geometry[diff.getNumGeometries()];
			for (int i = 0; i < diff.getNumGeometries(); i++) {
				polygons[i] = converter.toDto(diff.getGeometryN(i));
			}
			response.setGeometries(polygons);
		}
	}
}