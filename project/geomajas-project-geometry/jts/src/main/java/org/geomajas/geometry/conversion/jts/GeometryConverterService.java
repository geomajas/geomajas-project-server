/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.geometry.conversion.jts;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.GeometryService;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * Converter service for converting shapes between the Geomajas and the JTS geometry model. Supported types are:
 * <ul>
 * <li>{@link org.geomajas.geometry.Geometry}</li>
 * <li>{@link org.geomajas.geometry.Bbox}</li>
 * <li>{@link org.geomajas.geometry.Coordinate}</li>
 * </ul>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public final class GeometryConverterService {

	private GeometryConverterService() {
		// Final class should not have a public constructor.
	}

	/**
	 * Convert a Geomajas geometry to a JTS geometry.
	 *
	 * @param geometry Geomajas geometry
	 * @return JTS geometry
	 * @throws JtsConversionException conversion failed
	 */
	public static com.vividsolutions.jts.geom.Geometry toJts(Geometry geometry) throws JtsConversionException {
		if (geometry == null) {
			throw new  JtsConversionException("Cannot convert null argument");
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
		com.vividsolutions.jts.geom.Geometry jts;

		String geometryType = geometry.getGeometryType();
		if (GeometryService.isEmpty(geometry)) {
			jts = createEmpty(factory, geometryType);
		} else if (Geometry.POINT.equals(geometryType)) {
			jts = factory.createPoint(convertCoordinates(geometry)[0]);
		} else if (Geometry.LINEAR_RING.equals(geometryType)) {
			jts = factory.createLinearRing(convertCoordinates(geometry));
		} else if (Geometry.LINE_STRING.equals(geometryType)) {
			jts = factory.createLineString(convertCoordinates(geometry));
		} else if (Geometry.POLYGON.equals(geometryType)) {
			Geometry[] geometries = geometry.getGeometries();
			if (null != geometries && geometries.length > 0) {
				LinearRing exteriorRing = (LinearRing) toJts(geometries[0]);
				LinearRing[] interiorRings = new LinearRing[geometries.length - 1];
				for (int i = 0; i < interiorRings.length; i++) {
					interiorRings[i] = (LinearRing) toJts(geometries[i + 1]);
				}
				jts = factory.createPolygon(exteriorRing, interiorRings);
			} else {
				jts = factory.createPolygon(null, null);
			}
		} else if (Geometry.MULTI_POINT.equals(geometryType)) {
			Point[] points = new Point[geometry.getGeometries().length];
			jts = factory.createMultiPoint((Point[]) convertGeometries(geometry, points));
		} else if (Geometry.MULTI_LINE_STRING.equals(geometryType)) {
			LineString[] lineStrings = new LineString[geometry.getGeometries().length];
			jts = factory.createMultiLineString((LineString[]) convertGeometries(geometry, lineStrings));
		} else if (Geometry.MULTI_POLYGON.equals(geometryType)) {
			Polygon[] polygons = new Polygon[geometry.getGeometries().length];
			jts = factory.createMultiPolygon((Polygon[]) convertGeometries(geometry, polygons));
		} else {
			throw new JtsConversionException("Cannot convert geometry: Unsupported type.");
		}

		return jts;
	}

	/**
	 * Convert a Geomajas bounding box to a JTS envelope.
	 *
	 * @param bbox Geomajas bbox
	 * @return JTS Envelope
	 * @throws JtsConversionException conversion failed
	 */
	public static Envelope toJts(Bbox bbox) throws JtsConversionException {
		if (bbox == null) {
			throw new  JtsConversionException("Cannot convert null argument");
		}
		return new Envelope(bbox.getX(), bbox.getMaxX(), bbox.getY(), bbox.getMaxY());
	}

	/**
	 * Convert a Geomajas coordinate to a JTS coordinate.
	 *
	 * @param coordinate Geomajas coordinate
	 * @return JTS coordinate
	 * @throws JtsConversionException
	 */
	public static com.vividsolutions.jts.geom.Coordinate toJts(org.geomajas.geometry.Coordinate coordinate)
			throws JtsConversionException {
		if (coordinate == null) {
			throw new  JtsConversionException("Cannot convert null argument");
		}
		return new com.vividsolutions.jts.geom.Coordinate(coordinate.getX(), coordinate.getY());
	}

	/**
	 * Convert a JTS geometry to a Geomajas geometry.
	 * @param geometry JTS geometry
	 * @return Geomajas geometry
	 * @throws JtsConversionException conversion failed
	 */
	public static Geometry fromJts(com.vividsolutions.jts.geom.Geometry geometry) throws JtsConversionException {
		if (geometry == null) {
			throw new  JtsConversionException("Cannot convert null argument");
		}
		int srid = geometry.getSRID();
		int precision = -1;
		PrecisionModel precisionmodel = geometry.getPrecisionModel();
		if (!precisionmodel.isFloating()) {
			precision = (int) Math.log10(precisionmodel.getScale());
		}
		String geometryType = getGeometryType(geometry);
		Geometry dto = new Geometry(geometryType, srid, precision);
		if (geometry.isEmpty()) {
			// nothing to do
		} else if (geometry instanceof Point) {
			dto.setCoordinates(convertCoordinates(geometry));
		} else if (geometry instanceof LinearRing) {
			dto.setCoordinates(convertCoordinates(geometry));
		} else if (geometry instanceof LineString) {
			dto.setCoordinates(convertCoordinates(geometry));
		} else if (geometry instanceof Polygon) {
			Polygon polygon = (Polygon) geometry;
			Geometry[] geometries = new Geometry[polygon.getNumInteriorRing() + 1];
			for (int i = 0; i < geometries.length; i++) {
				if (i == 0) {
					geometries[i] = fromJts(polygon.getExteriorRing());
				} else {
					geometries[i] = fromJts(polygon.getInteriorRingN(i - 1));
				}
			}
			dto.setGeometries(geometries);
		} else if (geometry instanceof MultiPoint) {
			dto.setGeometries(convertGeometries(geometry));
		} else if (geometry instanceof MultiLineString) {
			dto.setGeometries(convertGeometries(geometry));
		} else if (geometry instanceof MultiPolygon) {
			dto.setGeometries(convertGeometries(geometry));
		} else {
			throw new JtsConversionException("Cannot convert geometry: Unsupported type.");
		}
		return dto;
	}

	/**
	 * Convert a JTS envelope to a Geomajas bounding box.
	 *
	 * @param envelope JTS envelope
	 * @return Geomajas bbox
	 * @throws JtsConversionException conversion failed
	 */
	public static Bbox fromJts(Envelope envelope) throws JtsConversionException {
		if (envelope == null) {
			throw new  JtsConversionException("Cannot convert null argument");
		}
		return new Bbox(envelope.getMinX(), envelope.getMinY(), envelope.getWidth(), envelope.getHeight());
	}

	/**
	 * Convert a GTS coordinate to a Geomajas coordinate.
	 *
	 * @param coordinate jTS coordinate
	 * @return Geomajas coordinate
	 * @throws JtsConversionException conversion failed
	 */
	public static Coordinate fromJts(com.vividsolutions.jts.geom.Coordinate coordinate) throws JtsConversionException {
		if (coordinate == null) {
			throw new  JtsConversionException("Cannot convert null argument");
		}
		return new Coordinate(coordinate.x, coordinate.y);
	}

	private static com.vividsolutions.jts.geom.Geometry createEmpty(GeometryFactory factory, String geometryType)
			throws JtsConversionException {
		if (Geometry.POINT.equals(geometryType)) {
			return new Point(null, factory); // do not use GeometryFactory.createPoint(null,...) as that returns null
		} else if (Geometry.LINEAR_RING.equals(geometryType)) {
			return factory.createLinearRing((com.vividsolutions.jts.geom.Coordinate[]) null);
		} else if (Geometry.LINE_STRING.equals(geometryType)) {
			return factory.createLineString((com.vividsolutions.jts.geom.Coordinate[]) null);
		} else if (Geometry.POLYGON.equals(geometryType)) {
			return factory.createPolygon(null, null);
		} else if (Geometry.MULTI_POINT.equals(geometryType)) {
			return factory.createMultiPoint((Point[]) null);
		} else if (Geometry.MULTI_LINE_STRING.equals(geometryType)) {
			return factory.createMultiLineString(null);
		} else if (Geometry.MULTI_POLYGON.equals(geometryType)) {
			return factory.createMultiPolygon(null);
		} else {
			throw new JtsConversionException("Error while converting to Geomajas: Unknown geometry type.");
		}
	}

	private static String getGeometryType(com.vividsolutions.jts.geom.Geometry geometry) throws JtsConversionException {
		if (geometry instanceof Point) {
			return Geometry.POINT;
		} else if (geometry instanceof LinearRing) {
			return Geometry.LINEAR_RING;
		} else if (geometry instanceof LineString) {
			return Geometry.LINE_STRING;
		} else if (geometry instanceof Polygon) {
			return Geometry.POLYGON;
		} else if (geometry instanceof MultiPoint) {
			return Geometry.MULTI_POINT;
		} else if (geometry instanceof MultiLineString) {
			return Geometry.MULTI_LINE_STRING;
		} else if (geometry instanceof GeometryCollection) {
			// Multi-polygon and other GeometryCollection implementations
			return Geometry.MULTI_POLYGON;
		} else {
			throw new JtsConversionException("Error while converting to Geomajas: Unknown geometry type.");
		}
	}

	private static Coordinate[] convertCoordinates(com.vividsolutions.jts.geom.Geometry geometry) {
		Coordinate[] coordinates = new Coordinate[geometry.getCoordinates().length];
		for (int i = 0; i < coordinates.length; i++) {
			coordinates[i] = new Coordinate(geometry.getCoordinates()[i].x, geometry.getCoordinates()[i].y);
		}
		return coordinates;
	}

	private static Geometry[] convertGeometries(com.vividsolutions.jts.geom.Geometry geometry)
			throws JtsConversionException {
		Geometry[] geometries = new Geometry[geometry.getNumGeometries()];
		for (int i = 0; i < geometries.length; i++) {
			geometries[i] = fromJts(geometry.getGeometryN(i));
		}
		return geometries;
	}

	private static com.vividsolutions.jts.geom.Coordinate[] convertCoordinates(Geometry geometry) {
		com.vividsolutions.jts.geom.Coordinate[] coordinates = new com.vividsolutions.jts.geom.Coordinate[geometry
				.getCoordinates().length];
		for (int i = 0; i < coordinates.length; i++) {
			coordinates[i] = new com.vividsolutions.jts.geom.Coordinate(geometry.getCoordinates()[i].getX(),
					geometry.getCoordinates()[i].getY());
		}
		return coordinates;
	}

	private static com.vividsolutions.jts.geom.Geometry[] convertGeometries(Geometry geometry,
			com.vividsolutions.jts.geom.Geometry[] geometries) throws JtsConversionException {
		for (int i = 0; i < geometries.length; i++) {
			geometries[i] = toJts(geometry.getGeometries()[i]);
		}
		return geometries;
	}
}