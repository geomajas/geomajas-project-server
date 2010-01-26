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
package org.geomajas.extension.command.feature;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.extension.command.dto.SearchFeatureRequest;
import org.geomajas.extension.command.dto.SearchFeatureResponse;
import org.geomajas.layer.feature.Feature;
import org.geomajas.layer.feature.SearchCriterion;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Test for the SearchFeatureCommand class.
 *
 * @author Joachim Van der Auwera
 */
public class SearchFeatureCommandTest {

	private static final double DOUBLE_TOLERANCE = .0000000001;
	private static final String LAYER_ID = "countries";
	private static final String NAME_ATTRIBUTE = "CNTRY_NAME";

	private static CommandDispatcher DISPATCHER;

	@BeforeClass
	public static void setUp() {
//		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
//				new String[] {"org/geomajas/spring/geomajasContext.xml",
//						"org/geomajas/testdata/layerCountries.xml",
//						"org/geomajas/testdata/simplevectorsContext.xml"});
//		DISPATCHER = applicationContext.getBean("command.CommandDispatcher", CommandDispatcher.class);
	}

	@Test
	public void testSearchOneCriterionNoLimit() throws Exception {
//		// prepare command
//		SearchFeatureRequest request = new SearchFeatureRequest();
//		request.setLayerId(LAYER_ID);
//		request.setCrs("EPSG:4326");
//		request.setMax(SearchFeatureRequest.MAX_UNLIMITED);
//		SearchCriterion searchCriterion = new SearchCriterion();
//		searchCriterion.setAttributeName(NAME_ATTRIBUTE);
//		searchCriterion.setOperator("like");
//		searchCriterion.setValue("'S%'");
//		request.setCriteria(new SearchCriterion[] {searchCriterion});
//
//		// execute
//		SearchFeatureResponse response = (SearchFeatureResponse) DISPATCHER.execute(
//				"command.feature.Search", request, null, "en");
//
//		// test
//		if (response.isError()) {
//			response.getErrors().get(0).printStackTrace();
//		}
//		Assert.assertFalse(response.isError());
//		Assert.assertEquals(LAYER_ID, response.getLayerId());
//		Feature[] features = response.getFeatures();
//		Assert.assertNotNull(features);
//		Assert.assertEquals(9, features.length);
//		List<String> names = new ArrayList<String>();
//		for (int i = 0; i < features.length; i++) {
//			names.add(features[i].getLabel());
//		}
//		Assert.assertTrue(names.contains("Sudan, Administered by Egypt"));
//		Assert.assertTrue(names.contains("Senegal"));
//		Assert.assertTrue(names.contains("Sudan"));
//		Assert.assertTrue(names.contains("Sudan, Administered by Kenya"));
//		Assert.assertTrue(names.contains("Sierra Leone"));
//		Assert.assertTrue(names.contains("South Africa"));
//		Assert.assertTrue(names.contains("Swaziland"));
//		Assert.assertTrue(names.contains("Somalia"));
//		Assert.assertTrue(names.contains("Spain"));
	}

	@Test
	public void testSearchOneCriterionLimit() throws Exception {
//		// prepare command
//		SearchFeatureRequest request = new SearchFeatureRequest();
//		request.setLayerId(LAYER_ID);
//		request.setCrs("EPSG:4326");
//		request.setMax(3);
//		SearchCriterion searchCriterion = new SearchCriterion();
//		searchCriterion.setAttributeName(NAME_ATTRIBUTE);
//		searchCriterion.setOperator("like");
//		searchCriterion.setValue("'S%'");
//		request.setCriteria(new SearchCriterion[] {searchCriterion});
//
//		// execute
//		SearchFeatureResponse response = (SearchFeatureResponse) DISPATCHER.execute(
//				"command.feature.Search", request, null, "en");
//
//		// test
//		if (response.isError()) {
//			response.getErrors().get(0).printStackTrace();
//		}
//		Assert.assertFalse(response.isError());
//		Assert.assertEquals(LAYER_ID, response.getLayerId());
//		Feature[] features = response.getFeatures();
//		Assert.assertNotNull(features);
//		Assert.assertEquals(3, features.length);
//		List<String> names = new ArrayList<String>();
//		for (Feature feature : features) {
//			names.add(feature.getLabel());
//		}
//		// verify three different results in the correct range
//		int count = 0;
//		if (names.contains("Sudan, Administered by Egypt")) {
//			count++;
//		}
//		if (names.contains("Senegal")) {
//			count++;
//		}
//		if (names.contains("Sudan")) {
//			count++;
//		}
//		if (names.contains("Sudan, Administered by Kenya")) {
//			count++;
//		}
//		if (names.contains("Sierra Leone")) {
//			count++;
//		}
//		if (names.contains("South Africa")) {
//			count++;
//		}
//		if (names.contains("Swaziland")) {
//			count++;
//		}
//		if (names.contains("Somalia")) {
//			count++;
//		}
//		if (names.contains("Spain")) {
//			count++;
//		}
//		Assert.assertEquals(3, count);
	}

	@Test
	public void testSearchMultipleCriteria() throws Exception {
//		// prepare command
//		SearchFeatureRequest request = new SearchFeatureRequest();
//		request.setLayerId(LAYER_ID);
//		request.setCrs("EPSG:4326");
//		SearchCriterion searchCriterion1 = new SearchCriterion();
//		searchCriterion1.setAttributeName(NAME_ATTRIBUTE);
//		searchCriterion1.setOperator("like");
//		searchCriterion1.setValue("'W%'");
//		SearchCriterion searchCriterion2 = new SearchCriterion();
//		searchCriterion2.setAttributeName(NAME_ATTRIBUTE);
//		searchCriterion2.setOperator("like");
//		searchCriterion2.setValue("'Z%'");
//		request.setCriteria(new SearchCriterion[] {searchCriterion1, searchCriterion2});
//		request.setBooleanOperator("or");
//
//		// execute
//		SearchFeatureResponse response = (SearchFeatureResponse) DISPATCHER.execute(
//				"command.feature.Search", request, null, "en");
//
//		// test
//		if (response.isError()) {
//			response.getErrors().get(0).printStackTrace();
//		}
//		Assert.assertFalse(response.isError());
//		Assert.assertEquals(LAYER_ID, response.getLayerId());
//		Feature[] features = response.getFeatures();
//		Assert.assertNotNull(features);
//		List<String> names = new ArrayList<String>();
//		for (Feature feature : features) {
//			names.add(feature.getLabel());
//		}
//		Assert.assertEquals(4, features.length);
//		Assert.assertTrue(names.contains("Western Sahara"));
//		Assert.assertTrue(names.contains("Zaire"));
//		Assert.assertTrue(names.contains("Zambia"));
//		Assert.assertTrue(names.contains("Zimbabwe"));
	}

	// @todo need to test filter
	// @todo need to test filter + limit + multiple criteria
	// @todo need to test no filter, no criteria (return all?)

	@Test
	public void testSearchTransform() throws Exception {
//		// prepare command, verify original coordinates first
//		SearchFeatureRequest request = new SearchFeatureRequest();
//		request.setLayerId(LAYER_ID);
//		request.setCrs("EPSG:4326");
//		request.setMax(3); // this immediately tests whether returning less than the maximum works
//		SearchCriterion searchCriterion = new SearchCriterion();
//		searchCriterion.setAttributeName(NAME_ATTRIBUTE);
//		searchCriterion.setOperator("like");
//		searchCriterion.setValue("'Sp%'");
//		request.setCriteria(new SearchCriterion[] {searchCriterion});
//
//		// execute
//		SearchFeatureResponse response = (SearchFeatureResponse) DISPATCHER.execute(
//				"command.feature.Search", request, null, "en");
//
//		// test
//		if (response.isError()) {
//			response.getErrors().get(0).printStackTrace();
//		}
//		Assert.assertFalse(response.isError());
//		Assert.assertEquals(LAYER_ID, response.getLayerId());
//		Feature[] features = response.getFeatures();
//		Assert.assertNotNull(features);
//		Assert.assertEquals(1, features.length);
//		Assert.assertEquals("Spain", features[0].getLabel());
//		Geometry geometry = features[0].getGeometry();
//		Assert.assertNotNull(geometry);
//		Coordinate coor = geometry.getGeometries()[0].getGeometries()[0].getCoordinates()[0];
//		Assert.assertEquals(-5.345833778381348, coor.getX(), DOUBLE_TOLERANCE);
//		Assert.assertEquals(35.84165954589844, coor.getY(), DOUBLE_TOLERANCE);
//
//		// try again using mercator
//		request.setCrs("EPSG:900913");
//		response = (SearchFeatureResponse) DISPATCHER.execute( "command.feature.Search", request, null, "en");
//		if (response.isError()) response.getErrors().get(0).printStackTrace();
//		Assert.assertFalse(response.isError());
//		Assert.assertEquals(LAYER_ID, response.getLayerId());
//		features = response.getFeatures();
//		Assert.assertNotNull(features);
//		Assert.assertEquals(1, features.length);
//		Assert.assertEquals("Spain", features[0].getLabel());
//		geometry = features[0].getGeometry();
//		Assert.assertNotNull(geometry);
//		coor = geometry.getGeometries()[0].getGeometries()[0].getCoordinates()[0];
//		// remark, this value is obtained using a test run, not externally verified
//		Assert.assertEquals(-595095.4940748933, coor.getX(), DOUBLE_TOLERANCE);
//		Assert.assertEquals(4278855.785717449, coor.getY(), DOUBLE_TOLERANCE);
	}
}
