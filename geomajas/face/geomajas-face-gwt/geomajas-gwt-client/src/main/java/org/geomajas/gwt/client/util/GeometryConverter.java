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

package org.geomajas.gwt.client.util;

import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.spatial.geometry.GeometryFactory;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.spatial.geometry.LinearRing;
import org.geomajas.gwt.client.spatial.geometry.MultiLineString;
import org.geomajas.gwt.client.spatial.geometry.MultiPoint;
import org.geomajas.gwt.client.spatial.geometry.MultiPolygon;
import org.geomajas.gwt.client.spatial.geometry.Point;
import org.geomajas.gwt.client.spatial.geometry.Polygon;

/**
 * Converter for geometric objects. This class supports converting between GWT and DTO geometries. The GWT geometries
 * are used client-side in the GWT face, while the DTO geometries are the ones sent to the server (hence the name...).
 *
 * @author Pieter De Graef
 */
public final class GeometryConverter {

	private GeometryConverter() {
	}

	/**
	 * Takes in a GWT geometry, and creates a new DTO geometry from it.
	 *
	 * @param geometry
	 *            The geometry to convert into a DTO geometry.
	 * @return Returns a DTO type geometry, that is serializable.
	 */
	public static Geometry toDto(org.geomajas.gwt.client.spatial.geometry.Geometry geometry) {
		if (geometry == null) {
			return null;
		}
		int srid = geometry.getSrid();
		int precision = geometry.getPrecision();

		Geometry dto = null;
		if (geometry instanceof Point) {
			dto = new Geometry("Point", srid, precision);
			dto.setCoordinates(geometry.getCoordinates());
		} else if (geometry instanceof LinearRing) {
			dto = new Geometry("LinearRing", srid, precision);
			dto.setCoordinates(geometry.getCoordinates());
		} else if (geometry instanceof LineString) {
			dto = new Geometry("LineString", srid, precision);
			dto.setCoordinates(geometry.getCoordinates());
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
	 * Takes in a DTO geometry, and converts it into a GWT geometry.
	 *
	 * @param geometry
	 *            The DTO geometry to convert into a GWT geometry.
	 * @return Returns a GWT geometry.
	 */
	public static org.geomajas.gwt.client.spatial.geometry.Geometry toGwt(Geometry geometry) {
		if (geometry == null) {
			return null;
		}
		GeometryFactory factory = new GeometryFactory(geometry.getSrid(), geometry.getPrecision());
		org.geomajas.gwt.client.spatial.geometry.Geometry gwt = null;

		String geometryType = geometry.getGeometryType();
		if ("Point".equals(geometryType)) {
			gwt = factory.createPoint(geometry.getCoordinates()[0]);
		} else if ("LinearRing".equals(geometryType)) {
			gwt = factory.createLinearRing(geometry.getCoordinates());
		} else if ("LineString".equals(geometryType)) {
			gwt = factory.createLineString(geometry.getCoordinates());
		} else if ("Polygon".equals(geometryType)) {
			LinearRing exteriorRing = (LinearRing) toGwt(geometry.getGeometries()[0]);
			LinearRing[] interiorRings = new LinearRing[geometry.getGeometries().length - 1];
			for (int i = 0; i < interiorRings.length; i++) {
				interiorRings[i] = (LinearRing) toGwt(geometry.getGeometries()[i + 1]);
			}
			gwt = factory.createPolygon(exteriorRing, interiorRings);
		} else if ("MultiPoint".equals(geometryType)) {
			Point[] points = new Point[geometry.getGeometries().length];
			gwt = factory.createMultiPoint((Point[]) convertGeometries(geometry, points));
		} else if ("MultiLineString".equals(geometryType)) {
			LineString[] lineStrings = new LineString[geometry.getGeometries().length];
			gwt = factory.createMultiLineString((LineString[]) convertGeometries(geometry, lineStrings));
		} else if ("MultiPolygon".equals(geometryType)) {
			Polygon[] polygons = new Polygon[geometry.getGeometries().length];
			gwt = factory.createMultiPolygon((Polygon[]) convertGeometries(geometry, polygons));
		}

		return gwt;
	}

	// -------------------------------------------------------------------------
	// Private functions converting from GWT to DTO:
	// -------------------------------------------------------------------------

	private static Geometry[] convertGeometries(org.geomajas.gwt.client.spatial.geometry.Geometry geometry) {
		Geometry[] geometries = new Geometry[geometry.getNumGeometries()];
		for (int i = 0; i < geometries.length; i++) {
			geometries[i] = toDto(geometry.getGeometryN(i));
		}
		return geometries;
	}

	// -------------------------------------------------------------------------
	// Private functions converting from DTO to GWT:
	// -------------------------------------------------------------------------

	private static org.geomajas.gwt.client.spatial.geometry.Geometry[] convertGeometries(Geometry geometry,
			org.geomajas.gwt.client.spatial.geometry.Geometry[] geometries) {
		for (int i = 0; i < geometries.length; i++) {
			geometries[i] = toGwt(geometry.getGeometries()[i]);
		}
		return geometries;
	}
}
