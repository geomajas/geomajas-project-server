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
import org.geomajas.plugin.geocoder.api.StaticRegexGeocoderLocationInfo;
import org.geomajas.service.GeoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Test for {@link StaticRegexGeocoderService}.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/completeContext.xml"})
public class StaticRegexGeocoderServiceTest {

	@Autowired
	@Qualifier("staticRegexGeocoderService")
	private StaticRegexGeocoderService service;

	@Autowired
	private GeoService geoService;

	@Autowired
	@Qualifier("BooischotShort")
	private StaticRegexGeocoderLocationInfo booischotShort;
	
	@Autowired
	@Qualifier("Booischot")
	private StaticRegexGeocoderLocationInfo booischot;

	@Autowired
	@Qualifier("BooischotStrict")
	private StaticRegexGeocoderLocationInfo booischotStrict;

	@Test
	public void testGetCrs() throws Exception {
		CoordinateReferenceSystem crs = service.getCrs();
		Assert.assertEquals(900913, geoService.getSridFromCrs(crs));
	}

	@Test
	public void testGetOneLocationShort() throws Exception {
		List<String> list = new ArrayList<String>();
		GetLocationResult result;

		list.clear();
		list.add("bla"); // needs to be "Booischot"
		result = service.getLocation(booischotShort, list);
		Assert.assertNull(result);

		list.clear();
		list.add("Booischot");
		result = service.getLocation(booischotShort, list);
		Assert.assertNotNull(result);

		list.clear();
		list.add("booischot"); // match needs to be case independent
		result = service.getLocation(booischotShort, list);
		Assert.assertNotNull(result);

		list.clear();
		list.add("Booischot");
		list.add("more");
		result = service.getLocation(booischotShort, list);
		Assert.assertNull(result);

		list.clear();
		list.add("xbooischot"); // match needs to be case independent
		result = service.getLocation(booischotShort, list);
		Assert.assertNull(result);

		list.clear();
		list.add("booischotx"); // match needs to be case independent
		result = service.getLocation(booischotShort, list);
		Assert.assertNull(result);
	}

	@Test
	public void testGetOneLocationNormal() throws Exception {
		List<String> list = new ArrayList<String>();
		GetLocationResult result;

		// one alternative with all details
		list.clear();
		list.add("belgium");
		list.add("antwerp");
		list.add("booischot");
		result = service.getLocation(booischot, list);
		Assert.assertNotNull(result);

		// another alternative with all details
		list.clear();
		list.add("België");
		list.add("Antwerpen");
		list.add("Booischot");
		result = service.getLocation(booischot, list);
		Assert.assertNotNull(result);

		// another alternative skips a level
		list.clear();
		list.add("Antwerpen");
		list.add("Booischot");
		result = service.getLocation(booischot, list);
		Assert.assertNull(result);

		// another alternative with too many details
		list.clear();
		list.add("België");
		list.add("Antwerpen");
		list.add("Booischot");
		list.add("Broekmansstraat");
		result = service.getLocation(booischot, list);
		Assert.assertNotNull(result);

		// another alternative with an optional bit skipped
		list.clear();
		list.add("België");
		list.add("Booischot");
		result = service.getLocation(booischot, list);
		Assert.assertNotNull(result);

		// another alternative with an optional bit and too many details
		list.clear();
		list.add("België");
		list.add("Booischot");
		list.add("Broekmansstraat");
		result = service.getLocation(booischot, list);
		Assert.assertNotNull(result);

		// another alternative with too little lines
		list.clear();
		list.add("België");
		result = service.getLocation(booischot, list);
		Assert.assertNull(result);
	}

	@Test
	public void testGetOneLocationStrict() throws Exception {
		List<String> list = new ArrayList<String>();
		GetLocationResult result;

		// all details
		list.clear();
		list.add("België");
		list.add("Antwerpen");
		list.add("Booischot");
		result = service.getLocation(booischotStrict, list);
		Assert.assertNotNull(result);

		// another alternative skips a level
		list.clear();
		list.add("Antwerpen");
		list.add("Booischot");
		result = service.getLocation(booischotStrict, list);
		Assert.assertNull(result);

		// another alternative with too many details
		list.clear();
		list.add("België");
		list.add("Antwerpen");
		list.add("Booischot");
		list.add("Broekmansstraat");
		result = service.getLocation(booischotStrict, list);
		Assert.assertNull(result);

		// another alternative with an optional bit skipped
		list.clear();
		list.add("België");
		list.add("Booischot");
		result = service.getLocation(booischotStrict, list);
		Assert.assertNotNull(result);

		// another alternative with too little lines
		list.clear();
		list.add("België");
		result = service.getLocation(booischotStrict, list);
		Assert.assertNull(result);
	}
}
