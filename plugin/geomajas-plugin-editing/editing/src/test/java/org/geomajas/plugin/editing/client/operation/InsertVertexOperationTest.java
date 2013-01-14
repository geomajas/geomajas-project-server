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

package org.geomajas.plugin.editing.client.operation;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.service.GeometryIndexService;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for inserting a vertex within any type of geometry.
 * 
 * @author Pieter De Graef
 */
public class InsertVertexOperationTest {

	private static final double DELTA = 0.0001;

	private static final double NEW_VALUE = 342;

	private GeometryIndexService service = new GeometryIndexService();

	private Coordinate target = new Coordinate(NEW_VALUE, NEW_VALUE);

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

	public InsertVertexOperationTest() {
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
		multiPoint.setGeometries(new Geometry[] { point, point2, new Geometry(Geometry.POINT, 0, 0) });

		Geometry lineString2 = new Geometry(Geometry.LINE_STRING, 0, 0);
		lineString2.setCoordinates(new Coordinate[] { new Coordinate(4, 4), new Coordinate(5, 5), new Coordinate(6, 6),
				new Coordinate(7, 7) });
		multiLineString.setGeometries(new Geometry[] { lineString, lineString2 });

		Geometry shell2 = new Geometry(Geometry.LINEAR_RING, 0, 0);
		shell2.setCoordinates(new Coordinate[] { new Coordinate(0, 0), new Coordinate(10, 0), new Coordinate(10, 10),
				new Coordinate(0, 10), new Coordinate(0, 0) });
		Geometry hole2 = new Geometry(Geometry.LINEAR_RING, 0, 0);
		hole2.setCoordinates(new Coordinate[] { new Coordinate(3, 3), new Coordinate(7, 3), new Coordinate(7, 7),
				new Coordinate(3, 7), new Coordinate(3, 3) });
		Geometry polygon2 = new Geometry(Geometry.POLYGON, 0, 0);
		polygon2.setGeometries(new Geometry[] { shell2, hole2 });
		multiPolygon.setGeometries(new Geometry[] { polygon, polygon2 });
	}

	// ------------------------------------------------------------------------
	// Test the GeometryIndexMoveVertexOperation for different geometry types.
	// ------------------------------------------------------------------------

	@Test
	public void testEmptyPoint() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);
		Geometry point = new Geometry(Geometry.POINT, 0, 0);

		// First a correct index. This should work:
		Geometry result = operation.execute(point, service.create(GeometryIndexType.TYPE_VERTEX, 0));
		Assert.assertEquals(NEW_VALUE, result.getCoordinates()[0].getX(), DELTA);

		// Undo the insert operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertNull(undone.getCoordinates());
	}

	@Test
	public void testPoint() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);

		// Cannot insert any vertices into a point that already has a coordinate:
		try {
			operation.execute(point, service.create(GeometryIndexType.TYPE_VERTEX, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testPointCornerCases() {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);
		Geometry point = new Geometry(Geometry.POINT, 0, 0);

		// Geometry index of wrong type:
		try {
			operation.execute(point, service.create(GeometryIndexType.TYPE_EDGE, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(point, service.create(GeometryIndexType.TYPE_GEOMETRY, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}

		// Non existing geometry index:
		try {
			operation.execute(point, service.create(GeometryIndexType.TYPE_VERTEX, -1));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(point, service.create(GeometryIndexType.TYPE_VERTEX, 1));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(point, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testEmptyLineString() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);
		Geometry lineString = new Geometry(Geometry.LINE_STRING, 0, 0);

		// Insert the first point:
		Geometry result = operation.execute(lineString, service.create(GeometryIndexType.TYPE_VERTEX, 0));
		Assert.assertEquals(NEW_VALUE, result.getCoordinates()[0].getX(), DELTA);

		// Undo the insert operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertNull(undone.getCoordinates());
	}

	@Test
	public void testEmptyLineStringCornerCases() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);
		Geometry lineString = new Geometry(Geometry.LINE_STRING, 0, 0);

		// First we try some faulty indices:
		try {
			operation.execute(lineString, service.create(GeometryIndexType.TYPE_VERTEX, -1));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(lineString, service.create(GeometryIndexType.TYPE_VERTEX, 1));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(lineString, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(lineString, service.create(GeometryIndexType.TYPE_EDGE, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testLineStringVertex0() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);
		int count = lineString.getCoordinates().length;
		double value = lineString.getCoordinates()[0].getX();

		// First a correct index. This should work:
		Geometry result = operation.execute(lineString, service.create(GeometryIndexType.TYPE_VERTEX, 0));
		Assert.assertEquals(NEW_VALUE, result.getCoordinates()[0].getX(), DELTA);
		Assert.assertEquals(count + 1, result.getCoordinates().length);

		// Undo the insert operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(value, undone.getCoordinates()[0].getX(), DELTA);
		Assert.assertEquals(count, undone.getCoordinates().length);
	}

	@Test
	public void testLineStringVertex1() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);
		int count = lineString.getCoordinates().length;
		double value = lineString.getCoordinates()[1].getX();

		// First a correct index. This should work:
		Geometry result = operation.execute(lineString, service.create(GeometryIndexType.TYPE_VERTEX, 1));
		Assert.assertEquals(NEW_VALUE, result.getCoordinates()[1].getX(), DELTA);
		Assert.assertEquals(count + 1, result.getCoordinates().length);

		// Undo the insert operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(value, undone.getCoordinates()[1].getX(), DELTA);
		Assert.assertEquals(count, undone.getCoordinates().length);
	}

	@Test
	public void testLineStringVertexAtEnd() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);
		int count = lineString.getCoordinates().length;

		// Add a vertex at the end:
		Geometry result = operation.execute(lineString, service.create(GeometryIndexType.TYPE_VERTEX, 3));
		Assert.assertEquals(NEW_VALUE, result.getCoordinates()[3].getX(), DELTA);
		Assert.assertEquals(count + 1, result.getCoordinates().length);

		// Undo the insert operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(count, undone.getCoordinates().length);
	}

	@Test
	public void testLineStringEdge0() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);
		int count = lineString.getCoordinates().length;
		double value = lineString.getCoordinates()[1].getX();

		// First a correct index. This should work:
		Geometry result = operation.execute(lineString, service.create(GeometryIndexType.TYPE_EDGE, 0));
		Assert.assertEquals(NEW_VALUE, result.getCoordinates()[1].getX(), DELTA);
		Assert.assertEquals(count + 1, result.getCoordinates().length);

		// Undo the insert operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(value, undone.getCoordinates()[1].getX(), DELTA);
		Assert.assertEquals(count, undone.getCoordinates().length);
	}

	@Test
	public void testLineStringEdge1() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);
		int count = lineString.getCoordinates().length;
		double value = lineString.getCoordinates()[2].getX();

		// First a correct index. This should work:
		Geometry result = operation.execute(lineString, service.create(GeometryIndexType.TYPE_EDGE, 1));
		Assert.assertEquals(NEW_VALUE, result.getCoordinates()[2].getX(), DELTA);
		Assert.assertEquals(count + 1, result.getCoordinates().length);

		// Undo the insert operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(value, undone.getCoordinates()[2].getX(), DELTA);
		Assert.assertEquals(count, undone.getCoordinates().length);
	}

	@Test
	public void testLineStringCornerCases() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);

		// Geometry index of wrong type:
		try {
			operation.execute(lineString, service.create(GeometryIndexType.TYPE_GEOMETRY, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}

		// Non existing geometry index:
		try {
			operation.execute(lineString, service.create(GeometryIndexType.TYPE_VERTEX, -1));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(lineString, service.create(GeometryIndexType.TYPE_VERTEX, 4));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(lineString, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(lineString, service.create(GeometryIndexType.TYPE_EDGE, -1));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(lineString, service.create(GeometryIndexType.TYPE_EDGE, 2));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(lineString, service.create(GeometryIndexType.TYPE_EDGE, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testEmptyLinearRing() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);
		Geometry linearRing = new Geometry(Geometry.LINEAR_RING, 0, 0);

		// Insert the first point:
		Geometry result = operation.execute(linearRing, service.create(GeometryIndexType.TYPE_VERTEX, 0));
		Assert.assertEquals(NEW_VALUE, result.getCoordinates()[0].getX(), DELTA);
		Assert.assertEquals(2, result.getCoordinates().length);

		// Undo the insert operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertNull(undone.getCoordinates());
	}

	@Test
	public void testEmptyLinearRingCornerCases() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);
		Geometry linearRing = new Geometry(Geometry.LINEAR_RING, 0, 0);

		// First we try some faulty indices:
		try {
			operation.execute(linearRing, service.create(GeometryIndexType.TYPE_VERTEX, -1));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(linearRing, service.create(GeometryIndexType.TYPE_VERTEX, 1));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(linearRing, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(linearRing, service.create(GeometryIndexType.TYPE_EDGE, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testLinearRingVertex0() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);
		int count = linearRing.getCoordinates().length;
		double value = linearRing.getCoordinates()[0].getX();

		// First a correct index. This should work:
		Geometry result = operation.execute(linearRing, service.create(GeometryIndexType.TYPE_VERTEX, 0));
		Assert.assertEquals(NEW_VALUE, result.getCoordinates()[0].getX(), DELTA);
		Assert.assertEquals(NEW_VALUE, result.getCoordinates()[count].getX(), DELTA);
		Assert.assertEquals(count + 1, result.getCoordinates().length);

		// Undo the insert operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(value, undone.getCoordinates()[0].getX(), DELTA);
		Assert.assertEquals(value, result.getCoordinates()[count - 1].getX(), DELTA);
		Assert.assertEquals(count, undone.getCoordinates().length);
	}

	@Test
	public void testLinearRingVertex1() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);
		int count = linearRing.getCoordinates().length;
		double value = linearRing.getCoordinates()[1].getX();

		// First a correct index. This should work:
		Geometry result = operation.execute(linearRing, service.create(GeometryIndexType.TYPE_VERTEX, 1));
		Assert.assertEquals(NEW_VALUE, result.getCoordinates()[1].getX(), DELTA);
		Assert.assertEquals(count + 1, result.getCoordinates().length);

		// Undo the insert operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(value, undone.getCoordinates()[1].getX(), DELTA);
		Assert.assertEquals(count, undone.getCoordinates().length);
	}

	@Test
	public void testLinearRingVertexAtEnd() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);
		int count = linearRing.getCoordinates().length;
		double value = linearRing.getCoordinates()[3].getX();

		// First a correct index. This should work:
		Geometry result = operation.execute(linearRing, service.create(GeometryIndexType.TYPE_VERTEX, 3));
		Assert.assertEquals(NEW_VALUE, result.getCoordinates()[3].getX(), DELTA);
		Assert.assertEquals(count + 1, result.getCoordinates().length);

		// Undo the insert operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(value, undone.getCoordinates()[3].getX(), DELTA);
		Assert.assertEquals(count, undone.getCoordinates().length);
	}

	@Test
	public void testLinearRingEdge0() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);
		int count = linearRing.getCoordinates().length;
		double value = linearRing.getCoordinates()[1].getX();

		// First a correct index. This should work:
		Geometry result = operation.execute(linearRing, service.create(GeometryIndexType.TYPE_EDGE, 0));
		Assert.assertNotNull(result);
		Assert.assertEquals(NEW_VALUE, result.getCoordinates()[1].getX(), DELTA);
		Assert.assertEquals(count + 1, result.getCoordinates().length);

		// Undo the insert operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(value, undone.getCoordinates()[1].getX(), DELTA);
		Assert.assertEquals(count, undone.getCoordinates().length);
	}

	@Test
	public void testLinearRingEdge1() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);
		int count = linearRing.getCoordinates().length;
		double value = linearRing.getCoordinates()[2].getX();

		// First a correct index. This should work:
		Geometry result = operation.execute(linearRing, service.create(GeometryIndexType.TYPE_EDGE, 1));
		Assert.assertNotNull(result);
		Assert.assertEquals(NEW_VALUE, result.getCoordinates()[2].getX(), DELTA);
		Assert.assertEquals(count + 1, result.getCoordinates().length);

		// Undo the insert operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(value, undone.getCoordinates()[2].getX(), DELTA);
		Assert.assertEquals(count, undone.getCoordinates().length);
	}

	@Test
	public void testLinearRingEdge2() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);
		int count = linearRing.getCoordinates().length;
		double value = linearRing.getCoordinates()[3].getX();

		// First a correct index. This should work:
		Geometry result = operation.execute(linearRing, service.create(GeometryIndexType.TYPE_EDGE, 2));
		Assert.assertNotNull(result);
		Assert.assertEquals(NEW_VALUE, result.getCoordinates()[3].getX(), DELTA);
		Assert.assertEquals(count + 1, result.getCoordinates().length);

		// Undo the insert operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(value, undone.getCoordinates()[3].getX(), DELTA);
		Assert.assertEquals(count, undone.getCoordinates().length);
	}

	@Test
	public void testLinearRingCornerCases() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);

		// Geometry index of wrong type:
		try {
			operation.execute(linearRing, service.create(GeometryIndexType.TYPE_GEOMETRY, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}

		// Non existing geometry index:
		try {
			operation.execute(linearRing, service.create(GeometryIndexType.TYPE_VERTEX, -1));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(linearRing, service.create(GeometryIndexType.TYPE_VERTEX, 4));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(linearRing, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(linearRing, service.create(GeometryIndexType.TYPE_EDGE, -1));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(linearRing, service.create(GeometryIndexType.TYPE_EDGE, 3));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(linearRing, service.create(GeometryIndexType.TYPE_EDGE, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testEmptyPolygon() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);
		Geometry polygon = new Geometry(Geometry.POLYGON, 0, 0);

		// Insert into a non-existent LinearRing will fail:
		try {
			operation.execute(polygon, service.create(GeometryIndexType.TYPE_EDGE, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testPolygonWithEmptyRing() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);
		Geometry polygon = new Geometry(Geometry.POLYGON, 0, 0);
		Geometry emptyRing = new Geometry(Geometry.LINEAR_RING, 0, 0);
		polygon.setGeometries(new Geometry[] { emptyRing });

		// First a correct index. This should work:
		Geometry result = operation.execute(polygon, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0));
		Assert.assertEquals(NEW_VALUE, result.getGeometries()[0].getCoordinates()[0].getX(), DELTA);
		Assert.assertEquals(2, result.getGeometries()[0].getCoordinates().length);

		// Undo the insert operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertNull(undone.getGeometries()[0].getCoordinates());
	}

	@Test
	public void testPolygon() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);
		int count = polygon.getGeometries()[0].getCoordinates().length;
		double value = polygon.getGeometries()[0].getCoordinates()[0].getX();

		// First a correct index. This should work:
		Geometry result = operation.execute(polygon, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0));
		Assert.assertNotNull(result);
		Assert.assertEquals(NEW_VALUE, result.getGeometries()[0].getCoordinates()[0].getX(), DELTA);
		Assert.assertEquals(count + 1, result.getGeometries()[0].getCoordinates().length);

		// Undo the insert operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(value, undone.getGeometries()[0].getCoordinates()[0].getX(), DELTA);
	}

	@Test
	public void testPolygonCornerCases() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);

		// Geometry index of wrong type:
		try {
			operation.execute(polygon, service.create(GeometryIndexType.TYPE_GEOMETRY, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}

		// Non existing geometry index:
		try {
			operation.execute(polygon, service.create(GeometryIndexType.TYPE_VERTEX, 0, 5));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(polygon, service.create(GeometryIndexType.TYPE_VERTEX, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(polygon, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testEmptyMultiPoint() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);
		Geometry multiPoint = new Geometry(Geometry.MULTI_POINT, 0, 0);

		// We can't expect it to create an extra Point:
		try {
			operation.execute(multiPoint, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testMultiPointWithEmptyPoint() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);
		Geometry multiPoint = new Geometry(Geometry.MULTI_POINT, 0, 0);
		Geometry emptyPoint = new Geometry(Geometry.POINT, 0, 0);
		multiPoint.setGeometries(new Geometry[] { emptyPoint });

		// First a correct index. This should work:
		Geometry result = operation.execute(multiPoint, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0));
		Assert.assertEquals(NEW_VALUE, result.getGeometries()[0].getCoordinates()[0].getX(), DELTA);

		// Undo the insert operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertNull(undone.getGeometries()[0].getCoordinates());
	}

	@Test
	public void testMultiPointCornerCases() {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);

		// Geometry index of wrong type:
		try {
			operation.execute(multiPoint, service.create(GeometryIndexType.TYPE_EDGE, 2, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}

		// Non existing geometry index:
		try {
			operation.execute(multiPoint, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(multiPoint, service.create(GeometryIndexType.TYPE_VERTEX, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(multiPoint, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testEmptyMultiLineString() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);
		Geometry multiLineString = new Geometry(Geometry.MULTI_LINE_STRING, 0, 0);

		// We can't expect it to create an extra LineString:
		try {
			operation.execute(multiLineString, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testMultiLineStringWithEmptyLine() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);
		Geometry multiLineString = new Geometry(Geometry.MULTI_LINE_STRING, 0, 0);
		Geometry emptyLineString = new Geometry(Geometry.LINE_STRING, 0, 0);
		multiLineString.setGeometries(new Geometry[] { emptyLineString });

		// First a correct index. This should work:
		Geometry result = operation.execute(multiLineString, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0));
		Assert.assertNotNull(result);
		Assert.assertEquals(NEW_VALUE, result.getGeometries()[0].getCoordinates()[0].getX(), DELTA);

		// Undo the insert operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertNull(undone.getGeometries()[0].getCoordinates());
	}

	@Test
	public void testMultiLineString() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);
		double value = multiLineString.getGeometries()[1].getCoordinates()[2].getX();

		// First a correct index. This should work:
		Geometry result = operation.execute(multiLineString, service.create(GeometryIndexType.TYPE_VERTEX, 1, 2));
		Assert.assertNotNull(result);
		Assert.assertEquals(NEW_VALUE, result.getGeometries()[1].getCoordinates()[2].getX(), DELTA);

		// Undo the insert operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(value, undone.getGeometries()[1].getCoordinates()[2].getX(), DELTA);
	}

	@Test
	public void testMultiLineStringCornerCases() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);

		// Geometry index of wrong type:
		try {
			operation.execute(multiLineString, service.create(GeometryIndexType.TYPE_GEOMETRY, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}

		// Non existing geometry index:
		try {
			operation.execute(multiLineString, service.create(GeometryIndexType.TYPE_VERTEX, 0, 4));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(multiLineString, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(multiLineString, service.create(GeometryIndexType.TYPE_VERTEX, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testEmptyMultiPolygon() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);
		Geometry multiPolygon = new Geometry(Geometry.MULTI_POLYGON, 0, 0);

		// We can't expect it to create an extra Polygon:
		try {
			operation.execute(multiPolygon, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testMultiPolygonWithEmptyPolyRing() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);
		Geometry multiPolygon = new Geometry(Geometry.MULTI_POLYGON, 0, 0);
		Geometry polygon = new Geometry(Geometry.POLYGON, 0, 0);
		Geometry emptyRing = new Geometry(Geometry.LINEAR_RING, 0, 0);
		polygon.setGeometries(new Geometry[] { emptyRing });
		multiPolygon.setGeometries(new Geometry[] { polygon });

		// First a correct index. This should work:
		Geometry result = operation.execute(multiPolygon, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0, 0));
		Assert.assertNotNull(result);
		Assert.assertEquals(NEW_VALUE, result.getGeometries()[0].getGeometries()[0].getCoordinates()[0].getX(), DELTA);

		// Undo the insert operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertNull(undone.getGeometries()[0].getGeometries()[0].getCoordinates());
	}

	@Test
	public void testMultiPolygon() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);
		double value = multiPolygon.getGeometries()[0].getGeometries()[0].getCoordinates()[1].getX();

		// First a correct index. This should work:
		Geometry result = operation.execute(multiPolygon, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0, 1));
		Assert.assertNotNull(result);
		Assert.assertEquals(NEW_VALUE, result.getGeometries()[0].getGeometries()[0].getCoordinates()[1].getX(), DELTA);

		// Undo the insert operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(value, undone.getGeometries()[0].getGeometries()[0].getCoordinates()[1].getX(), DELTA);
	}

	@Test
	public void testMultiPolygonCornerCases() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new InsertVertexOperation(service, target);

		// Geometry index of wrong type:
		try {
			operation.execute(multiPolygon, service.create(GeometryIndexType.TYPE_GEOMETRY, 0, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}

		// Non existing geometry index:
		try {
			operation.execute(multiPolygon, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0, 5));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(multiPolygon, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(multiPolygon, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}
}