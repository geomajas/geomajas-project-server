/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.geometry.service;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for the {@link MathService}.
 * 
 * @author Pieter De Graef
 */
public class MathServiceTest {

	private static final double DELTA = 0.0001;

	private final Coordinate c1 = new Coordinate(0, 0);

	private final Coordinate c2 = new Coordinate(10, 20);

	private final Coordinate c3 = new Coordinate(-10, -20);

	private final Coordinate c4 = new Coordinate(-20, 10);

	private final Coordinate c5 = new Coordinate(20, 10);

	@Test
	public void testDistanceTwoPoint() {
		Assert.assertEquals(40.0, MathService.distance(c4, c5), DELTA);
		Assert.assertEquals(5.0, MathService.distance(new Coordinate(3, 4), new Coordinate(0, 0)), DELTA);
		try {
			MathService.distance(null, null);
			Assert.fail();
		} catch (NullPointerException npe) {
			// Test passed.
		}
	}

	@Test
	public void testDistancePointLine() {
		Assert.assertEquals(0.0, MathService.distance(c2, c3, c1), DELTA);
		Assert.assertEquals(10.0, MathService.distance(c4, c5, c1), DELTA);
		Assert.assertEquals(Math.sqrt(15 * 15 + 15 * 15), MathService.distance(c2, c5, c1), DELTA);
		try {
			MathService.distance(c1, c2, null);
			Assert.fail();
		} catch (NullPointerException npe) {
			// Test passed.
		}
	}

	@Test
	public void testIntersects() {
		Assert.assertFalse(MathService.intersectsLineSegment(c1, c2, c3, c4));
		Assert.assertTrue(MathService.intersectsLineSegment(c2, c3, c4, c5));
		try {
			MathService.intersectsLineSegment(c1, c2, null, c4);
			Assert.fail();
		} catch (NullPointerException npe) {
			// Test passed.
		}
	}

	@Test
	public void testLineIntersection() {
		// Intersection is between the coordinates:
		Coordinate intersection = MathService.lineIntersection(c2, c3, c4, c5);
		Assert.assertEquals(5.0, intersection.getX(), DELTA);
		Assert.assertEquals(10.0, intersection.getY(), DELTA);

		// Intersection is on the line extending from the coordinates:
		intersection = MathService.lineIntersection(c2, c3, new Coordinate(-10, 20), new Coordinate(5, 10));
		Assert.assertEquals(5.0, intersection.getX(), DELTA);
		Assert.assertEquals(10.0, intersection.getY(), DELTA);

		intersection = MathService.lineIntersection(c2, c3, new Coordinate(0, 20), new Coordinate(-20, -20));
		Assert.assertTrue(intersection.getX() == Double.NEGATIVE_INFINITY
				|| intersection.getX() == Double.POSITIVE_INFINITY);
		Assert.assertTrue(intersection.getY() == Double.NEGATIVE_INFINITY
				|| intersection.getY() == Double.POSITIVE_INFINITY);

		Assert.assertNull(MathService.lineSegmentIntersection(c1, c2, c3, c4));
		try {
			MathService.lineSegmentIntersection(c1, null, c3, c4);
			Assert.fail();
		} catch (NullPointerException npe) {
			// Test passed.
		}
	}

	@Test
	public void testLineSegmentIntersection() {
		// Test the normal case:
		Coordinate intersection = MathService.lineSegmentIntersection(c2, c3, c4, c5);
		Assert.assertEquals(5.0, intersection.getX(), DELTA);
		Assert.assertEquals(10.0, intersection.getY(), DELTA);

		// Lines have equal distance:
		intersection = MathService.lineSegmentIntersection(c2, c3, new Coordinate(0, 20), new Coordinate(-20, -20));
		Assert.assertNull(intersection);

		Assert.assertNull(MathService.lineSegmentIntersection(c1, c2, c3, c4));
		try {
			MathService.lineSegmentIntersection(c1, null, c3, c4);
			Assert.fail();
		} catch (NullPointerException npe) {
			// Test passed.
		}
	}

	@Test
	public void testNearest() {
		Coordinate nearest = MathService.nearest(c2, c3, new Coordinate(-20, 10));
		Assert.assertEquals(0.0, nearest.getX(), DELTA);
		Assert.assertEquals(0.0, nearest.getY(), DELTA);

		nearest = MathService.nearest(c2, c3, new Coordinate(100, 10));
		Assert.assertEquals(c2.getX(), nearest.getX(), DELTA);
		Assert.assertEquals(c2.getY(), nearest.getY(), DELTA);

		nearest = MathService.nearest(c2, c3, new Coordinate(-100, -10));
		Assert.assertEquals(c3.getX(), nearest.getX(), DELTA);
		Assert.assertEquals(c3.getY(), nearest.getY(), DELTA);
	}

	@Test
	public void testIsWithin() {
		// Is within point? Always false.
		Geometry point = new Geometry(Geometry.POINT, 0, 0);
		point.setCoordinates(new Coordinate[] { new Coordinate(0, 0) });
		Assert.assertFalse(MathService.isWithin(point, new Coordinate(0, 0)));

		// Is within LinearRing?
		Geometry ring = new Geometry(Geometry.LINEAR_RING, 0, 0);
		ring.setCoordinates(new Coordinate[] { new Coordinate(0, 0), new Coordinate(10, 0), new Coordinate(10, 10),
				new Coordinate(0, 10), new Coordinate(0, 0) });
		Assert.assertTrue(MathService.isWithin(ring, new Coordinate(8, 1)));
		Assert.assertFalse(MathService.isWithin(ring, new Coordinate(8, 11)));
		Assert.assertFalse(MathService.isWithin(ring, new Coordinate(5, 0)));

		// Is within Polygon?
		Geometry hole = new Geometry(Geometry.LINEAR_RING, 0, 0);
		hole.setCoordinates(new Coordinate[] { new Coordinate(0, -5), new Coordinate(5, 0), new Coordinate(0, 5),
				new Coordinate(-5, 0), new Coordinate(0, -5) });
		Geometry polygon = new Geometry(Geometry.POLYGON, 0, 0);
		polygon.setGeometries(new Geometry[] { ring, hole });
		Assert.assertTrue(MathService.isWithin(polygon, new Coordinate(6, 6)));
		Assert.assertFalse(MathService.isWithin(polygon, new Coordinate(1, 2)));
		Assert.assertFalse(MathService.isWithin(polygon, new Coordinate(2.5, 2.5)));

		// Is within MultiPolygon?
		Geometry mp = new Geometry(Geometry.MULTI_POLYGON, 0, 0);
		mp.setGeometries(new Geometry[] { polygon });
		Assert.assertTrue(MathService.isWithin(mp, new Coordinate(6, 6)));
		Assert.assertFalse(MathService.isWithin(mp, new Coordinate(1, 2)));
		Assert.assertFalse(MathService.isWithin(mp, new Coordinate(2.5, 2.5)));
	}

	@Test
	public void testTouches() {
		// Touches point?
		Geometry point = new Geometry(Geometry.POINT, 0, 0);
		point.setCoordinates(new Coordinate[] { new Coordinate(0, 0) });
		Assert.assertTrue(MathService.touches(point, new Coordinate(0, 0)));

		// Touches LinearRing?
		Geometry ring = new Geometry(Geometry.LINEAR_RING, 0, 0);
		ring.setCoordinates(new Coordinate[] { new Coordinate(0, 0), new Coordinate(10, 0), new Coordinate(10, 10),
				new Coordinate(0, 10), new Coordinate(0, 0) });
		Assert.assertFalse(MathService.touches(ring, new Coordinate(8, 1)));
		Assert.assertFalse(MathService.touches(ring, new Coordinate(8, 11)));
		Assert.assertTrue(MathService.touches(ring, new Coordinate(5, 0)));

		// Touches Polygon?
		Geometry hole = new Geometry(Geometry.LINEAR_RING, 0, 0);
		hole.setCoordinates(new Coordinate[] { new Coordinate(0, -5), new Coordinate(5, 0), new Coordinate(0, 5),
				new Coordinate(-5, 0), new Coordinate(0, -5) });
		Geometry polygon = new Geometry(Geometry.POLYGON, 0, 0);
		polygon.setGeometries(new Geometry[] { ring, hole });
		Assert.assertFalse(MathService.touches(polygon, new Coordinate(6, 6)));
		Assert.assertFalse(MathService.touches(polygon, new Coordinate(1, 2)));
		Assert.assertTrue(MathService.touches(polygon, new Coordinate(2.5, 2.5)));

		// Touches MultiPolygon?
		Geometry mp = new Geometry(Geometry.MULTI_POLYGON, 0, 0);
		mp.setGeometries(new Geometry[] { polygon });
		Assert.assertFalse(MathService.touches(mp, new Coordinate(6, 6)));
		Assert.assertFalse(MathService.touches(mp, new Coordinate(1, 2)));
		Assert.assertTrue(MathService.touches(mp, new Coordinate(2.5, 2.5)));
	}
}