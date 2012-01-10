/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
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
}