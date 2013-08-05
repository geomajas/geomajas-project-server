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

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.smartgwt.client.spatial.geometry.GeometryFactory;
import org.geomajas.smartgwt.client.spatial.geometry.LineString;
import org.geomajas.smartgwt.client.spatial.geometry.LinearRing;
import org.geomajas.smartgwt.client.spatial.geometry.MultiLineString;
import org.geomajas.smartgwt.client.spatial.geometry.MultiPoint;
import org.geomajas.smartgwt.client.spatial.geometry.MultiPolygon;
import org.geomajas.smartgwt.client.spatial.geometry.Point;
import org.geomajas.smartgwt.client.spatial.geometry.Polygon;
import org.geomajas.smartgwt.client.util.GeometryConverter;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for converting geometries between DTOs and GWT client versions.
 * 
 * @author Pieter De Graef
 */
public class GeometryConverterTest {

	private static final double DELTA = 0.0001;

	private GeometryFactory factory = new GeometryFactory(0, 0);

	@Test
	public void testPointToGwt() {
		// Test empty point:
		Geometry geometry = new Geometry(Geometry.POINT, 0, 0);
		org.geomajas.smartgwt.client.spatial.geometry.Geometry result = GeometryConverter.toGwt(geometry);
		Assert.assertTrue(result instanceof Point);
		Assert.assertEquals(0, result.getNumPoints());

		// Test normal point:
		geometry.setCoordinates(new Coordinate[] { new Coordinate(1, 1) });
		result = GeometryConverter.toGwt(geometry);
		Assert.assertEquals(1, result.getNumPoints());
		Assert.assertEquals(1, result.getCoordinate().getX(), DELTA);
	}

	@Test
	public void testPointToDto() {
		// Test empty point:
		Point point = factory.createPoint(null);
		Geometry result = GeometryConverter.toDto(point);
		Assert.assertNull(result.getCoordinates());
		Assert.assertNull(result.getGeometries());

		// Test normal point:
		point = factory.createPoint(new Coordinate(1, 1));
		result = GeometryConverter.toDto(point);
		Assert.assertNotNull(result.getCoordinates());
		Assert.assertEquals(1, result.getCoordinates()[0].getX(), DELTA);
		Assert.assertNull(result.getGeometries());
	}

	@Test
	public void testLineStringToGwt() {
		// Test empty line:
		Geometry geometry = new Geometry(Geometry.LINE_STRING, 0, 0);
		org.geomajas.smartgwt.client.spatial.geometry.Geometry result = GeometryConverter.toGwt(geometry);
		Assert.assertTrue(result instanceof LineString);
		Assert.assertEquals(0, result.getNumPoints());

		// Test normal line:
		geometry.setCoordinates(new Coordinate[] { new Coordinate(1, 1), new Coordinate(2, 2) });
		result = GeometryConverter.toGwt(geometry);
		Assert.assertEquals(2, result.getNumPoints());
		Assert.assertEquals(1, result.getCoordinate().getX(), DELTA);
		Assert.assertEquals(2, result.getCoordinates()[1].getX(), DELTA);
	}

	@Test
	public void testLineStringToDto() {
		// Test empty line:
		LineString line = factory.createLineString(null);
		Geometry result = GeometryConverter.toDto(line);
		Assert.assertNull(result.getCoordinates());
		Assert.assertNull(result.getGeometries());

		// Test normal line:
		line = factory.createLineString(new Coordinate[] { new Coordinate(1, 1), new Coordinate(2, 2) });
		result = GeometryConverter.toDto(line);
		Assert.assertNotNull(result.getCoordinates());
		Assert.assertEquals(1, result.getCoordinates()[0].getX(), DELTA);
		Assert.assertEquals(2, result.getCoordinates()[1].getX(), DELTA);
		Assert.assertNull(result.getGeometries());
	}

	@Test
	public void testLinearRingToGwt() {
		// Test empty line:
		Geometry geometry = new Geometry(Geometry.LINEAR_RING, 0, 0);
		org.geomajas.smartgwt.client.spatial.geometry.Geometry result = GeometryConverter.toGwt(geometry);
		Assert.assertTrue(result instanceof LineString);
		Assert.assertEquals(0, result.getNumPoints());

		// Test normal line:
		geometry.setCoordinates(new Coordinate[] { new Coordinate(1, 1), new Coordinate(2, 2) });
		result = GeometryConverter.toGwt(geometry);
		Assert.assertEquals(3, result.getNumPoints());
		Assert.assertEquals(1, result.getCoordinate().getX(), DELTA);
		Assert.assertEquals(2, result.getCoordinates()[1].getX(), DELTA);
	}

	@Test
	public void testLinearRingToDto() {
		// Test empty line:
		LinearRing line = factory.createLinearRing(new Coordinate[] {});
		Geometry result = GeometryConverter.toDto(line);
		Assert.assertNull(result.getCoordinates());
		Assert.assertNull(result.getGeometries());

		// Test normal line:
		line = factory.createLinearRing(new Coordinate[] { new Coordinate(1, 1), new Coordinate(2, 2) });
		result = GeometryConverter.toDto(line);
		Assert.assertNotNull(result.getCoordinates());
		Assert.assertEquals(3, result.getCoordinates().length);
		Assert.assertEquals(1, result.getCoordinates()[0].getX(), DELTA);
		Assert.assertEquals(2, result.getCoordinates()[1].getX(), DELTA);
		Assert.assertNull(result.getGeometries());
	}

	@Test
	public void testPolygonToGwt() {
		// Test empty polygon:
		Geometry geometry = new Geometry(Geometry.POLYGON, 0, 0);
		org.geomajas.smartgwt.client.spatial.geometry.Geometry result = GeometryConverter.toGwt(geometry);
		Assert.assertTrue(result instanceof Polygon);
		Assert.assertEquals(0, result.getNumPoints());

		// Test normal polygon:
		Geometry shell = new Geometry(Geometry.LINEAR_RING, 0, 0);
		shell.setCoordinates(new Coordinate[] { new Coordinate(1, 1), new Coordinate(2, 2) });
		Geometry hole = new Geometry(Geometry.LINEAR_RING, 0, 0);
		hole.setCoordinates(new Coordinate[] { new Coordinate(3, 3), new Coordinate(4, 4) });
		geometry.setGeometries(new Geometry[] { shell, hole });
		result = GeometryConverter.toGwt(geometry);
		Assert.assertTrue(result instanceof Polygon);
		Assert.assertEquals(6, result.getNumPoints());
		Assert.assertEquals(1, result.getCoordinate().getX(), DELTA);
		Assert.assertEquals(3, ((Polygon) result).getInteriorRingN(0).getCoordinate().getX(), DELTA);
	}

	@Test
	public void testPolygonToDto() {
		// Test empty polygon:
		Polygon polygon = factory.createPolygon(null, null);
		Geometry result = GeometryConverter.toDto(polygon);
		Assert.assertNull(result.getCoordinates());
		Assert.assertNull(result.getGeometries());

		// Test normal polygon:
		LinearRing shell = factory.createLinearRing(new Coordinate[] { new Coordinate(1, 1), new Coordinate(2, 2) });
		LinearRing hole = factory.createLinearRing(new Coordinate[] { new Coordinate(3, 3), new Coordinate(4, 4) });
		polygon = factory.createPolygon(shell, new LinearRing[] { hole });
		result = GeometryConverter.toDto(polygon);
		Assert.assertEquals(1, result.getGeometries()[0].getCoordinates()[0].getX(), DELTA);
		Assert.assertEquals(4, result.getGeometries()[1].getCoordinates()[1].getX(), DELTA);
		Assert.assertNull(result.getCoordinates());
	}

	@Test
	public void testMultiPointToGwt() {
		// Test empty point:
		Geometry geometry = new Geometry(Geometry.MULTI_POINT, 0, 0);
		org.geomajas.smartgwt.client.spatial.geometry.Geometry result = GeometryConverter.toGwt(geometry);
		Assert.assertTrue(result instanceof MultiPoint);
		Assert.assertEquals(0, result.getNumPoints());
		Assert.assertTrue(result.isEmpty());

		// Test normal point:
		Geometry point = new Geometry(Geometry.POINT, 0, 0);
		point.setCoordinates(new Coordinate[] { new Coordinate(1, 1) });
		geometry.setGeometries(new Geometry[] { point });
		result = GeometryConverter.toGwt(geometry);
		Assert.assertEquals(1, result.getNumPoints());
		Assert.assertEquals(1, result.getCoordinate().getX(), DELTA);
	}

	@Test
	public void testMultiPointToDto() {
		// Test empty point:
		MultiPoint multiPoint = factory.createMultiPoint(null);
		Geometry result = GeometryConverter.toDto(multiPoint);
		Assert.assertNull(result.getCoordinates());
		Assert.assertNull(result.getGeometries());

		// Test normal point:
		Point point = factory.createPoint(new Coordinate(1, 1));
		multiPoint = factory.createMultiPoint(new Point[] { point });
		result = GeometryConverter.toDto(multiPoint);
		Assert.assertNull(result.getCoordinates());
		Assert.assertEquals(1, result.getGeometries()[0].getCoordinates()[0].getX(), DELTA);
	}

	@Test
	public void testMultiLineStringToGwt() {
		// Test empty MultiLineString:
		Geometry geometry = new Geometry(Geometry.MULTI_LINE_STRING, 0, 0);
		org.geomajas.smartgwt.client.spatial.geometry.Geometry result = GeometryConverter.toGwt(geometry);
		Assert.assertTrue(result instanceof MultiLineString);
		Assert.assertEquals(0, result.getNumPoints());
		Assert.assertTrue(result.isEmpty());

		// Test normal MultiLineString:
		Geometry line = new Geometry(Geometry.LINE_STRING, 0, 0);
		line.setCoordinates(new Coordinate[] { new Coordinate(1, 1), new Coordinate(2, 2) });
		geometry.setGeometries(new Geometry[] { line });
		result = GeometryConverter.toGwt(geometry);
		Assert.assertEquals(2, result.getNumPoints());
		Assert.assertEquals(1, result.getCoordinate().getX(), DELTA);
	}

	@Test
	public void testMultiLineStringToDto() {
		// Test empty point:
		MultiLineString multiLineString = factory.createMultiLineString(null);
		Geometry result = GeometryConverter.toDto(multiLineString);
		Assert.assertNull(result.getCoordinates());
		Assert.assertNull(result.getGeometries());

		// Test normal point:
		LineString line = factory.createLineString(new Coordinate[] { new Coordinate(1, 1), new Coordinate(2, 2) });
		multiLineString = factory.createMultiLineString(new LineString[] { line });
		result = GeometryConverter.toDto(multiLineString);
		Assert.assertNull(result.getCoordinates());
		Assert.assertEquals(2, result.getGeometries()[0].getCoordinates()[1].getX(), DELTA);
	}

	@Test
	public void testMultiPolygonToGwt() {
		// Test empty MultiPolygon:
		Geometry geometry = new Geometry(Geometry.MULTI_POLYGON, 0, 0);
		org.geomajas.smartgwt.client.spatial.geometry.Geometry result = GeometryConverter.toGwt(geometry);
		Assert.assertTrue(result instanceof MultiPolygon);
		Assert.assertTrue(result.isEmpty());

		// Test normal MultiPolygon:
		Geometry shell = new Geometry(Geometry.LINEAR_RING, 0, 0);
		shell.setCoordinates(new Coordinate[] { new Coordinate(1, 1), new Coordinate(2, 2) });
		Geometry hole = new Geometry(Geometry.LINEAR_RING, 0, 0);
		hole.setCoordinates(new Coordinate[] { new Coordinate(3, 3), new Coordinate(4, 4) });
		Geometry polygon = new Geometry(Geometry.POLYGON, 0, 0);
		polygon.setGeometries(new Geometry[] { shell, hole });
		geometry.setGeometries(new Geometry[] { polygon });
		result = GeometryConverter.toGwt(geometry);
		Assert.assertTrue(result instanceof MultiPolygon);
		Assert.assertEquals(6, result.getNumPoints());
		Assert.assertEquals(1, result.getCoordinate().getX(), DELTA);
		Assert.assertTrue(result.getGeometryN(0) instanceof Polygon);
		Polygon temp = (Polygon) result.getGeometryN(0);
		Assert.assertEquals(4, temp.getInteriorRingN(0).getCoordinateN(1).getX(), DELTA);
	}

	@Test
	public void testMultiPolygonToDto() {
		// Test empty MultiPolygon:
		MultiPolygon multiPolygon = factory.createMultiPolygon(null);
		Geometry result = GeometryConverter.toDto(multiPolygon);
		Assert.assertNull(result.getCoordinates());
		Assert.assertNull(result.getGeometries());

		// Test normal polygon:
		LinearRing shell = factory.createLinearRing(new Coordinate[] { new Coordinate(1, 1), new Coordinate(2, 2) });
		LinearRing hole = factory.createLinearRing(new Coordinate[] { new Coordinate(3, 3), new Coordinate(4, 4) });
		Polygon polygon = factory.createPolygon(shell, new LinearRing[] { hole });
		multiPolygon = factory.createMultiPolygon(new Polygon[] { polygon });
		result = GeometryConverter.toDto(multiPolygon);
		Assert.assertEquals(1, result.getGeometries()[0].getGeometries()[0].getCoordinates()[0].getX(), DELTA);
		Assert.assertEquals(4, result.getGeometries()[0].getGeometries()[1].getCoordinates()[1].getX(), DELTA);
		Assert.assertNull(result.getCoordinates());
	}
}