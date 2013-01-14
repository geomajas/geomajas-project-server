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
 * Test cases for testing the moving of a vertex within any type of geometry.
 * 
 * @author Pieter De Graef
 */
public class MoveVertexOperationTest {

	private static final double DELTA = 0.0001;

	private static final double NEW_VALUE = 342;

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

	public MoveVertexOperationTest() {
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
	public void testPoint() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new MoveVertexOperation(service, new Coordinate(NEW_VALUE, 0));

		// First a correct index. This should work:
		Geometry result = operation.execute(point, service.create(GeometryIndexType.TYPE_VERTEX, 0));
		Assert.assertNotNull(result);
		Assert.assertEquals(NEW_VALUE, result.getCoordinates()[0].getX(), DELTA);

		// Revert again should return original result:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(point.getCoordinates()[0].getX(), undone.getCoordinates()[0].getX(), DELTA);
	}

	@Test
	public void testPointCornerCases() {
		GeometryIndexOperation operation = new MoveVertexOperation(service, new Coordinate(NEW_VALUE, 0));

		// Geometry index of wrong type:
		try {
			operation.execute(point, service.create(GeometryIndexType.TYPE_EDGE, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}

		// Non existing geometry index:
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
	public void testLineString() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new MoveVertexOperation(service, new Coordinate(NEW_VALUE, 0));

		// First a correct index. This should work:
		Geometry result = operation.execute(lineString, service.create(GeometryIndexType.TYPE_VERTEX, 1));
		Assert.assertNotNull(result);
		Assert.assertEquals(NEW_VALUE, result.getCoordinates()[1].getX(), DELTA);

		// Revert again should return original result:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(lineString.getCoordinates()[1].getX(), undone.getCoordinates()[1].getX(), DELTA);
	}

	@Test
	public void testLineStringCornerCases() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new MoveVertexOperation(service, new Coordinate(NEW_VALUE, 0));

		// Geometry index of wrong type:
		try {
			operation.execute(lineString, service.create(GeometryIndexType.TYPE_EDGE, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}

		// Non existing geometry index:
		try {
			operation.execute(lineString, service.create(GeometryIndexType.TYPE_VERTEX, 3));
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
	}

	@Test
	public void testLinearRing() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new MoveVertexOperation(service, new Coordinate(NEW_VALUE, 0));

		// First a correct index. This should work:
		Geometry result = operation.execute(linearRing, service.create(GeometryIndexType.TYPE_VERTEX, 1));
		Assert.assertNotNull(result);
		Assert.assertEquals(NEW_VALUE, result.getCoordinates()[1].getX(), DELTA);

		// Revert again should return original result:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(linearRing.getCoordinates()[1].getX(), undone.getCoordinates()[1].getX(), DELTA);
	}

	@Test
	public void testLinearRingIsClosed() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new MoveVertexOperation(service, new Coordinate(NEW_VALUE, 0));

		// First a correct index. This should simply work:
		Geometry result = operation.execute(linearRing, service.create(GeometryIndexType.TYPE_VERTEX, 0));
		Assert.assertNotNull(result);
		Assert.assertEquals(NEW_VALUE, result.getCoordinates()[0].getX(), DELTA);
		Assert.assertEquals(NEW_VALUE, result.getCoordinates()[result.getCoordinates().length - 1].getX(), DELTA);

		// Revert again should return original result:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(linearRing.getCoordinates()[0].getX(), undone.getCoordinates()[0].getX(), DELTA);
		Assert.assertEquals(linearRing.getCoordinates()[0].getX(),
				undone.getCoordinates()[result.getCoordinates().length - 1].getX(), DELTA);
	}

	@Test
	public void testLinearRingCornerCases() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new MoveVertexOperation(service, new Coordinate(NEW_VALUE, 0));

		// Geometry index of wrong type:
		try {
			operation.execute(linearRing, service.create(GeometryIndexType.TYPE_EDGE, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}

		// Non existing geometry index:
		try {
			operation.execute(linearRing, service.create(GeometryIndexType.TYPE_VERTEX, 3));
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
	}

	@Test
	public void testPolygon() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new MoveVertexOperation(service, new Coordinate(NEW_VALUE, 0));

		// First a correct index. This should work:
		Geometry result = operation.execute(polygon, service.create(GeometryIndexType.TYPE_VERTEX, 0, 1));
		Assert.assertNotNull(result);
		Assert.assertEquals(NEW_VALUE, result.getGeometries()[0].getCoordinates()[1].getX(), DELTA);

		// Revert again should return original result:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(polygon.getGeometries()[0].getCoordinates()[1].getX(),
				undone.getGeometries()[0].getCoordinates()[1].getX(), DELTA);
	}

	@Test
	public void testPolygonCornerCases() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new MoveVertexOperation(service, new Coordinate(NEW_VALUE, 0));

		// Geometry index of wrong type:
		try {
			operation.execute(polygon, service.create(GeometryIndexType.TYPE_EDGE, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}

		// Non existing geometry index:
		try {
			operation.execute(polygon, service.create(GeometryIndexType.TYPE_VERTEX, 0, 4));
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
	public void testMultiPoint() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new MoveVertexOperation(service, new Coordinate(NEW_VALUE, 0));

		// First a correct index. This should work:
		Geometry result = operation.execute(multiPoint, service.create(GeometryIndexType.TYPE_VERTEX, 1, 0));
		Assert.assertNotNull(result);
		Assert.assertEquals(NEW_VALUE, result.getGeometries()[1].getCoordinates()[0].getX(), DELTA);

		// Revert again should return original result:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(multiPoint.getGeometries()[1].getCoordinates()[0].getX(),
				undone.getGeometries()[1].getCoordinates()[0].getX(), DELTA);
	}

	@Test
	public void testMultiPointCornerCases() {
		GeometryIndexOperation operation = new MoveVertexOperation(service, new Coordinate(NEW_VALUE, 0));

		// Geometry index of wrong type:
		try {
			operation.execute(multiPoint, service.create(GeometryIndexType.TYPE_EDGE, 1, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}

		// Non existing geometry index:
		try {
			operation.execute(multiPoint, service.create(GeometryIndexType.TYPE_VERTEX, 1, 1));
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
	public void testMultiLineString() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new MoveVertexOperation(service, new Coordinate(NEW_VALUE, 0));

		// First a correct index. This should work:
		Geometry result = operation.execute(multiLineString, service.create(GeometryIndexType.TYPE_VERTEX, 1, 1));
		Assert.assertNotNull(result);
		Assert.assertEquals(NEW_VALUE, result.getGeometries()[1].getCoordinates()[1].getX(), DELTA);

		// Revert again should return original result:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(multiLineString.getGeometries()[1].getCoordinates()[1].getX(),
				undone.getGeometries()[1].getCoordinates()[1].getX(), DELTA);
	}

	@Test
	public void testMultiLineStringCornerCases() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new MoveVertexOperation(service, new Coordinate(NEW_VALUE, 0));

		// Geometry index of wrong type:
		try {
			operation.execute(multiLineString, service.create(GeometryIndexType.TYPE_EDGE, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}

		// Non existing geometry index:
		try {
			operation.execute(multiLineString, service.create(GeometryIndexType.TYPE_VERTEX, 0, 3));
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
	public void testMultiPolygon() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new MoveVertexOperation(service, new Coordinate(NEW_VALUE, 0));

		// First a correct index. This should work:
		Geometry result = operation.execute(multiPolygon, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0, 1));
		Assert.assertNotNull(result);
		Assert.assertEquals(NEW_VALUE, result.getGeometries()[0].getGeometries()[0].getCoordinates()[1].getX(), DELTA);

		// Revert again should return original result:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(multiPolygon.getGeometries()[0].getGeometries()[0].getCoordinates()[1].getX(),
				undone.getGeometries()[0].getGeometries()[0].getCoordinates()[1].getX(), DELTA);
	}

	@Test
	public void testMultiPolygonCornerCases() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new MoveVertexOperation(service, new Coordinate(NEW_VALUE, 0));

		// Geometry index of wrong type:
		try {
			operation.execute(multiPolygon, service.create(GeometryIndexType.TYPE_EDGE, 0, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}

		// Non existing geometry index:
		try {
			operation.execute(multiPolygon, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0, 4));
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