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
import java.util.Locale;

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
		result = geocoder.getLocation(list, 50, null);
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
		list.add("booischot");
		result = geocoder.getLocation(list, 50, new Locale("nl_BE"));
		Assert.assertNotNull(result);
		Assert.assertEquals(1,result.length);
		Assert.assertNotNull(result[0].getCoordinate());
		Assert.assertEquals(4.77397, result[0].getCoordinate().x, .00001);
		Assert.assertEquals(51.05125, result[0].getCoordinate().y, .00001);
		Assert.assertEquals(2, result[0].getCanonicalStrings().size());
		Assert.assertEquals("België", result[0].getCanonicalStrings().get(0));
		Assert.assertEquals("2221 Booischot", result[0].getCanonicalStrings().get(1));
		Assert.assertEquals(4.75513, result[0].getEnvelope().getMinX(), .00001);
		Assert.assertEquals(4.79043, result[0].getEnvelope().getMaxX(), .00001);
		Assert.assertEquals(51.031898, result[0].getEnvelope().getMinY(), .00001);
		Assert.assertEquals(51.060329, result[0].getEnvelope().getMaxY(), .00001);

		list.clear();
		list.add("london");
		result = geocoder.getLocation(list, 50, null);
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
		result = geocoder.getLocation(list, 50, null);
		Assert.assertNotNull(result);
		Assert.assertEquals(1,result.length);
		Assert.assertEquals("United States", result[0].getCanonicalStrings().get(0));
		Assert.assertEquals("London, CA  93618", result[0].getCanonicalStrings().get(1));

		list.clear();
		list.add("blablabla");
		result = geocoder.getLocation(list, 50, null);
		Assert.assertTrue(null == result || 0 == result.length);
	}
	
	@Test
	public void testTranslatedLocale() {
		List<String> list = new ArrayList<String>();
		GetLocationResult[] result;
		list.clear();
		list.add("booischot");
		// french locale reports "Aucune erreur" vs "No error" for nl_be
		result = geocoder.getLocation(list, 50, new Locale("fr"));
		Assert.assertNotNull(result);
		Assert.assertEquals(1,result.length);
		Assert.assertNotNull(result[0].getCoordinate());
		Assert.assertEquals(4.77397, result[0].getCoordinate().x, .00001);
		Assert.assertEquals(51.05125, result[0].getCoordinate().y, .00001);
		Assert.assertEquals(2, result[0].getCanonicalStrings().size());
		Assert.assertEquals("Belgique", result[0].getCanonicalStrings().get(0));
		Assert.assertEquals("2221 Booischot", result[0].getCanonicalStrings().get(1));
		Assert.assertEquals(4.75513, result[0].getEnvelope().getMinX(), .00001);
		Assert.assertEquals(4.79043, result[0].getEnvelope().getMaxX(), .00001);
		Assert.assertEquals(51.031898, result[0].getEnvelope().getMinY(), .00001);
		Assert.assertEquals(51.060329, result[0].getEnvelope().getMaxY(), .00001);
	}
	
	@Test
	public void testException() {
		List<String> list = new ArrayList<String>();
		GetLocationResult[] result;
		// empty list
		result = geocoder.getLocation(list, 50, null);
		Assert.assertNull(result);
	}

}
