/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client.map.feature;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Geometry;
import org.geomajas.puregwt.client.spatial.GeometryFactory;
import org.geomajas.puregwt.client.spatial.LineString;
import org.geomajas.puregwt.client.spatial.LinearRing;
import org.geomajas.puregwt.client.spatial.MultiLineString;
import org.geomajas.puregwt.client.spatial.MultiPoint;
import org.geomajas.puregwt.client.spatial.MultiPolygon;
import org.geomajas.puregwt.client.spatial.Point;
import org.geomajas.puregwt.client.spatial.Polygon;

import com.google.inject.Inject;

/**
 * Converter for geometric objects. This class supports converting between GWT and DTO geometries. The GWT geometries
 * are used client-side in the GWT face, while the DTO geometries are the ones sent to the server (hence the name...).
 *
 * @author Pieter De Graef
 */
public final class GeometryConverter {
	
	@Inject
	private GeometryFactory factory;

	@Inject
	private GeometryConverter() {
	}

	/**
	 * Takes in a GWT geometry, and creates a new DTO geometry from it.
	 *
	 * @param geometry
	 *            The geometry to convert into a DTO geometry.
	 * @return Returns a DTO type geometry, that is serializable.
	 */
	public Geometry toDto(org.geomajas.puregwt.client.spatial.Geometry geometry) {
		if (geometry == null) {
			return null;
		}

		Geometry dto = null;
		if (geometry instanceof Point) {
			dto = new Geometry(Geometry.POINT, 0, 0);
			dto.setCoordinates(geometry.getCoordinates());
		} else if (geometry instanceof LinearRing) {
			dto = new Geometry(Geometry.LINEAR_RING, 0, 0);
			dto.setCoordinates(geometry.getCoordinates());
		} else if (geometry instanceof LineString) {
			dto = new Geometry(Geometry.LINE_STRING, 0, 0);
			dto.setCoordinates(geometry.getCoordinates());
		} else if (geometry instanceof Polygon) {
			dto = new Geometry(Geometry.POLYGON, 0, 0);
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
			dto = new Geometry(Geometry.MULTI_POINT, 0, 0);
			dto.setGeometries(convertGeometries(geometry));
		} else if (geometry instanceof MultiLineString) {
			dto = new Geometry(Geometry.MULTI_LINE_STRING, 0, 0);
			dto.setGeometries(convertGeometries(geometry));
		} else if (geometry instanceof MultiPolygon) {
			dto = new Geometry(Geometry.MULTI_POLYGON, 0, 0);
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
	public org.geomajas.puregwt.client.spatial.Geometry toGwt(Geometry geometry) {
		if (geometry == null) {
			return null;
		}
		org.geomajas.puregwt.client.spatial.Geometry gwt = null;

		String geometryType = geometry.getGeometryType();
		if (Geometry.POINT.equals(geometryType)) {
			gwt = factory.createPoint(geometry.getCoordinates()[0]);
		} else if (Geometry.LINEAR_RING.equals(geometryType)) {
			gwt = factory.createLinearRing(geometry.getCoordinates());
		} else if (Geometry.LINE_STRING.equals(geometryType)) {
			gwt = factory.createLineString(geometry.getCoordinates());
		} else if (Geometry.POLYGON.equals(geometryType)) {
			LinearRing exteriorRing = (LinearRing) toGwt(geometry.getGeometries()[0]);
			LinearRing[] interiorRings = new LinearRing[geometry.getGeometries().length - 1];
			for (int i = 0; i < interiorRings.length; i++) {
				interiorRings[i] = (LinearRing) toGwt(geometry.getGeometries()[i + 1]);
			}
			gwt = factory.createPolygon(exteriorRing, interiorRings);
		} else if (Geometry.MULTI_POINT.equals(geometryType)) {
			Point[] points = new Point[geometry.getGeometries().length];
			gwt = factory.createMultiPoint((Point[]) convertGeometries(geometry, points));
		} else if (Geometry.MULTI_LINE_STRING.equals(geometryType)) {
			LineString[] lineStrings = new LineString[geometry.getGeometries().length];
			gwt = factory.createMultiLineString((LineString[]) convertGeometries(geometry, lineStrings));
		} else if (Geometry.MULTI_POLYGON.equals(geometryType)) {
			Polygon[] polygons = new Polygon[geometry.getGeometries().length];
			gwt = factory.createMultiPolygon((Polygon[]) convertGeometries(geometry, polygons));
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
	public Bbox toDto(org.geomajas.puregwt.client.spatial.Bbox bbox) {
		return new Bbox(bbox.getX(), bbox.getY(), bbox.getWidth(), bbox.getHeight());
	}
	
	/**
	 * Takes in a DTO bbox, and creates a new GWT bbox from it.
	 *
	 * @param bbox
	 *            The bbox to convert into a GWT bbox.
	 * @return Returns a GWT type bbox, that has functionality.
	 */
	public org.geomajas.puregwt.client.spatial.Bbox toGwt(Bbox bbox) {
		return factory.createBbox(bbox); 
	}

	// -------------------------------------------------------------------------
	// Private functions converting from GWT to DTO:
	// -------------------------------------------------------------------------

	private Geometry[] convertGeometries(org.geomajas.puregwt.client.spatial.Geometry geometry) {
		Geometry[] geometries = new Geometry[geometry.getNumGeometries()];
		for (int i = 0; i < geometries.length; i++) {
			geometries[i] = toDto(geometry.getGeometryN(i));
		}
		return geometries;
	}

	// -------------------------------------------------------------------------
	// Private functions converting from DTO to GWT:
	// -------------------------------------------------------------------------

	private org.geomajas.puregwt.client.spatial.Geometry[] convertGeometries(Geometry geometry,
			org.geomajas.puregwt.client.spatial.Geometry[] geometries) {
		for (int i = 0; i < geometries.length; i++) {
			geometries[i] = toGwt(geometry.getGeometries()[i]);
		}
		return geometries;
	}
}