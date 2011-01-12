/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt.client.spatial.geometry.operation;

import com.vividsolutions.jts.util.Assert;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.spatial.geometry.GeometryFactory;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.spatial.geometry.LinearRing;
import org.junit.Test;

/**
 * Tests the {@link TranslateCoordinateOperation} class.
 *
 * @author Pieter De Graef
 */
public class TranslateCoordinateOperationTest {

	private final static int SRID = 4326;

	private final static int PRECISION = -1;

	private LineString lineString;

	private LinearRing linearRing;

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------

	public TranslateCoordinateOperationTest() {
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
		TranslateCoordinateOperation op = new TranslateCoordinateOperation(0, 10, -10);
		LineString result = (LineString) op.execute(lineString);
		Assert.equals(20.0, result.getCoordinateN(0).getX());
		Assert.equals(20.0, result.getCoordinateN(1).getX());
		Assert.equals(lineString.getNumPoints(), result.getNumPoints());
	}

	@Test
	public void testLineStringBigIndex() {
		TranslateCoordinateOperation op = new TranslateCoordinateOperation(10, 10, -10);
		LineString result = (LineString) op.execute(lineString);
		Assert.equals(10.0, result.getCoordinateN(0).getX());
		Assert.equals(30.0, result.getCoordinateN(result.getNumPoints() - 1).getX());
		Assert.equals(lineString.getNumPoints(), result.getNumPoints());
	}

	@Test
	public void testLineStringMiddleIndex() {
		TranslateCoordinateOperation op = new TranslateCoordinateOperation(2, 10, -10);
		LineString result = (LineString) op.execute(lineString);
		Assert.equals(10.0, result.getCoordinateN(0).getX());
		Assert.equals(30.0, result.getCoordinateN(2).getX());
		Assert.equals(lineString.getNumPoints(), result.getNumPoints());
	}

	@Test
	public void testLinearRingZeroIndex() {
		TranslateCoordinateOperation op = new TranslateCoordinateOperation(0, 10, -10);
		LinearRing result = (LinearRing) op.execute(linearRing);
		Assert.equals(20.0, result.getCoordinateN(0).getX());
		Assert.equals(20.0, result.getCoordinateN(1).getX());
		Assert.equals(linearRing.getNumPoints(), result.getNumPoints());
		Assert.isTrue(result.isClosed());
	}

	@Test
	public void testLinearRingBigIndex() {
		TranslateCoordinateOperation op = new TranslateCoordinateOperation(10, 10, -10);
		LinearRing result = (LinearRing) op.execute(linearRing);
		Assert.equals(10.0, result.getCoordinateN(0).getX());
		Assert.equals(30.0, result.getCoordinateN(result.getNumPoints() - 2).getX());
		Assert.equals(linearRing.getNumPoints(), result.getNumPoints());
		Assert.isTrue(result.isClosed());
	}

	@Test
	public void testLinearRingMiddleIndex() {
		TranslateCoordinateOperation op = new TranslateCoordinateOperation(2, 10, -10);
		LinearRing result = (LinearRing) op.execute(linearRing);
		Assert.equals(10.0, result.getCoordinateN(0).getX());
		Assert.equals(30.0, result.getCoordinateN(2).getX());
		Assert.equals(linearRing.getNumPoints(), result.getNumPoints());
		Assert.isTrue(result.isClosed());
	}
}
