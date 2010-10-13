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

import org.geomajas.command.dto.GetVectorTileRequest;
import org.geomajas.geometry.Coordinate;
import org.geomajas.global.GeomajasConstant;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.layer.tile.TileMetadata;
import org.geomajas.plugin.caching.service.CacheCategory;
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

import javax.swing.*;
import java.util.List;

/**
 * Tests for the cached variant of the GetBounds pipeline.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/META-INF/geomajasContext.xml", "/org/geomajas/plugin/caching/DefaultCachedPipelines.xml",
		"/pipelineContext.xml", "/org/geomajas/testdata/layerBeans.xml", "/org/geomajas/testdata/layerCountries.xml",
		"/org/geomajas/spring/testRecorder.xml"})
public class GetTileTest {

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
	private org.geomajas.security.SecurityManager securityManager;

	@Test
	public void testGetTile() throws Exception {
		securityManager.createSecurityContext(null); // assure a security context exists for this thread
		InternalTile tile;
		TileMetadata tmd = new GetVectorTileRequest();
		tmd.setCrs("EPSG:4326");
		tmd.setCode(new TileCode(1,0,1));
		tmd.setLayerId(LAYER_BEANS);
		tmd.setRenderer(TileMetadata.PARAM_SVG_RENDERER);
		tmd.setScale(1.0);
		tmd.setPanOrigin(new Coordinate(0, 0));

		// first run, this should put things in the cache
		recorder.clear();
		tile = vectorLayerService.getTile(tmd);
		Assert.assertNotNull(tile);
		Assert.assertEquals("<g id=\"beans.features.1-0-1\"/>", tile.getFeatureContent());
		Assert.assertEquals("", recorder.matches(CacheCategory.FEATURE, "Put item in cache"));
		Assert.assertEquals("", recorder.matches(CacheCategory.SVG, "Put item in cache"));
		Assert.assertEquals("", recorder.matches(CacheCategory.TILE, "Put item in cache"));

		// verify that data is in the cache
		DummyCacheService cache = (DummyCacheService)cacheManager.getCacheForTesting(LAYER_BEANS, CacheCategory.TILE);
		Assert.assertEquals(1, cache.size());
		String key = cache.getKey();
		TileCacheContainer tcc = (TileCacheContainer) cache.getObject();
		tcc.getTile().setFeatureContent("<dummy />");

		// get tile again, the result should be different because we changed the cached value
		recorder.clear();
		tile = vectorLayerService.getTile(tmd);
		Assert.assertNotNull(tile);
		Assert.assertEquals("<dummy />", tile.getFeatureContent());
		Assert.assertEquals("", recorder.matches(CacheCategory.TILE, "Got item from cache"));
		Assert.assertEquals("", recorder.matches(CacheCategory.FEATURE));
		Assert.assertEquals("", recorder.matches(CacheCategory.SVG));

		// ask for different layer, should not be found in cache as context is different
		recorder.clear();
		tmd.setLayerId(LAYER_COUNTRIES);
		tile = vectorLayerService.getTile(tmd);
		Assert.assertNotNull(tile);
		//Assert.assertEquals(4, features.size());
		Assert.assertEquals("", recorder.matches(CacheCategory.FEATURE, "Put item in cache"));
		Assert.assertEquals("", recorder.matches(CacheCategory.SVG, "Put item in cache"));
		Assert.assertEquals("", recorder.matches(CacheCategory.TILE, "Put item in cache"));
	}
}
