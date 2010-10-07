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

package org.geomajas.plugin.caching.step;

import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheContext;
import org.geomajas.plugin.caching.service.CacheManagerServiceImpl;
import org.geomajas.plugin.caching.service.DummyCacheService;
import org.geomajas.service.GeoService;
import org.geomajas.service.TestRecorder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
		"/pipelineContext.xml", "/org/geomajas/testdata/layerBeans.xml", "/org/geomajas/spring/testRecorder.xml"})
public class GetBoundsTest {

	private static final String LAYER_BEANS = "beans";
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
	private org.geomajas.security.SecurityManager securityManager;

	@Test
	public void testGetBounds() throws Exception {
		securityManager.createSecurityContext(null); // assure a security context exists for this thread
		Envelope bounds;

		// first run, this should put things in the cache
		recorder.clear();
		bounds = vectorLayerService.getBounds(LAYER_BEANS, geoService.getCrs("EPSG:4326"), null);
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
		bounds = vectorLayerService.getBounds(LAYER_BEANS, geoService.getCrs("EPSG:4326"), null);
		Assert.assertNotNull(bounds);
		Assert.assertEquals(0.0, bounds.getMinX(), DELTA);
		Assert.assertEquals(0.0, bounds.getMinY(), DELTA);
		Assert.assertEquals(10.0, bounds.getMaxX(), DELTA);
		Assert.assertEquals(10.0, bounds.getMaxY(), DELTA);
		Assert.assertEquals("", recorder.matches(CacheCategory.BOUNDS,
				"Got item from cache",
				"Put item in cache"));
	}
}
