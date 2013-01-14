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

import junit.framework.Assert;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link BboxService} methods.
 * 
 * @author Pieter De Graef
 */
public class BboxServiceTest {

	private Bbox empty = new Bbox(0, 0, 0, 0);

	private Bbox origin = new Bbox(0, 0, 10, 10);

	private Bbox movedEmpty = new Bbox(-10, -10, 0, 0);

	private Bbox movedEmpty2 = new Bbox(5, 5, 0, 0);

	private Bbox normal = new Bbox(-5, -5, 20, 20);

	@Before
	public void setUp() {
		empty = new Bbox(0, 0, 0, 0);
		origin = new Bbox(0, 0, 10, 10);
		movedEmpty = new Bbox(-10, -10, 0, 0);
		movedEmpty2 = new Bbox(5, 5, 0, 0);
		normal = new Bbox(-5, -5, 20, 20);
	}

	@Test
	public void testEquals() {
		Assert.assertTrue(BboxService.equals(empty, empty, 0.0001));
		Assert.assertFalse(BboxService.equals(normal, empty, 0.0001));
	}

	@Test
	public void testGetCenterPoint() {
		Coordinate c = BboxService.getCenterPoint(empty);
		Assert.assertEquals(c.getX(), 0.0);
		Assert.assertEquals(c.getY(), 0.0);

		c = BboxService.getCenterPoint(origin);
		Assert.assertEquals(c.getX(), 5.0);
		Assert.assertEquals(c.getY(), 5.0);

		c = BboxService.getCenterPoint(movedEmpty);
		Assert.assertEquals(c.getX(), -10.0);
		Assert.assertEquals(c.getY(), -10.0);

		c = BboxService.getCenterPoint(normal);
		Assert.assertEquals(c.getX(), 5.0);
		Assert.assertEquals(c.getY(), 5.0);
	}

	@Test
	public void testUnion() {
		// Result should equal empty:
		Bbox union = BboxService.union(empty, empty);
		Assert.assertTrue(BboxService.equals(union, empty, 0.0001));

		// Result should equal origin:
		union = BboxService.union(empty, origin);
		Assert.assertTrue(BboxService.equals(union, origin, 0.0001));

		// Result should equal origin:
		union = BboxService.union(origin, empty);
		Assert.assertTrue(BboxService.equals(union, origin, 0.0001));

		// Result should equal movedEmpty:
		union = BboxService.union(movedEmpty, empty);
		Assert.assertTrue(BboxService.equals(union, movedEmpty, 0.0001));

		// Result should equal movedEmpty:
		union = BboxService.union(empty, movedEmpty);
		Assert.assertTrue(BboxService.equals(union, movedEmpty, 0.0001));

		union = BboxService.union(movedEmpty, origin);
		Assert.assertEquals(union.getX(), -10.0);
		Assert.assertEquals(union.getWidth(), 20.0);
		Assert.assertEquals(union.getY(), -10.0);
		Assert.assertEquals(union.getHeight(), 20.0);

		union = BboxService.union(origin, movedEmpty);
		Assert.assertEquals(union.getX(), -10.0);
		Assert.assertEquals(union.getWidth(), 20.0);
		Assert.assertEquals(union.getY(), -10.0);
		Assert.assertEquals(union.getHeight(), 20.0);

		union = BboxService.union(movedEmpty, movedEmpty2);
		Assert.assertEquals(union.getX(), -10.0);
		Assert.assertEquals(union.getWidth(), 15.0);
		Assert.assertEquals(union.getY(), -10.0);
		Assert.assertEquals(union.getHeight(), 15.0);

		union = BboxService.union(movedEmpty, normal);
		Assert.assertEquals(union.getX(), -10.0);
		Assert.assertEquals(union.getWidth(), 25.0);
		Assert.assertEquals(union.getY(), -10.0);
		Assert.assertEquals(union.getHeight(), 25.0);
	}

	@Test
	public void testBuffer() {
		Bbox buffer = BboxService.buffer(empty, 10);
		Assert.assertEquals(buffer.getX(), -10.0);
		Assert.assertEquals(buffer.getWidth(), 20.0);
		Assert.assertEquals(buffer.getY(), -10.0);
		Assert.assertEquals(buffer.getHeight(), 20.0);
	}

	@Test
	public void testIntersection() {
		Bbox intersection = BboxService.intersection(empty, empty);
		Assert.assertTrue(BboxService.equals(intersection, empty, 0.0001));

		intersection = BboxService.intersection(empty, origin);
		Assert.assertTrue(BboxService.equals(intersection, empty, 0.0001));

		intersection = BboxService.intersection(origin, empty);
		Assert.assertTrue(BboxService.equals(intersection, empty, 0.0001));

		intersection = BboxService.intersection(empty, movedEmpty);
		Assert.assertNull(intersection);

		intersection = BboxService.intersection(movedEmpty, empty);
		Assert.assertNull(intersection);

		intersection = BboxService.intersection(normal, origin);
		Assert.assertEquals(intersection.getX(), 0.0);
		Assert.assertEquals(intersection.getWidth(), 10.0);
		Assert.assertEquals(intersection.getY(), 0.0);
		Assert.assertEquals(intersection.getHeight(), 10.0);
	}

	@Test
	public void testIntersects() {
		Assert.assertTrue(BboxService.intersects(empty, empty));
		Assert.assertTrue(BboxService.intersects(empty, origin));
		Assert.assertTrue(BboxService.intersects(origin, empty));
		Assert.assertFalse(BboxService.intersects(empty, movedEmpty));
		Assert.assertFalse(BboxService.intersects(movedEmpty, empty));
		Assert.assertTrue(BboxService.intersects(normal, origin));
	}

	@Test
	public void testContains() {
		Assert.assertTrue(BboxService.contains(normal, empty));
		Assert.assertTrue(BboxService.contains(normal, origin));
		Assert.assertTrue(BboxService.contains(normal, normal));
		Assert.assertFalse(BboxService.contains(origin, normal));
	}

	@Test
	public void testContainsCoordinate() {
		// Normal cases outside:
		Assert.assertFalse(BboxService.contains(origin, new Coordinate(-1, 5)));
		Assert.assertFalse(BboxService.contains(origin, new Coordinate(5, -1)));
		Assert.assertFalse(BboxService.contains(origin, new Coordinate(11, 5)));
		Assert.assertFalse(BboxService.contains(origin, new Coordinate(5, 11)));

		// Corner cases outside:
		Assert.assertFalse(BboxService.contains(empty, new Coordinate(0, 0)));
		Assert.assertFalse(BboxService.contains(origin, new Coordinate(0, 5)));
		Assert.assertFalse(BboxService.contains(origin, new Coordinate(5, 0)));
		Assert.assertFalse(BboxService.contains(origin, new Coordinate(10, 5)));
		Assert.assertFalse(BboxService.contains(origin, new Coordinate(5, 10)));

		// Inside:
		Assert.assertTrue(BboxService.contains(origin, new Coordinate(5, 5)));
	}

	@Test
	public void testScale() {
		Bbox scaled = BboxService.scale(normal, 2);
		Assert.assertEquals(BboxService.getCenterPoint(normal).getX(), BboxService.getCenterPoint(scaled).getX());
		Assert.assertEquals(BboxService.getCenterPoint(normal).getY(), BboxService.getCenterPoint(scaled).getY());
		Assert.assertEquals(scaled.getX(), -15.0);
		Assert.assertEquals(scaled.getWidth(), 40.0);
		Assert.assertEquals(scaled.getY(), -15.0);
		Assert.assertEquals(scaled.getHeight(), 40.0);
	}

	@Test
	public void testIsEmpty() {
		Assert.assertTrue(BboxService.isEmpty(empty));
		Assert.assertFalse(BboxService.isEmpty(normal));
	}
}