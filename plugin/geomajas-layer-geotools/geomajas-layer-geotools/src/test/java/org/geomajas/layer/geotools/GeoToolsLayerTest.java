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
package org.geomajas.layer.geotools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.GeometryAttributeInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.PrimitiveType;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.layer.VectorLayer;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Test for GeoTools layer.
 * 
 * @author Pieter De Graef
 */
public class GeoToolsLayerTest extends AbstractGeoToolsTest {

	private GeoToolsLayer layer;

	private Filter filter;

	@Before
	public void init() throws Exception {
		layer = (GeoToolsLayer) applicationContext.getBean("test", VectorLayer.class);

		FeatureInfo ft = new FeatureInfo();
		ft.setDataSourceName(LAYER_NAME);

		PrimitiveAttributeInfo ia = new PrimitiveAttributeInfo();
		ia.setLabel("id");
		ia.setName("Id");
		ia.setType(PrimitiveType.STRING);
		ft.setIdentifier(ia);

		GeometryAttributeInfo ga = new GeometryAttributeInfo();
		ga.setName("the_geom");
		ga.setEditable(false);
		ft.setGeometryType(ga);

		List<AttributeInfo> attr = new ArrayList<AttributeInfo>();
		PrimitiveAttributeInfo pa = new PrimitiveAttributeInfo();
		pa.setLabel("Name");
		pa.setName("NAME");
		pa.setEditable(false);
		pa.setIdentifying(true);
		pa.setType(PrimitiveType.STRING);

		attr.add(pa);
		ft.setAttributes(attr);

		VectorLayerInfo layerInfo = new VectorLayerInfo();
		layerInfo.setFeatureInfo(ft);
		layerInfo.setCrs("EPSG:4326");

		layer.setLayerInfo(layerInfo);
		layer.initFeatures();
		filter = filterCreator.createCompareFilter(ATTRIBUTE_POPULATION, ">", "1000000");
	}

	@Test
	public void testRead() throws Exception {
		SimpleFeature f = (SimpleFeature) layer.read(LAYER_NAME + ".2"); // id always starts with layer id
		Assert.assertEquals("Vatican City", f.getAttribute(ATTRIBUTE_NAME));
		Assert.assertEquals(562430, f.getAttribute(ATTRIBUTE_POPULATION));
	}

	@Test
	public void testUpdate() throws Exception {
		SimpleFeature f = (SimpleFeature) layer.read(LAYER_NAME + ".3"); // id always starts with layer id
		f.setAttribute("NAME", "Luxembourg2");
		layer.update(f);
		Assert.assertEquals("Luxembourg2", f.getAttribute(ATTRIBUTE_NAME));
	}

	@Test
	public void create() throws Exception {
		WKTReader wktReader = new WKTReader();
		Point geometry = (Point) wktReader.read("POINT (0 0)");

		SimpleFeatureBuilder build = new SimpleFeatureBuilder(layer.getSchema());
		SimpleFeature feature = build.buildFeature("100000", new Object[] { geometry, "Tsjakamaka", 342 });

		Object created = layer.create(feature);
		Assert.assertNotNull(created);
	}

	@Test
	public void testDelete() throws Exception {
		SimpleFeature f = (SimpleFeature) layer.read(LAYER_NAME + ".4"); // id always starts with layer id
		Assert.assertNotNull(f);
		layer.delete(LAYER_NAME + ".4"); // id always starts with layer id
		Assert.assertTrue(true);
	}

	@Test
	public void testGetBounds() throws Exception {
		Envelope bbox = layer.getBounds();
		Assert.assertEquals(-175.22, bbox.getMinX(), .01);
		Assert.assertEquals(179.21, bbox.getMaxX(), .01);
		Assert.assertEquals(-41.29, bbox.getMinY(), .01);
		Assert.assertEquals(64.15, bbox.getMaxY(), .01);
	}

	@Test
	public void testGetBoundsFilter() throws Exception {
		Envelope bbox = layer.getBounds(filter);
		Assert.assertEquals(-122.34, bbox.getMinX(), .01);
		Assert.assertEquals(151.18, bbox.getMaxX(), .01);
		Assert.assertEquals(-37.81, bbox.getMinY(), .01);
		Assert.assertEquals(55.75, bbox.getMaxY(), .01);
	}

	@Test
	public void testGetElements() throws Exception {
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int counter = 0;
		while (it.hasNext()) {
			it.next();
			counter++;
		}
		Assert.assertEquals(198, counter);
	}
}