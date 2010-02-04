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

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.GeometricAttributeInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.PrimitiveType;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.layer.VectorLayer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

public class GeoToolsFilterTest extends AbstractGeoToolsTest {

	private static final String SHAPE_FILE = "org/geomajas/testdata/shapes/filtertest/filtertest.shp";

	@Autowired
	@Qualifier("filterTest")
	private GeoToolsLayer layer;

	@Before
	public void setUp() throws Exception {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		URL url = classloader.getResource(SHAPE_FILE);

		FeatureInfo ft = new FeatureInfo();
		ft.setDataSourceName("filtertest");

		PrimitiveAttributeInfo ia = new PrimitiveAttributeInfo();
		ia.setLabel("id");
		ia.setName("Id");
		ia.setType(PrimitiveType.STRING);
		ft.setIdentifier(ia);

		GeometricAttributeInfo ga = new GeometricAttributeInfo();
		ga.setName("the_geom");
		ga.setEditable(false);
		ft.setGeometryType(ga);

		List<AttributeInfo> attr = new ArrayList<AttributeInfo>();
		PrimitiveAttributeInfo pa = new PrimitiveAttributeInfo();
		pa.setLabel("textAttr");
		pa.setName("textAttr");
		pa.setEditable(false);
		pa.setIdentifying(true);
		pa.setType(PrimitiveType.STRING);

		attr.add(pa);

		PrimitiveAttributeInfo pa2 = new PrimitiveAttributeInfo();
		pa2.setLabel("numberAttr");
		pa2.setName("numberAttr");
		pa2.setEditable(false);
		pa2.setIdentifying(true);
		pa2.setType(PrimitiveType.INTEGER);

		attr.add(pa2);
		ft.setAttributes(attr);

		VectorLayerInfo layerInfo = new VectorLayerInfo();
		layerInfo.setFeatureInfo(ft);
		layerInfo.setCrs("EPSG:4326");

		layer.setLayerInfo(layerInfo);
	}

	@Test
	public void testBetweenFilter() throws Exception {
		Filter filter = filterCreator.createBetweenFilter("numberAttr", "2", "8");
		Iterator<?> it = layer.getElements(filter);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(2, t);
	}

	@Test
	public void testCompareFilter() throws Exception {
		Filter filter = filterCreator.createCompareFilter("numberAttr", "<", "15");
		Iterator<?> it = layer.getElements(filter);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(4, t);
	}

	@Test
	public void testLikeFilter() throws Exception {
		Filter filter = filterCreator.createLikeFilter("textAttr", "*sid*");
		Iterator<?> it = layer.getElements(filter);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(2, t);
	}

	@Test
	public void testLogicFilter() throws Exception {
		Filter filter1 = filterCreator.createCompareFilter("numberAttr", "<", "15");
		Filter filter2 = filterCreator.createLikeFilter("textAttr", "over*");
		Filter filter = filterCreator.createLogicFilter(filter1, "and", filter2);

		Iterator<?> it = layer.getElements(filter);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(1, t);
	}

	@Test
	public void testFIDFilter() throws Exception {
		Filter filter = filterCreator.createFidFilter(new String[] { "1" });
		Iterator<?> it = layer.getElements(filter);
		SimpleFeature f = (SimpleFeature) it.next();
		Assert.assertEquals("centraal", f.getAttribute("textAttr"));
	}

	@Test
	public void testContainsFilter() throws Exception {
		Geometry geom = (Geometry) ((SimpleFeature) layer.read("3")).getDefaultGeometry();
		Filter filter = filterCreator.createContainsFilter(geom, "the_geom");
		Iterator<?> it = layer.getElements(filter);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void testWithinFilter() throws Exception {
		Geometry geom = (Geometry) ((SimpleFeature) layer.read("1")).getDefaultGeometry();
		Filter filter = filterCreator.createWithinFilter(geom, "the_geom");
		Iterator<?> it = layer.getElements(filter);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(4, t);
	}

	@Test
	public void testIntersectsFilter() throws Exception {
		Geometry geom = (Geometry) ((SimpleFeature) layer.read("1")).getDefaultGeometry();
		Filter filter = filterCreator.createIntersectsFilter(geom, "the_geom");
		Iterator<?> it = layer.getElements(filter);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(6, t);
	}

	@Test
	public void testTouchesFilter() throws Exception {
		Geometry geom = (Geometry) ((SimpleFeature) layer.read("1")).getDefaultGeometry();
		Filter filter = filterCreator.createTouchesFilter(geom, "the_geom");
		Iterator<?> it = layer.getElements(filter);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(1, t);
	}

	@Test
	public void testcreateBBoxFilter() throws Exception {
		Envelope bbox = new Envelope(-0.4d, -0.2d, -0.3d, 0.1d);
		Filter filter = filterCreator.createBboxFilter("EPSG:4326", bbox, "the_geom");
		Iterator<?> it = layer.getElements(filter);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void testOverlapsFilter() throws Exception {
		Geometry geom = (Geometry) ((SimpleFeature) layer.read("4")).getDefaultGeometry();
		Filter filter = filterCreator.createOverlapsFilter(geom, "the_geom");
		Iterator<?> it = layer.getElements(filter);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(1, t);
	}
}