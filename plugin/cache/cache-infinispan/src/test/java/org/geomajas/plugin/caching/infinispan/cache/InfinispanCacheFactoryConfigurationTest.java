/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.caching.infinispan.cache;

import org.geomajas.layer.Layer;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheManagerService;
import org.geomajas.service.TestRecorder;
import org.geomajas.spring.ThreadScopeContextHolder;
import org.geomajas.testdata.ReloadContext;
import org.geomajas.testdata.ReloadContextTestExecutionListener;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * Test for the configuration options in {@link InfinispanCacheFactory}.
 *
 * @author Joachim Van der Auwera
 */
@TestExecutionListeners(listeners = {ReloadContextTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/testdata/layerBeans.xml",
		"/dummySecurity.xml", "/org/geomajas/spring/testRecorder.xml", "/infinispanConfigurationContext.xml" })
@ReloadContext
public class InfinispanCacheFactoryConfigurationTest {

	@Autowired
	private TestRecorder recorder;

	@Autowired
	private CacheManagerService cacheManagerService;

	@Autowired
	@Qualifier("infiniLayer")
	private Layer layer;

	@After
	public void clearSecurityContext() {
		cacheManagerService.drop(layer);
		cacheManagerService.drop(null);
		recorder.clear();
		ThreadScopeContextHolder.clear();
	}

	/**
	 * Verify that the configured Infinispan configuration is used when requested.
	 */
	@Test
	public void testConfiguration() {
		// should be set in recorder from building the application context
		Assert.assertEquals("", recorder.matches("infinispan",
				"configuration name $bounds$defaultInfinispanCacheConfig",
				"configuration name test",
				// for some reason the @PostConstruct method is invoked twice... so need it twice here
				"configuration name $bounds$defaultInfinispanCacheConfig",
				"configuration name test"));
	}

	/**
	 * Assure we can put something in the cache and get it back both with and without layer.
	 */
	@Test
	public void testPutGet() {
		cacheManagerService.put(null, CacheCategory.BOUNDS, "blabla", "1", null);
		Assert.assertEquals("1", cacheManagerService.get(null, CacheCategory.BOUNDS, "blabla"));

		cacheManagerService.put(layer, CacheCategory.BOUNDS, "albalb", "2", null);
		Assert.assertEquals("2", cacheManagerService.get(layer, CacheCategory.BOUNDS, "albalb"));
	}

	/**
	 * Verify there is no caching when explicitly switched off or not configured.
	 */
	@Test
	public void testCacheOff() {
		cacheManagerService.put(layer, CacheCategory.FEATURE, "albalb", "2", null);
		Assert.assertNull(cacheManagerService.get(layer, CacheCategory.FEATURE, "albalb"));
	}
}
