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
package org.geomajas.layer.shapeinmem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.GeometricAttributeInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.PrimitiveType;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.layer.shapeinmem.ShapeInMemLayer;
import org.geomajas.service.FilterService;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class ShapeInMemLayerTest {

	private static final String SHAPE_FILE = "classpath:org/geomajas/testdata/shapes/cities_world/cities.shp";

	private ShapeInMemLayer layer;

	private Filter filter;

	@Before
	public void setUp() throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] {
				"org/geomajas/spring/geomajasContext.xml", "org/geomajas/testdata/layerCountries.xml",
				"org/geomajas/testdata/simplevectorsContext.xml", "org/geomajas/layer/shapeinmem/test.xml" });
		FilterService filterCreator = applicationContext.getBean("service.FilterService", FilterService.class);
		layer = applicationContext.getBean("test", ShapeInMemLayer.class);
		layer.setUrl(SHAPE_FILE);

		FeatureInfo ft = new FeatureInfo();
		ft.setDataSourceName("cities");

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
		pa.setLabel("City");
		pa.setName("City");
		pa.setEditable(false);
		pa.setIdentifying(true);
		pa.setType(PrimitiveType.STRING);

		attr.add(pa);
		ft.setAttributes(attr);

		VectorLayerInfo layerInfo = new VectorLayerInfo();
		layerInfo.setFeatureInfo(ft);
		layerInfo.setCrs("EPSG:4326");

		layer.setLayerInfo(layerInfo);
		filter = filterCreator.createCompareFilter("Population", ">", "49900");
	}

	@Test
	public void read() {
		SimpleFeature f = null;
		try {
			f = (SimpleFeature) layer.read("9703");
		} catch (Exception e) {
		}

		Assert.assertNotNull(f);
		Assert.assertEquals("Elmhurst", f.getAttribute("City"));
	}

	@Test
	public void create() {
		Object created = null;
		try {
			WKTReader wktReader = new WKTReader();
			Point geometry = null;
			try {
				geometry = (Point) wktReader.read("POINT (0 0)");
			} catch (ParseException e) {
			}

			SimpleFeature feature = CommonFactoryFinder.getFeatureFactory(null).createSimpleFeature(
					new Object[] { geometry, "Tsjakamaka", 342 }, layer.getSchema(), "100000");

			created = layer.create(feature);
		} catch (Exception e) {
		}
		Assert.assertNotNull(created);
	}

	@Test
	public void delete() {
		try {
			SimpleFeature f = (SimpleFeature) layer.read("10580");
			Assert.assertNotNull(f);
			layer.delete("10580");
			Assert.assertTrue(true);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	@Test
	public void getBounds() throws Exception {
		// Checked in QGis!
		Envelope bbox = layer.getBounds();
		Assert.assertEquals(-175.22, bbox.getMinX(), .0001);
		Assert.assertEquals(179.38, bbox.getMaxX(), .0001);
		Assert.assertEquals(-46.41, bbox.getMinY(), .0001);
		Assert.assertEquals(69.41, bbox.getMaxY(), .0001);
	}

	@Test
	public void getBoundsFilter() throws Exception {
		// Checked in QGis!
		Envelope bbox = layer.getBounds(filter);
		Assert.assertEquals(-118.01, bbox.getMinX(), .0001);
		Assert.assertEquals(120.86, bbox.getMaxX(), .0001);
		Assert.assertEquals(-19.99, bbox.getMinY(), .0001);
		Assert.assertEquals(53.09, bbox.getMaxY(), .0001);
	}

	@Test
	public void getElements() {
		try {
			// Checked in QGis!
			Iterator<?> it = layer.getElements(filter);
			int counter = 0;
			while (it.hasNext()) {
				it.next();
				counter++;
			}
			Assert.assertEquals(16, counter);
		} catch (Exception e) {
		}
	}
}