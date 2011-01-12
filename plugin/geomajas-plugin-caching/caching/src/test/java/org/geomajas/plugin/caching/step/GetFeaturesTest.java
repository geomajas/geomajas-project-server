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

package org.geomajas.plugin.caching.step;

import org.geomajas.global.GeomajasConstant;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheManagerServiceImpl;
import org.geomajas.plugin.caching.service.DummyCacheService;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;
import org.geomajas.service.TestRecorder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.filter.Filter;
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
		"/pipelineContext.xml", "/org/geomajas/testdata/layerBeans.xml", "/org/geomajas/testdata/layerCountries.xml",
		"/org/geomajas/spring/testRecorder.xml"})
public class GetFeaturesTest {

	private static final String LAYER_BEANS = "beans";
	private static final String LAYER_COUNTRIES = "countries";
	private static final double DELTA = 1e-10;

	@Autowired
	@Qualifier(LAYER_BEANS)
	private VectorLayer layerBeans;

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

	@Autowired
	private FilterService filterService;

	@Before
	public void init() {
		cacheManager.drop(layerBeans);
		securityManager.createSecurityContext(null); // assure a security context exists for this thread
	}

	@Test
	public void testFeatures() throws Exception {
		List<InternalFeature> features;

		// first run, this should put things in the cache
		recorder.clear();
		features = vectorLayerService.getFeatures(LAYER_BEANS, geoService.getCrs2("EPSG:4326"), null, null,
				GeomajasConstant.FEATURE_INCLUDE_NONE);
		Assert.assertNotNull(features);
		Assert.assertEquals(3, features.size());
		Assert.assertEquals("", recorder.matches(CacheCategory.FEATURE,
				"Put item in cache"));

		// verify that data is in the cache
		DummyCacheService cache = (DummyCacheService)cacheManager.getCacheForTesting(LAYER_BEANS, CacheCategory.FEATURE);
		Assert.assertEquals(1, cache.size());
		String key = cache.getKey();
		FeaturesCacheContainer bcc = (FeaturesCacheContainer) cache.getObject();
		bcc.getFeatures().remove(2);

		// get features again, the result should be different because we changed the cached value
		recorder.clear();
		features = vectorLayerService.getFeatures(LAYER_BEANS, geoService.getCrs2("EPSG:4326"), null, null,
				GeomajasConstant.FEATURE_INCLUDE_NONE);
		Assert.assertNotNull(features);
		Assert.assertEquals(2, features.size());
		Assert.assertEquals("", recorder.matches(CacheCategory.FEATURE,
				"Got item from cache",
				"Put item in cache"));

		// ask for different layer, should not be found in cache as context is different
		recorder.clear();
		features = vectorLayerService.getFeatures(LAYER_COUNTRIES, geoService.getCrs2("EPSG:4326"), null, null,
				GeomajasConstant.FEATURE_INCLUDE_NONE);
		Assert.assertNotNull(features);
		Assert.assertEquals(4, features.size());
		Assert.assertEquals("", recorder.matches(CacheCategory.FEATURE,
				"Put item in cache"));
	}

	@Test
	public void testFeaturesFiltered() throws Exception {
		List<InternalFeature> features;
		InternalFeature feature;
		Filter filter = filterService.createFidFilter(new String[] {"3"});

		// first run, this should put things in the cache
		recorder.clear();
		features = vectorLayerService.getFeatures(LAYER_BEANS, geoService.getCrs2("EPSG:4326"), filter, null,
				GeomajasConstant.FEATURE_INCLUDE_GEOMETRY);
		Assert.assertNotNull(features);
		Assert.assertEquals(1, features.size());
		feature = features.get(0);
		Assert.assertEquals(2.0, feature.getGeometry().getCoordinates()[0].x, DELTA);
		Assert.assertEquals(1.0, feature.getGeometry().getCoordinates()[0].y, DELTA);
		Assert.assertEquals("", recorder.matches(CacheCategory.FEATURE,
				"Put item in cache"));

		// get features again, the result should come from the cache and be the same
		recorder.clear();
		features = vectorLayerService.getFeatures(LAYER_BEANS, geoService.getCrs2("EPSG:4326"), filter, null,
				GeomajasConstant.FEATURE_INCLUDE_GEOMETRY);
		Assert.assertNotNull(features);
		Assert.assertEquals(1, features.size());
		feature = features.get(0);
		Assert.assertEquals(2.0, feature.getGeometry().getCoordinates()[0].x, DELTA);
		Assert.assertEquals(1.0, feature.getGeometry().getCoordinates()[0].y, DELTA);
		Assert.assertEquals("", recorder.matches(CacheCategory.FEATURE,
				"Got item from cache",
				"Put item in cache"));
	}

	@Test
	public void testFeaturesFilteredTransformed() throws Exception {
		List<InternalFeature> features;
		InternalFeature feature;
		Filter filter = filterService.createFidFilter(new String[] {"3"});

		// first run, this should put things in the cache
		recorder.clear();
		features = vectorLayerService.getFeatures(LAYER_BEANS, geoService.getCrs2("EPSG:900913"), filter, null,
				GeomajasConstant.FEATURE_INCLUDE_GEOMETRY);
		Assert.assertNotNull(features);
		Assert.assertEquals(1, features.size());
		feature = features.get(0);
		Assert.assertEquals(222638.98158654713, feature.getGeometry().getCoordinates()[0].x, DELTA);
		Assert.assertEquals(111325.14286638486, feature.getGeometry().getCoordinates()[0].y, DELTA);
		Assert.assertEquals("", recorder.matches(CacheCategory.FEATURE,
				"Put item in cache"));

		// get features again, the result should come from the cache and be the same
		recorder.clear();
		features = vectorLayerService.getFeatures(LAYER_BEANS, geoService.getCrs2("EPSG:900913"), filter, null,
				GeomajasConstant.FEATURE_INCLUDE_GEOMETRY);
		Assert.assertNotNull(features);
		Assert.assertEquals(1, features.size());
		feature = features.get(0);
		Assert.assertEquals(222638.98158654713, feature.getGeometry().getCoordinates()[0].x, DELTA);
		Assert.assertEquals(111325.14286638486, feature.getGeometry().getCoordinates()[0].y, DELTA);
		Assert.assertEquals("", recorder.matches(CacheCategory.FEATURE,
				"Got item from cache",
				"Put item in cache"));
	}
}
