/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.caching.service;

import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.layer.VectorLayer;
import org.geomajas.spring.ThreadScopeContextHolder;
import org.junit.After;
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
		"/managerContext.xml", "/dummySecurity.xml", "/org/geomajas/testdata/layerCountries.xml",
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

	private Envelope envelope;
	private Envelope overlappingEnvelope;

	@Before
	public void init() throws Exception {
		envelope = new Envelope(0, 10, 0, 10);
		overlappingEnvelope = new Envelope(5, 15, 5, 15);
	}

	@After
	public void clearSecurityContext() {
		cacheManager.drop(layer);
		cacheManager.drop(otherLayer);
		ThreadScopeContextHolder.clear();
	}

	@Test
	public void testGetInfo() throws Exception {
		CacheServiceInfo serviceInfo;

		// full match, use last
		serviceInfo = cacheManager.getInfo("test", CacheCategory.BOUNDS, CacheServiceInfo.class);
		Assert.assertEquals("full2", ((DummyCacheFactory) serviceInfo.getCacheFactory()).getTest());

		// match on layer only
		serviceInfo = cacheManager.getInfo("test", CacheCategory.RASTER, CacheServiceInfo.class);
		Assert.assertEquals("semiTest", ((DummyCacheFactory) serviceInfo.getCacheFactory()).getTest());

		// match on category only
		serviceInfo = cacheManager.getInfo("bla", CacheCategory.BOUNDS, CacheServiceInfo.class);
		Assert.assertEquals("semiBOUNDS", ((DummyCacheFactory) serviceInfo.getCacheFactory()).getTest());

		// no match, use default
		serviceInfo = cacheManager.getInfo("bla", CacheCategory.RASTER, CacheServiceInfo.class);
		Assert.assertEquals("default", ((DummyCacheFactory) serviceInfo.getCacheFactory()).getTest());

		// match on layer only and on category only, check use layer
		serviceInfo = cacheManager.getInfo("blabla", CacheCategory.BOUNDS, CacheServiceInfo.class);
		Assert.assertEquals("semiBlabla", ((DummyCacheFactory) serviceInfo.getCacheFactory()).getTest());
	}

	@Test
	public void testSimplePutGet() throws Exception {
		String data = "data";
		String key = "123";
		String otherKey = "321";
		cacheManager.put(layer, CacheCategory.REBUILD, key, data, envelope);
		Assert.assertEquals(data, cacheManager.get(layer, CacheCategory.REBUILD, key));
		Assert.assertEquals(data, cacheManager.get(layer, CacheCategory.REBUILD, key, String.class));
		Assert.assertNull(data, cacheManager.get(layer, CacheCategory.REBUILD, otherKey));
		Assert.assertNull(data, cacheManager.get(layer, CacheCategory.FEATURE, key));
		Assert.assertNull(cacheManager.get(otherLayer, CacheCategory.REBUILD, key)); // DummyCacheFactory has different cache per layer
		cacheManager.remove(layer, CacheCategory.REBUILD, key);
		Assert.assertNull(cacheManager.get(layer, CacheCategory.REBUILD, key));
	}

	@Test
	public void dropOne() throws Exception {
		String data = "data";
		String key = "123";
		cacheManager.put(layer, CacheCategory.REBUILD, key, data, envelope);
		cacheManager.put(layer, CacheCategory.FEATURE, key, data, envelope);
		cacheManager.put(null, CacheCategory.REBUILD, key, data, envelope);
		cacheManager.put(null, CacheCategory.FEATURE, key, data, envelope);
		Assert.assertEquals(data, cacheManager.get(layer, CacheCategory.REBUILD, key));
		Assert.assertEquals(data, cacheManager.get(layer, CacheCategory.FEATURE, key));
		Assert.assertEquals(data, cacheManager.get(null, CacheCategory.REBUILD, key));
		Assert.assertEquals(data, cacheManager.get(null, CacheCategory.FEATURE, key));
		cacheManager.drop(layer, CacheCategory.REBUILD);
		Assert.assertNull(cacheManager.get(layer, CacheCategory.REBUILD, key));
		Assert.assertNull(cacheManager.get(null, CacheCategory.REBUILD, key));
		Assert.assertEquals(data, cacheManager.get(layer, CacheCategory.FEATURE, key));
		Assert.assertEquals(data, cacheManager.get(null, CacheCategory.FEATURE, key));
	}

	@Test
	public void dropLayer() throws Exception {
		String data = "data";
		String key = "123";
		cacheManager.put(layer, CacheCategory.REBUILD, key, data, envelope);
		cacheManager.put(layer, CacheCategory.FEATURE, key, data, envelope);
		cacheManager.put(null, CacheCategory.FEATURE, key, data, envelope);
		Assert.assertEquals(data, cacheManager.get(layer, CacheCategory.REBUILD, key));
		Assert.assertEquals(data, cacheManager.get(layer, CacheCategory.FEATURE, key));
		Assert.assertEquals(data, cacheManager.get(null, CacheCategory.FEATURE, key));
		cacheManager.drop(layer);
		Assert.assertNull(cacheManager.get(layer, CacheCategory.REBUILD, key));
		Assert.assertNull(cacheManager.get(layer, CacheCategory.FEATURE, key));
		Assert.assertNull(cacheManager.get(null, CacheCategory.FEATURE, key));
	}

	@Test
	public void invalidateOne() throws Exception {
		String data = "data";
		String key = "123";
		cacheManager.put(layer, CacheCategory.BOUNDS, key, data, envelope);
		cacheManager.put(layer, CacheCategory.FEATURE, key, data, envelope);
		cacheManager.put(null, CacheCategory.BOUNDS, key, data, envelope);
		cacheManager.put(null, CacheCategory.FEATURE, key, data, envelope);
		Assert.assertEquals(data, cacheManager.get(layer, CacheCategory.BOUNDS, key));
		Assert.assertEquals(data, cacheManager.get(layer, CacheCategory.FEATURE, key));
		Assert.assertEquals(data, cacheManager.get(null, CacheCategory.BOUNDS, key));
		Assert.assertEquals(data, cacheManager.get(null, CacheCategory.FEATURE, key));
		cacheManager.invalidate(layer, CacheCategory.BOUNDS, overlappingEnvelope);
		Assert.assertNull(cacheManager.get(layer, CacheCategory.BOUNDS, key));
		Assert.assertEquals(data, cacheManager.get(layer, CacheCategory.FEATURE, key));
		Assert.assertNull(cacheManager.get(null, CacheCategory.REBUILD, key));
		Assert.assertEquals(data, cacheManager.get(null, CacheCategory.FEATURE, key));
	}

	@Test
	public void invalidateLayer() throws Exception {
		String data = "data";
		String key = "123";
		cacheManager.put(layer, CacheCategory.BOUNDS, key, data, envelope);
		cacheManager.put(layer, CacheCategory.FEATURE, key, data, envelope);
		cacheManager.put(null, CacheCategory.FEATURE, key, data, envelope);
		Assert.assertEquals(data, cacheManager.get(layer, CacheCategory.BOUNDS, key));
		Assert.assertEquals(data, cacheManager.get(layer, CacheCategory.FEATURE, key));
		Assert.assertEquals(data, cacheManager.get(null, CacheCategory.FEATURE, key));
		cacheManager.invalidate(layer, overlappingEnvelope);
		Assert.assertNull(cacheManager.get(layer, CacheCategory.BOUNDS, key));
		Assert.assertNull(cacheManager.get(layer, CacheCategory.FEATURE, key));
		Assert.assertNull(cacheManager.get(null, CacheCategory.FEATURE, key));
	}

	@Test
	public void invalidateNoIndexLayer() throws Exception {
		String data = "data";
		String key = "123";
		cacheManager.put(otherLayer, CacheCategory.REBUILD, key, data, envelope);
		cacheManager.put(otherLayer, CacheCategory.FEATURE, key, data, envelope);
		cacheManager.put(null, CacheCategory.FEATURE, key, data, envelope);
		Assert.assertEquals(data, cacheManager.get(otherLayer, CacheCategory.REBUILD, key));
		Assert.assertEquals(data, cacheManager.get(otherLayer, CacheCategory.FEATURE, key));
		Assert.assertEquals(data, cacheManager.get(null, CacheCategory.FEATURE, key));
		cacheManager.invalidate(otherLayer, overlappingEnvelope);
		Assert.assertNull(cacheManager.get(otherLayer, CacheCategory.REBUILD, key));
		Assert.assertNull(cacheManager.get(otherLayer, CacheCategory.FEATURE, key));
		Assert.assertNull(cacheManager.get(null, CacheCategory.FEATURE, key));
	}
}
