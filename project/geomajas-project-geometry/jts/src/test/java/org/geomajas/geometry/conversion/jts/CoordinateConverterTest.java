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

package org.geomajas.geometry.conversion.jts;

import org.junit.Assert;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Tests for the GeometryConverterService, specifically testing the {@link org.geomajas.geometry.Coordinate}
 * conversions.
 * 
 * @author Pieter De Graef
 */
public class CoordinateConverterTest {

	private static final double DELTA = .000001;

	@Test
	public void testToJts() throws JtsConversionException {
		org.geomajas.geometry.Coordinate coordinate = new org.geomajas.geometry.Coordinate(10.0, 20.0);
		Coordinate result = GeometryConverterService.toJts(coordinate);
		Assert.assertEquals(10.0, result.x, DELTA);
		Assert.assertEquals(20.0, result.y, DELTA);

		try {
			GeometryConverterService.toJts((org.geomajas.geometry.Coordinate) null);
			Assert.fail();
		} catch (JtsConversionException e) {
			// As expected...
		}
	}

	@Test
	public void testFromJts() throws JtsConversionException {
		Coordinate coordinate = new Coordinate(10.0, 20.0);
		org.geomajas.geometry.Coordinate result = GeometryConverterService.fromJts(coordinate);
		Assert.assertEquals(10.0, result.getX(), DELTA);
		Assert.assertEquals(20.0, result.getY(), DELTA);

		try {
			GeometryConverterService.fromJts((Coordinate) null);
			Assert.fail();
		} catch (JtsConversionException e) {
			// As expected...
		}
	}
}