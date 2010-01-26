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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import org.geomajas.command.CommandDispatcher;
import org.geomajas.extension.command.dto.SearchByLocationRequest;
import org.geomajas.extension.command.dto.SearchByLocationResponse;
import org.geomajas.layer.feature.Feature;
import org.geomajas.service.DtoConverter;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Test for the SearchByLocationCommand class.
 *
 * @author Pieter De Graef
 */
public class SearchByLocationCommandTest {

	private static final String LAYER_ID = "countries";

	private static CommandDispatcher DISPATCHER;

	private static DtoConverter CONVERTOR;

	@BeforeClass
	public static void setUp() {
//		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] {
//				"org/geomajas/spring/geomajasContext.xml", "org/geomajas/testdata/layerCountries.xml",
//				"org/geomajas/testdata/simplevectorsContext.xml"});
//		CONVERTOR = applicationContext.getBean("service.DtoConverter", DtoConverter.class);
//		DISPATCHER = applicationContext.getBean("command.CommandDispatcher", CommandDispatcher.class);
	}

	@Test
	public void intersectAfricaOnEquator() throws Exception {
//		// prepare command
//		SearchByLocationRequest request = new SearchByLocationRequest();
//		request.setCrs("EPSG:4326");
//		request.setQueryType(SearchByLocationRequest.QUERY_INTERSECTS);
//		request.setSearchType(SearchByLocationRequest.SEARCH_ALL_LAYERS);
//		request.setLayerIds(new String[] {LAYER_ID});
//
//		GeometryFactory factory = new GeometryFactory();
//		LineString equator = factory
//				.createLineString(new Coordinate[] {new Coordinate(0, 0), new Coordinate(360, 0)});
//		request.setLocation(CONVERTOR.toDto(equator));
//
//		// execute
//		SearchByLocationResponse response = (SearchByLocationResponse) DISPATCHER.execute(
//				"command.feature.SearchByLocation", request, null, "en");
//
//		// test
//		if (response.isError()) {
//			response.getErrors().get(0).printStackTrace();
//		}
//		List<Feature> features = response.getFeatureMap().get(LAYER_ID);
//		Assert.assertNotNull(features);
//		Assert.assertEquals(6, features.size());
//		List<String> names = new ArrayList<String>();
//		for (Feature feature : features) {
//			names.add(feature.getLabel());
//		}
//		Assert.assertTrue(names.contains("Gabon"));
//		Assert.assertTrue(names.contains("Congo"));
//		Assert.assertTrue(names.contains("Zaire"));
//		Assert.assertTrue(names.contains("Uganda"));
//		Assert.assertTrue(names.contains("Kenya"));
//		Assert.assertTrue(names.contains("Somalia"));
	}

	@Test
	public void intersect50percentAfricaBelowEquator() throws Exception {
//		// prepare command
//		SearchByLocationRequest request = new SearchByLocationRequest();
//		request.setCrs("EPSG:4326");
//		request.setQueryType(SearchByLocationRequest.QUERY_INTERSECTS);
//		request.setSearchType(SearchByLocationRequest.SEARCH_ALL_LAYERS);
//		request.setRatio(0.5f);
//		request.setLayerIds(new String[] {LAYER_ID});
//
//		GeometryFactory factory = new GeometryFactory();
//		LinearRing belowEquator = factory.createLinearRing(new Coordinate[] {new Coordinate(0, 0),
//				new Coordinate(360, 0), new Coordinate(360, -180), new Coordinate(0, -180), new Coordinate(0, 0)});
//		Polygon polygon = factory.createPolygon(belowEquator, null);
//		request.setLocation(CONVERTOR.toDto(polygon));
//
//		// execute
//		SearchByLocationResponse response = (SearchByLocationResponse) DISPATCHER.execute(
//				"command.feature.SearchByLocation", request, null, "en");
//
//		// test
//		if (response.isError()) {
//			response.getErrors().get(0).printStackTrace();
//		}
//		List<Feature> features = response.getFeatureMap().get(LAYER_ID);
//		Assert.assertNotNull(features);
//		List<String> names = new ArrayList<String>();
//		for (Feature feature : features) {
//			names.add(feature.getLabel());
//		}
//		Assert.assertTrue(names.contains("Gabon"));
//		Assert.assertTrue(names.contains("Congo"));
//		Assert.assertTrue(names.contains("Zaire"));
//		Assert.assertTrue(!names.contains("Uganda")); // less then 50% below equator
//		Assert.assertTrue(!names.contains("Kenya")); // less then 50% below equator
//		Assert.assertTrue(!names.contains("Somalia"));// less then 50% below equator
	}
}
