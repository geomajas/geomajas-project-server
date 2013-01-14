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
 * Test cases for deleting sub-geometries within any type of geometry.
 * 
 * @author Pieter De Graef
 */
public class DeleteGeometryOperationTest {

	private static final double DELTA = 0.0001;

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

	public DeleteGeometryOperationTest() {
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
	// Test-cases for the operation:
	// ------------------------------------------------------------------------

	@Test
	public void testPoint() {
		GeometryIndexOperation operation = new DeleteGeometryOperation(service);

		try {
			operation.execute(point, service.create(GeometryIndexType.TYPE_GEOMETRY, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testPointCornerCases() {
		GeometryIndexOperation operation = new DeleteGeometryOperation(service);

		try {
			operation.execute(point, service.create(GeometryIndexType.TYPE_EDGE, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(point, service.create(GeometryIndexType.TYPE_VERTEX, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(point, service.create(GeometryIndexType.TYPE_GEOMETRY, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testLineString() {
		GeometryIndexOperation operation = new DeleteGeometryOperation(service);

		try {
			operation.execute(lineString, service.create(GeometryIndexType.TYPE_GEOMETRY, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testLineStringCornerCases() {
		GeometryIndexOperation operation = new DeleteGeometryOperation(service);

		try {
			operation.execute(lineString, service.create(GeometryIndexType.TYPE_EDGE, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(lineString, service.create(GeometryIndexType.TYPE_VERTEX, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(lineString, service.create(GeometryIndexType.TYPE_GEOMETRY, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testLinearRing() {
		GeometryIndexOperation operation = new DeleteGeometryOperation(service);

		try {
			operation.execute(linearRing, service.create(GeometryIndexType.TYPE_GEOMETRY, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testLinearRingCornerCases() {
		GeometryIndexOperation operation = new DeleteGeometryOperation(service);

		try {
			operation.execute(linearRing, service.create(GeometryIndexType.TYPE_EDGE, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(linearRing, service.create(GeometryIndexType.TYPE_VERTEX, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(linearRing, service.create(GeometryIndexType.TYPE_GEOMETRY, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testPolygonAtBegin() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new DeleteGeometryOperation(service);
		int count = polygon.getGeometries().length;
		double value = polygon.getGeometries()[1].getCoordinates()[0].getX();

		// Delete the first ring:
		Geometry result = operation.execute(polygon, service.create(GeometryIndexType.TYPE_GEOMETRY, 0));
		Assert.assertEquals(count - 1, result.getGeometries().length);
		Assert.assertEquals(value, result.getGeometries()[0].getCoordinates()[0].getX(), DELTA);

		// Undo delete operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(count, undone.getGeometries().length);
		Assert.assertEquals(value, undone.getGeometries()[1].getCoordinates()[0].getX(), DELTA);
	}

	@Test
	public void testPolygonAtEnd() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new DeleteGeometryOperation(service);
		int count = polygon.getGeometries().length;
		double value = polygon.getGeometries()[0].getCoordinates()[0].getX();

		// Delete a ring at the end:
		Geometry result = operation.execute(polygon, service.create(GeometryIndexType.TYPE_GEOMETRY, 1));
		Assert.assertEquals(count - 1, result.getGeometries().length);
		Assert.assertEquals(value, result.getGeometries()[0].getCoordinates()[0].getX(), DELTA);

		// Undo delete operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(count, undone.getGeometries().length);
		Assert.assertEquals(value, undone.getGeometries()[0].getCoordinates()[0].getX(), DELTA);
	}

	@Test
	public void testPolygonCornerCases() {
		GeometryIndexOperation operation = new DeleteGeometryOperation(service);

		try {
			operation.execute(polygon, service.create(GeometryIndexType.TYPE_EDGE, 0));
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
			operation.execute(polygon, service.create(GeometryIndexType.TYPE_GEOMETRY, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testMultiPointAtBegin() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new DeleteGeometryOperation(service);
		int count = multiPoint.getGeometries().length;
		double value = multiPoint.getGeometries()[1].getCoordinates()[0].getX();

		// Delete a point at the end:
		Geometry result = operation.execute(multiPoint, service.create(GeometryIndexType.TYPE_GEOMETRY, 0));
		Assert.assertEquals(count - 1, result.getGeometries().length);
		Assert.assertEquals(value, result.getGeometries()[0].getCoordinates()[0].getX(), DELTA);

		// Undo delete operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(count, undone.getGeometries().length);
		Assert.assertEquals(value, undone.getGeometries()[1].getCoordinates()[0].getX(), DELTA);
	}

	@Test
	public void testMultiPointAtEnd() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new DeleteGeometryOperation(service);
		int count = multiPoint.getGeometries().length;
		double value = multiPoint.getGeometries()[0].getCoordinates()[0].getX();

		// Delete a point at the end:
		Geometry result = operation.execute(multiPoint, service.create(GeometryIndexType.TYPE_GEOMETRY, 1));
		Assert.assertEquals(count - 1, result.getGeometries().length);
		Assert.assertEquals(value, result.getGeometries()[0].getCoordinates()[0].getX(), DELTA);

		// Undo delete operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(count, undone.getGeometries().length);
		Assert.assertEquals(value, undone.getGeometries()[0].getCoordinates()[0].getX(), DELTA);
	}

	@Test
	public void testMultiPointCornerCases() {
		GeometryIndexOperation operation = new DeleteGeometryOperation(service);

		try {
			operation.execute(multiPoint, service.create(GeometryIndexType.TYPE_EDGE, 0));
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
			operation.execute(multiPoint, service.create(GeometryIndexType.TYPE_GEOMETRY, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testMultiLineStringAtBegin() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new DeleteGeometryOperation(service);
		int count = multiLineString.getGeometries().length;
		double value = multiLineString.getGeometries()[1].getCoordinates()[0].getX();

		// Delete a LineString at the beginning:
		Geometry result = operation.execute(multiLineString, service.create(GeometryIndexType.TYPE_GEOMETRY, 0));
		Assert.assertEquals(count - 1, result.getGeometries().length);
		Assert.assertEquals(value, result.getGeometries()[0].getCoordinates()[0].getX(), DELTA);

		// Undo delete operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(count, undone.getGeometries().length);
		Assert.assertEquals(value, undone.getGeometries()[1].getCoordinates()[0].getX(), DELTA);
	}

	@Test
	public void testMultiLineStringAtEnd() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new DeleteGeometryOperation(service);
		int count = multiLineString.getGeometries().length;
		double value = multiLineString.getGeometries()[0].getCoordinates()[0].getX();

		// Delete a LineString at the end:
		Geometry result = operation.execute(multiLineString, service.create(GeometryIndexType.TYPE_GEOMETRY, 1));
		Assert.assertEquals(count - 1, result.getGeometries().length);
		Assert.assertEquals(value, result.getGeometries()[0].getCoordinates()[0].getX(), DELTA);

		// Undo delete operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(count, undone.getGeometries().length);
		Assert.assertEquals(value, undone.getGeometries()[0].getCoordinates()[0].getX(), DELTA);
	}

	@Test
	public void testMultiLineStringCornerCases() {
		GeometryIndexOperation operation = new DeleteGeometryOperation(service);

		try {
			operation.execute(multiLineString, service.create(GeometryIndexType.TYPE_EDGE, 0));
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
		try {
			operation.execute(multiLineString, service.create(GeometryIndexType.TYPE_GEOMETRY, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testMultiPolygonFirstPolygon() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new DeleteGeometryOperation(service);
		int count = multiPolygon.getGeometries().length;
		double value = multiPolygon.getGeometries()[1].getGeometries()[0].getCoordinates()[0].getX();

		// Delete the first polygon:
		Geometry result = operation.execute(multiPolygon, service.create(GeometryIndexType.TYPE_GEOMETRY, 0));
		Assert.assertEquals(count - 1, result.getGeometries().length);
		Assert.assertEquals(value, result.getGeometries()[0].getGeometries()[0].getCoordinates()[0].getX(), DELTA);

		// Undo delete operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(count, undone.getGeometries().length);
		Assert.assertEquals(value, undone.getGeometries()[1].getGeometries()[0].getCoordinates()[0].getX(), DELTA);
	}

	@Test
	public void testMultiPolygonLastPolygon() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new DeleteGeometryOperation(service);
		int count = multiPolygon.getGeometries().length;
		double value = multiPolygon.getGeometries()[0].getGeometries()[0].getCoordinates()[0].getX();

		// Delete the first polygon:
		Geometry result = operation.execute(multiPolygon, service.create(GeometryIndexType.TYPE_GEOMETRY, 1));
		Assert.assertEquals(count - 1, result.getGeometries().length);
		Assert.assertEquals(value, result.getGeometries()[0].getGeometries()[0].getCoordinates()[0].getX(), DELTA);

		// Undo delete operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(count, undone.getGeometries().length);
		Assert.assertEquals(value, undone.getGeometries()[0].getGeometries()[0].getCoordinates()[0].getX(), DELTA);
	}

	@Test
	public void testMultiPolygonFirstPolyFirstRing() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new DeleteGeometryOperation(service);
		int count = multiPolygon.getGeometries()[0].getGeometries().length;
		double value = multiPolygon.getGeometries()[0].getGeometries()[1].getCoordinates()[0].getX();

		// Delete the first polygon:
		Geometry result = operation.execute(multiPolygon, service.create(GeometryIndexType.TYPE_GEOMETRY, 0, 0));
		Assert.assertEquals(count - 1, result.getGeometries()[0].getGeometries().length);
		Assert.assertEquals(value, result.getGeometries()[0].getGeometries()[0].getCoordinates()[0].getX(), DELTA);

		// Undo delete operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(count, undone.getGeometries()[0].getGeometries().length);
		Assert.assertEquals(value, undone.getGeometries()[0].getGeometries()[1].getCoordinates()[0].getX(), DELTA);
	}

	@Test
	public void testMultiPolygonFirstPolyLastRing() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new DeleteGeometryOperation(service);
		int count = multiPolygon.getGeometries()[0].getGeometries().length;
		double value = multiPolygon.getGeometries()[0].getGeometries()[0].getCoordinates()[0].getX();

		// Delete the first polygon:
		Geometry result = operation.execute(multiPolygon, service.create(GeometryIndexType.TYPE_GEOMETRY, 0, 1));
		Assert.assertEquals(count - 1, result.getGeometries()[0].getGeometries().length);
		Assert.assertEquals(value, result.getGeometries()[0].getGeometries()[0].getCoordinates()[0].getX(), DELTA);

		// Undo delete operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(count, undone.getGeometries()[0].getGeometries().length);
		Assert.assertEquals(value, undone.getGeometries()[0].getGeometries()[0].getCoordinates()[0].getX(), DELTA);
	}

	@Test
	public void testMultiPolygonCornerCases() {
		GeometryIndexOperation operation = new DeleteGeometryOperation(service);

		try {
			operation.execute(multiPolygon, service.create(GeometryIndexType.TYPE_EDGE, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(multiPolygon, service.create(GeometryIndexType.TYPE_EDGE, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(multiPolygon, service.create(GeometryIndexType.TYPE_VERTEX, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(multiPolygon, service.create(GeometryIndexType.TYPE_GEOMETRY, 0, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}
}