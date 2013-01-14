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

package org.geomajas.plugin.editing.client.service;

import junit.framework.Assert;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.junit.Test;

/**
 * Test cases for the parsing and formatting options of the {@link GeometryIndexService}.
 * 
 * @author Pieter De Graef
 */
public class GeometryIndexServiceGeneralTest {

	private GeometryIndexService service = new GeometryIndexService();

	private Geometry point = new Geometry(Geometry.POINT, 0, 0);

	private Geometry lineString = new Geometry(Geometry.LINE_STRING, 0, 0);

	private Geometry linearRing = new Geometry(Geometry.LINEAR_RING, 0, 0);

	private Geometry polygon = new Geometry(Geometry.POLYGON, 0, 0);

	private Geometry multiPoint = new Geometry(Geometry.MULTI_POINT, 0, 0);

	private Geometry multiLineString = new Geometry(Geometry.MULTI_LINE_STRING, 0, 0);

	private Geometry multiPolygon = new Geometry(Geometry.MULTI_POLYGON, 0, 0);

	// ------------------------------------------------------------------------
	// Constructor: initialize geometries.
	// ------------------------------------------------------------------------

	public GeometryIndexServiceGeneralTest() {
		point.setCoordinates(new Coordinate[] { new Coordinate(1, 1) });
		lineString
				.setCoordinates(new Coordinate[] { new Coordinate(1, 1), new Coordinate(2, 2), new Coordinate(3, 3) });
		linearRing.setCoordinates(new Coordinate[] { new Coordinate(1, 1), new Coordinate(2, 2), new Coordinate(3, 3),
				new Coordinate(1, 1) });

		Geometry shell = new Geometry(Geometry.LINEAR_RING, 0, 0);
		shell.setCoordinates(new Coordinate[] { new Coordinate(0, 0), new Coordinate(10, 0), new Coordinate(10, 10),
				new Coordinate(0, 10), new Coordinate(0, 0) });
		Geometry hole = new Geometry(Geometry.LINEAR_RING, 0, 0);
		hole.setCoordinates(new Coordinate[] { new Coordinate(4, 4), new Coordinate(6, 4), new Coordinate(6, 6),
				new Coordinate(4, 6), new Coordinate(4, 4) });
		polygon.setGeometries(new Geometry[] { shell, hole });

		Geometry point2 = new Geometry(Geometry.POINT, 0, 0);
		point2.setCoordinates(new Coordinate[] { new Coordinate(2, 2) });
		multiPoint.setGeometries(new Geometry[] { point, point2 });

		Geometry lineString2 = new Geometry(Geometry.LINE_STRING, 0, 0);
		lineString2.setCoordinates(new Coordinate[] { new Coordinate(4, 4), new Coordinate(5, 5), new Coordinate(6, 6),
				new Coordinate(7, 7) });
		multiLineString.setGeometries(new Geometry[] { lineString, lineString2 });

		Geometry shell2 = new Geometry(Geometry.LINEAR_RING, 0, 0);
		shell.setCoordinates(new Coordinate[] { new Coordinate(0, 0), new Coordinate(10, 0), new Coordinate(10, 10),
				new Coordinate(0, 10), new Coordinate(0, 0) });
		Geometry hole2 = new Geometry(Geometry.LINEAR_RING, 0, 0);
		hole2.setCoordinates(new Coordinate[] { new Coordinate(3, 3), new Coordinate(7, 3), new Coordinate(7, 7),
				new Coordinate(3, 7), new Coordinate(3, 3) });
		Geometry polygon2 = new Geometry(Geometry.POLYGON, 0, 0);
		polygon2.setGeometries(new Geometry[] { shell2, hole2 });
		multiPolygon.setGeometries(new Geometry[] { polygon, polygon2 });
	}

	// ------------------------------------------------------------------------
	// Test the GeometryIndexService.
	// ------------------------------------------------------------------------

	@Test
	public void createTest() {
		GeometryIndex index = service.create(GeometryIndexType.TYPE_GEOMETRY, 1);
		Assert.assertFalse(index.hasChild());
		Assert.assertNull(index.getChild());
		Assert.assertEquals(GeometryIndexType.TYPE_GEOMETRY, index.getType());
		Assert.assertEquals(1, index.getValue());

		index = service.create(GeometryIndexType.TYPE_VERTEX, 1, 2);
		Assert.assertTrue(index.hasChild());
		Assert.assertNotNull(index.getChild());
		Assert.assertEquals(GeometryIndexType.TYPE_GEOMETRY, index.getType());
		Assert.assertEquals(1, index.getValue());

		Assert.assertFalse(index.getChild().hasChild());
		Assert.assertNull(index.getChild().getChild());
		Assert.assertEquals(GeometryIndexType.TYPE_VERTEX, index.getChild().getType());
		Assert.assertEquals(2, index.getChild().getValue());

		try {
			service.create(GeometryIndexType.TYPE_VERTEX, null);
			Assert.fail();
		} catch (NullPointerException npe) {
			// I foresaw this....
		}
	}

	@Test
	public void addChildrenTest() {
		GeometryIndex index = service.create(GeometryIndexType.TYPE_GEOMETRY, 1);
		Assert.assertFalse(index.hasChild());

		index = service.addChildren(index, GeometryIndexType.TYPE_EDGE, 2, 3);
		Assert.assertTrue(index.hasChild());
		Assert.assertTrue(index.getChild().hasChild());
		Assert.assertFalse(index.getChild().getChild().hasChild());

		index = service.create(GeometryIndexType.TYPE_GEOMETRY, 1, 2);
		index = service.addChildren(index, GeometryIndexType.TYPE_EDGE, 3);
		Assert.assertTrue(index.hasChild());
		Assert.assertTrue(index.getChild().hasChild());
		Assert.assertFalse(index.getChild().getChild().hasChild());

		try {
			service.addChildren(index, GeometryIndexType.TYPE_EDGE, 4); // Can't add edges to edges...
			Assert.fail();
		} catch (IllegalArgumentException e) {
			// As expected...
		}
	}

	@Test
	public void isVertexTest() {
		Assert.assertFalse(service.isVertex(service.create(GeometryIndexType.TYPE_GEOMETRY, 0)));
		Assert.assertFalse(service.isVertex(service.create(GeometryIndexType.TYPE_EDGE, 0)));
		Assert.assertTrue(service.isVertex(service.create(GeometryIndexType.TYPE_VERTEX, 0)));

		Assert.assertFalse(service.isVertex(service.create(GeometryIndexType.TYPE_GEOMETRY, 0, 1)));
		Assert.assertFalse(service.isVertex(service.create(GeometryIndexType.TYPE_EDGE, 0, 1)));
		Assert.assertTrue(service.isVertex(service.create(GeometryIndexType.TYPE_VERTEX, 0, 1)));
	}

	@Test
	public void isEdgeTest() {
		Assert.assertFalse(service.isEdge(service.create(GeometryIndexType.TYPE_GEOMETRY, 0)));
		Assert.assertTrue(service.isEdge(service.create(GeometryIndexType.TYPE_EDGE, 0)));
		Assert.assertFalse(service.isEdge(service.create(GeometryIndexType.TYPE_VERTEX, 0)));

		Assert.assertFalse(service.isEdge(service.create(GeometryIndexType.TYPE_GEOMETRY, 0, 1)));
		Assert.assertTrue(service.isEdge(service.create(GeometryIndexType.TYPE_EDGE, 0, 1)));
		Assert.assertFalse(service.isEdge(service.create(GeometryIndexType.TYPE_VERTEX, 0, 1)));
	}

	@Test
	public void isGeometryTest() {
		Assert.assertTrue(service.isGeometry(service.create(GeometryIndexType.TYPE_GEOMETRY, 0)));
		Assert.assertFalse(service.isGeometry(service.create(GeometryIndexType.TYPE_EDGE, 0)));
		Assert.assertFalse(service.isGeometry(service.create(GeometryIndexType.TYPE_VERTEX, 0)));

		Assert.assertTrue(service.isGeometry(service.create(GeometryIndexType.TYPE_GEOMETRY, 0, 1)));
		Assert.assertFalse(service.isGeometry(service.create(GeometryIndexType.TYPE_EDGE, 0, 1)));
		Assert.assertFalse(service.isGeometry(service.create(GeometryIndexType.TYPE_VERTEX, 0, 1)));
	}

	@Test
	public void getTypeTest() {
		GeometryIndexType type = service.getType(service.create(GeometryIndexType.TYPE_GEOMETRY, 0));
		Assert.assertEquals(GeometryIndexType.TYPE_GEOMETRY, type);
		type = service.getType(service.create(GeometryIndexType.TYPE_EDGE, 0));
		Assert.assertEquals(GeometryIndexType.TYPE_EDGE, type);
		type = service.getType(service.create(GeometryIndexType.TYPE_VERTEX, 0));
		Assert.assertEquals(GeometryIndexType.TYPE_VERTEX, type);

		type = service.getType(service.create(GeometryIndexType.TYPE_GEOMETRY, 0, 1));
		Assert.assertEquals(GeometryIndexType.TYPE_GEOMETRY, type);
		type = service.getType(service.create(GeometryIndexType.TYPE_EDGE, 0, 1));
		Assert.assertEquals(GeometryIndexType.TYPE_EDGE, type);
		type = service.getType(service.create(GeometryIndexType.TYPE_VERTEX, 0, 1));
		Assert.assertEquals(GeometryIndexType.TYPE_VERTEX, type);
	}

	@Test
	public void getGeometryTypeTest() throws GeometryIndexNotFoundException {
		String type = service.getGeometryType(multiPolygon, null);
		Assert.assertEquals(Geometry.MULTI_POLYGON, type);
		type = service.getGeometryType(multiPolygon, service.create(GeometryIndexType.TYPE_GEOMETRY, 0));
		Assert.assertEquals(Geometry.POLYGON, type);
		type = service.getGeometryType(multiPolygon, service.create(GeometryIndexType.TYPE_GEOMETRY, 0, 0));
		Assert.assertEquals(Geometry.LINEAR_RING, type);
		type = service.getGeometryType(multiPolygon, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0, 0));
		Assert.assertEquals(Geometry.LINEAR_RING, type);

		try {
			service.getVertex(multiPolygon, service.create(GeometryIndexType.TYPE_GEOMETRY, 0, 0, 0));
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect to get here...
		}
		try {
			service.getVertex(multiPolygon, service.create(GeometryIndexType.TYPE_GEOMETRY, 10));
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect to get here...
		}

		type = service.getGeometryType(multiLineString, service.create(GeometryIndexType.TYPE_GEOMETRY, 0));
		Assert.assertEquals(Geometry.LINE_STRING, type);
		type = service.getGeometryType(multiLineString, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0));
		Assert.assertEquals(Geometry.LINE_STRING, type);
		type = service.getGeometryType(multiPoint, service.create(GeometryIndexType.TYPE_GEOMETRY, 0));
		Assert.assertEquals(Geometry.POINT, type);
		type = service.getGeometryType(multiPoint, service.create(GeometryIndexType.TYPE_EDGE, 0, 0));
		Assert.assertEquals(Geometry.POINT, type);
		type = service.getGeometryType(polygon, service.create(GeometryIndexType.TYPE_GEOMETRY, 0));
		Assert.assertEquals(Geometry.LINEAR_RING, type);
		type = service.getGeometryType(polygon, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0));
		Assert.assertEquals(Geometry.LINEAR_RING, type);
		try {
			service.getVertex(multiLineString, service.create(GeometryIndexType.TYPE_GEOMETRY, 0, 10));
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect to get here...
		}
	}

	@Test
	public void getVertexTest() throws GeometryIndexNotFoundException {
		Coordinate c = service.getVertex(multiPolygon, service.create(GeometryIndexType.TYPE_VERTEX, 0, 1, 2));
		Assert.assertEquals(6.0, c.getX());
		Assert.assertEquals(6.0, c.getY());

		try {
			service.getVertex(multiPolygon, service.create(GeometryIndexType.TYPE_EDGE, 0, 1, 2));
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect to get here...
		}
		try {
			service.getVertex(multiPolygon, service.create(GeometryIndexType.TYPE_GEOMETRY, 0, 1, 2));
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect to get here...
		}
		try {
			service.getVertex(multiPolygon, service.create(GeometryIndexType.TYPE_VERTEX, 0, 1));
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect to get here...
		}
		try {
			service.getVertex(multiPolygon, service.create(GeometryIndexType.TYPE_VERTEX, 3, 0, 1));
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect to get here...
		}
		try {
			service.getVertex(multiPolygon, service.create(GeometryIndexType.TYPE_VERTEX, 0, 1, 2, 0));
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect to get here...
		}
	}

	@Test
	public void getEdgeTest() throws GeometryIndexNotFoundException {
		Coordinate[] c = service.getEdge(multiPolygon, service.create(GeometryIndexType.TYPE_EDGE, 0, 1, 2));
		Assert.assertEquals(6.0, c[0].getX());
		Assert.assertEquals(6.0, c[0].getY());

		try {
			service.getEdge(multiPolygon, service.create(GeometryIndexType.TYPE_VERTEX, 0, 1, 2));
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect to get here...
		}
		try {
			service.getEdge(multiPolygon, service.create(GeometryIndexType.TYPE_GEOMETRY, 0, 1, 2));
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect to get here...
		}
		try {
			service.getEdge(multiPolygon, service.create(GeometryIndexType.TYPE_EDGE, 0, 1));
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect to get here...
		}
		try {
			service.getEdge(multiPolygon, service.create(GeometryIndexType.TYPE_EDGE, 3, 0, 1));
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect to get here...
		}
		try {
			service.getEdge(multiPolygon, service.create(GeometryIndexType.TYPE_EDGE, 0, 1, 2, 0));
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect to get here...
		}
	}

	@Test
	public void getGeometryTest() throws GeometryIndexNotFoundException {
		Geometry geom = service.getGeometry(multiPolygon, service.create(GeometryIndexType.TYPE_GEOMETRY, 0));
		Assert.assertNull(geom.getCoordinates());
		Assert.assertEquals(6.0, geom.getGeometries()[1].getCoordinates()[2].getX());
		Assert.assertEquals(6.0, geom.getGeometries()[1].getCoordinates()[2].getY());

		geom = service.getGeometry(multiPolygon, service.create(GeometryIndexType.TYPE_GEOMETRY, 0, 1));
		Assert.assertEquals(6.0, geom.getCoordinates()[2].getX());
		Assert.assertEquals(6.0, geom.getCoordinates()[2].getY());

		try {
			service.getGeometry(multiPolygon, service.create(GeometryIndexType.TYPE_VERTEX, 0, 1, 2));
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect to get here...
		}
		try {
			service.getGeometry(multiPolygon, service.create(GeometryIndexType.TYPE_EDGE, 0, 1, 2));
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect to get here...
		}
		try {
			service.getGeometry(multiPolygon, service.create(GeometryIndexType.TYPE_GEOMETRY, 0, 10));
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect to get here...
		}
		try {
			service.getGeometry(multiPolygon, service.create(GeometryIndexType.TYPE_GEOMETRY, 0, 1, 2, 0));
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect to get here...
		}
	}

	@Test
	public void isChildOfTest() {
		GeometryIndex parentIndex = service.create(GeometryIndexType.TYPE_GEOMETRY, 0);
		GeometryIndex childIndex = service.create(GeometryIndexType.TYPE_VERTEX, 0, 1);
		Assert.assertTrue(service.isChildOf(parentIndex, childIndex));

		parentIndex = service.create(GeometryIndexType.TYPE_GEOMETRY, 0, 1);
		childIndex = service.create(GeometryIndexType.TYPE_VERTEX, 0, 1, 2);
		Assert.assertTrue(service.isChildOf(parentIndex, childIndex));
		Assert.assertFalse(service.isChildOf(childIndex, parentIndex));

		childIndex = service.create(GeometryIndexType.TYPE_VERTEX, 0, 1, 2, 3);
		Assert.assertTrue(service.isChildOf(parentIndex, childIndex));
		Assert.assertFalse(service.isChildOf(childIndex, parentIndex));

		parentIndex = service.create(GeometryIndexType.TYPE_GEOMETRY, 0, 1);
		childIndex = service.create(GeometryIndexType.TYPE_VERTEX, 0, 1);
		Assert.assertFalse(service.isChildOf(parentIndex, childIndex));

		parentIndex = service.create(GeometryIndexType.TYPE_GEOMETRY, 0, 1);
		childIndex = service.create(GeometryIndexType.TYPE_VERTEX, 0, 2);
		Assert.assertFalse(service.isChildOf(parentIndex, childIndex));
	}

	@Test
	public void getValueTest() {
		Assert.assertEquals(1, service.getValue(service.create(GeometryIndexType.TYPE_GEOMETRY, 1)));
		Assert.assertEquals(1, service.getValue(service.create(GeometryIndexType.TYPE_EDGE, 1)));
		Assert.assertEquals(1, service.getValue(service.create(GeometryIndexType.TYPE_VERTEX, 1)));
		Assert.assertEquals(2, service.getValue(service.create(GeometryIndexType.TYPE_GEOMETRY, 0, 1, 2)));
		Assert.assertEquals(2, service.getValue(service.create(GeometryIndexType.TYPE_EDGE, 0, 1, 2)));
		Assert.assertEquals(2, service.getValue(service.create(GeometryIndexType.TYPE_VERTEX, 0, 1, 2)));
	}

	@Test
	public void getSiblingCountTest() {
		GeometryIndex index = service.create(GeometryIndexType.TYPE_VERTEX, 0);
		Assert.assertEquals(lineString.getCoordinates().length, service.getSiblingCount(lineString, index));
		index = service.create(GeometryIndexType.TYPE_EDGE, 0);
		Assert.assertEquals(lineString.getCoordinates().length - 1, service.getSiblingCount(lineString, index));
		index = service.create(GeometryIndexType.TYPE_GEOMETRY, 0);
		Assert.assertEquals(0, service.getSiblingCount(lineString, index));

		index = service.create(GeometryIndexType.TYPE_EDGE, 0);
		Assert.assertEquals(linearRing.getCoordinates().length, service.getSiblingCount(linearRing, index));

		index = service.create(GeometryIndexType.TYPE_VERTEX, 0);
		Assert.assertEquals(point.getCoordinates().length, service.getSiblingCount(point, index));
	}

	@Test
	public void getSiblingVerticesTest() throws GeometryIndexNotFoundException {
		Coordinate[] c = service.getSiblingVertices(lineString, service.create(GeometryIndexType.TYPE_VERTEX, 0));
		Assert.assertEquals(1.0, c[0].getX());
		Assert.assertEquals(1.0, c[0].getY());
		c = service.getSiblingVertices(lineString, service.create(GeometryIndexType.TYPE_EDGE, 0));
		Assert.assertEquals(1.0, c[0].getX());
		Assert.assertEquals(1.0, c[0].getY());
		try {
			service.getSiblingVertices(lineString, service.create(GeometryIndexType.TYPE_GEOMETRY, 0));
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect to get here...
		}

		c = service.getSiblingVertices(polygon, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0));
		Assert.assertEquals(10.0, c[2].getX());
		Assert.assertEquals(10.0, c[2].getY());
		try {
			service.getSiblingVertices(polygon, service.create(GeometryIndexType.TYPE_GEOMETRY, 0));
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect to get here...
		}
		try {
			service.getSiblingVertices(polygon, service.create(GeometryIndexType.TYPE_GEOMETRY, 0, 0));
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect to get here...
		}
	}
}