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

import org.geomajas.command.dto.GetVectorTileRequest;
import org.geomajas.geometry.Coordinate;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.layer.tile.TileMetadata;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheManagerServiceImpl;
import org.geomajas.plugin.caching.service.DummyCacheService;
import org.geomajas.service.TestRecorder;
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

import java.util.List;

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
public class GetTileTest {

	private static final String LAYER_BEANS = "beans";
	private static final String LAYER_COUNTRIES = "countries";
	private static final double DELTA = 1e-10;

	@Autowired
	@Qualifier(LAYER_BEANS)
	VectorLayer layerBeans;

	@Autowired
	private TestRecorder recorder;

	@Autowired
	private CacheManagerServiceImpl cacheManager;

	@Autowired
	private VectorLayerService vectorLayerService;

	@Autowired
	private org.geomajas.security.SecurityManager securityManager;

	@Before
	public void init() {
		cacheManager.drop(layerBeans);
		securityManager.createSecurityContext(null); // assure a security context exists for this thread
	}

	@After
	public void clearSecurityContext() {
		cacheManager.drop(layerBeans);
		recorder.clear();
		ThreadScopeContextHolder.clear();
	}

	@Test
	public void testGetTile() throws Exception {
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
		Assert.assertEquals("", recorder.matches(CacheCategory.TILE, "Put item in cache"));

		// verify that data is in the cache
		DummyCacheService cache = (DummyCacheService) cacheManager.getCacheForTesting(LAYER_BEANS, CacheCategory.TILE);
		Assert.assertEquals(1, cache.size());
		TileCacheContainer tcc = (TileCacheContainer) cache.getObject();
		tcc.getTile().setFeatureContent("<dummy />");

		// get tile again, the result should be different because we changed the cached value
		recorder.clear();
		tile = vectorLayerService.getTile(tmd);
		Assert.assertNotNull(tile);
		Assert.assertEquals("<dummy />", tile.getFeatureContent());
		Assert.assertEquals("", recorder.matches(CacheCategory.TILE, "Got item from cache"));
		Assert.assertEquals("", recorder.matches(CacheCategory.FEATURE));

		// ask for different layer, should not be found in cache as context is different
		recorder.clear();
		tmd.setLayerId(LAYER_COUNTRIES);
		tile = vectorLayerService.getTile(tmd);
		Assert.assertNotNull(tile);
		//Assert.assertEquals(4, features.size());
		Assert.assertEquals("", recorder.matches(CacheCategory.FEATURE, "Put item in cache"));
		Assert.assertEquals("", recorder.matches(CacheCategory.TILE, "Put item in cache"));
	}

	@Test
	public void testGetTileCheckFeatureStringCache() throws Exception {
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
		Assert.assertEquals("", recorder.matches(CacheCategory.TILE, "Put item in cache"));

		// verify that data is in the cache
		DummyCacheService cache = (DummyCacheService)cacheManager.getCacheForTesting(LAYER_BEANS, CacheCategory.TILE);
		Assert.assertEquals(1, cache.size());
		cache.clear(); // remove tile from cache

		// get tile again, should put tile in cache again but use features and string from cache
		recorder.clear();
		tile = vectorLayerService.getTile(tmd);
		Assert.assertNotNull(tile);
		Assert.assertEquals("<g id=\"beans.features.1-0-1\"/>", tile.getFeatureContent());
		Assert.assertEquals("", recorder.matches(CacheCategory.TILE, "Put item in cache"));
		Assert.assertEquals("", recorder.matches(CacheCategory.FEATURE, "Got item from cache"));
	}

	/**
	 * Test which verifies that the cached feature data is not changed while in the cache.
	 *
	 * @throws Exception oops
	 */
	@Test
	public void testGetTileWithTransformationCheckFeatureCache() throws Exception {
		InternalTile tile;
		TileMetadata tmd = new GetVectorTileRequest();
		tmd.setCrs("EPSG:900913");
		tmd.setCode(new TileCode(1,1,1));
		tmd.setLayerId(LAYER_BEANS);
		tmd.setRenderer(TileMetadata.PARAM_SVG_RENDERER);
		tmd.setScale(1.0);
		tmd.setPanOrigin(new Coordinate(0, 0));

		// first run, this should put things in the cache
		recorder.clear();
		tile = vectorLayerService.getTile(tmd);
		Assert.assertNotNull(tile);
		Assert.assertEquals(
				"<g id=\"beans.features.1-1-1\"><g style=\"fill:#995500;fill-opacity:0.6;stroke:#995500;" +
						"stroke-opacity:0.3;stroke-width:1px;\" id=\"beans.features.1-1-1.0\">" +
						"<path fill-rule=\"evenodd\" d=\"M445278 0l0 -334111 222639 0 0 334111 -222639 0 Z\" " +
						"id=\"2\"></path><path fill-rule=\"evenodd\" " +
						"d=\"M222639 -111325l0 -111359 556597 0 -111319 111359 -445278 0 Z\" id=\"3\"/></g></g>",
				tile.getFeatureContent());
		Assert.assertEquals("", recorder.matches(CacheCategory.FEATURE, "Put item in cache"));
		Assert.assertEquals("", recorder.matches(CacheCategory.TILE, "Put item in cache"));

		// verify that data is in the cache
		DummyCacheService cache = (DummyCacheService)cacheManager.getCacheForTesting(LAYER_BEANS, CacheCategory.TILE);
		Assert.assertEquals(1, cache.size());
		cache.clear(); // remove tile from cache

		cache = (DummyCacheService) cacheManager.getCacheForTesting(LAYER_BEANS, CacheCategory.FEATURE);
		Assert.assertEquals(1, cache.size());
		FeaturesCacheContainer fcc = (FeaturesCacheContainer) cache.getObject();
		List<InternalFeature> features = fcc.getFeatures();
		Assert.assertEquals(3, features.size());
		InternalFeature feature = features.get(2);
		Assert.assertEquals(2, feature.getGeometry().getCoordinates()[0].x, DELTA);
		Assert.assertEquals(1, feature.getGeometry().getCoordinates()[0].y, DELTA);

		// get tile again, should put tile in cache again but use features and string from cache
		recorder.clear();
		tile = vectorLayerService.getTile(tmd);
		Assert.assertNotNull(tile);
		Assert.assertEquals(
				"<g id=\"beans.features.1-1-1\"><g style=\"fill:#995500;fill-opacity:0.6;stroke:#995500;" +
						"stroke-opacity:0.3;stroke-width:1px;\" id=\"beans.features.1-1-1.0\">" +
						"<path fill-rule=\"evenodd\" d=\"M445278 0l0 -334111 222639 0 0 334111 -222639 0 Z\" " +
						"id=\"2\"></path><path fill-rule=\"evenodd\" " +
						"d=\"M222639 -111325l0 -111359 556597 0 -111319 111359 -445278 0 Z\" id=\"3\"/></g></g>",
				tile.getFeatureContent());
		Assert.assertEquals("", recorder.matches(CacheCategory.TILE, "Put item in cache"));
		Assert.assertEquals("", recorder.matches(CacheCategory.FEATURE, "Got item from cache"));

		cache = (DummyCacheService) cacheManager.getCacheForTesting(LAYER_BEANS, CacheCategory.FEATURE);
		Assert.assertEquals(1, cache.size());
		fcc = (FeaturesCacheContainer) cache.getObject();
		features = fcc.getFeatures();
		Assert.assertEquals(3, features.size());
		feature = features.get(2);
		Assert.assertEquals(2, feature.getGeometry().getCoordinates()[0].x, DELTA);
		Assert.assertEquals(1, feature.getGeometry().getCoordinates()[0].y, DELTA);

	}
}
