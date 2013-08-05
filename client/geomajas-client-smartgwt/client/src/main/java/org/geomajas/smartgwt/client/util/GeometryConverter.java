/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.smartgwt.client.util;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Geometry;
import org.geomajas.smartgwt.client.spatial.geometry.GeometryFactory;
import org.geomajas.smartgwt.client.spatial.geometry.LineString;
import org.geomajas.smartgwt.client.spatial.geometry.LinearRing;
import org.geomajas.smartgwt.client.spatial.geometry.MultiLineString;
import org.geomajas.smartgwt.client.spatial.geometry.MultiPoint;
import org.geomajas.smartgwt.client.spatial.geometry.MultiPolygon;
import org.geomajas.smartgwt.client.spatial.geometry.Point;
import org.geomajas.smartgwt.client.spatial.geometry.Polygon;
import org.geomajas.gwt.client.util.Log;

/**
 * Converter for geometric objects. This class supports converting between GWT and DTO geometries. The GWT geometries
 * are used client-side in the GWT face, while the DTO geometries are the ones sent to the server (hence the name...).
 * 
 * @author Pieter De Graef
 * @since 1.9.0
 */
@Api(allMethods = true)
public final class GeometryConverter {

	private GeometryConverter() {
		// hide constructor
	}

	/**
	 * Takes in a GWT geometry, and creates a new DTO geometry from it.
	 * 
	 * @param geometry
	 *            The geometry to convert into a DTO geometry.
	 * @return Returns a DTO type geometry, that is serializable.
	 */
	public static Geometry toDto(org.geomajas.smartgwt.client.spatial.geometry.Geometry geometry) {
		if (geometry == null) {
			return null;
		}
		int srid = geometry.getSrid();
		int precision = geometry.getPrecision();

		Geometry dto;
		if (geometry instanceof Point) {
			dto = new Geometry(Geometry.POINT, srid, precision);
			dto.setCoordinates(geometry.getCoordinates());
		} else if (geometry instanceof LinearRing) {
			dto = new Geometry(Geometry.LINEAR_RING, srid, precision);
			dto.setCoordinates(geometry.getCoordinates());
		} else if (geometry instanceof LineString) {
			dto = new Geometry(Geometry.LINE_STRING, srid, precision);
			dto.setCoordinates(geometry.getCoordinates());
		} else if (geometry instanceof Polygon) {
			dto = new Geometry(Geometry.POLYGON, srid, precision);
			Polygon polygon = (Polygon) geometry;
			if (!polygon.isEmpty()) {
				Geometry[] geometries = new Geometry[polygon.getNumInteriorRing() + 1];
				for (int i = 0; i < geometries.length; i++) {
					if (i == 0) {
						geometries[i] = toDto(polygon.getExteriorRing());
					} else {
						geometries[i] = toDto(polygon.getInteriorRingN(i - 1));
					}
				}
				dto.setGeometries(geometries);
			}
		} else if (geometry instanceof MultiPoint) {
			dto = new Geometry(Geometry.MULTI_POINT, srid, precision);
			dto.setGeometries(convertGeometries(geometry));
		} else if (geometry instanceof MultiLineString) {
			dto = new Geometry(Geometry.MULTI_LINE_STRING, srid, precision);
			dto.setGeometries(convertGeometries(geometry));
		} else if (geometry instanceof MultiPolygon) {
			dto = new Geometry(Geometry.MULTI_POLYGON, srid, precision);
			dto.setGeometries(convertGeometries(geometry));
		} else {
			String msg = "GeometryConverter.toDto() unrecognized geometry type";
			Log.logServer(Log.LEVEL_ERROR, msg);
			throw new IllegalStateException(msg);
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
	public static org.geomajas.smartgwt.client.spatial.geometry.Geometry toGwt(Geometry geometry) {
		if (geometry == null) {
			return null;
		}
		GeometryFactory factory = new GeometryFactory(geometry.getSrid(), geometry.getPrecision());
		org.geomajas.smartgwt.client.spatial.geometry.Geometry gwt;

		String geometryType = geometry.getGeometryType();
		if (Geometry.POINT.equals(geometryType)) {
			if (geometry.getCoordinates() != null) {
				gwt = factory.createPoint(geometry.getCoordinates()[0]);
			} else {
				gwt = factory.createPoint(null);
			}
		} else if (Geometry.LINEAR_RING.equals(geometryType)) {
			gwt = factory.createLinearRing(geometry.getCoordinates());
		} else if (Geometry.LINE_STRING.equals(geometryType)) {
			gwt = factory.createLineString(geometry.getCoordinates());
		} else if (Geometry.POLYGON.equals(geometryType)) {
			if (geometry.getGeometries() == null) {
				gwt = factory.createPolygon(null, null);
			} else {
				LinearRing exteriorRing = (LinearRing) toGwt(geometry.getGeometries()[0]);
				LinearRing[] interiorRings = new LinearRing[geometry.getGeometries().length - 1];
				for (int i = 0; i < interiorRings.length; i++) {
					interiorRings[i] = (LinearRing) toGwt(geometry.getGeometries()[i + 1]);
				}
				gwt = factory.createPolygon(exteriorRing, interiorRings);
			}
		} else if (Geometry.MULTI_POINT.equals(geometryType)) {
			if (geometry.getGeometries() == null) {
				gwt = factory.createMultiPoint(null);
			} else {
				Point[] points = new Point[geometry.getGeometries().length];
				gwt = factory.createMultiPoint((Point[]) convertGeometries(geometry, points));
			}
		} else if (Geometry.MULTI_LINE_STRING.equals(geometryType)) {
			if (geometry.getGeometries() == null) {
				gwt = factory.createMultiLineString(null);
			} else {
				LineString[] lineStrings = new LineString[geometry.getGeometries().length];
				gwt = factory.createMultiLineString((LineString[]) convertGeometries(geometry, lineStrings));
			}
		} else if (Geometry.MULTI_POLYGON.equals(geometryType)) {
			if (geometry.getGeometries() == null) {
				gwt = factory.createMultiPolygon(null);
			} else {
				Polygon[] polygons = new Polygon[geometry.getGeometries().length];
				gwt = factory.createMultiPolygon((Polygon[]) convertGeometries(geometry, polygons));
			}
		} else {
			String msg = "GeometryConverter.toGwt() unrecognized geometry type " + geometryType;
			Log.logServer(Log.LEVEL_ERROR, msg);
			throw new IllegalStateException(msg);
		}

		return gwt;
	}

	/**
	 * Takes in a GWT bbox, and creates a new DTO bbox from it.
	 * 
	 * @param bbox
	 *            The bbox to convert into a DTO bbox.
	 * @return Returns a DTO type bbox, that is serializable.
	 */
	public static Bbox toDto(org.geomajas.smartgwt.client.spatial.Bbox bbox) {
		return new Bbox(bbox.getX(), bbox.getY(), bbox.getWidth(), bbox.getHeight());
	}

	/**
	 * Takes in a DTO bbox, and creates a new GWT bbox from it.
	 * 
	 * @param bbox
	 *            The bbox to convert into a GWT bbox.
	 * @return Returns a GWT type bbox, that has functionality.
	 */
	public static org.geomajas.smartgwt.client.spatial.Bbox toGwt(Bbox bbox) {
		return new org.geomajas.smartgwt.client.spatial.Bbox(bbox);
	}

	// -------------------------------------------------------------------------
	// Private functions converting from GWT to DTO:
	// -------------------------------------------------------------------------

	private static Geometry[] convertGeometries(org.geomajas.smartgwt.client.spatial.geometry.Geometry geometry) {
		if (geometry.isEmpty()) {
			return null;
		}
		Geometry[] geometries = new Geometry[geometry.getNumGeometries()];
		for (int i = 0; i < geometries.length; i++) {
			geometries[i] = toDto(geometry.getGeometryN(i));
		}
		return geometries;
	}

	// -------------------------------------------------------------------------
	// Private functions converting from DTO to GWT:
	// -------------------------------------------------------------------------

	private static org.geomajas.smartgwt.client.spatial.geometry.Geometry[] convertGeometries(Geometry geometry,
			org.geomajas.smartgwt.client.spatial.geometry.Geometry[] geometries) {
		for (int i = 0; i < geometries.length; i++) {
			geometries[i] = toGwt(geometry.getGeometries()[i]);
		}
		return geometries;
	}
}
