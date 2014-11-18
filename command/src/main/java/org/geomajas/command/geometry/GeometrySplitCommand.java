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
import org.geomajas.command.dto.GeometrySplitRequest;
import org.geomajas.command.dto.GeometrySplitResponse;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.service.DtoConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.operation.polygonize.Polygonizer;

/**
 * Command that splits up a given geometry using a splitting line. This command will return polygons for a polygon-like
 * geometry and linestrings for line-like in the result.
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 * @since 1.11.0
 */
@Component
@Api
public class GeometrySplitCommand implements Command<GeometrySplitRequest, GeometrySplitResponse> {

	public static final double DELTA = 0.00001;

	@Autowired
	private DtoConverterService converter;

	@Override
	public GeometrySplitResponse getEmptyCommandResponse() {
		return new GeometrySplitResponse();
	}

	@Override
	public void execute(GeometrySplitRequest request, GeometrySplitResponse response) throws Exception {
		// Check the parameters:
		if (null == request.getGeometry()) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "geometry");
		}
		if (null == request.getSplitLine()) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "splittingLine");
		}

		// Convert geometries to JTS model:
		Geometry geometry = converter.toInternal(request.getGeometry());
		Geometry splittingLine = converter.toInternal(request.getSplitLine());
		// split
		List<Geometry> splitted = split(geometry, splittingLine);
		// Convert to Geomajas
		List<org.geomajas.geometry.Geometry> geometries = new ArrayList<org.geomajas.geometry.Geometry>();
		for (Geometry geom : splitted) {
			geometries.add(converter.toDto(geom));
		}
		response.setGeometries(geometries);
	}

	List<Geometry> split(Geometry geometry, Geometry splittingLine) throws Exception {
		// Convert both to multilinestring
		MultiLineString geomLines = toMultiLineString(geometry);
		MultiLineString splittingLines = toMultiLineString(splittingLine);

		// Get the unary union
		Geometry union = geomLines.union(splittingLines);
		if (union instanceof MultiLineString) {
			if (geometry instanceof Polygon || geometry instanceof MultiPolygon) {
				List<Geometry> polygons = new ArrayList<Geometry>();
				// polygonize, but remove the holes outside the original polygon
				Polygonizer polygonizer = new Polygonizer();
				polygonizer.add(union);
				for (Object o : polygonizer.getPolygons()) {
					Polygon p = (Polygon) o;
					if (geometry.contains(p.getInteriorPoint())) {
						polygons.add(p);
					}
				}
				return polygons;
			} else if (geometry instanceof LineString || geometry instanceof MultiLineString) {
				List<Geometry> lines = new ArrayList<Geometry>();
				for (int i = 0; i < union.getNumGeometries(); i++) {
					LineString ls = (LineString) union.getGeometryN(i);
					// collect the linestrings, but not the split line !
					if (geometry.contains(ls)) {
						lines.add(ls);
					}
				}
				return lines;
			}
		}
		throw new GeomajasException(ExceptionCode.PARAMETER_INVALID_VALUE, "geometry or splittingline");
	}

	public MultiLineString toMultiLineString(Geometry g) {
		List<LineString> lines = new ArrayList<LineString>();
		collectLines(g, lines);
		return g.getFactory().createMultiLineString(lines.toArray(new LineString[0]));
	}

	private void collectLines(Geometry g, List<LineString> lines) {
		if (g.getNumGeometries() > 1) {
			for (int i = 0; i < g.getNumGeometries(); i++) {
				collectLines(g.getGeometryN(i), lines);
			}
		} else {
			if (g instanceof Polygon) {
				lines.add(((Polygon) g).getExteriorRing());
				for (int i = 0; i < ((Polygon) g).getNumInteriorRing(); i++) {
					lines.add(((Polygon) g).getInteriorRingN(i));
				}
			} else if (g instanceof LineString) {
				lines.add((LineString) g);
			} else {
				// we only collect linestring or polygon-like elements
			}
		}
	}

}