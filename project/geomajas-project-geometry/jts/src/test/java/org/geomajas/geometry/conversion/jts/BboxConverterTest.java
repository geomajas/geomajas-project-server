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

import org.geomajas.geometry.Bbox;
import org.junit.Assert;
import org.junit.Test;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Tests for the GeometryConverterService, specifically testing the {@link Bbox} conversions.
 * 
 * @author Pieter De Graef
 */
public class BboxConverterTest {

	private static final double DELTA = .000001;

	@Test
	public void testToJts() throws JtsConversionException {
		Bbox bbox = new Bbox(10.0, 20.0, 30.0, 40.0);
		Envelope envelope = GeometryConverterService.toJts(bbox);
		Assert.assertEquals(10.0, envelope.getMinX(), DELTA);
		Assert.assertEquals(20.0, envelope.getMinY(), DELTA);
		Assert.assertEquals(40.0, envelope.getMaxX(), DELTA);
		Assert.assertEquals(60.0, envelope.getMaxY(), DELTA);

		try {
			GeometryConverterService.toJts((Bbox) null);
			Assert.fail();
		} catch (JtsConversionException e) {
			// As expected...
		}
	}

	@Test
	public void testFromJts() throws JtsConversionException {
		Envelope envelope = new Envelope(10.0, 20.0, 30.0, 40.0);
		Bbox bbox = GeometryConverterService.fromJts(envelope);
		Assert.assertEquals(10.0, bbox.getX(), DELTA);
		Assert.assertEquals(30.0, bbox.getY(), DELTA);
		Assert.assertEquals(20.0, bbox.getMaxX(), DELTA);
		Assert.assertEquals(40.0, bbox.getMaxY(), DELTA);

		try {
			GeometryConverterService.fromJts((Envelope) null);
			Assert.fail();
		} catch (JtsConversionException e) {
			// As expected...
		}
	}
}