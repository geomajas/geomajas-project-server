/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.caching.cache;

import com.sun.deploy.association.Association;
import org.geomajas.layer.Layer;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheManagerService;
import org.geomajas.service.TestRecorder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test for the configuration options in {@link InfinispanCacheFactory}.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/layer/bean/beanContext.xml", "/org/geomajas/layer/bean/layerBeans.xml",
		"/org/geomajas/spring/testRecorder.xml", "/infinispanConfigurationContext.xml" })
public class InfinispanCacheFactoryConfigurationTest {

	@Autowired
	private TestRecorder recorder;

	@Autowired
	private CacheManagerService cacheManagerService;

	@Autowired
	@Qualifier("infiniLayer")
	private Layer layer;

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
}
