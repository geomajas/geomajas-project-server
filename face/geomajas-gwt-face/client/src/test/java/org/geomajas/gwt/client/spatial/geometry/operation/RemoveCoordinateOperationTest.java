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

package org.geomajas.gwt.client.spatial.geometry.operation;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.spatial.geometry.GeometryFactory;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.spatial.geometry.LinearRing;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the {@link RemoveCoordinateOperation} class.
 *
 * @author Pieter De Graef
 */
public class RemoveCoordinateOperationTest {

	private static final int SRID = 4326;

	private static final int PRECISION = -1;

	private static final double DELTA = 1e-10;

	private LineString lineString;

	private LinearRing linearRing;

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------

	public RemoveCoordinateOperationTest() {
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
		RemoveCoordinateOperation op = new RemoveCoordinateOperation(0);
		LineString result = (LineString) op.execute(lineString);
		Assert.assertEquals(20.0, result.getCoordinateN(0).getX(), DELTA);
		Assert.assertEquals(20.0, result.getCoordinateN(1).getX(), DELTA);
		Assert.assertEquals(lineString.getNumPoints(), result.getNumPoints() + 1);
	}

	@Test
	public void testLineStringBigIndex() {
		RemoveCoordinateOperation op = new RemoveCoordinateOperation(lineString.getNumPoints());
		LineString result = (LineString) op.execute(lineString);
		Assert.assertEquals(10.0, result.getCoordinateN(0).getX(), DELTA);
		Assert.assertEquals(20.0, result.getCoordinateN(result.getNumPoints() - 1).getX(), DELTA);
		Assert.assertEquals(lineString.getNumPoints(), result.getNumPoints() + 1);
	}

	@Test
	public void testLineStringMiddleIndex() {
		RemoveCoordinateOperation op = new RemoveCoordinateOperation(1);
		LineString result = (LineString) op.execute(lineString);
		Assert.assertEquals(10.0, result.getCoordinateN(0).getX(), DELTA);
		Assert.assertEquals(20.0, result.getCoordinateN(1).getX(), DELTA);
		Assert.assertEquals(lineString.getNumPoints(), result.getNumPoints() + 1, DELTA);
	}

	@Test
	public void testLinearRingZeroIndex() {
		RemoveCoordinateOperation op = new RemoveCoordinateOperation(0);
		LinearRing result = (LinearRing) op.execute(linearRing);
		Assert.assertEquals(20.0, result.getCoordinateN(0).getX(), DELTA);
		Assert.assertEquals(20.0, result.getCoordinateN(2).getX(), DELTA);
		Assert.assertEquals(linearRing.getNumPoints(), result.getNumPoints() + 1);
		Assert.assertTrue(result.isClosed());
	}

	@Test
	public void testLinearRingBigIndex() {
		RemoveCoordinateOperation op = new RemoveCoordinateOperation(linearRing.getNumPoints());
		LinearRing result = (LinearRing) op.execute(linearRing);
		Assert.assertEquals(10.0, result.getCoordinateN(0).getX(), DELTA);
		Assert.assertEquals(20.0, result.getCoordinateN(result.getNumPoints() - 2).getX(), DELTA);
		Assert.assertEquals(linearRing.getNumPoints(), result.getNumPoints() + 1);
		Assert.assertTrue(result.isClosed());
	}

	@Test
	public void testLinearRingMiddleIndex() {
		RemoveCoordinateOperation op = new RemoveCoordinateOperation(1);
		LinearRing result = (LinearRing) op.execute(linearRing);
		Assert.assertEquals(10.0, result.getCoordinateN(0).getX(), DELTA);
		Assert.assertEquals(10.0, result.getCoordinateN(2).getX(), DELTA);
		Assert.assertEquals(linearRing.getNumPoints(), result.getNumPoints() + 1, DELTA);
		Assert.assertTrue(result.isClosed());
	}
}
