/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
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
import org.junit.Ignore;
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
		"/completeContext.xml"})
@Ignore //FIXME: GC-54
public class GeonamesGeocoderServiceTest {

	@Autowired
	private GeonamesGeocoderService geocoder;

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
		result = geocoder.getLocation(list, 500, null);
		Assert.assertNotNull("Geonames may be down", result);
		Assert.assertEquals(1,result.length);
		Assert.assertNotNull(result[0].getCoordinate());
		Assert.assertEquals(4.7751, result[0].getCoordinate().x, .00001);
		Assert.assertEquals(51.05219, result[0].getCoordinate().y, .00001);

		list.clear();
		list.add("london");
		result = geocoder.getLocation(list, 500, null);		
		Assert.assertNotNull("Geonames may be down", result);
		int londonWorldCount = result.length;
		Assert.assertTrue(londonWorldCount > 1);

		list.clear();
		list.add("london");
		list.add("UK");
		result = geocoder.getLocation(list, 500, null);
		Assert.assertNotNull("Geonames may be down", result);
		Assert.assertTrue(result.length > 1);
		Assert.assertTrue(result.length < londonWorldCount);

		list.clear();
		list.add("blablabla");
		result = geocoder.getLocation(list, 500, null);
		Assert.assertNotNull("Geonames may be down", result);
		Assert.assertTrue(0 == result.length);
	}
}
