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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.dto.SearchByLocationRequest;
import org.geomajas.command.dto.SearchByLocationResponse;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.layer.feature.Feature;
import org.geomajas.service.DtoConverterService;
import org.geomajas.testdata.ReloadContext;
import org.geomajas.testdata.ReloadContextTestExecutionListener;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Test for the {@link SearchByLocationCommand} class.
 *
 * @author Pieter De Graef
 * @author Jan De Moerloose
 * @author An Buyle
 */
@TestExecutionListeners(listeners = { ReloadContextTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/layerCountries.xml", "/org/geomajas/testdata/simplevectorsContext.xml"})
@ReloadContext
public class SearchByLocationCommandTest {

	private static final String LAYER_ID = "countries"; /* server layer ID */

	@Autowired
	private CommandDispatcher dispatcher;

	@Autowired
	private DtoConverterService converter;

	@Test
	public void intersectCountriesOnEquator() throws Exception {
		// prepare command
		SearchByLocationRequest request = new SearchByLocationRequest();
		request.setCrs("EPSG:4326");
		request.setQueryType(SearchByLocationRequest.QUERY_INTERSECTS);
		request.setSearchType(SearchByLocationRequest.SEARCH_ALL_LAYERS);
		request.setLayerIds(new String[] {LAYER_ID});

		GeometryFactory factory = new GeometryFactory();
		LineString equator = factory.createLineString(new Coordinate[] {new Coordinate(0, 0),
				new Coordinate(-180, 180)});
		request.setLocation(converter.toDto(equator));

		// execute
		SearchByLocationResponse response = (SearchByLocationResponse) dispatcher.execute(
				SearchByLocationRequest.COMMAND, request, null, "en");

		// test
		Assert.assertFalse(response.isError());
		List<Feature> features = response.getFeatureMap().get(LAYER_ID);
		Assert.assertNotNull(features);
		Assert.assertEquals(4, features.size());
		List<String> actual = new ArrayList<String>();
		for (Feature feature : features) {
			actual.add(feature.getLabel());
		}
		Assert.assertTrue(actual.contains("Country 4"));
		Assert.assertTrue(actual.contains("Country 3"));
		Assert.assertTrue(actual.contains("Country 2"));
		Assert.assertTrue(actual.contains("Country 1"));
	}

	@Test
	public void intersectCountriesOnEquatorWithFilter() throws Exception {
		// prepare command
		SearchByLocationRequest request = new SearchByLocationRequest();
		request.setCrs("EPSG:4326");
		request.setQueryType(SearchByLocationRequest.QUERY_INTERSECTS);
		request.setSearchType(SearchByLocationRequest.SEARCH_ALL_LAYERS);
		request.setLayerIds(new String[] {LAYER_ID});
		//note that setting a global filter on the SearchByLocationRequest will only work if the filter is applicable
		//to all layers! In this test case there is only one layer.
		request.setFilter("region='Region 1'");

		GeometryFactory factory = new GeometryFactory();
		LineString equator = factory.createLineString(new Coordinate[] {new Coordinate(0, 0),
				new Coordinate(-180, 180)});
		request.setLocation(converter.toDto(equator));

		// execute
		SearchByLocationResponse response = (SearchByLocationResponse) dispatcher.execute(
				SearchByLocationRequest.COMMAND, request, null, "en");

		// test
		Assert.assertFalse(response.isError());
		List<Feature> features = response.getFeatureMap().get(LAYER_ID);
		Assert.assertNotNull(features);
		Assert.assertEquals(2, features.size());
		List<String> actual = new ArrayList<String>();
		for (Feature feature : features) {
			actual.add(feature.getLabel());
		}
		Assert.assertTrue(actual.contains("Country 2"));
		Assert.assertTrue(actual.contains("Country 1"));
	}

	@Test
	public void intersectCountriesOnEquatorWithLayerFilter() throws Exception {
		// prepare command
		SearchByLocationRequest request = new SearchByLocationRequest();
		request.setCrs("EPSG:4326");
		request.setQueryType(SearchByLocationRequest.QUERY_INTERSECTS);
		request.setSearchType(SearchByLocationRequest.SEARCH_ALL_LAYERS);
		request.setLayerIds(new String[] {LAYER_ID});
		request.setFilter(LAYER_ID, "region='Region 1'");

		GeometryFactory factory = new GeometryFactory();
		LineString equator = factory.createLineString(new Coordinate[] {new Coordinate(0, 0),
				new Coordinate(-180, 180)});
		request.setLocation(converter.toDto(equator));

		// execute
		SearchByLocationResponse response = (SearchByLocationResponse) dispatcher.execute(
				SearchByLocationRequest.COMMAND, request, null, "en");

		// test
		Assert.assertFalse(response.isError());
		List<Feature> features = response.getFeatureMap().get(LAYER_ID);
		Assert.assertNotNull(features);
		Assert.assertEquals(2, features.size());
		List<String> actual = new ArrayList<String>();
		for (Feature feature : features) {
			actual.add(feature.getLabel());
		}
		Assert.assertTrue(actual.contains("Country 2"));
		Assert.assertTrue(actual.contains("Country 1"));
	}

	@Test
	public void intersectCountriesOnEquatorWithLayerFilters() throws Exception {
		// prepare command
		SearchByLocationRequest request = new SearchByLocationRequest();
		request.setCrs("EPSG:4326");
		request.setQueryType(SearchByLocationRequest.QUERY_INTERSECTS);
		request.setSearchType(SearchByLocationRequest.SEARCH_ALL_LAYERS);
		
		final String region1ResultTag = "countries layer region 1";
		final String region2ResultTag = "countries layer region 2";
		
		request.addLayerWithFilter(region1ResultTag, LAYER_ID, "region='Region 1'");
		request.addLayerWithFilter(region2ResultTag, LAYER_ID, "region='Region 2'");
		
		GeometryFactory factory = new GeometryFactory();
		LineString equator = factory.createLineString(new Coordinate[] {new Coordinate(0, 0),
				new Coordinate(-180, 180)});
		request.setLocation(converter.toDto(equator));
		String requestToString = request.toString();
		Assert.assertTrue(requestToString.length() > 24);

		// execute
		SearchByLocationResponse response = (SearchByLocationResponse) dispatcher.execute(
				SearchByLocationRequest.COMMAND, request, null, "en");

		// test
		Assert.assertFalse(response.isError());
		List<Feature> features;
		List<String> actual;
		features = response.getFeatureMap().get(region1ResultTag);
		Assert.assertNotNull(features);
		Assert.assertEquals(2, features.size());
		actual = new ArrayList<String>();
		for (Feature feature : features) {
			actual.add(feature.getLabel());
		}
		Assert.assertTrue(actual.contains("Country 2"));
		Assert.assertTrue(actual.contains("Country 1"));
		features = response.getFeatureMap().get(region2ResultTag);
		Assert.assertNotNull(features);
		Assert.assertEquals(2, features.size());
		actual = new ArrayList<String>();
		for (Feature feature : features) {
			actual.add(feature.getLabel());
		}
		Assert.assertTrue(actual.contains("Country 4"));
	}
	
	@Test
	public void firstLayer() throws Exception {
		// prepare command
		SearchByLocationRequest request = new SearchByLocationRequest();
		request.setCrs("EPSG:4326");
		request.setQueryType(SearchByLocationRequest.QUERY_INTERSECTS);
		request.setSearchType(SearchByLocationRequest.SEARCH_FIRST_LAYER);
		
		final String region1ResultTag = "countries layer region 1";
		final String region2ResultTag = "countries layer region 2";
		request.addLayerWithFilter(region1ResultTag, LAYER_ID, "region='Region 1'");
		request.addLayerWithFilter(region2ResultTag, LAYER_ID, "region='Region 2'");
		
		request.setLocation(GeometryService.toPolygon(new Bbox(-180, -90, 360, 180)));

		// execute
		SearchByLocationResponse response = (SearchByLocationResponse) dispatcher.execute(
				SearchByLocationRequest.COMMAND, request, null, "en");

		// test
		Assert.assertFalse(response.isError());
		Assert.assertEquals(1, response.getFeatureMap().size());
		Assert.assertNotNull(response.getFeatureMap().get(region1ResultTag));
	}

	@Test
	public void intersect50percentOverlapExactly() throws Exception {
		// prepare command
		SearchByLocationRequest request = new SearchByLocationRequest();
		request.setCrs("EPSG:4326");
		request.setQueryType(SearchByLocationRequest.QUERY_INTERSECTS);
		request.setSearchType(SearchByLocationRequest.SEARCH_ALL_LAYERS);
		request.setRatio(0.5f);
		request.setLayerIds(new String[] {LAYER_ID});

		// create a rectangle that overlaps 50 %
		GeometryFactory factory = new GeometryFactory();
		LinearRing half1 = factory.createLinearRing(new Coordinate[] {new Coordinate(0, 0), new Coordinate(1, 0),
				new Coordinate(1, 0.5), new Coordinate(0, 0.5), new Coordinate(0, 0)});
		Polygon polygon = factory.createPolygon(half1, null);
		request.setLocation(converter.toDto(polygon));

		// execute
		SearchByLocationResponse response = (SearchByLocationResponse) dispatcher.execute(
				SearchByLocationRequest.COMMAND, request, null, "en");

		// test
		Assert.assertFalse(response.isError());
		List<Feature> features = response.getFeatureMap().get(LAYER_ID);
		Assert.assertNotNull(features);
		List<String> actual = new ArrayList<String>();
		for (Feature feature : features) {
			actual.add(feature.getLabel());
		}
		List<String> expected = new ArrayList<String>();
		expected.add("Country 1");
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void intersect50percentOverlapAlmost() throws Exception {
		// prepare command
		SearchByLocationRequest request = new SearchByLocationRequest();
		request.setCrs("EPSG:4326");
		request.setQueryType(SearchByLocationRequest.QUERY_INTERSECTS);
		request.setSearchType(SearchByLocationRequest.SEARCH_ALL_LAYERS);
		request.setRatio(0.5f);
		request.setLayerIds(new String[] {LAYER_ID});

		// create a rectangle that overlaps 49 %
		GeometryFactory factory = new GeometryFactory();
		LinearRing half1 = factory.createLinearRing(new Coordinate[] {new Coordinate(0, 0), new Coordinate(1, 0),
				new Coordinate(1, 0.49), new Coordinate(0, 0.49), new Coordinate(0, 0)});
		Polygon polygon = factory.createPolygon(half1, null);
		request.setLocation(converter.toDto(polygon));

		// execute
		SearchByLocationResponse response = (SearchByLocationResponse) dispatcher.execute(
				SearchByLocationRequest.COMMAND, request, null, "en");
		// test
		List<Feature> features = response.getFeatureMap().get(LAYER_ID);
		Assert.assertNull(features);
	}
}
