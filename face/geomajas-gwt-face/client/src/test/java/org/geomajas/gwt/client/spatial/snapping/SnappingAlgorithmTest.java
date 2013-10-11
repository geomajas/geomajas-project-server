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

package org.geomajas.gwt.client.spatial.snapping;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.spatial.geometry.GeometryFactory;
import org.geomajas.gwt.client.spatial.geometry.LinearRing;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Test case that tests the different <code>SnappingAlgorithm</code> implementations.
 * </p>
 *
 * @author Pieter De Graef
 */
public class SnappingAlgorithmTest {

	private static final double DELTA = 1e-10;

	private List<Geometry> geometries = new ArrayList<Geometry>();

	private double ruleDistance = 5;

	/**
	 * Constructor that sets up an initial list of geometries to snap to.
	 */
	public SnappingAlgorithmTest() {
		GeometryFactory factory = new GeometryFactory(4326, -1);
		LinearRing shell1 = factory.createLinearRing(new Coordinate[] {new Coordinate(10.0, 10.0),
				new Coordinate(20.0, 10.0), new Coordinate(20.0, 20.0), new Coordinate(10.0, 20.0),
				new Coordinate(10.0, 10.0)});
		LinearRing hole1 = factory.createLinearRing(new Coordinate[] {new Coordinate(12.0, 12.0),
				new Coordinate(18.0, 12.0), new Coordinate(18.0, 18.0), new Coordinate(12.0, 18.0),
				new Coordinate(12.0, 12.0)});
		geometries.add(factory.createPolygon(shell1, new LinearRing[] {hole1}));
		LinearRing shell2 = factory.createLinearRing(new Coordinate[] {new Coordinate(5.0, 5.0),
				new Coordinate(15.0, 5.0), new Coordinate(15.0, 25.0), new Coordinate(5.0, 25.0)});
		geometries.add(factory.createPolygon(shell2, null));
	}

	@Test
	public void testClosestPoint() {
		SnappingAlgorithm algorithm = new ClosestPointAlgorithm(geometries, ruleDistance);
		Coordinate snapped = algorithm.getSnappingPoint(new Coordinate(16, 16), Double.MAX_VALUE);
		Assert.assertEquals(18.0, snapped.getX(), DELTA);
		Assert.assertEquals(18.0, snapped.getY(), DELTA);
		snapped = algorithm.getSnappingPoint(new Coordinate(7, 7.5), Double.MAX_VALUE);
		Assert.assertEquals(5.0, snapped.getX(), DELTA);
		Assert.assertEquals(5.0, snapped.getY(), DELTA);
	}

	@Test
	public void testNearest() {
		SnappingAlgorithm algorithm = new NearestAlgorithm(geometries, ruleDistance);
		Coordinate snapped = algorithm.getSnappingPoint(new Coordinate(16, 16), Double.MAX_VALUE);
		Assert.assertEquals(15.0, snapped.getX(), DELTA);
		Assert.assertEquals(16.0, snapped.getY(), DELTA);
		snapped = algorithm.getSnappingPoint(new Coordinate(14, 18.9), Double.MAX_VALUE);
		Assert.assertEquals(14.0, snapped.getX(), DELTA);
		Assert.assertEquals(18.0, snapped.getY(), DELTA);
	}
}
