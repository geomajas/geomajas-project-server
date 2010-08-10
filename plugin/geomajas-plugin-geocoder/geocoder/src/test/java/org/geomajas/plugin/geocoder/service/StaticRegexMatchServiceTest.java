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
import org.geomajas.plugin.geocoder.api.StaticRegexMatchLocationInfo;
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
 * Test for {@link StaticRegexMatchService}.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/completeContext.xml"})
public class StaticRegexMatchServiceTest {

	@Autowired
	private StaticRegexMatchService service;

	@Autowired
	private GeoService geoService;

	@Autowired
	@Qualifier("BooischotShort")
	private StaticRegexMatchLocationInfo booischotShort;
	
	@Autowired
	@Qualifier("Booischot")
	private StaticRegexMatchLocationInfo booischot;

	@Autowired
	@Qualifier("BooischotStrict")
	private StaticRegexMatchLocationInfo booischotStrict;

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
