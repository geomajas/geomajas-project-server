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
import org.geomajas.gwt.client.spatial.geometry.LinearRing;
import org.geomajas.gwt.client.spatial.geometry.Polygon;
import org.junit.Test;

/**
 * Tests the {@link RemoveRingOperation} class.
 *
 * @author Pieter De Graef
 */
public class RemoveRingOperationTest {

	private final static int SRID = 4326;

	private final static int PRECISION = -1;

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
		Assert.equals(polygon.getNumInteriorRing() - 1, result.getNumInteriorRing());
		Assert.equals(16.0, result.getInteriorRingN(0).getCoordinate().getX());
	}

	@Test
	public void testLineStringBigIndex() {
		GeometryOperation op = new RemoveRingOperation(polygon.getNumInteriorRing());
		Polygon result = (Polygon) op.execute(polygon);
		Assert.equals(polygon.getNumInteriorRing() - 1, result.getNumInteriorRing());
		Assert.equals(12.0, result.getInteriorRingN(0).getCoordinate().getX());
	}

	@Test
	public void testLineStringMiddleIndex() {
		GeometryOperation op = new RemoveRingOperation(1);
		Polygon result = (Polygon) op.execute(polygon);
		Assert.equals(polygon.getNumInteriorRing() - 1, result.getNumInteriorRing());
		Assert.equals(12.0, result.getInteriorRingN(0).getCoordinate().getX());
	}
}
