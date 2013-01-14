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

package org.geomajas.geometry.service;

import junit.framework.Assert;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the {@link WktService}. Tests EWKT parsing and formatting.
 * 
 * @author Pieter De Graef
 */
public class WktServiceEwktTest {

	private static final int SRID = 4326;

	private static final String EWKT_EXT = "SRID=" + SRID + ";";

	private final Geometry point = new Geometry(Geometry.POINT, SRID, 0);

	private final Geometry lineString = new Geometry(Geometry.LINE_STRING, SRID, 0);

	private final Geometry linearRing = new Geometry(Geometry.LINEAR_RING, SRID, 0);

	private final Geometry polygon = new Geometry(Geometry.POLYGON, SRID, 0);

	private final Geometry multiPoint = new Geometry(Geometry.MULTI_POINT, SRID, 0);

	private final Geometry multiLineString = new Geometry(Geometry.MULTI_LINE_STRING, SRID, 0);

	private final Geometry multiPolygon = new Geometry(Geometry.MULTI_POLYGON, SRID, 0);

	@Before
	public void setup() {
		Coordinate c1 = new Coordinate(10, 10);
		Coordinate c2 = new Coordinate(10, 20);
		Coordinate c3 = new Coordinate(20, 20);
		point.setCoordinates(new Coordinate[] { c2 });
		lineString.setCoordinates(new Coordinate[] { c1, c2, c3 });
		linearRing.setCoordinates(new Coordinate[] { c1, c2, c3, c1 });

		Geometry hole = new Geometry(Geometry.LINEAR_RING, SRID, 0);
		hole.setCoordinates(new Coordinate[] { new Coordinate(14, 14), new Coordinate(14, 16), new Coordinate(16, 16),
				new Coordinate(14, 14) });
		polygon.setGeometries(new Geometry[] { linearRing, hole });

		Geometry point2 = new Geometry(Geometry.POINT, SRID, 0);
		point2.setCoordinates(new Coordinate[] { c3 });
		multiPoint.setGeometries(new Geometry[] { point, point2 });

		Geometry lineString2 = new Geometry(Geometry.LINE_STRING, SRID, 0);
		lineString2.setCoordinates(new Coordinate[] { new Coordinate(14, 14), new Coordinate(14, 16) });
		multiLineString.setGeometries(new Geometry[] { lineString, lineString2 });

		Geometry polygon2 = new Geometry(Geometry.POLYGON, SRID, 0);
		polygon2.setGeometries(new Geometry[] { linearRing });
		multiPolygon.setGeometries(new Geometry[] { polygon, polygon2 });
	}

	@Test
	public void testParsePoint() throws WktException {
		Assert.assertEquals(EWKT_EXT + "POINT EMPTY", WktService.toEwkt(new Geometry(Geometry.POINT, SRID, 0)));
		Assert.assertEquals(EWKT_EXT + "POINT (10.0 20.0)", WktService.toEwkt(point));
	}

	@Test
	public void testParseLineString() throws WktException {
		Assert.assertEquals(EWKT_EXT + "LINESTRING EMPTY",
				WktService.toEwkt(new Geometry(Geometry.LINE_STRING, SRID, 0)));
		Assert.assertEquals(EWKT_EXT + "LINESTRING (10.0 10.0, 10.0 20.0, 20.0 20.0)", WktService.toEwkt(lineString));
	}

	@Test
	public void testParseLinearRing() throws WktException {
		Assert.assertEquals(EWKT_EXT + "LINESTRING EMPTY",
				WktService.toEwkt(new Geometry(Geometry.LINEAR_RING, SRID, 0)));
		Assert.assertEquals(EWKT_EXT + "LINESTRING (10.0 10.0, 10.0 20.0, 20.0 20.0, 10.0 10.0)",
				WktService.toEwkt(linearRing));
	}

	@Test
	public void testParsePolygon() throws WktException {
		Assert.assertEquals(EWKT_EXT + "POLYGON EMPTY", WktService.toEwkt(new Geometry(Geometry.POLYGON, SRID, 0)));
		Assert.assertEquals(EWKT_EXT + "POLYGON ((10.0 10.0, 10.0 20.0, 20.0 20.0, 10.0 10.0),"
				+ "(14.0 14.0, 14.0 16.0, 16.0 16.0, 14.0 14.0))", WktService.toEwkt(polygon));
	}

	@Test
	public void testParseMultiPoint() throws WktException {
		Assert.assertEquals(EWKT_EXT + "MULTIPOINT EMPTY",
				WktService.toEwkt(new Geometry(Geometry.MULTI_POINT, SRID, 0)));
		Assert.assertEquals(EWKT_EXT + "MULTIPOINT ((10.0 20.0),(20.0 20.0))", WktService.toEwkt(multiPoint));
	}

	@Test
	public void testParseMultiLineString() throws WktException {
		Assert.assertEquals(EWKT_EXT + "MULTILINESTRING EMPTY",
				WktService.toEwkt(new Geometry(Geometry.MULTI_LINE_STRING, SRID, 0)));
		Assert.assertEquals(EWKT_EXT + "MULTILINESTRING ((10.0 10.0, 10.0 20.0, 20.0 20.0),(14.0 14.0, 14.0 16.0))",
				WktService.toEwkt(multiLineString));
	}

	@Test
	public void testParseMultiPolygon() throws WktException {
		Assert.assertEquals(EWKT_EXT + "MULTIPOLYGON EMPTY",
				WktService.toEwkt(new Geometry(Geometry.MULTI_POLYGON, SRID, 0)));
		Assert.assertEquals(EWKT_EXT + "MULTIPOLYGON (((10.0 10.0, 10.0 20.0, 20.0 20.0, 10.0 10.0),"
				+ "(14.0 14.0, 14.0 16.0, 16.0 16.0, 14.0 14.0)),((10.0 10.0, 10.0 20.0, 20.0 20.0, 10.0 10.0)))",
				WktService.toEwkt(multiPolygon));
	}

	// ------------------------------------------------------------------------
	// Format Points:
	// ------------------------------------------------------------------------

	@Test
	public void formatEmptyPoint() throws WktException {
		Geometry geometry = WktService.toGeometry(EWKT_EXT + "POINT EMPTY");
		Assert.assertEquals(Geometry.POINT, geometry.getGeometryType());
		Assert.assertNull(geometry.getCoordinates());
		Assert.assertNull(geometry.getGeometries());
		Assert.assertEquals(SRID, geometry.getSrid());
	}

	@Test
	public void formatPoint() throws WktException {
		Geometry geometry = WktService.toGeometry(EWKT_EXT + "POINT (10.0 20.0)");
		Assert.assertEquals(Geometry.POINT, geometry.getGeometryType());
		Assert.assertNull(geometry.getGeometries());
		Assert.assertEquals(10.0, geometry.getCoordinates()[0].getX());
		Assert.assertEquals(20.0, geometry.getCoordinates()[0].getY());
		Assert.assertEquals(SRID, geometry.getSrid());
	}

	@Test
	public void formatPointCornerCases() {
		try {
			WktService.toGeometry(EWKT_EXT + "POINT(10.0 20.0)");
			Assert.fail();
		} catch (WktException e) {
			// We expect to get here.
		}

		try {
			WktService.toGeometry(EWKT_EXT + "POINT (10.0 20.0, 0.0 0.0)");
			Assert.fail();
		} catch (WktException e) {
			// We expect to get here.
		}

		try {
			WktService.toGeometry(EWKT_EXT + "POINT (10.0, 20.0)");
			Assert.fail();
		} catch (WktException e) {
			// We expect to get here.
		}

		try {
			WktService.toGeometry(EWKT_EXT + "POINT ((10.0 20.0))");
			Assert.fail();
		} catch (WktException e) {
			// We expect to get here.
		}
	}

	// ------------------------------------------------------------------------
	// Format LineStrings:
	// ------------------------------------------------------------------------

	@Test
	public void formatEmptyLineString() throws WktException {
		Geometry geometry = WktService.toGeometry(EWKT_EXT + "LINESTRING EMPTY");
		Assert.assertEquals(Geometry.LINE_STRING, geometry.getGeometryType());
		Assert.assertNull(geometry.getCoordinates());
		Assert.assertNull(geometry.getGeometries());
		Assert.assertEquals(SRID, geometry.getSrid());
	}

	@Test
	public void formatLineString() throws WktException {
		Geometry geometry = WktService.toGeometry(EWKT_EXT + "LINESTRING (1.0 2.0, 3.0 4.0)");
		Assert.assertEquals(Geometry.LINE_STRING, geometry.getGeometryType());
		Assert.assertEquals(1.0, geometry.getCoordinates()[0].getX());
		Assert.assertEquals(2.0, geometry.getCoordinates()[0].getY());
		Assert.assertEquals(3.0, geometry.getCoordinates()[1].getX());
		Assert.assertEquals(4.0, geometry.getCoordinates()[1].getY());
		Assert.assertNull(geometry.getGeometries());
		Assert.assertEquals(SRID, geometry.getSrid());
	}

	@Test
	public void formatLineStringCornerCases() {
		try {
			WktService.toGeometry(EWKT_EXT + "LINESTRING (10.0, 20.0)");
			Assert.fail();
		} catch (WktException e) {
			// We expect to get here.
		}

		try {
			WktService.toGeometry(EWKT_EXT + "LINESTRING ((10.0 20.0))");
			Assert.fail();
		} catch (WktException e) {
			// We expect to get here.
		}

		try {
			WktService.toGeometry(EWKT_EXT + "LINESTRING (10.0 20.0, 0.0 0.0))");
			Assert.fail();
		} catch (WktException e) {
			// We expect to get here.
		}
	}

	// ------------------------------------------------------------------------
	// Format Polygons:
	// ------------------------------------------------------------------------

	@Test
	public void formatEmptyPolygon() throws WktException {
		Geometry geometry = WktService.toGeometry(EWKT_EXT + "POLYGON EMPTY");
		Assert.assertEquals(Geometry.POLYGON, geometry.getGeometryType());
		Assert.assertNull(geometry.getCoordinates());
		Assert.assertNull(geometry.getGeometries());
		Assert.assertEquals(SRID, geometry.getSrid());
	}

	@Test
	public void formatPolygon() throws WktException {
		Geometry geometry = WktService.toGeometry(EWKT_EXT + "POLYGON ((1.0 2.0, 3.0 4.0, 1.0 2.0))");
		Assert.assertEquals(Geometry.POLYGON, geometry.getGeometryType());
		Assert.assertEquals(1.0, geometry.getGeometries()[0].getCoordinates()[0].getX());
		Assert.assertEquals(2.0, geometry.getGeometries()[0].getCoordinates()[0].getY());
		Assert.assertEquals(3.0, geometry.getGeometries()[0].getCoordinates()[1].getX());
		Assert.assertEquals(4.0, geometry.getGeometries()[0].getCoordinates()[1].getY());
		Assert.assertEquals(1.0, geometry.getGeometries()[0].getCoordinates()[2].getX());
		Assert.assertEquals(2.0, geometry.getGeometries()[0].getCoordinates()[2].getY());
		Assert.assertEquals(SRID, geometry.getSrid());
		Assert.assertEquals(SRID, geometry.getGeometries()[0].getSrid());

		geometry = WktService
				.toGeometry(EWKT_EXT + "POLYGON ((1.0 2.0, 3.0 4.0, 1.0 2.0),(5.0 6.0, 7.0 8.0, 5.0 6.0))");
		Assert.assertEquals(Geometry.POLYGON, geometry.getGeometryType());
		Assert.assertEquals(1.0, geometry.getGeometries()[0].getCoordinates()[0].getX());
		Assert.assertEquals(2.0, geometry.getGeometries()[0].getCoordinates()[0].getY());
		Assert.assertEquals(3.0, geometry.getGeometries()[0].getCoordinates()[1].getX());
		Assert.assertEquals(4.0, geometry.getGeometries()[0].getCoordinates()[1].getY());
		Assert.assertEquals(1.0, geometry.getGeometries()[0].getCoordinates()[2].getX());
		Assert.assertEquals(2.0, geometry.getGeometries()[0].getCoordinates()[2].getY());
		Assert.assertEquals(5.0, geometry.getGeometries()[1].getCoordinates()[0].getX());
		Assert.assertEquals(6.0, geometry.getGeometries()[1].getCoordinates()[0].getY());
		Assert.assertEquals(7.0, geometry.getGeometries()[1].getCoordinates()[1].getX());
		Assert.assertEquals(8.0, geometry.getGeometries()[1].getCoordinates()[1].getY());
		Assert.assertEquals(5.0, geometry.getGeometries()[1].getCoordinates()[2].getX());
		Assert.assertEquals(6.0, geometry.getGeometries()[1].getCoordinates()[2].getY());
		Assert.assertEquals(SRID, geometry.getSrid());
		Assert.assertEquals(SRID, geometry.getGeometries()[0].getSrid());
	}

	@Test
	public void formatPolygonCornerCases() {
		try {
			WktService.toGeometry(EWKT_EXT + "POLYGON (1.0 2.0, 3.0 4.0, 1.0 2.0)");
			Assert.fail();
		} catch (WktException e) {
			// We expect to get here.
		}

		try {
			WktService.toGeometry(EWKT_EXT + "POLYGON (((1.0 2.0, 3.0 4.0, 1.0 2.0)))");
			Assert.fail();
		} catch (WktException e) {
			// We expect to get here.
		}

		try {
			WktService.toGeometry(EWKT_EXT + "POLYGON ((1.0 2.0, 3.0 4.0, 1.0 2.0),((5.0 6.0, 7.0 8.0, 5.0 6.0)))");
			Assert.fail();
		} catch (WktException e) {
			// We expect to get here.
		}
	}

	// ------------------------------------------------------------------------
	// Format MultiPoints:
	// ------------------------------------------------------------------------

	@Test
	public void formatEmptyMultiPoint() throws WktException {
		Geometry geometry = WktService.toGeometry(EWKT_EXT + "MULTIPOINT EMPTY");
		Assert.assertEquals(Geometry.MULTI_POINT, geometry.getGeometryType());
		Assert.assertNull(geometry.getCoordinates());
		Assert.assertNull(geometry.getGeometries());
		Assert.assertEquals(SRID, geometry.getSrid());
	}

	@Test
	public void formatMultiPoint() throws WktException {
		Geometry geometry = WktService.toGeometry(EWKT_EXT + "MULTIPOINT ((1.0 2.0))");
		Assert.assertEquals(Geometry.MULTI_POINT, geometry.getGeometryType());
		Assert.assertEquals(1.0, geometry.getGeometries()[0].getCoordinates()[0].getX());
		Assert.assertEquals(2.0, geometry.getGeometries()[0].getCoordinates()[0].getY());
		Assert.assertEquals(SRID, geometry.getSrid());
		Assert.assertEquals(SRID, geometry.getGeometries()[0].getSrid());

		geometry = WktService.toGeometry(EWKT_EXT + "MULTIPOINT ((1.0 2.0),(3.0 4.0))");
		Assert.assertEquals(Geometry.MULTI_POINT, geometry.getGeometryType());
		Assert.assertEquals(1.0, geometry.getGeometries()[0].getCoordinates()[0].getX());
		Assert.assertEquals(2.0, geometry.getGeometries()[0].getCoordinates()[0].getY());
		Assert.assertEquals(3.0, geometry.getGeometries()[1].getCoordinates()[0].getX());
		Assert.assertEquals(4.0, geometry.getGeometries()[1].getCoordinates()[0].getY());
		Assert.assertEquals(SRID, geometry.getSrid());
		Assert.assertEquals(SRID, geometry.getGeometries()[0].getSrid());
		Assert.assertEquals(SRID, geometry.getGeometries()[1].getSrid());
	}

	@Test
	public void formatMultiPointCornerCases() {
		try {
			WktService.toGeometry(EWKT_EXT + "MULTIPOINT ((10.0 20.0, 0.0 0.0))");
			Assert.fail();
		} catch (WktException e) {
			// We expect to get here.
		}

		try {
			WktService.toGeometry(EWKT_EXT + "MULTIPOINT ((10.0, 20.0))");
			Assert.fail();
		} catch (WktException e) {
			// We expect to get here.
		}

		try {
			WktService.toGeometry(EWKT_EXT + "MULTIPOINT ((10.0, 20.0),((10.0, 20.0)))");
			Assert.fail();
		} catch (WktException e) {
			// We expect to get here.
		}

		try {
			WktService.toGeometry(EWKT_EXT + "MULTIPOINT ((10.0, 20.0)(10.0, 20.0))");
			Assert.fail();
		} catch (WktException e) {
			// We expect to get here.
		}

		try {
			WktService.toGeometry(EWKT_EXT + "MULTIPOINT (((10.0 20.0)))");
			Assert.fail();
		} catch (WktException e) {
			// We expect to get here.
		}
	}

	// ------------------------------------------------------------------------
	// Format MultiPoints:
	// ------------------------------------------------------------------------

	@Test
	public void formatEmptyMultiLineString() throws WktException {
		Geometry geometry = WktService.toGeometry(EWKT_EXT + "MULTILINESTRING EMPTY");
		Assert.assertEquals(Geometry.MULTI_LINE_STRING, geometry.getGeometryType());
		Assert.assertNull(geometry.getCoordinates());
		Assert.assertNull(geometry.getGeometries());
		Assert.assertEquals(SRID, geometry.getSrid());
	}

	@Test
	public void formatMultiLineString() throws WktException {
		Geometry geometry = WktService.toGeometry(EWKT_EXT + "MULTILINESTRING ((1.0 2.0, 3.0 4.0))");
		Assert.assertEquals(Geometry.MULTI_LINE_STRING, geometry.getGeometryType());
		Assert.assertEquals(1.0, geometry.getGeometries()[0].getCoordinates()[0].getX());
		Assert.assertEquals(2.0, geometry.getGeometries()[0].getCoordinates()[0].getY());
		Assert.assertEquals(3.0, geometry.getGeometries()[0].getCoordinates()[1].getX());
		Assert.assertEquals(4.0, geometry.getGeometries()[0].getCoordinates()[1].getY());
		Assert.assertEquals(SRID, geometry.getSrid());
		Assert.assertEquals(SRID, geometry.getGeometries()[0].getSrid());

		geometry = WktService.toGeometry(EWKT_EXT + "MULTILINESTRING ((1.0 2.0, 3.0 4.0), (5.0 6.0, 7.0 8.0))");
		Assert.assertEquals(Geometry.MULTI_LINE_STRING, geometry.getGeometryType());
		Assert.assertEquals(1.0, geometry.getGeometries()[0].getCoordinates()[0].getX());
		Assert.assertEquals(2.0, geometry.getGeometries()[0].getCoordinates()[0].getY());
		Assert.assertEquals(3.0, geometry.getGeometries()[0].getCoordinates()[1].getX());
		Assert.assertEquals(4.0, geometry.getGeometries()[0].getCoordinates()[1].getY());
		Assert.assertEquals(5.0, geometry.getGeometries()[1].getCoordinates()[0].getX());
		Assert.assertEquals(6.0, geometry.getGeometries()[1].getCoordinates()[0].getY());
		Assert.assertEquals(7.0, geometry.getGeometries()[1].getCoordinates()[1].getX());
		Assert.assertEquals(8.0, geometry.getGeometries()[1].getCoordinates()[1].getY());
		Assert.assertEquals(SRID, geometry.getSrid());
		Assert.assertEquals(SRID, geometry.getGeometries()[0].getSrid());
		Assert.assertEquals(SRID, geometry.getGeometries()[1].getSrid());
	}

	@Test
	public void formatMultiLineStringCornerCases() {
		try {
			WktService.toGeometry(EWKT_EXT + "MULTILINESTRING (10.0 20.0, 0.0 0.0)");
			Assert.fail();
		} catch (WktException e) {
			// We expect to get here.
		}

		try {
			WktService.toGeometry(EWKT_EXT + "MULTILINESTRING (10.0 20.0, 0.0 0.0))");
			Assert.fail();
		} catch (WktException e) {
			// We expect to get here.
		}

		try {
			WktService.toGeometry(EWKT_EXT + "MULTILINESTRING ((10.0 20.0, 0.0 0.0)))");
			Assert.fail();
		} catch (WktException e) {
			// We expect to get here.
		}

		try {
			WktService.toGeometry(EWKT_EXT + "MULTILINESTRING (10.0, 20.0)");
			Assert.fail();
		} catch (WktException e) {
			// We expect to get here.
		}

		try {
			WktService.toGeometry(EWKT_EXT + "MULTILINESTRING (((10.0 20.0)))");
			Assert.fail();
		} catch (WktException e) {
			// We expect to get here.
		}
	}

	// ------------------------------------------------------------------------
	// Format Polygons:
	// ------------------------------------------------------------------------

	@Test
	public void formatEmptyMultiPolygon() throws WktException {
		Geometry geometry = WktService.toGeometry(EWKT_EXT + "MULTIPOLYGON EMPTY");
		Assert.assertEquals(Geometry.MULTI_POLYGON, geometry.getGeometryType());
		Assert.assertNull(geometry.getCoordinates());
		Assert.assertNull(geometry.getGeometries());
		Assert.assertEquals(SRID, geometry.getSrid());
	}

	@Test
	public void formatMultiPolygon() throws WktException {
		Geometry geometry = WktService.toGeometry(EWKT_EXT + "MULTIPOLYGON (((1.0 2.0, 3.0 4.0, 1.0 2.0)))");
		Assert.assertEquals(Geometry.MULTI_POLYGON, geometry.getGeometryType());
		Assert.assertEquals(1.0, geometry.getGeometries()[0].getGeometries()[0].getCoordinates()[0].getX());
		Assert.assertEquals(2.0, geometry.getGeometries()[0].getGeometries()[0].getCoordinates()[0].getY());
		Assert.assertEquals(3.0, geometry.getGeometries()[0].getGeometries()[0].getCoordinates()[1].getX());
		Assert.assertEquals(4.0, geometry.getGeometries()[0].getGeometries()[0].getCoordinates()[1].getY());
		Assert.assertEquals(1.0, geometry.getGeometries()[0].getGeometries()[0].getCoordinates()[2].getX());
		Assert.assertEquals(2.0, geometry.getGeometries()[0].getGeometries()[0].getCoordinates()[2].getY());
		Assert.assertEquals(SRID, geometry.getSrid());
		Assert.assertEquals(SRID, geometry.getGeometries()[0].getSrid());
		Assert.assertEquals(SRID, geometry.getGeometries()[0].getGeometries()[0].getSrid());

		geometry = WktService.toGeometry(EWKT_EXT
				+ "MULTIPOLYGON (((1.0 2.0, 3.0 4.0, 1.0 2.0),(5.0 6.0, 7.0 8.0, 5.0 6.0)))");
		Assert.assertEquals(Geometry.MULTI_POLYGON, geometry.getGeometryType());
		Assert.assertEquals(1.0, geometry.getGeometries()[0].getGeometries()[0].getCoordinates()[0].getX());
		Assert.assertEquals(2.0, geometry.getGeometries()[0].getGeometries()[0].getCoordinates()[0].getY());
		Assert.assertEquals(3.0, geometry.getGeometries()[0].getGeometries()[0].getCoordinates()[1].getX());
		Assert.assertEquals(4.0, geometry.getGeometries()[0].getGeometries()[0].getCoordinates()[1].getY());
		Assert.assertEquals(1.0, geometry.getGeometries()[0].getGeometries()[0].getCoordinates()[2].getX());
		Assert.assertEquals(2.0, geometry.getGeometries()[0].getGeometries()[0].getCoordinates()[2].getY());
		Assert.assertEquals(5.0, geometry.getGeometries()[0].getGeometries()[1].getCoordinates()[0].getX());
		Assert.assertEquals(6.0, geometry.getGeometries()[0].getGeometries()[1].getCoordinates()[0].getY());
		Assert.assertEquals(7.0, geometry.getGeometries()[0].getGeometries()[1].getCoordinates()[1].getX());
		Assert.assertEquals(8.0, geometry.getGeometries()[0].getGeometries()[1].getCoordinates()[1].getY());
		Assert.assertEquals(5.0, geometry.getGeometries()[0].getGeometries()[1].getCoordinates()[2].getX());
		Assert.assertEquals(6.0, geometry.getGeometries()[0].getGeometries()[1].getCoordinates()[2].getY());
		Assert.assertEquals(SRID, geometry.getSrid());
		Assert.assertEquals(SRID, geometry.getGeometries()[0].getSrid());
		Assert.assertEquals(SRID, geometry.getGeometries()[0].getGeometries()[0].getSrid());
		Assert.assertEquals(SRID, geometry.getGeometries()[0].getGeometries()[1].getSrid());
	}
}