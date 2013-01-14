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

package org.geomajas.geometry;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Tests the {@link Coordinate} methods.
 * 
 * @author Pieter De Graef
 */
public class CoordinateTest {

	private static final double DELTA = 1.e-10;

	private static final double X = 3.42;

	private static final double Y = 34.2;

	@Test
	public void testConstructors() {
		Coordinate coordinate = new Coordinate();
		Assert.assertEquals(0.0, coordinate.getX(), DELTA);
		Assert.assertEquals(0.0, coordinate.getY(), DELTA);

		coordinate = new Coordinate(X, Y);
		Assert.assertEquals(X, coordinate.getX(), DELTA);
		Assert.assertEquals(Y, coordinate.getY(), DELTA);

		Coordinate coordinate2 = new Coordinate(coordinate);
		Assert.assertEquals(X, coordinate2.getX(), DELTA);
		Assert.assertEquals(Y, coordinate2.getY(), DELTA);
	}

	@Test
	public void testSetCoordinate() {
		Coordinate coordinate = new Coordinate();
		Assert.assertEquals(0.0, coordinate.getX(), DELTA);
		Assert.assertEquals(0.0, coordinate.getY(), DELTA);

		coordinate.setCoordinate(new Coordinate(X, Y));
		Assert.assertEquals(X, coordinate.getX(), DELTA);
		Assert.assertEquals(Y, coordinate.getY(), DELTA);
	}

	@Test
	public void testEquals() {
		Coordinate coordinate = new Coordinate(X, Y);

		Assert.assertFalse(coordinate.equals(null));
		Assert.assertFalse(coordinate.equals(new Object()));
		Assert.assertFalse(coordinate.equals(new Coordinate(X, X)));
		Assert.assertFalse(coordinate.equals(new Coordinate(Y, Y)));
		Assert.assertFalse(coordinate.equals(new Coordinate(Y, X)));

		Assert.assertTrue(coordinate.equals(coordinate));
		Assert.assertTrue(coordinate.equals(new Coordinate(X, Y)));
	}

	@Test
	public void testEqualsDelta() {
		Coordinate coordinate = new Coordinate(X, Y);

		Assert.assertFalse(coordinate.equalsDelta(null, DELTA));
		Assert.assertFalse(coordinate.equalsDelta(new Coordinate(X, X), DELTA));
		Assert.assertFalse(coordinate.equalsDelta(new Coordinate(Y, Y), DELTA));
		Assert.assertFalse(coordinate.equalsDelta(new Coordinate(Y, X), DELTA));

		Assert.assertTrue(coordinate.equalsDelta(coordinate, DELTA));
		Assert.assertTrue(coordinate.equalsDelta(new Coordinate(X, Y), DELTA));

		Assert.assertTrue(coordinate.equalsDelta(new Coordinate(X + 1.e-11, Y + 1.e-11), DELTA));
	}

	@Test
	public void testCompareTo() {
		Coordinate coordinate = new Coordinate(X, Y);

		try {
			coordinate.compareTo(null);
			Assert.fail();
		} catch (NullPointerException e) {
			// As expected....
		}
		Assert.assertEquals(0, coordinate.compareTo(new Coordinate(X, Y)));
		Assert.assertEquals(1, coordinate.compareTo(new Coordinate(X - 1, Y)));
		Assert.assertEquals(-1, coordinate.compareTo(new Coordinate(X + 1, Y)));
		Assert.assertEquals(1, coordinate.compareTo(new Coordinate(X, Y - 1)));
		Assert.assertEquals(-1, coordinate.compareTo(new Coordinate(X, Y + 1)));
		Assert.assertEquals(1, coordinate.compareTo(new Coordinate(X - 1, Y + 1)));
	}

	@Test
	public void testToString() {
		Coordinate coordinate = new Coordinate(X, Y);
		Assert.assertEquals("(" + X + ", " + Y + ")", coordinate.toString());
	}

	@Test
	public void testClone() {
		Coordinate coordinate = new Coordinate(X, Y);
		Coordinate clone = (Coordinate) coordinate.clone();

		Assert.assertTrue(coordinate.equals(clone));
		Assert.assertFalse(coordinate == clone);
	}

	@Test
	public void testDistance() {
		Coordinate c1 = new Coordinate();
		Coordinate c2 = new Coordinate(X, 0);
		Coordinate c3 = new Coordinate(0, Y);
		Coordinate c4 = new Coordinate(3, 4);

		Assert.assertEquals(0, c1.distance(c1), DELTA);
		Assert.assertEquals(X, c1.distance(c2), DELTA);
		Assert.assertEquals(Y, c1.distance(c3), DELTA);
		Assert.assertEquals(5, c1.distance(c4), DELTA);
	}

	@Test
	public void testHashCode() {
		Coordinate coordinate = new Coordinate(X, Y);
		Coordinate clone = (Coordinate) coordinate.clone();

		Assert.assertTrue(coordinate.hashCode() == clone.hashCode());
	}

	@Test
	public void testSetters() {
		Coordinate coordinate = new Coordinate();
		Assert.assertEquals(0.0, coordinate.getX(), DELTA);
		Assert.assertEquals(0.0, coordinate.getY(), DELTA);

		coordinate.setX(X);
		Assert.assertEquals(X, coordinate.getX(), DELTA);
		Assert.assertEquals(0.0, coordinate.getY(), DELTA);

		coordinate.setY(Y);
		Assert.assertEquals(X, coordinate.getX(), DELTA);
		Assert.assertEquals(Y, coordinate.getY(), DELTA);
	}
}