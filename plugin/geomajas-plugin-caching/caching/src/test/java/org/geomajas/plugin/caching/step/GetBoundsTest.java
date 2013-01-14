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

package org.geomajas.plugin.caching.step;

import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheContext;
import org.geomajas.plugin.caching.service.CacheManagerServiceImpl;
import org.geomajas.plugin.caching.service.DummyCacheService;
import org.geomajas.service.GeoService;
import org.geomajas.service.TestRecorder;
import org.geomajas.spring.ThreadScopeContextHolder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests for the cached variant of the GetBounds pipeline.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/META-INF/geomajasContext.xml", "/org/geomajas/plugin/caching/DefaultCachedPipelines.xml",
		"/cacheServiceContext.xml", "/org/geomajas/testdata/layerBeans.xml", "/org/geomajas/testdata/layerCountries.xml",
		"/dummySecurity.xml", "/org/geomajas/spring/testRecorder.xml"})
public class GetBoundsTest {

	private static final String LAYER_BEANS = "beans";
	private static final String LAYER_COUNTRIES = "countries";
	private static final double DELTA = 1e-10;

	@Autowired
	private TestRecorder recorder;

	@Autowired
	private CacheManagerServiceImpl cacheManager;

	@Autowired
	private VectorLayerService vectorLayerService;

	@Autowired
	private GeoService geoService;

	@Autowired
	@Qualifier(LAYER_BEANS)
	private VectorLayer layerBeans;

	@Autowired
	private org.geomajas.security.SecurityManager securityManager;

	@After
	public void clearSecurityContext() {
		cacheManager.drop(layerBeans);
		recorder.clear();
		ThreadScopeContextHolder.clear();
	}

	@Test
	public void testGetBounds() throws Exception {
		securityManager.createSecurityContext(null); // assure a security context exists for this thread
		Envelope bounds;

		// first run, this should put things in the cache
		recorder.clear();
		bounds = vectorLayerService.getBounds(LAYER_BEANS, geoService.getCrs2("EPSG:4326"), null);
		Assert.assertNotNull(bounds);
		Assert.assertEquals(0.0, bounds.getMinX(), DELTA);
		Assert.assertEquals(0.0, bounds.getMinY(), DELTA);
		Assert.assertEquals(7.0, bounds.getMaxX(), DELTA);
		Assert.assertEquals(3.0, bounds.getMaxY(), DELTA);
		Assert.assertEquals("", recorder.matches(CacheCategory.BOUNDS,
				"Put item in cache"));

		// verify that data is in the cache
		DummyCacheService cache = (DummyCacheService)cacheManager.getCacheForTesting(LAYER_BEANS, CacheCategory.BOUNDS);
		Assert.assertEquals(1, cache.size());
		String key = cache.getKey();
		BoundsCacheContainer bcc = (BoundsCacheContainer) cache.getObject();
		CacheContext cc = bcc.getContext();
		bcc = new BoundsCacheContainer(new Envelope(0, 10, 0, 10));
		bcc.setContext(cc);
		cache.put(key, bcc);

		// get bounds again, the result should be different because we changed the cached value
		recorder.clear();
		bounds = vectorLayerService.getBounds(LAYER_BEANS, geoService.getCrs2("EPSG:4326"), null);
		Assert.assertNotNull(bounds);
		Assert.assertEquals(0.0, bounds.getMinX(), DELTA);
		Assert.assertEquals(0.0, bounds.getMinY(), DELTA);
		Assert.assertEquals(10.0, bounds.getMaxX(), DELTA);
		Assert.assertEquals(10.0, bounds.getMaxY(), DELTA);
		Assert.assertEquals("", recorder.matches(CacheCategory.BOUNDS,
				"Got item from cache"));

		// ask for different layer, should not be found in cache as context is different
		recorder.clear();
		bounds = vectorLayerService.getBounds(LAYER_COUNTRIES, geoService.getCrs2("EPSG:4326"), null);
		Assert.assertNotNull(bounds);
		Assert.assertEquals("", recorder.matches(CacheCategory.BOUNDS,
				"Put item in cache"));
	}
}
