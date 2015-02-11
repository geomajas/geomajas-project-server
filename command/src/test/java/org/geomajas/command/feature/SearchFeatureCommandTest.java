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
package org.geomajas.command.feature;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.dto.SearchFeatureRequest;
import org.geomajas.command.dto.SearchFeatureResponse;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.layer.feature.Feature;
import org.geomajas.layer.feature.SearchCriterion;
import org.geomajas.testdata.ReloadContext;
import org.geomajas.testdata.ReloadContextTestExecutionListener;
import org.geotools.filter.FidFilter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test for the SearchFeatureCommand class.
 *
 * @author Joachim Van der Auwera
 */
@TestExecutionListeners(listeners = { ReloadContextTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/layerCountries.xml", "/org/geomajas/testdata/simplevectorsContext.xml"})
@ReloadContext
public class SearchFeatureCommandTest {

	private static final double DOUBLE_TOLERANCE = .00000001;
	private static final String LAYER_ID = "countries";
	private static final String REGION_ATTRIBUTE = "region";
	private static final String NAME_ATTRIBUTE = "name";
	private static final String ID_ATTRIBUTE = "id";

	@Autowired
	private CommandDispatcher dispatcher;

	@Autowired
	private SearchFeatureCommand searchFeatureCommand;

	@Test
	public void testSearchOneCriterionNoLimit() throws Exception {
		// prepare command
		SearchFeatureRequest request = new SearchFeatureRequest();
		request.setLayerId(LAYER_ID);
		request.setCrs("EPSG:4326");
		request.setMax(SearchFeatureRequest.MAX_UNLIMITED);
		SearchCriterion searchCriterion = new SearchCriterion();
		searchCriterion.setAttributeName(REGION_ATTRIBUTE);
		searchCriterion.setOperator("like");
		searchCriterion.setValue("'%1'");
		request.setCriteria(new SearchCriterion[] {searchCriterion});

		// execute
		SearchFeatureResponse response = (SearchFeatureResponse) dispatcher.execute(
				SearchFeatureRequest.COMMAND, request, null, "en");

		// test
		Assert.assertFalse(response.isError());
		Assert.assertEquals(LAYER_ID, response.getLayerId());
		List<Feature> features = Arrays.asList(response.getFeatures());
		Assert.assertNotNull(features);
		Assert.assertEquals(2, features.size());
		List<String> actual = new ArrayList<String>();
		for (Feature feature : features) {
			actual.add(feature.getLabel());
		}
		Assert.assertTrue(actual.contains("Country 1"));
		Assert.assertTrue(actual.contains("Country 2"));
	}

	@Test
	public void testSearchOneCriterionOffset() throws Exception {
		// prepare command
		SearchFeatureRequest request = new SearchFeatureRequest();
		request.setLayerId(LAYER_ID);
		request.setCrs("EPSG:4326");
		request.setMax(SearchFeatureRequest.MAX_UNLIMITED);
		request.setOffSet(2);
		SearchCriterion searchCriterion = new SearchCriterion();
		searchCriterion.setAttributeName(REGION_ATTRIBUTE);
		searchCriterion.setOperator("like");
		searchCriterion.setValue("'%1'");
		request.setCriteria(new SearchCriterion[] {searchCriterion});

		// execute
		SearchFeatureResponse response = (SearchFeatureResponse) dispatcher.execute(
				SearchFeatureRequest.COMMAND, request, null, "en");

		// test
		Assert.assertFalse(response.isError());
		Assert.assertEquals(LAYER_ID, response.getLayerId());
		List<Feature> features = Arrays.asList(response.getFeatures());
		Assert.assertNotNull(features);
		Assert.assertEquals(0, features.size());
	}

	@Test
	public void testSearchOneCriterionLimit() throws Exception {
		// prepare command
		SearchFeatureRequest request = new SearchFeatureRequest();
		request.setLayerId(LAYER_ID);
		request.setCrs("EPSG:4326");
		request.setMax(3);
		SearchCriterion searchCriterion = new SearchCriterion();
		searchCriterion.setAttributeName(REGION_ATTRIBUTE);
		searchCriterion.setOperator("like");
		searchCriterion.setValue("'R%'");
		request.setCriteria(new SearchCriterion[] {searchCriterion});

		// execute
		SearchFeatureResponse response = (SearchFeatureResponse) dispatcher.execute(
				SearchFeatureRequest.COMMAND, request, null, "en");

		// test
		Assert.assertFalse(response.isError());
		Assert.assertEquals(LAYER_ID, response.getLayerId());
		List<Feature> features = Arrays.asList(response.getFeatures());
		Assert.assertNotNull(features);
		Assert.assertEquals(3, features.size());
		List<String> actual = new ArrayList<String>();
		for (Feature feature : features) {
			actual.add(feature.getLabel());
		}
		int count = 0;
		if (actual.contains("Country 1")) {
			count++;
		}
		if (actual.contains("Country 2")) {
			count++;
		}
		if (actual.contains("Country 3")) {
			count++;
		}
		if (actual.contains("Country 4")) {
			count++;
		}
		Assert.assertEquals(3, count);
	}

	@Test
	public void testSearchMultipleCriteria() throws Exception {
		// prepare command
		SearchFeatureRequest request = new SearchFeatureRequest();
		request.setLayerId(LAYER_ID);
		request.setCrs("EPSG:4326");
		SearchCriterion searchCriterion1 = new SearchCriterion();
		searchCriterion1.setAttributeName(REGION_ATTRIBUTE);
		searchCriterion1.setOperator("like");
		searchCriterion1.setValue("'%egion 1'");
		SearchCriterion searchCriterion2 = new SearchCriterion();
		searchCriterion2.setAttributeName(REGION_ATTRIBUTE);
		searchCriterion2.setOperator("like");
		searchCriterion2.setValue("'%egion 2'");
		request.setCriteria(new SearchCriterion[] {searchCriterion1, searchCriterion2});
		request.setBooleanOperator("or");

		// execute
		SearchFeatureResponse response = (SearchFeatureResponse) dispatcher.execute(
				SearchFeatureRequest.COMMAND, request, null, "en");

		// test
		Assert.assertFalse(response.isError());
		Assert.assertEquals(LAYER_ID, response.getLayerId());
		List<Feature> features = Arrays.asList(response.getFeatures());
		Assert.assertNotNull(features);
		Assert.assertEquals(4, features.size());

		List<String> actual = new ArrayList<String>();
		for (Feature feature : features) {
			actual.add(feature.getLabel());
		}
		Assert.assertTrue(actual.contains("Country 1"));
		Assert.assertTrue(actual.contains("Country 2"));
		Assert.assertTrue(actual.contains("Country 3"));
		Assert.assertTrue(actual.contains("Country 4"));
	}

	@Test
	public void testSearchNoCriteria() throws Exception {
		// prepare command
		SearchFeatureRequest request = new SearchFeatureRequest();
		request.setLayerId(LAYER_ID);
		request.setCrs("EPSG:4326");
		// execute
		SearchFeatureResponse response = (SearchFeatureResponse) dispatcher.execute(
				SearchFeatureRequest.COMMAND, request, null, "en");

		// test
		Assert.assertFalse(response.isError());
		Assert.assertEquals(LAYER_ID, response.getLayerId());
		List<Feature> features = Arrays.asList(response.getFeatures());
		Assert.assertNotNull(features);
		Assert.assertEquals(4, features.size());

		List<String> actual = new ArrayList<String>();
		for (Feature feature : features) {
			actual.add(feature.getLabel());
		}
		Assert.assertTrue(actual.contains("Country 1"));
		Assert.assertTrue(actual.contains("Country 2"));
		Assert.assertTrue(actual.contains("Country 3"));
		Assert.assertTrue(actual.contains("Country 4"));
	}

	// @todo need to test filter
	// @todo need to test filter + limit + multiple criteria
	// @todo need to test no filter, no criteria (return all?)

	@Test
	public void testSearchTransform() throws Exception {
		// prepare command, verify original coordinates first
		SearchFeatureRequest request = new SearchFeatureRequest();
		request.setLayerId(LAYER_ID);
		request.setCrs("EPSG:4326");
		request.setMax(3); // this immediately tests whether returning less than the maximum works
		SearchCriterion searchCriterion = new SearchCriterion();
		searchCriterion.setAttributeName(NAME_ATTRIBUTE);
		searchCriterion.setOperator("like");
		searchCriterion.setValue("'%3'");
		request.setCriteria(new SearchCriterion[] {searchCriterion});

		// execute
		SearchFeatureResponse response = (SearchFeatureResponse) dispatcher.execute(
				SearchFeatureRequest.COMMAND, request, null, "en");

		// test
		Assert.assertFalse(response.isError());
		Assert.assertEquals(LAYER_ID, response.getLayerId());
		Feature[] features = response.getFeatures();
		Assert.assertNotNull(features);
		Assert.assertEquals(1, features.length);
		Assert.assertEquals("Country 3", features[0].getLabel());
		Geometry geometry = features[0].getGeometry();
		Assert.assertNotNull(geometry);
		Coordinate coor = geometry.getGeometries()[0].getGeometries()[0].getCoordinates()[0];
		Assert.assertEquals(-1, coor.getX(), DOUBLE_TOLERANCE);
		Assert.assertEquals(0, coor.getY(), DOUBLE_TOLERANCE);

		// try again using mercator
		request.setCrs("EPSG:900913");
		response = (SearchFeatureResponse) dispatcher.execute(SearchFeatureRequest.COMMAND, request, null, "en");
		if (response.isError()) {
			response.getErrors().get(0).printStackTrace();
		}
		Assert.assertFalse(response.isError());
		Assert.assertEquals(LAYER_ID, response.getLayerId());
		features = response.getFeatures();
		Assert.assertNotNull(features);
		Assert.assertEquals(1, features.length);
		Assert.assertEquals("Country 3", features[0].getLabel());
		geometry = features[0].getGeometry();
		Assert.assertNotNull(geometry);
		coor = geometry.getGeometries()[0].getGeometries()[0].getCoordinates()[0];
		// remark, this value is obtained using a test run, not externally verified
		Assert.assertEquals(-111319.49079327357, coor.getX(), DOUBLE_TOLERANCE);
		Assert.assertEquals(0, coor.getY(), DOUBLE_TOLERANCE);
	}

	@Test
	@DirtiesContext // @todo need to check why this is necessary, otherwise next test fails? (GetVectorTileCommandTest)
	// probably cause by directly using the command service which has an injected security context
	public void createFilterTest() throws Exception {
		SearchFeatureRequest request = new SearchFeatureRequest();
		request.setLayerId(LAYER_ID);
		request.setCrs("EPSG:4326");
		SearchCriterion searchCriterion = new SearchCriterion();
		Filter filter;

		// needs to be FidFilter when equals test on id
		searchCriterion.setAttributeName(ID_ATTRIBUTE);
		searchCriterion.setOperator("=");
		searchCriterion.setValue("'1'");
		request.setCriteria(new SearchCriterion[] {searchCriterion});
		filter = searchFeatureCommand.createFilter(request, LAYER_ID);
		Assert.assertTrue(filter instanceof FidFilter);

		// but *not* when other test
		searchCriterion.setAttributeName(ID_ATTRIBUTE);
		searchCriterion.setOperator("like");
		searchCriterion.setValue("'%a%'");
		request.setCriteria(new SearchCriterion[] {searchCriterion});
		filter = searchFeatureCommand.createFilter(request, LAYER_ID);
		Assert.assertFalse(filter instanceof FidFilter);
	}
}
