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
package org.geomajas.command.feature;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.dto.SearchByLocationRequest;
import org.geomajas.command.dto.SearchByLocationResponse;
import org.geomajas.layer.feature.Feature;
import org.geomajas.service.DtoConverterService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Test for the SearchByLocationCommand class.
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/layerCountries.xml", "/org/geomajas/testdata/simplevectorsContext.xml" })
public class SearchByLocationCommandTest {

	private static final String LAYER_ID = "countries";

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
		request.setLayerIds(new String[] { LAYER_ID });

		GeometryFactory factory = new GeometryFactory();
		LineString equator = factory.createLineString(new Coordinate[] { new Coordinate(0, 0),
				new Coordinate(-180, 180) });
		request.setLocation(converter.toDto(equator));

		// execute
		SearchByLocationResponse response = (SearchByLocationResponse) dispatcher.execute(
				"command.feature.SearchByLocation", request, null, "en");

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
		request.setLayerIds(new String[] { LAYER_ID });
		request.setFilter("region='Region 1'");

		GeometryFactory factory = new GeometryFactory();
		LineString equator = factory.createLineString(new Coordinate[] { new Coordinate(0, 0),
				new Coordinate(-180, 180) });
		request.setLocation(converter.toDto(equator));

		// execute
		SearchByLocationResponse response = (SearchByLocationResponse) dispatcher.execute(
				"command.feature.SearchByLocation", request, null, "en");

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
	public void intersect50percentOverlapExactly() throws Exception {
		// prepare command
		SearchByLocationRequest request = new SearchByLocationRequest();
		request.setCrs("EPSG:4326");
		request.setQueryType(SearchByLocationRequest.QUERY_INTERSECTS);
		request.setSearchType(SearchByLocationRequest.SEARCH_ALL_LAYERS);
		request.setRatio(0.5f);
		request.setLayerIds(new String[] { LAYER_ID });

		// create a rectangle that overlaps 50 %
		GeometryFactory factory = new GeometryFactory();
		LinearRing half1 = factory.createLinearRing(new Coordinate[] { new Coordinate(0, 0), new Coordinate(1, 0),
				new Coordinate(1, 0.5), new Coordinate(0, 0.5), new Coordinate(0, 0) });
		Polygon polygon = factory.createPolygon(half1, null);
		request.setLocation(converter.toDto(polygon));

		// execute
		SearchByLocationResponse response = (SearchByLocationResponse) dispatcher.execute(
				"command.feature.SearchByLocation", request, null, "en");

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
		request.setLayerIds(new String[] { LAYER_ID });

		// create a rectangle that overlaps 49 %
		GeometryFactory factory = new GeometryFactory();
		LinearRing half1 = factory.createLinearRing(new Coordinate[] { new Coordinate(0, 0), new Coordinate(1, 0),
				new Coordinate(1, 0.49), new Coordinate(0, 0.49), new Coordinate(0, 0) });
		Polygon polygon = factory.createPolygon(half1, null);
		request.setLocation(converter.toDto(polygon));

		// execute
		SearchByLocationResponse response = (SearchByLocationResponse) dispatcher.execute(
				"command.feature.SearchByLocation", request, null, "en");
		// test
		List<Feature> features = response.getFeatureMap().get(LAYER_ID);
		Assert.assertNull(features);
	}
}
