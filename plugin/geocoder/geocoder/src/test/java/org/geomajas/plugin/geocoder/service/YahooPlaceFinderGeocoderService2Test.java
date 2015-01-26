/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.geocoder.service;

import junit.framework.Assert;
import org.geomajas.plugin.geocoder.api.GetLocationResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Test for the GeonamesGeocoderService, verifies that setting the appId using a property also works.
 * The property is set in the pom, in the surefire plugin section.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/ypf2Context.xml"})
public class YahooPlaceFinderGeocoderService2Test {

	@Autowired
	private YahooPlaceFinderGeocoderService geocoder;

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
		Assert.assertEquals(51.05677, result[0].getEnvelope().getMaxY(), .00001);
	}
}
