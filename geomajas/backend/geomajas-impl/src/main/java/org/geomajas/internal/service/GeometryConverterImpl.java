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

package org.geomajas.internal.service;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.service.GeometryConverter;
import org.springframework.stereotype.Component;

/**
 * Converter for geometric objects. This class supports converting between JTS and DTO geometries. The JTS geometries
 * are used servers-side, while the DTO geometries are the ones sent to the client (hence the name...).
 *
 * @author Pieter De Graef
 */
@Component
public final class GeometryConverterImpl implements GeometryConverter {

	/**
	 * Takes in a JTS geometry, and creates a new DTO geometry from it.
	 *
	 * @param geometry
	 *            The geometry to convert into a DTO geometry.
	 * @return Returns a DTO type geometry, that is serializable.
	 */
	public Geometry toDto(com.vividsolutions.jts.geom.Geometry geometry) {
		if (geometry == null) {
			return null;
		}
		int srid = geometry.getSRID();
		int precision = -1;
		PrecisionModel precisionmodel = geometry.getPrecisionModel();
		if (!precisionmodel.isFloating()) {
			precision = (int) Math.log10(precisionmodel.getScale());
		}

		Geometry dto = null;
		if (geometry instanceof Point) {
			dto = new Geometry("Point", srid, precision);
			dto.setCoordinates(convertCoordinates(geometry));
		} else if (geometry instanceof LinearRing) {
			dto = new Geometry("LinearRing", srid, precision);
			dto.setCoordinates(convertCoordinates(geometry));
		} else if (geometry instanceof LineString) {
			dto = new Geometry("LineString", srid, precision);
			dto.setCoordinates(convertCoordinates(geometry));
		} else if (geometry instanceof Polygon) {
			dto = new Geometry("Polygon", srid, precision);
			Polygon polygon = (Polygon) geometry;
			Geometry[] geometries = new Geometry[polygon.getNumInteriorRing() + 1];
			for (int i = 0; i < geometries.length; i++) {
				if (i == 0) {
					geometries[i] = toDto(polygon.getExteriorRing());
				} else {
					geometries[i] = toDto(polygon.getInteriorRingN(i - 1));
				}
			}
			dto.setGeometries(geometries);
		} else if (geometry instanceof MultiPoint) {
			dto = new Geometry("MultiPoint", srid, precision);
			dto.setGeometries(convertGeometries(geometry));
		} else if (geometry instanceof MultiLineString) {
			dto = new Geometry("MultiLineString", srid, precision);
			dto.setGeometries(convertGeometries(geometry));
		} else if (geometry instanceof MultiPolygon) {
			dto = new Geometry("MultiPolygon", srid, precision);
			dto.setGeometries(convertGeometries(geometry));
		}

		return dto;
	}

	/**
	 * Takes in a DTO geometry, and converts it into a JTS geometry.
	 *
	 * @param geometry
	 *            The DTO geometry to convert into a JTS geometry.
	 * @return Returns a JTS geometry.
	 */
	public com.vividsolutions.jts.geom.Geometry toJts(Geometry geometry) {
		if (geometry == null) {
			return null;
		}
		int srid = geometry.getSrid();
		int precision = geometry.getPrecision();
		PrecisionModel model;
		if (precision == -1) {
			model = new PrecisionModel(PrecisionModel.FLOATING);
		} else {
			model = new PrecisionModel(Math.pow(10, precision));
		}
		GeometryFactory factory = new GeometryFactory(model, srid);
		com.vividsolutions.jts.geom.Geometry jts = null;

		String geometryType = geometry.getGeometryType();
		if ("Point".equals(geometryType)) {
			jts = factory.createPoint(convertCoordinates(geometry)[0]);
		} else if ("LinearRing".equals(geometryType)) {
			jts = factory.createLinearRing(convertCoordinates(geometry));
		} else if ("LineString".equals(geometryType)) {
			jts = factory.createLineString(convertCoordinates(geometry));
		} else if ("Polygon".equals(geometryType)) {
			LinearRing exteriorRing = (LinearRing) toJts(geometry.getGeometries()[0]);
			LinearRing[] interiorRings = new LinearRing[geometry.getGeometries().length - 1];
			for (int i = 0; i < interiorRings.length; i++) {
				interiorRings[i] = (LinearRing) toJts(geometry.getGeometries()[i + 1]);
			}
			jts = factory.createPolygon(exteriorRing, interiorRings);
		} else if ("MultiPoint".equals(geometryType)) {
			Point[] points = new Point[geometry.getGeometries().length];
			jts = factory.createMultiPoint((Point[]) convertGeometries(geometry, points));
		} else if ("MultiLineString".equals(geometryType)) {
			LineString[] lineStrings = new LineString[geometry.getGeometries().length];
			jts = factory.createMultiLineString((LineString[]) convertGeometries(geometry, lineStrings));
		} else if ("MultiPolygon".equals(geometryType)) {
			Polygon[] polygons = new Polygon[geometry.getGeometries().length];
			jts = factory.createMultiPolygon((Polygon[]) convertGeometries(geometry, polygons));
		}

		return jts;
	}

	// -------------------------------------------------------------------------
	// Private functions converting from JTS to DTO:
	// -------------------------------------------------------------------------

	private Coordinate[] convertCoordinates(com.vividsolutions.jts.geom.Geometry geometry) {
		Coordinate[] coordinates = new Coordinate[geometry.getCoordinates().length];
		for (int i = 0; i < coordinates.length; i++) {
			coordinates[i] = new Coordinate(geometry.getCoordinates()[i].x, geometry.getCoordinates()[i].y);
		}
		return coordinates;
	}

	private Geometry[] convertGeometries(com.vividsolutions.jts.geom.Geometry geometry) {
		Geometry[] geometries = new Geometry[geometry.getNumGeometries()];
		for (int i = 0; i < geometries.length; i++) {
			geometries[i] = toDto(geometry.getGeometryN(i));
		}
		return geometries;
	}

	// -------------------------------------------------------------------------
	// Private functions converting from DTO to JTS:
	// -------------------------------------------------------------------------

	private com.vividsolutions.jts.geom.Coordinate[] convertCoordinates(Geometry geometry) {
		com.vividsolutions.jts.geom.Coordinate[] coordinates = new com.vividsolutions.jts.geom.Coordinate[geometry
				.getCoordinates().length];
		for (int i = 0; i < coordinates.length; i++) {
			coordinates[i] = new com.vividsolutions.jts.geom.Coordinate(geometry.getCoordinates()[i].getX(),
					geometry.getCoordinates()[i].getY());
		}
		return coordinates;
	}

	private com.vividsolutions.jts.geom.Geometry[] convertGeometries(Geometry geometry,
			com.vividsolutions.jts.geom.Geometry[] geometries) {
		for (int i = 0; i < geometries.length; i++) {
			geometries[i] = toJts(geometry.getGeometries()[i]);
		}
		return geometries;
	}
}
