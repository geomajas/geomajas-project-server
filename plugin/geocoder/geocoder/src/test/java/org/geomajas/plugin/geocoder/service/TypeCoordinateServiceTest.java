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
 * Test for {@link TypeCoordinateService}.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/tcsContext.xml"})
public class TypeCoordinateServiceTest {

	private static final double DELTA = 1e-6;

	@Autowired
	private TypeCoordinateService geocoder;

	@Test
	public void testGeocoderSimple() {
		List<String> list = new ArrayList<String>();
		GetLocationResult[] result;

		list.clear();
		list.add("4.77397  51.05125  ");
		result = geocoder.getLocation(list, 50, null);
		Assert.assertNotNull(result);
		Assert.assertEquals(1,result.length);
		Assert.assertNotNull(result[0].getCoordinate());
		Assert.assertEquals(4.77397, result[0].getCoordinate().x, DELTA);
		Assert.assertEquals(51.05125, result[0].getCoordinate().y, DELTA);
		Assert.assertEquals(1, result[0].getCanonicalStrings().size());
		Assert.assertEquals("4.77397 51.05125", result[0].getCanonicalStrings().get(0));
		Assert.assertNull(result[0].getEnvelope());
	}

	@Test
	public void testGeocoderSkipExtra() {
		List<String> list = new ArrayList<String>();
		GetLocationResult[] result;

		list.clear();
		list.add("4.77397  51.05125 bla ");
		result = geocoder.getLocation(list, 50, null);
		Assert.assertNotNull(result);
		Assert.assertEquals(1,result.length);
		Assert.assertNotNull(result[0].getCoordinate());
		Assert.assertEquals(4.77397, result[0].getCoordinate().x, DELTA);
		Assert.assertEquals(51.05125, result[0].getCoordinate().y, DELTA);
		Assert.assertEquals(1, result[0].getCanonicalStrings().size());
		Assert.assertEquals("4.77397 51.05125", result[0].getCanonicalStrings().get(0));
		Assert.assertNull(result[0].getEnvelope());
	}

	@Test
	public void testGeocoderTransform() {
		List<String> list = new ArrayList<String>();
		GetLocationResult[] result;

		list.clear();
		list.add("4.77397 \t 51.05125  crs:EPSG:4326");
		result = geocoder.getLocation(list, 50, null);
		Assert.assertNotNull(result);
		Assert.assertEquals(1,result.length);
		Assert.assertNotNull(result[0].getCoordinate());
		Assert.assertEquals(531435.9094623642, result[0].getCoordinate().x, DELTA);
		Assert.assertEquals(6630364.2661175905, result[0].getCoordinate().y, DELTA);
		Assert.assertEquals(1, result[0].getCanonicalStrings().size());
		Assert.assertEquals("4.77397 51.05125 crs:EPSG:4326", result[0].getCanonicalStrings().get(0));
		Assert.assertNull(result[0].getEnvelope());
	}

	@Test
	public void testGeocoderInvalidTransform() {
		List<String> list = new ArrayList<String>();
		GetLocationResult[] result;

		list.clear();
		list.add("4.77397  51.05125  crs:bla");
		result = geocoder.getLocation(list, 50, null);
		Assert.assertNotNull(result);
		Assert.assertEquals(1,result.length);
		Assert.assertNotNull(result[0].getCoordinate());
		Assert.assertEquals(4.77397, result[0].getCoordinate().x, DELTA);
		Assert.assertEquals(51.05125, result[0].getCoordinate().y, DELTA);
		Assert.assertEquals(1, result[0].getCanonicalStrings().size());
		Assert.assertEquals("4.77397 51.05125", result[0].getCanonicalStrings().get(0));
		Assert.assertNull(result[0].getEnvelope());
	}

}
