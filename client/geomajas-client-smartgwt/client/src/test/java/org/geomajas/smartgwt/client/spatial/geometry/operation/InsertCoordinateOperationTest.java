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

package org.geomajas.smartgwt.client.spatial.geometry.operation;

import org.geomajas.geometry.Coordinate;
import org.geomajas.smartgwt.client.spatial.geometry.GeometryFactory;
import org.geomajas.smartgwt.client.spatial.geometry.LineString;
import org.geomajas.smartgwt.client.spatial.geometry.LinearRing;
import org.geomajas.smartgwt.client.spatial.geometry.operation.InsertCoordinateOperation;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the {@link org.geomajas.smartgwt.client.spatial.geometry.operation.InsertCoordinateOperation} class.
 *
 * @author Pieter De Graef
 */
public class InsertCoordinateOperationTest {

	private static final int SRID = 4326;

	private static final int PRECISION = -1;

	private static final double DELTA = 1e-10;

	private LineString lineString;

	private LinearRing linearRing;

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------

	public InsertCoordinateOperationTest() {
		GeometryFactory gwtFactory = new GeometryFactory(SRID, PRECISION);
		lineString = gwtFactory.createLineString(new Coordinate[] {new Coordinate(10.0, 10.0),
				new Coordinate(20.0, 10.0), new Coordinate(20.0, 20.0)});
		linearRing = gwtFactory.createLinearRing(new Coordinate[] {new Coordinate(10.0, 10.0),
				new Coordinate(20.0, 10.0), new Coordinate(20.0, 20.0), new Coordinate(10.0, 10.0)});
	}

	// -------------------------------------------------------------------------
	// Actual tests:
	// -------------------------------------------------------------------------

	@Test
	public void testLineStringZeroIndex() {
		InsertCoordinateOperation op = new InsertCoordinateOperation(0, new Coordinate(0, 0));
		LineString result = (LineString) op.execute(lineString);
		Assert.assertEquals(result.getCoordinateN(0).getX(), 0.0, DELTA);
		Assert.assertEquals(result.getCoordinateN(1).getX(), 10.0, DELTA);
		Assert.assertEquals(result.getNumPoints(), lineString.getNumPoints() + 1);
	}

	@Test
	public void testLineStringBigIndex() {
		InsertCoordinateOperation op = new InsertCoordinateOperation(lineString.getNumPoints(), new Coordinate(30, 30));
		LineString result = (LineString) op.execute(lineString);
		Assert.assertEquals(result.getCoordinateN(0).getX(), 10.0, DELTA);
		Assert.assertEquals(result.getCoordinateN(result.getNumPoints() - 1).getX(), 30.0, DELTA);
		Assert.assertEquals(result.getNumPoints(), lineString.getNumPoints() + 1);
	}

	@Test
	public void testLineStringMiddleIndex() {
		InsertCoordinateOperation op = new InsertCoordinateOperation(2, new Coordinate(30, 30));
		LineString result = (LineString) op.execute(lineString);
		Assert.assertEquals(result.getCoordinateN(0).getX(), 10.0, DELTA);
		Assert.assertEquals(result.getCoordinateN(2).getX(), 30.0, DELTA);
		Assert.assertEquals(result.getCoordinateN(3).getX(), 20.0, DELTA);
		Assert.assertEquals(result.getNumPoints(), lineString.getNumPoints() + 1);
	}

	@Test
	public void testLinearRingZeroIndex() {
		InsertCoordinateOperation op = new InsertCoordinateOperation(0, new Coordinate(0, 0));
		LinearRing result = (LinearRing) op.execute(linearRing);
		Assert.assertEquals(result.getCoordinateN(0).getX(), 0.0, DELTA);
		Assert.assertEquals(result.getCoordinateN(1).getX(), 10.0, DELTA);
		Assert.assertEquals(result.getNumPoints(), linearRing.getNumPoints() + 1);
		Assert.assertTrue(result.isClosed());
	}

	@Test
	public void testLinearRingBigIndex() {
		InsertCoordinateOperation op = new InsertCoordinateOperation(linearRing.getNumPoints(), new Coordinate(30, 30));
		LinearRing result = (LinearRing) op.execute(linearRing);
		Assert.assertEquals(result.getCoordinateN(0).getX(), 10.0, DELTA);
		Assert.assertEquals(result.getCoordinateN(result.getNumPoints() - 2).getX(), 30.0, DELTA);
		Assert.assertEquals(result.getNumPoints(), linearRing.getNumPoints() + 1);
		Assert.assertTrue(result.isClosed());
	}

	@Test
	public void testLinearRingMiddleIndex() {
		InsertCoordinateOperation op = new InsertCoordinateOperation(2, new Coordinate(30, 30));
		LinearRing result = (LinearRing) op.execute(linearRing);
		Assert.assertEquals(result.getCoordinateN(0).getX(), 10.0, DELTA);
		Assert.assertEquals(result.getCoordinateN(2).getX(), 30.0, DELTA);
		Assert.assertEquals(result.getCoordinateN(3).getX(), 20.0, DELTA);
		Assert.assertEquals(result.getNumPoints(), linearRing.getNumPoints() + 1);
		Assert.assertTrue(result.isClosed());
	}
}
