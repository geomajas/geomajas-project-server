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

package org.geomajas.plugin.editing.client.snap;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.snap.algorithm.NearestEdgeOfIntersection;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the {@link NearestEdgeOfIntersection}.
 * 
 * @author Pieter De Graef
 */
public class NearestEdgeOfIntersectionTest {

	private static final double DELTA = 0.0001;

	private final NearestEdgeOfIntersection algorithm = new NearestEdgeOfIntersection();

	@Test
	public void testPoint() {
		Geometry point1 = new Geometry(Geometry.POINT, 0, 0);
		point1.setCoordinates(new Coordinate[] { new Coordinate(0, 0) });
		Geometry point2 = new Geometry(Geometry.POINT, 0, 0);
		point2.setCoordinates(new Coordinate[] { new Coordinate(10, 10) });
		algorithm.setGeometries(new Geometry[] { point1, point2 });

		Coordinate result = algorithm.snap(new Coordinate(1, 1), 1);
		Assert.assertEquals(1.0, result.getX(), DELTA);
		Assert.assertEquals(1.0, result.getY(), DELTA);

		result = algorithm.snap(new Coordinate(1, 1), 2);
		Assert.assertEquals(1.0, result.getX(), DELTA);
		Assert.assertEquals(1.0, result.getY(), DELTA);
	}

	@Test
	public void testLineString() {
		Geometry line1 = new Geometry(Geometry.LINE_STRING, 0, 0);
		line1.setCoordinates(new Coordinate[] { new Coordinate(0, 0), new Coordinate(10, 0) });
		Geometry line2 = new Geometry(Geometry.LINE_STRING, 0, 0);
		line2.setCoordinates(new Coordinate[] { new Coordinate(0, 10), new Coordinate(10, 10) });
		algorithm.setGeometries(new Geometry[] { line1, line2 });

		Coordinate result = algorithm.snap(new Coordinate(1, 1), 1);
		Assert.assertEquals(1.0, result.getX(), DELTA);
		Assert.assertEquals(1.0, result.getY(), DELTA);

		result = algorithm.snap(new Coordinate(1, 1), 2);
		Assert.assertEquals(1.0, result.getX(), DELTA);
		Assert.assertEquals(1.0, result.getY(), DELTA);
	}

	@Test
	public void testLinearRing() {
		Geometry ring1 = new Geometry(Geometry.LINEAR_RING, 0, 0);
		ring1.setCoordinates(new Coordinate[] { new Coordinate(0, 0), new Coordinate(10, 0), new Coordinate(10, 10),
				new Coordinate(0, 10), new Coordinate(0, 0) });
		Geometry ring2 = new Geometry(Geometry.LINEAR_RING, 0, 0);
		ring2.setCoordinates(new Coordinate[] { new Coordinate(4, 4), new Coordinate(6, 4), new Coordinate(6, 6),
				new Coordinate(4, 6), new Coordinate(4, 4) });
		algorithm.setGeometries(new Geometry[] { ring1, ring2 });

		Coordinate result = algorithm.snap(new Coordinate(2, 1), 1);
		Assert.assertEquals(2.0, result.getX(), DELTA);
		Assert.assertEquals(0.0, result.getY(), DELTA);

		result = algorithm.snap(new Coordinate(2, 1), 2);
		Assert.assertEquals(2.0, result.getX(), DELTA);
		Assert.assertEquals(0.0, result.getY(), DELTA);

		result = algorithm.snap(new Coordinate(5, 4.5), 2);
		Assert.assertEquals(5.0, result.getX(), DELTA);
		Assert.assertEquals(4.0, result.getY(), DELTA);

		result = algorithm.snap(new Coordinate(1, 11), 2);
		Assert.assertEquals(1.0, result.getX(), DELTA);
		Assert.assertEquals(11.0, result.getY(), DELTA);
	}

	@Test
	public void testPolygon() {
		Geometry ring1 = new Geometry(Geometry.LINEAR_RING, 0, 0);
		ring1.setCoordinates(new Coordinate[] { new Coordinate(0, 0), new Coordinate(10, 0), new Coordinate(10, 10),
				new Coordinate(0, 10), new Coordinate(0, 0) });
		Geometry ring2 = new Geometry(Geometry.LINEAR_RING, 0, 0);
		ring2.setCoordinates(new Coordinate[] { new Coordinate(4, 4), new Coordinate(6, 4), new Coordinate(6, 6),
				new Coordinate(4, 6), new Coordinate(4, 4) });
		Geometry polygon = new Geometry(Geometry.POLYGON, 0, 0);
		polygon.setGeometries(new Geometry[] { ring1, ring2 });
		algorithm.setGeometries(new Geometry[] { polygon });

		Coordinate result = algorithm.snap(new Coordinate(2, 1), 1);
		Assert.assertEquals(2.0, result.getX(), DELTA);
		Assert.assertEquals(0.0, result.getY(), DELTA);

		result = algorithm.snap(new Coordinate(2, 1), 2);
		Assert.assertEquals(2.0, result.getX(), DELTA);
		Assert.assertEquals(0.0, result.getY(), DELTA);

		result = algorithm.snap(new Coordinate(5, 4.5), 2);
		Assert.assertEquals(5.0, result.getX(), DELTA);
		Assert.assertEquals(4.5, result.getY(), DELTA);

		result = algorithm.snap(new Coordinate(1, 11), 2);
		Assert.assertEquals(1.0, result.getX(), DELTA);
		Assert.assertEquals(11.0, result.getY(), DELTA);
	}
}