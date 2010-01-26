/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.spatial.geometry.operation;

import com.vividsolutions.jts.util.Assert;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.spatial.geometry.GeometryFactory;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.spatial.geometry.LinearRing;
import org.junit.Test;

/**
 * Tests the {@link RemoveCoordinateOperation} class.
 *
 * @author Pieter De Graef
 */
public class RemoveCoordinateOperationTest {

	private final static int SRID = 4326;

	private final static int PRECISION = -1;

	private GeometryFactory gwtFactory;

	private LineString lineString;

	private LinearRing linearRing;

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------

	public RemoveCoordinateOperationTest() {
		gwtFactory = new GeometryFactory(SRID, PRECISION);
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
		Assert.equals(20.0, result.getCoordinateN(0).getX());
		Assert.equals(20.0, result.getCoordinateN(1).getX());
		Assert.equals(lineString.getNumPoints(), result.getNumPoints() + 1);
	}

	@Test
	public void testLineStringBigIndex() {
		RemoveCoordinateOperation op = new RemoveCoordinateOperation(lineString.getNumPoints());
		LineString result = (LineString) op.execute(lineString);
		Assert.equals(10.0, result.getCoordinateN(0).getX());
		Assert.equals(20.0, result.getCoordinateN(result.getNumPoints() - 1).getX());
		Assert.equals(lineString.getNumPoints(), result.getNumPoints() + 1);
	}

	@Test
	public void testLineStringMiddleIndex() {
		RemoveCoordinateOperation op = new RemoveCoordinateOperation(1);
		LineString result = (LineString) op.execute(lineString);
		Assert.equals(10.0, result.getCoordinateN(0).getX());
		Assert.equals(20.0, result.getCoordinateN(1).getX());
		Assert.equals(lineString.getNumPoints(), result.getNumPoints() + 1);
	}

	@Test
	public void testLinearRingZeroIndex() {
		RemoveCoordinateOperation op = new RemoveCoordinateOperation(0);
		LinearRing result = (LinearRing) op.execute(linearRing);
		Assert.equals(20.0, result.getCoordinateN(0).getX());
		Assert.equals(20.0, result.getCoordinateN(2).getX());
		Assert.equals(linearRing.getNumPoints(), result.getNumPoints() + 1);
		Assert.isTrue(result.isClosed());
	}

	@Test
	public void testLinearRingBigIndex() {
		RemoveCoordinateOperation op = new RemoveCoordinateOperation(linearRing.getNumPoints());
		LinearRing result = (LinearRing) op.execute(linearRing);
		Assert.equals(10.0, result.getCoordinateN(0).getX());
		Assert.equals(20.0, result.getCoordinateN(result.getNumPoints() - 2).getX());
		Assert.equals(linearRing.getNumPoints(), result.getNumPoints() + 1);
		Assert.isTrue(result.isClosed());
	}

	@Test
	public void testLinearRingMiddleIndex() {
		RemoveCoordinateOperation op = new RemoveCoordinateOperation(1);
		LinearRing result = (LinearRing) op.execute(linearRing);
		Assert.equals(10.0, result.getCoordinateN(0).getX());
		Assert.equals(10.0, result.getCoordinateN(2).getX());
		Assert.equals(linearRing.getNumPoints(), result.getNumPoints() + 1);
		Assert.isTrue(result.isClosed());
	}
}
