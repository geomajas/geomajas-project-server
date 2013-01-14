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
 * Tests the {@link Bbox} methods.
 * 
 * @author Pieter De Graef
 */
public class BboxTest {

	private static final double X = 3.42;

	private static final double WIDTH = 10.0;

	private static final double Y = 34.2;

	private static final double HEIGHT = 20.0;

	@Test
	public void testConstructors() {
		Bbox bbox = new Bbox();
		Assert.assertEquals(0.0, bbox.getX());
		Assert.assertEquals(0.0, bbox.getY());
		Assert.assertEquals(0.0, bbox.getMaxX());
		Assert.assertEquals(0.0, bbox.getMaxY());
		Assert.assertEquals(0.0, bbox.getWidth());
		Assert.assertEquals(0.0, bbox.getHeight());

		bbox = new Bbox(X, Y, WIDTH, HEIGHT);
		Assert.assertEquals(X, bbox.getX());
		Assert.assertEquals(Y, bbox.getY());
		Assert.assertEquals(X + WIDTH, bbox.getMaxX());
		Assert.assertEquals(Y + HEIGHT, bbox.getMaxY());
		Assert.assertEquals(WIDTH, bbox.getWidth());
		Assert.assertEquals(HEIGHT, bbox.getHeight());
	}

	@Test
	public void testWidth() {
		Bbox bbox = new Bbox(X, Y, 0, 0);
		bbox.setWidth(WIDTH);
		Assert.assertEquals(X, bbox.getX());
		Assert.assertEquals(X + WIDTH, bbox.getMaxX());
		Assert.assertEquals(WIDTH, bbox.getWidth());
		Assert.assertEquals(Y, bbox.getY());

		bbox.setWidth(-WIDTH);
		Assert.assertEquals(X - WIDTH, bbox.getX());
		Assert.assertEquals(X, bbox.getMaxX());
		Assert.assertEquals(WIDTH, bbox.getWidth());
		Assert.assertEquals(Y, bbox.getY());
	}

	@Test
	public void testHeight() {
		Bbox bbox = new Bbox(X, Y, 0, 0);
		bbox.setHeight(HEIGHT);
		Assert.assertEquals(Y, bbox.getY());
		Assert.assertEquals(Y + HEIGHT, bbox.getMaxY());
		Assert.assertEquals(HEIGHT, bbox.getHeight());
		Assert.assertEquals(X, bbox.getX());

		bbox.setHeight(-HEIGHT);
		Assert.assertEquals(Y - HEIGHT, bbox.getY());
		Assert.assertEquals(Y, bbox.getMaxY());
		Assert.assertEquals(HEIGHT, bbox.getHeight());
		Assert.assertEquals(X, bbox.getX());
	}

	@Test
	public void testMaxX() {
		Bbox bbox = new Bbox(X, Y, 0, 0);
		bbox.setMaxX(X + WIDTH);
		Assert.assertEquals(X, bbox.getX());
		Assert.assertEquals(X + WIDTH, bbox.getMaxX());
		Assert.assertEquals(WIDTH, bbox.getWidth());
		Assert.assertEquals(Y, bbox.getY());

		bbox.setMaxX(X - WIDTH);
		Assert.assertEquals(X - WIDTH, bbox.getX());
		Assert.assertEquals(X, bbox.getMaxX());
		Assert.assertEquals(WIDTH, bbox.getWidth());
		Assert.assertEquals(Y, bbox.getY());
	}

	@Test
	public void testMaxY() {
		Bbox bbox = new Bbox(X, Y, 0, 0);
		bbox.setMaxY(Y + HEIGHT);
		Assert.assertEquals(Y, bbox.getY());
		Assert.assertEquals(Y + HEIGHT, bbox.getMaxY());
		Assert.assertEquals(HEIGHT, bbox.getHeight());
		Assert.assertEquals(X, bbox.getX());

		bbox.setMaxY(Y - HEIGHT);
		Assert.assertEquals(Y - HEIGHT, bbox.getY());
		Assert.assertEquals(Y, bbox.getMaxY());
		Assert.assertEquals(HEIGHT, bbox.getHeight());
		Assert.assertEquals(X, bbox.getX());
	}
}