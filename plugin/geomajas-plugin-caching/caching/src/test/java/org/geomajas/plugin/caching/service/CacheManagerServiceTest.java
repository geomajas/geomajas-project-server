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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test for the CacheManagerServiceImpl class.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/managerContext.xml"})
public class CacheManagerServiceTest {

	@Autowired
	private CacheManagerServiceImpl cacheManager;

	@Test
	public void testGetInfo() throws Exception {
		CacheInfo info;

		info = cacheManager.getInfo("test", CacheCategory.SVG, CacheInfo.class);
		Assert.assertEquals("full2", ((DummyCacheFactory)info.getCacheFactory()).getTest());

		info = cacheManager.getInfo("test", CacheCategory.RASTER, CacheInfo.class);
		Assert.assertEquals("semiTest", ((DummyCacheFactory)info.getCacheFactory()).getTest());

		info = cacheManager.getInfo("bla", CacheCategory.SVG, CacheInfo.class);
		Assert.assertEquals("semiSVG", ((DummyCacheFactory)info.getCacheFactory()).getTest());

		info = cacheManager.getInfo("bla", CacheCategory.RASTER, CacheInfo.class);
		Assert.assertEquals("default", ((DummyCacheFactory)info.getCacheFactory()).getTest());
	}
}
