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

package org.geomajas.plugin.caching.service;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKTReader;
import org.geomajas.layer.VectorLayer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test for the CacheManagerServiceImpl class.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/managerContext.xml", "/org/geomajas/testdata/layerCountries.xml",
		"/org/geomajas/testdata/layerBeans.xml"})
public class CacheManagerServiceTest {

	@Autowired
	private CacheManagerServiceImpl cacheManager;

	@Autowired
	@Qualifier("countries")
	private VectorLayer layer;

	@Autowired
	@Qualifier("beans")
	private VectorLayer otherLayer;

	Geometry geometry;
	Geometry overlappingGeometry;

	@Before
	public void init() throws Exception {
		WKTReader reader = new WKTReader(new GeometryFactory(new PrecisionModel(), 900913));
		geometry = reader.read("MULTIPOLYGON(((0 0,10 0,10 10,0 10,0 0)))");
		overlappingGeometry = reader.read("MULTIPOLYGON(((5 5,5 15,15 15,15 5,5 5)))");
	}

	@Test
	public void testGetInfo() throws Exception {
		CacheInfo info;

		// full match, use last
		info = cacheManager.getInfo("test", CacheCategory.SVG, CacheInfo.class);
		Assert.assertEquals("full2", ((DummyCacheFactory) info.getCacheFactory()).getTest());

		// match on layer only
		info = cacheManager.getInfo("test", CacheCategory.RASTER, CacheInfo.class);
		Assert.assertEquals("semiTest", ((DummyCacheFactory) info.getCacheFactory()).getTest());

		// match on category only
		info = cacheManager.getInfo("bla", CacheCategory.SVG, CacheInfo.class);
		Assert.assertEquals("semiSVG", ((DummyCacheFactory) info.getCacheFactory()).getTest());

		// no match, use default
		info = cacheManager.getInfo("bla", CacheCategory.RASTER, CacheInfo.class);
		Assert.assertEquals("default", ((DummyCacheFactory) info.getCacheFactory()).getTest());

		// match on layer only and on category only, check use layer
		info = cacheManager.getInfo("blabla", CacheCategory.VML, CacheInfo.class);
		Assert.assertEquals("semiBlabla", ((DummyCacheFactory) info.getCacheFactory()).getTest());
	}

	@Test
	public void testSimplePutGet() throws Exception {
		String data = "data";
		String key = "123";
		String otherKey = "321";
		cacheManager.put(layer, CacheCategory.REBUILD, key, data, geometry);
		Assert.assertEquals(data, cacheManager.get(layer, CacheCategory.REBUILD, key));
		Assert.assertEquals(data, cacheManager.get(layer, CacheCategory.REBUILD, key, String.class));
		Assert.assertNull(data, cacheManager.get(layer, CacheCategory.REBUILD, otherKey));
		Assert.assertNull(data, cacheManager.get(layer, CacheCategory.FEATURE, key));
		Assert.assertNull(data, cacheManager.get(otherLayer, CacheCategory.REBUILD, key));
		cacheManager.remove(layer, CacheCategory.REBUILD, key);
		Assert.assertNull(cacheManager.get(layer, CacheCategory.REBUILD, key));
	}

	@Test
	public void dropOne() throws Exception {
		String data = "data";
		String key = "123";
		cacheManager.put(layer, CacheCategory.REBUILD, key, data, geometry);
		Assert.assertEquals(data, cacheManager.get(layer, CacheCategory.REBUILD, key));
		cacheManager.drop(layer, CacheCategory.REBUILD);
		Assert.assertNull(cacheManager.get(layer, CacheCategory.REBUILD, key));
	}

	@Test
	public void dropLayer() throws Exception {
		String data = "data";
		String key = "123";
		cacheManager.put(layer, CacheCategory.REBUILD, key, data, geometry);
		cacheManager.put(layer, CacheCategory.FEATURE, key, data, geometry);
		Assert.assertEquals(data, cacheManager.get(layer, CacheCategory.REBUILD, key));
		Assert.assertEquals(data, cacheManager.get(layer, CacheCategory.FEATURE, key));
		cacheManager.drop(layer);
		Assert.assertNull(cacheManager.get(layer, CacheCategory.REBUILD, key));
		Assert.assertNull(cacheManager.get(layer, CacheCategory.FEATURE, key));
	}

	@Test
	public void invalidateOne() throws Exception {
		String data = "data";
		String key = "123";
		cacheManager.put(layer, CacheCategory.REBUILD, key, data, geometry);
		Assert.assertEquals(data, cacheManager.get(layer, CacheCategory.REBUILD, key));
		cacheManager.invalidate(layer, CacheCategory.REBUILD, overlappingGeometry);
		Assert.assertNull(cacheManager.get(layer, CacheCategory.REBUILD, key));
	}

	@Test
	public void invalidateLayer() throws Exception {
		String data = "data";
		String key = "123";
		cacheManager.put(layer, CacheCategory.REBUILD, key, data, geometry);
		cacheManager.put(layer, CacheCategory.FEATURE, key, data, geometry);
		Assert.assertEquals(data, cacheManager.get(layer, CacheCategory.REBUILD, key));
		Assert.assertEquals(data, cacheManager.get(layer, CacheCategory.FEATURE, key));
		cacheManager.invalidate(layer, overlappingGeometry);
		Assert.assertNull(cacheManager.get(layer, CacheCategory.REBUILD, key));
		Assert.assertNull(cacheManager.get(layer, CacheCategory.FEATURE, key));
	}

	@Test
	public void invalidateNoIndexLayer() throws Exception {
		String data = "data";
		String key = "123";
		cacheManager.put(otherLayer, CacheCategory.REBUILD, key, data, geometry);
		cacheManager.put(otherLayer, CacheCategory.FEATURE, key, data, geometry);
		Assert.assertEquals(data, cacheManager.get(otherLayer, CacheCategory.REBUILD, key));
		Assert.assertEquals(data, cacheManager.get(otherLayer, CacheCategory.FEATURE, key));
		cacheManager.invalidate(otherLayer, overlappingGeometry);
		Assert.assertNull(cacheManager.get(otherLayer, CacheCategory.REBUILD, key));
		Assert.assertNull(cacheManager.get(otherLayer, CacheCategory.FEATURE, key));
	}
}
