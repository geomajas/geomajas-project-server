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
package org.geomajas.internal.service;

import junit.framework.Assert;

import org.geomajas.service.CacheService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * @author Oliver May
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/testdata/layerBeans.xml"})
public class CacheServiceTest {

	private static final String CACHE_KEY = "testCache";
	
	@Autowired
	private CacheService cacheService;
	
	@Test
	public void testPutGet() {
		String s1 = "Test object";
		cacheService.put(CACHE_KEY, "myKey", s1);
		String s2 = cacheService.get(CACHE_KEY, "myKey", String.class);
		Assert.assertEquals(s1, s2);
	}
	
	@Test
	public void testCleanup() {
		String s1 = "Test string";
		cacheService.put(CACHE_KEY, "myKey", s1, -1L);
		((DefaultCacheService) cacheService).cleanUp();
		String s2 = cacheService.get(CACHE_KEY, "myKey", String.class);
		Assert.assertNull(s2);
	}
	
	@Test
	public void testClear() {
		String s1 = "Test string";
		cacheService.put(CACHE_KEY, "myKey", s1);
		cacheService.clear(CACHE_KEY);
		String s2 = cacheService.get(CACHE_KEY, "myKey", String.class);
		Assert.assertNull(s2);
	}
}
