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
 * Tests the {@link Matrix} methods.
 * 
 * @author Pieter De Graef
 */
public class MatrixTest {

	private static final double DELTA = 1.e-10;

	private static final double XX = 1.0;

	private static final double XY = 2.0;

	private static final double YX = 3.0;

	private static final double YY = 4.0;

	private static final double DX = 5.0;

	private static final double DY = 6.0;

	@Test
	public void testConstructors() {
		Matrix matrix = new Matrix();
		Assert.assertEquals(0.0, matrix.getXx(), DELTA);
		Assert.assertEquals(0.0, matrix.getXy(), DELTA);
		Assert.assertEquals(0.0, matrix.getYx(), DELTA);
		Assert.assertEquals(0.0, matrix.getYy(), DELTA);
		Assert.assertEquals(0.0, matrix.getDx(), DELTA);
		Assert.assertEquals(0.0, matrix.getDy(), DELTA);

		matrix = new Matrix(XX, XY, YX, YY, DX, DY);
		Assert.assertEquals(XX, matrix.getXx(), DELTA);
		Assert.assertEquals(XY, matrix.getXy(), DELTA);
		Assert.assertEquals(YX, matrix.getYx(), DELTA);
		Assert.assertEquals(YY, matrix.getYy(), DELTA);
		Assert.assertEquals(DX, matrix.getDx(), DELTA);
		Assert.assertEquals(DY, matrix.getDy(), DELTA);
	}

	@Test
	public void testIdentityMatrix() {
		Matrix matrix = Matrix.IDENTITY;
		Assert.assertEquals(1.0, matrix.getXx(), DELTA);
		Assert.assertEquals(0.0, matrix.getXy(), DELTA);
		Assert.assertEquals(0.0, matrix.getYx(), DELTA);
		Assert.assertEquals(1.0, matrix.getYy(), DELTA);
		Assert.assertEquals(0.0, matrix.getDx(), DELTA);
		Assert.assertEquals(0.0, matrix.getDy(), DELTA);
	}

	@Test
	public void testToString() {
		Matrix matrix = new Matrix(XX, XY, YX, YY, DX, DY);
		Assert.assertEquals("[" + XX + ", " + XY + ", " + YX + ", " + YY + ", " + DX + ", " + DY + "]",
				matrix.toString());
	}

	@Test
	public void testHashCode() {
		// TODO implement me...
	}

	@Test
	public void testEquals() {
		Matrix matrix = new Matrix(XX, XY, YX, YY, DX, DY);
		Assert.assertFalse(matrix.equals(new Matrix(0, XY, YX, YY, DX, DY)));
		Assert.assertFalse(matrix.equals(new Matrix(XX, 0, YX, YY, DX, DY)));
		Assert.assertFalse(matrix.equals(new Matrix(XX, XY, 0, YY, DX, DY)));
		Assert.assertFalse(matrix.equals(new Matrix(XX, XY, YX, 0, DX, DY)));
		Assert.assertFalse(matrix.equals(new Matrix(XX, XY, YX, YY, 0, DY)));
		Assert.assertFalse(matrix.equals(new Matrix(XX, XY, YX, YY, DX, 0)));
		Assert.assertTrue(matrix.equals(new Matrix(XX, XY, YX, YY, DX, DY)));
	}
}