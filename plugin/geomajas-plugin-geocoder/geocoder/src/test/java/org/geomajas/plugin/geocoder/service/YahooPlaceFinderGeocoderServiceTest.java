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

import junit.framework.Assert;
import org.geomajas.plugin.geocoder.api.GetLocationResult;
import org.geomajas.service.GeoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Test for the GeonamesGeocoderService.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/ypfContext.xml"})
public class YahooPlaceFinderGeocoderServiceTest {

	@Autowired
	private YahooPlaceFinderGeocoderService geocoder;

	@Autowired
	private GeoService geoService;


	@Test
	public void testGetCrs() throws Exception {
		CoordinateReferenceSystem crs = geocoder.getCrs();
		Assert.assertEquals(4326, geoService.getSridFromCrs(crs));
	}

	@Test
	public void testGeocoder() {
		List<String> list = new ArrayList<String>();
		GetLocationResult[] result;

		list.clear();
		list.add("booischot");
		result = geocoder.getLocation(list);
		Assert.assertNotNull(result);
		Assert.assertEquals(1,result.length);
		Assert.assertNotNull(result[0].getCoordinate());
		Assert.assertEquals(4.77397, result[0].getCoordinate().x, .00001);
		Assert.assertEquals(51.05125, result[0].getCoordinate().y, .00001);
		Assert.assertEquals(2, result[0].getCanonicalStrings().size());
		Assert.assertEquals("Belgium", result[0].getCanonicalStrings().get(0));
		Assert.assertEquals("2221 Boisschot", result[0].getCanonicalStrings().get(1));
		Assert.assertEquals(4.75513, result[0].getEnvelope().getMinX(), .00001);
		Assert.assertEquals(4.79043, result[0].getEnvelope().getMaxX(), .00001);
		Assert.assertEquals(51.031898, result[0].getEnvelope().getMinY(), .00001);
		Assert.assertEquals(51.060329, result[0].getEnvelope().getMaxY(), .00001);

		list.clear();
		list.add("london");
		result = geocoder.getLocation(list);
		Assert.assertNotNull(result);
		Assert.assertEquals(1,result.length);
		Assert.assertNotNull(result[0].getCoordinate());
		Assert.assertEquals(-0.127144, result[0].getCoordinate().x, .00001);
		Assert.assertEquals(51.506325, result[0].getCoordinate().y, .00001);
		Assert.assertEquals(2, result[0].getCanonicalStrings().size());
		Assert.assertEquals("United Kingdom", result[0].getCanonicalStrings().get(0));
		Assert.assertEquals("London", result[0].getCanonicalStrings().get(1));

		list.clear();
		list.add("london");
		list.add("CA");
		result = geocoder.getLocation(list);
		Assert.assertNotNull(result);
		Assert.assertEquals(1,result.length);
		Assert.assertEquals("United States", result[0].getCanonicalStrings().get(0));
		Assert.assertEquals("London, CA  93618", result[0].getCanonicalStrings().get(1));

		list.clear();
		list.add("blablabla");
		result = geocoder.getLocation(list);
		Assert.assertTrue(null == result || 0 == result.length);
	}
}
