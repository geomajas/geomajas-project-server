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
 * Tests the {@link Geometry} methods.
 * 
 * @author Pieter De Graef
 */
public class GeometryTest {

	@Test
	public void testConstructors() {
		Geometry geometry = new Geometry();
		Assert.assertEquals(0, geometry.getSrid());
		Assert.assertEquals(0, geometry.getPrecision());
		Assert.assertNull(geometry.getGeometryType());
		Assert.assertNull(geometry.getCoordinates());
		Assert.assertNull(geometry.getGeometries());

		geometry = new Geometry(Geometry.POINT, 1, 2);
		Assert.assertEquals(1, geometry.getSrid());
		Assert.assertEquals(2, geometry.getPrecision());
		Assert.assertEquals(Geometry.POINT, geometry.getGeometryType());
		Assert.assertNull(geometry.getCoordinates());
		Assert.assertNull(geometry.getGeometries());
	}

	@Test
	public void testSrid() {
		Geometry geometry = new Geometry();
		Assert.assertEquals(0, geometry.getSrid());

		geometry.setSrid(1);
		Assert.assertEquals(1, geometry.getSrid());
	}

	@Test
	public void testPrecision() {
		Geometry geometry = new Geometry();
		Assert.assertEquals(0, geometry.getPrecision());

		geometry.setPrecision(1);
		Assert.assertEquals(1, geometry.getPrecision());
	}

	@Test
	public void testGeometryType() {
		Geometry geometry = new Geometry();
		Assert.assertNull(geometry.getGeometryType());

		geometry.setGeometryType(Geometry.LINE_STRING);
		Assert.assertEquals(Geometry.LINE_STRING, geometry.getGeometryType());
	}

	@Test
	public void testCoordinates() {
		Geometry geometry = new Geometry();
		Assert.assertNull(geometry.getCoordinates());

		Coordinate coordinate = new Coordinate(3, 42);
		geometry.setCoordinates(new Coordinate[] { coordinate });
		Assert.assertEquals(coordinate, geometry.getCoordinates()[0]);
	}

	@Test
	public void testGeometries() {
		Geometry geometry = new Geometry();
		Assert.assertNull(geometry.getCoordinates());

		Geometry other = new Geometry(Geometry.POLYGON, 3, 4);
		geometry.setGeometries(new Geometry[] { other });
		Assert.assertEquals(other, geometry.getGeometries()[0]);
	}

	@Test
	public void testClonePoint() {
		Geometry geometry = new Geometry(Geometry.POINT, 1, 2);
		geometry.setCoordinates(new Coordinate[] { new Coordinate(3, 42) });

		Geometry clone = (Geometry) geometry.clone();
		Assert.assertEquals(geometry.getGeometryType(), clone.getGeometryType());
		Assert.assertEquals(geometry.getSrid(), clone.getSrid());
		Assert.assertEquals(geometry.getPrecision(), clone.getPrecision());
		Assert.assertNull(clone.getGeometries());
		Assert.assertEquals(geometry.getCoordinates().length, clone.getCoordinates().length);
		Assert.assertEquals(geometry.getCoordinates()[0], clone.getCoordinates()[0]);
	}

	@Test
	public void testCloneLineString() {
		Geometry geometry = new Geometry(Geometry.LINE_STRING, 1, 2);
		geometry.setCoordinates(new Coordinate[] { new Coordinate(3, 42), new Coordinate(6, 84) });

		Geometry clone = (Geometry) geometry.clone();
		Assert.assertEquals(geometry.getGeometryType(), clone.getGeometryType());
		Assert.assertEquals(geometry.getSrid(), clone.getSrid());
		Assert.assertEquals(geometry.getPrecision(), clone.getPrecision());
		Assert.assertNull(clone.getGeometries());
		Assert.assertEquals(geometry.getCoordinates().length, clone.getCoordinates().length);
		Assert.assertEquals(geometry.getCoordinates()[0], clone.getCoordinates()[0]);
		Assert.assertEquals(geometry.getCoordinates()[1], clone.getCoordinates()[1]);
	}

	@Test
	public void testClonePolygon() {
		Geometry geometry = new Geometry(Geometry.POLYGON, 1, 2);
		Geometry shell = new Geometry(Geometry.LINEAR_RING, 3, 4);
		shell.setCoordinates(new Coordinate[] { new Coordinate(3, 42), new Coordinate(6, 84), new Coordinate(3, 42) });
		Geometry hole = new Geometry(Geometry.LINEAR_RING, 5, 6);
		hole.setCoordinates(new Coordinate[] { new Coordinate(1, 11), new Coordinate(2, 22), new Coordinate(1, 11) });
		geometry.setGeometries(new Geometry[] { shell, hole });

		// Check top level:
		Geometry clone = (Geometry) geometry.clone();
		Assert.assertEquals(geometry.getGeometryType(), clone.getGeometryType());
		Assert.assertEquals(geometry.getSrid(), clone.getSrid());
		Assert.assertEquals(geometry.getPrecision(), clone.getPrecision());
		Assert.assertNull(clone.getCoordinates());

		// Check shell:
		Geometry shellClone = clone.getGeometries()[0];
		Assert.assertEquals(shell.getGeometryType(), shellClone.getGeometryType());
		Assert.assertEquals(shell.getSrid(), shellClone.getSrid());
		Assert.assertEquals(shell.getPrecision(), shellClone.getPrecision());
		Assert.assertNull(shellClone.getGeometries());
		Assert.assertEquals(shell.getCoordinates().length, shellClone.getCoordinates().length);
		for (int i = 0; i < shell.getCoordinates().length; i++) {
			Assert.assertEquals(shell.getCoordinates()[i], shellClone.getCoordinates()[i]);
		}

		// Check hole:
		Geometry holeClone = clone.getGeometries()[1];
		Assert.assertEquals(hole.getGeometryType(), holeClone.getGeometryType());
		Assert.assertEquals(hole.getSrid(), holeClone.getSrid());
		Assert.assertEquals(hole.getPrecision(), holeClone.getPrecision());
		Assert.assertNull(holeClone.getGeometries());
		Assert.assertEquals(hole.getCoordinates().length, holeClone.getCoordinates().length);
		for (int i = 0; i < hole.getCoordinates().length; i++) {
			Assert.assertEquals(hole.getCoordinates()[i], holeClone.getCoordinates()[i]);
		}
	}

	@Test
	public void testCloneMultiPolygon() {
		Geometry polygon1 = new Geometry(Geometry.POLYGON, 1, 2);
		Geometry shell1 = new Geometry(Geometry.LINEAR_RING, 3, 4);
		shell1.setCoordinates(new Coordinate[] { new Coordinate(3, 42), new Coordinate(6, 84), new Coordinate(3, 42) });
		Geometry hole1 = new Geometry(Geometry.LINEAR_RING, 5, 6);
		hole1.setCoordinates(new Coordinate[] { new Coordinate(1, 11), new Coordinate(2, 22), new Coordinate(1, 11) });
		polygon1.setGeometries(new Geometry[] { shell1, hole1 });

		Geometry polygon2 = new Geometry(Geometry.POLYGON, 7, 8);
		Geometry shell2 = new Geometry(Geometry.LINEAR_RING, 9, 10);
		shell2.setCoordinates(new Coordinate[] { new Coordinate(3, 33), new Coordinate(4, 44), new Coordinate(3, 33) });
		Geometry hole2 = new Geometry(Geometry.LINEAR_RING, 11, 12);
		hole2.setCoordinates(new Coordinate[] { new Coordinate(5, 55), new Coordinate(6, 66), new Coordinate(7, 77) });
		polygon2.setGeometries(new Geometry[] { shell2, hole2 });

		Geometry geometry = new Geometry(Geometry.MULTI_POLYGON, 13, 14);
		geometry.setGeometries(new Geometry[] { polygon1, polygon2 });

		// Check top level:
		Geometry clone = (Geometry) geometry.clone();
		Assert.assertEquals(geometry.getGeometryType(), clone.getGeometryType());
		Assert.assertEquals(geometry.getSrid(), clone.getSrid());
		Assert.assertEquals(geometry.getPrecision(), clone.getPrecision());
		Assert.assertNull(clone.getCoordinates());

		// Check Polygon1:
		Geometry poly1Clone = clone.getGeometries()[0];
		Assert.assertEquals(polygon1.getGeometryType(), poly1Clone.getGeometryType());
		Assert.assertEquals(polygon1.getSrid(), poly1Clone.getSrid());
		Assert.assertEquals(polygon1.getPrecision(), poly1Clone.getPrecision());
		Assert.assertNull(poly1Clone.getCoordinates());

		// Check Polygon2:
		Geometry poly2Clone = clone.getGeometries()[1];
		Assert.assertEquals(polygon2.getGeometryType(), poly2Clone.getGeometryType());
		Assert.assertEquals(polygon2.getSrid(), poly2Clone.getSrid());
		Assert.assertEquals(polygon2.getPrecision(), poly2Clone.getPrecision());
		Assert.assertNull(poly2Clone.getCoordinates());

		// Check shell1:
		Geometry shell1Clone = clone.getGeometries()[0].getGeometries()[0];
		Assert.assertEquals(shell1.getGeometryType(), shell1Clone.getGeometryType());
		Assert.assertEquals(shell1.getSrid(), shell1Clone.getSrid());
		Assert.assertEquals(shell1.getPrecision(), shell1Clone.getPrecision());
		Assert.assertNull(shell1Clone.getGeometries());
		Assert.assertEquals(shell1.getCoordinates().length, shell1Clone.getCoordinates().length);
		for (int i = 0; i < shell1.getCoordinates().length; i++) {
			Assert.assertEquals(shell1.getCoordinates()[i], shell1Clone.getCoordinates()[i]);
		}

		// Check hole1:
		Geometry hole1Clone = clone.getGeometries()[0].getGeometries()[1];
		Assert.assertEquals(hole1.getGeometryType(), hole1Clone.getGeometryType());
		Assert.assertEquals(hole1.getSrid(), hole1Clone.getSrid());
		Assert.assertEquals(hole1.getPrecision(), hole1Clone.getPrecision());
		Assert.assertNull(hole1Clone.getGeometries());
		Assert.assertEquals(hole1.getCoordinates().length, hole1Clone.getCoordinates().length);
		for (int i = 0; i < hole1.getCoordinates().length; i++) {
			Assert.assertEquals(hole1.getCoordinates()[i], hole1Clone.getCoordinates()[i]);
		}

		// Check shell2:
		Geometry shell2Clone = clone.getGeometries()[1].getGeometries()[0];
		Assert.assertEquals(shell2.getGeometryType(), shell2Clone.getGeometryType());
		Assert.assertEquals(shell2.getSrid(), shell2Clone.getSrid());
		Assert.assertEquals(shell2.getPrecision(), shell2Clone.getPrecision());
		Assert.assertNull(shell2Clone.getGeometries());
		Assert.assertEquals(shell2.getCoordinates().length, shell2Clone.getCoordinates().length);
		for (int i = 0; i < shell2.getCoordinates().length; i++) {
			Assert.assertEquals(shell2.getCoordinates()[i], shell2Clone.getCoordinates()[i]);
		}

		// Check hole1:
		Geometry hole2Clone = clone.getGeometries()[1].getGeometries()[1];
		Assert.assertEquals(hole2.getGeometryType(), hole2Clone.getGeometryType());
		Assert.assertEquals(hole2.getSrid(), hole2Clone.getSrid());
		Assert.assertEquals(hole2.getPrecision(), hole2Clone.getPrecision());
		Assert.assertNull(hole2Clone.getGeometries());
		Assert.assertEquals(hole2.getCoordinates().length, hole2Clone.getCoordinates().length);
		for (int i = 0; i < hole2.getCoordinates().length; i++) {
			Assert.assertEquals(hole2.getCoordinates()[i], hole2Clone.getCoordinates()[i]);
		}
	}
}