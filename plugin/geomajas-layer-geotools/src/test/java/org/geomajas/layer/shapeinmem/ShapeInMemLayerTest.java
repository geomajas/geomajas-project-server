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

import java.util.Iterator;

import org.geomajas.layer.LayerException;
import org.geomajas.service.FilterService;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/layerCountries.xml", "/org/geomajas/testdata/simplevectorsContext.xml",
		"/org/geomajas/layer/shapeinmem/test.xml" })
public class ShapeInMemLayerTest {

	@Autowired
	private FilterService filterService;

	@Autowired()
	@Qualifier("test")
	private ShapeInMemLayer layer;

	private Filter filter;

	@Before
	public void setUp() throws Exception {
		filter = filterService.createCompareFilter("Population", ">", "49900");
	}

	@Test
	public void read() {
		SimpleFeature f = null;
		try {
			f = (SimpleFeature) layer.read("cities.9703");
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
			SimpleFeature f = (SimpleFeature) layer.read("cities.10580");
			Assert.assertNotNull(f);
			layer.delete("cities.10580");
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
			Iterator<?> it = layer.getElements(filter, 0, 0);
			int counter = 0;
			while (it.hasNext()) {
				it.next();
				counter++;
			}
			Assert.assertEquals(16, counter);
		} catch (Exception e) {
		}
	}
	
	@After
	public void refreshContext() throws LayerException {
		layer.initFeatures();
	}
	
}