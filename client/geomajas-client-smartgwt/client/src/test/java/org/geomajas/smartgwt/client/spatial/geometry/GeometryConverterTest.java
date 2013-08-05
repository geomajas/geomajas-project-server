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

package org.geomajas.smartgwt.client.spatial.geometry;

import junit.framework.Assert;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.smartgwt.client.util.GeometryConverter;
import org.geomajas.smartgwt.client.spatial.geometry.*;
import org.junit.Test;

/**
 * <p>
 * Test class that test conversions between GWT and DTO geometries. It specifically tests the {@link GeometryConverter}
 * class, which executes these tests.
 * </p>
 *
 * @author Pieter De Graef
 */
public class GeometryConverterTest {

	private static final int SRID = 31300;

	private GeometryFactory factory;

	private Coordinate c1;

	private Coordinate c2;

	private Coordinate c3;

	private Coordinate c4;

	private Coordinate c5;

	private Coordinate c6;

	private Coordinate c7;

	private Coordinate c8;

	// -------------------------------------------------------------------------
	// Constructor, initializes all variables:
	// -------------------------------------------------------------------------

	public GeometryConverterTest() {
		factory = new GeometryFactory(SRID, -1);

		c1 = new Coordinate(10.0, 10.0);
		c2 = new Coordinate(20.0, 10.0);
		c3 = new Coordinate(20.0, 20.0);
		c4 = new Coordinate(10.0, 20.0);
		c5 = new Coordinate(12.0, 12.0);
		c6 = new Coordinate(12.0, 18.0);
		c7 = new Coordinate(18.0, 18.0);
		c8 = new Coordinate(18.0, 12.0);
	}

	// -------------------------------------------------------------------------
	// Test geometry conversions from GWT to DTO:
	// -------------------------------------------------------------------------

	@Test
	public void gwtPointToDto() {
		// Test GWT Point to DTO:
		Geometry point = GeometryConverter.toDto(createJtsPoint());
		Assert.assertEquals(c1.getX(), point.getCoordinates()[0].getX());
	}

	@Test
	public void gwtLineStringToDto() {
		// Test GWT LineString to DTO:
		Geometry lineString = GeometryConverter.toDto(createJtsLineString());
		Assert.assertEquals(c2.getX(), lineString.getCoordinates()[1].getX());
	}

	@Test
	public void gwtLinearRingToDto() {
		// Test GWT LinearRing to DTO:
		Geometry linearRing = GeometryConverter.toDto(createJtsLinearRing());
		Assert.assertEquals(c4.getX(), linearRing.getCoordinates()[3].getX());
	}

	@Test
	public void gwtPolygonToDto() {
		// Test GWT Polygon to DTO:
		Geometry polygon = GeometryConverter.toDto(createJtsPolygon());
		Assert.assertEquals(c6.getX(), polygon.getGeometries()[1].getCoordinates()[1].getX());
	}

	@Test
	public void gwtMultiPointToDto() {
		// Test GWT MultiPoint to DTO:
		Geometry multiPoint = GeometryConverter.toDto(createJtsMultiPoint());
		Assert.assertEquals(c3.getX(), multiPoint.getGeometries()[2].getCoordinates()[0].getX());
	}

	@Test
	public void gwtMultiLineStringToDto() {
		// Test GWT MultiLineString to DTO:
		Geometry multiLineString = GeometryConverter.toDto(createJtsMultiLineString());
		Assert.assertEquals(c7.getX(), multiLineString.getGeometries()[1].getCoordinates()[2].getX());
	}

	@Test
	public void gwtMultiPolygonToDto() {
		// Test GWT MultiPolygon to DTO:
		Geometry multiPolygon = GeometryConverter.toDto(createJtsMultiPolygon());
		Assert.assertEquals(c7.getX(), multiPolygon.getGeometries()[1].getGeometries()[1].getCoordinates()[2].getX());
	}

	// -------------------------------------------------------------------------
	// Test geometry conversions from DTO to GWT:
	// -------------------------------------------------------------------------

	@Test
	public void dtoPointToJts() {
		// Test DTO Point to GWT:
		Point point = (Point) GeometryConverter.toGwt(createDtoPoint());
		Assert.assertEquals(c1.getX(), point.getX());
	}

	@Test
	public void dtoLineStringToJts() {
		// Test DTO LineString to GWT:
		LineString lineString = (LineString) GeometryConverter.toGwt(createDtoLineString());
		Assert.assertEquals(c3.getX(), lineString.getCoordinateN(2).getX());
	}

	@Test
	public void dtoLinearRingToJts() {
		// Test DTO LinearRing to GWT:
		LinearRing linearRing = (LinearRing) GeometryConverter.toGwt(createDtoLinearRing());
		Assert.assertEquals(c3.getX(), linearRing.getCoordinateN(2).getX());
	}

	@Test
	public void dtoPolygonToJts() {
		// Test DTO Polygon to GWT:
		Polygon polygon = (Polygon) GeometryConverter.toGwt(createDtoPolygon());
		Assert.assertEquals(c6.getX(), polygon.getInteriorRingN(0).getCoordinateN(1).getX());
	}

	@Test
	public void dtoMultiPointToJts() {
		// Test DTO MultiPoint to GWT:
		MultiPoint multiPoint = (MultiPoint) GeometryConverter.toGwt(createDtoMultiPoint());
		Assert.assertEquals(c2.getX(), multiPoint.getGeometryN(1).getCoordinates()[0].getX());
	}

	@Test
	public void dtoMultiLineStringToJts() {
		// Test DTO MultiLineString to GWT:
		MultiLineString multiLineString = (MultiLineString) GeometryConverter.toGwt(createDtoMultiLineString());
		Assert.assertEquals(c7.getX(), multiLineString.getGeometryN(1).getCoordinates()[2].getX());
	}

	@Test
	public void dtoMultiPolygonToJts() {
		// Test DTO MultiPolygon to GWT:
		MultiPolygon multiPolygon = (MultiPolygon) GeometryConverter.toGwt(createDtoMultiPolygon());
		Polygon polygon = (Polygon) multiPolygon.getGeometryN(1);
		Assert.assertEquals(c6.getX(), polygon.getInteriorRingN(0).getCoordinateN(1).getX());
	}

	// -------------------------------------------------------------------------
	// Private methods for creating GWT geometries:
	// -------------------------------------------------------------------------

	private Point createJtsPoint() {
		return factory.createPoint(c1);
	}

	private LineString createJtsLineString() {
		return factory.createLineString(new Coordinate[] {c1, c2, c3, c4});
	}

	private LinearRing createJtsLinearRing() {
		return factory.createLinearRing(new Coordinate[] {c1, c2, c3, c4,
				c1});
	}

	private Polygon createJtsPolygon() {
		LinearRing shell = factory.createLinearRing(new Coordinate[] {c1, c2, c3,
				c4, c1});
		LinearRing hole = factory.createLinearRing(new Coordinate[] {c5, c6, c7,
				c8, c5});
		return factory.createPolygon(shell, new LinearRing[] {hole});
	}

	private MultiPoint createJtsMultiPoint() {
		Point p1 = factory.createPoint(c1);
		Point p2 = factory.createPoint(c2);
		Point p3 = factory.createPoint(c3);
		return factory.createMultiPoint(new Point[] {p1, p2, p3});
	}

	private MultiLineString createJtsMultiLineString() {
		LineString l1 = factory.createLineString(new Coordinate[] {c1, c2, c3,
				c4});
		LineString l2 = factory.createLineString(new Coordinate[] {c5, c6, c7,
				c8});
		return factory.createMultiLineString(new LineString[] {l1, l2});
	}

	private MultiPolygon createJtsMultiPolygon() {
		LinearRing shell = factory.createLinearRing(new Coordinate[] {c1, c2, c3,
				c4, c1});
		LinearRing hole = factory.createLinearRing(new Coordinate[] {c5, c6, c7,
				c8, c5});
		Polygon polygon1 = factory.createPolygon(shell, new LinearRing[] {});
		Polygon polygon2 = factory.createPolygon(shell, new LinearRing[] {hole});
		return factory.createMultiPolygon(new Polygon[] {polygon1, polygon2});
	}

	// -------------------------------------------------------------------------
	// Private methods for creating DTO geometries:
	// -------------------------------------------------------------------------

	private Geometry createDtoPoint() {
		Geometry geometry = new Geometry(Geometry.POINT, SRID, -1);
		geometry.setCoordinates(new Coordinate[] {c1});
		return geometry;
	}

	private Geometry createDtoLineString() {
		Geometry geometry = new Geometry(Geometry.LINE_STRING, SRID, -1);
		geometry.setCoordinates(new Coordinate[] {c1, c2, c3, c4});
		return geometry;
	}

	private Geometry createDtoLinearRing() {
		Geometry geometry = new Geometry(Geometry.LINEAR_RING, SRID, -1);
		geometry.setCoordinates(new Coordinate[] {c1, c2, c3, c4, c1});
		return geometry;
	}

	private Geometry createDtoPolygon() {
		Geometry shell = new Geometry(Geometry.LINEAR_RING, SRID, -1);
		shell.setCoordinates(new Coordinate[] {c1, c2, c3, c4, c1});

		Geometry hole = new Geometry(Geometry.LINEAR_RING, SRID, -1);
		hole.setCoordinates(new Coordinate[] {c5, c6, c7, c8, c5});

		Geometry geometry = new Geometry(Geometry.POLYGON, SRID, -1);
		geometry.setGeometries(new Geometry[] {shell, hole});
		return geometry;
	}

	private Geometry createDtoMultiPoint() {
		Geometry point1 = new Geometry(Geometry.POINT, SRID, -1);
		point1.setCoordinates(new Coordinate[] {c1});

		Geometry point2 = new Geometry(Geometry.POINT, SRID, -1);
		point2.setCoordinates(new Coordinate[] {c2});

		Geometry geometry = new Geometry(Geometry.MULTI_POINT, SRID, -1);
		geometry.setGeometries(new Geometry[] {point1, point2});
		return geometry;
	}

	private Geometry createDtoMultiLineString() {
		Geometry lineString1 = new Geometry(Geometry.LINE_STRING, SRID, -1);
		lineString1.setCoordinates(new Coordinate[] {c1, c2, c3, c4});

		Geometry lineString2 = new Geometry(Geometry.LINE_STRING, SRID, -1);
		lineString2.setCoordinates(new Coordinate[] {c5, c6, c7, c8});

		Geometry geometry = new Geometry(Geometry.MULTI_LINE_STRING, SRID, -1);
		geometry.setGeometries(new Geometry[] {lineString1, lineString2});
		return geometry;
	}

	private Geometry createDtoMultiPolygon() {
		Geometry geometry = new Geometry(Geometry.MULTI_POLYGON, SRID, -1);
		geometry.setGeometries(new Geometry[] {createDtoPolygon(), createDtoPolygon()});
		return geometry;
	}
}
