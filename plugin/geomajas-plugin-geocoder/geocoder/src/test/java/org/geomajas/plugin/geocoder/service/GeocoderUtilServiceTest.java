/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.plugin.geocoder.service;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import junit.framework.Assert;
import org.geomajas.service.GeoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test for GeocoderUtilService implementation.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/completeContext.xml"})
public class GeocoderUtilServiceTest {

	private static final double DELTA = 1e-20;

	@Autowired
	private GeoService geoService;

	@Autowired
	private GeocoderUtilService geocoderUtilService;

	@Test
	public void transformEnvelopeTest() throws Exception {
		CoordinateReferenceSystem source = geoService.getCrs("EPSG:900913");
		CoordinateReferenceSystem target = geoService.getCrs("EPSG:4326");
		Envelope envelope = new Envelope(10, 20, 30, 40);
		Envelope transformed = geocoderUtilService.transform(envelope, source, target);
		Assert.assertEquals(8.983152841195215E-5, transformed.getMinX(), DELTA);
		Assert.assertEquals(2.6949458522981454E-4, transformed.getMinY(), DELTA);
		Assert.assertEquals(1.796630568239043E-4, transformed.getMaxX(), DELTA);
		Assert.assertEquals(3.593261136397527E-4, transformed.getMaxY(), DELTA);
	}

	@Test
	public void extendPointTest() throws Exception {
		CoordinateReferenceSystem crs;
		Coordinate coordinate;
		Envelope envelope;

		crs = geoService.getCrs("EPSG:900913");
		coordinate = new Coordinate(0, 0);
		envelope = geocoderUtilService.extendPoint(coordinate, crs, 200, 100);
		Assert.assertEquals(-100.00000376900834, envelope.getMinX(), DELTA);
		Assert.assertEquals(-50.33697512339748, envelope.getMinY(), DELTA);
		Assert.assertEquals(100.00000376900834, envelope.getMaxX(), DELTA);
		Assert.assertEquals(50.33697512339748, envelope.getMaxY(), DELTA);

		coordinate = new Coordinate(10000, 10000);
		envelope = geocoderUtilService.extendPoint(coordinate, crs, 200, 100);
		Assert.assertEquals(9899.999874146251, envelope.getMinX(), DELTA);
		Assert.assertEquals(9949.6629648107, envelope.getMinY(), DELTA);
		Assert.assertEquals(10100.000125853749, envelope.getMaxX(), DELTA);
		Assert.assertEquals(10050.3370351893, envelope.getMaxY(), DELTA);

		crs = geoService.getCrs("EPSG:4326");
		coordinate = new Coordinate(0, 0);
		envelope = geocoderUtilService.extendPoint(coordinate, crs, 200, 100);
		Assert.assertEquals(-8.983152841199021E-4, envelope.getMinX(), DELTA);
		Assert.assertEquals(-4.5218473391466234E-4, envelope.getMinY(), DELTA);
		Assert.assertEquals(8.983152841199021E-4, envelope.getMaxX(), DELTA);
		Assert.assertEquals(4.5218473391466234E-4, envelope.getMaxY(), DELTA);

		coordinate = new Coordinate(100, 50);
		envelope = geocoderUtilService.extendPoint(coordinate, crs, 200, 100);
		Assert.assertEquals(99.99860521715158, envelope.getMinX(), DELTA);
		Assert.assertEquals(49.99955048109636, envelope.getMinY(), DELTA);
		Assert.assertEquals(100.00139478284842, envelope.getMaxX(), DELTA);
		Assert.assertEquals(50.00044951890364, envelope.getMaxY(), DELTA);
	}
}
