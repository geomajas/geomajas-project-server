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
import org.geomajas.smartgwt.client.spatial.geometry.LinearRing;
import org.geomajas.smartgwt.client.spatial.geometry.Polygon;
import org.geomajas.smartgwt.client.spatial.geometry.operation.GeometryOperation;
import org.geomajas.smartgwt.client.spatial.geometry.operation.RemoveRingOperation;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the {@link org.geomajas.smartgwt.client.spatial.geometry.operation.RemoveRingOperation} class.
 *
 * @author Pieter De Graef
 */
public class RemoveRingOperationTest {

	private static final int SRID = 4326;

	private static final double TOLERANCE = .000001;

	private static final int PRECISION = -1;

	private Polygon polygon;

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------

	public RemoveRingOperationTest() {
		GeometryFactory gwtFactory = new GeometryFactory(SRID, PRECISION);
		LinearRing exteriorRing = gwtFactory.createLinearRing(new Coordinate[] {new Coordinate(0.0, 0.0),
				new Coordinate(20.0, 0.0), new Coordinate(20.0, 20.0), new Coordinate(0.0, 20.0)});
		LinearRing interiorRing1 = gwtFactory.createLinearRing(new Coordinate[] {new Coordinate(12.0, 12.0),
				new Coordinate(14.0, 12.0), new Coordinate(14.0, 18.0), new Coordinate(12.0, 18.0)});
		LinearRing interiorRing2 = gwtFactory.createLinearRing(new Coordinate[] {new Coordinate(16.0, 12.0),
				new Coordinate(18.0, 12.0), new Coordinate(18.0, 18.0), new Coordinate(16.0, 18.0)});

		polygon = gwtFactory.createPolygon(exteriorRing, new LinearRing[] {interiorRing1, interiorRing2});
	}

	// -------------------------------------------------------------------------
	// Actual tests:
	// -------------------------------------------------------------------------

	@Test
	public void testLineStringZeroIndex() {
		GeometryOperation op = new RemoveRingOperation(0);
		Polygon result = (Polygon) op.execute(polygon);
		Assert.assertEquals(polygon.getNumInteriorRing() - 1, result.getNumInteriorRing());
		Assert.assertEquals(16.0, result.getInteriorRingN(0).getCoordinate().getX(), TOLERANCE);
	}

	@Test
	public void testLineStringBigIndex() {
		GeometryOperation op = new RemoveRingOperation(polygon.getNumInteriorRing());
		Polygon result = (Polygon) op.execute(polygon);
		Assert.assertEquals(polygon.getNumInteriorRing() - 1, result.getNumInteriorRing());
		Assert.assertEquals(12.0, result.getInteriorRingN(0).getCoordinate().getX(), TOLERANCE);
	}

	@Test
	public void testLineStringMiddleIndex() {
		GeometryOperation op = new RemoveRingOperation(1);
		Polygon result = (Polygon) op.execute(polygon);
		Assert.assertEquals(polygon.getNumInteriorRing() - 1, result.getNumInteriorRing());
		Assert.assertEquals(12.0, result.getInteriorRingN(0).getCoordinate().getX(), TOLERANCE);
	}
}
