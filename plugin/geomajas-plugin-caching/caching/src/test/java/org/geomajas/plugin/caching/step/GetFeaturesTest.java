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

import org.geomajas.geometry.Crs;
import org.geomajas.global.GeomajasConstant;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.bean.BeanLayer;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheManagerServiceImpl;
import org.geomajas.plugin.caching.service.DummyCacheService;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;
import org.geomajas.service.TestRecorder;
import org.geomajas.spring.ThreadScopeContextHolder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
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
public class GetFeaturesTest {

	private static final String LAYER_BEANS = "beans";
	private static final String LAYER_COUNTRIES = "countries";
	private static final double DELTA = 1e-10;


	@Autowired
	@Qualifier(LAYER_BEANS)
	private VectorLayer layerBeans;

	@Autowired
	@Qualifier(LAYER_COUNTRIES)
	private VectorLayer layerCountries;

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

	private Crs wgs84;
	private Crs mercator;

	@Before
	public void init() throws LayerException {
		cacheManager.drop(layerBeans);
		securityManager.createSecurityContext(null); // assure a security context exists for this thread
		wgs84 = geoService.getCrs2("EPSG:4326");
		mercator = geoService.getCrs2("EPSG:900913");
	}

	@After
	public void clearSecurityContext() {
		cacheManager.drop(layerBeans);
		cacheManager.drop(layerCountries);
		recorder.clear();
		ThreadScopeContextHolder.clear();
	}

	@Test
	public void testFeatures() throws Exception {
		List<InternalFeature> features;

		// first run, this should put things in the cache
		recorder.clear();
		features = vectorLayerService.getFeatures(LAYER_BEANS, wgs84, null, null,
				GeomajasConstant.FEATURE_INCLUDE_NONE);
		Assert.assertNotNull(features);
		Assert.assertEquals(3, features.size());
		Assert.assertEquals("", recorder.matches(CacheCategory.FEATURE,
				"Put item in cache"));

		// verify that data is in the cache
		DummyCacheService cache = (DummyCacheService)cacheManager.getCacheForTesting(LAYER_BEANS, CacheCategory.FEATURE);
		Assert.assertEquals(1, cache.size());
		FeaturesCacheContainer bcc = (FeaturesCacheContainer) cache.getObject();
		bcc.getFeatures().remove(2);

		// get features again, the result should be different because we changed the cached value
		recorder.clear();
		features = vectorLayerService.getFeatures(LAYER_BEANS, wgs84, null, null,
				GeomajasConstant.FEATURE_INCLUDE_NONE);
		Assert.assertNotNull(features);
		Assert.assertEquals(2, features.size());
		Assert.assertEquals("", recorder.matches(CacheCategory.FEATURE,
				"Got item from cache"));

		// ask for different layer, should not be found in cache as context is different
		recorder.clear();
		features = vectorLayerService.getFeatures(LAYER_COUNTRIES, wgs84, null, null,
				GeomajasConstant.FEATURE_INCLUDE_NONE);
		Assert.assertNotNull(features);
		Assert.assertEquals(4, features.size());
		Assert.assertEquals("", recorder.matches(CacheCategory.FEATURE,
				"Put item in cache"));
	}

	@Test
	@DirtiesContext //changing lazy loading in layerBeans
	public void testFeaturesLazyConverted() throws Exception {
		((BeanLayer)layerBeans).setUseLazyFeatureConversion(true);

		List<InternalFeature> features;

		// first run, this should put things in the cache
		recorder.clear();
		features = vectorLayerService.getFeatures(LAYER_BEANS, wgs84, null, null,
				GeomajasConstant.FEATURE_INCLUDE_NONE);
		Assert.assertNotNull(features);
		Assert.assertEquals(3, features.size());
		Assert.assertEquals("", recorder.matches(CacheCategory.FEATURE)); // no "Put item in cache"

		// get features again, the result should be different because we changed the cached value
		recorder.clear();
		features = vectorLayerService.getFeatures(LAYER_BEANS, wgs84, null, null,
				GeomajasConstant.FEATURE_INCLUDE_NONE);
		Assert.assertNotNull(features);
		Assert.assertEquals(3, features.size());
		Assert.assertEquals("", recorder.matches(CacheCategory.FEATURE)); // no "Got item from cache"
	}

	@Test
	public void testFeaturesFiltered() throws Exception {
		List<InternalFeature> features;
		InternalFeature feature;
		Filter filter = filterService.createFidFilter(new String[] {"3"});

		// first run, this should put things in the cache
		recorder.clear();
		features = vectorLayerService.getFeatures(LAYER_BEANS, wgs84, filter, null,
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
		features = vectorLayerService.getFeatures(LAYER_BEANS, wgs84, filter, null,
				GeomajasConstant.FEATURE_INCLUDE_GEOMETRY);
		Assert.assertNotNull(features);
		Assert.assertEquals(1, features.size());
		feature = features.get(0);
		Assert.assertEquals(2.0, feature.getGeometry().getCoordinates()[0].x, DELTA);
		Assert.assertEquals(1.0, feature.getGeometry().getCoordinates()[0].y, DELTA);
		Assert.assertEquals("", recorder.matches(CacheCategory.FEATURE,
				"Got item from cache"));
	}

	@Test
	public void testFeaturesFilteredTransformed() throws Exception {
		List<InternalFeature> features;
		InternalFeature feature;
		Filter filter = filterService.createFidFilter(new String[] {"3"});

		// first run, this should put things in the cache
		recorder.clear();
		features = vectorLayerService.getFeatures(LAYER_BEANS, mercator, filter, null,
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
		features = vectorLayerService.getFeatures(LAYER_BEANS, mercator, filter, null,
				GeomajasConstant.FEATURE_INCLUDE_GEOMETRY);
		Assert.assertNotNull(features);
		Assert.assertEquals(1, features.size());
		feature = features.get(0);
		Assert.assertEquals(222638.98158654713, feature.getGeometry().getCoordinates()[0].x, DELTA);
		Assert.assertEquals(111325.14286638486, feature.getGeometry().getCoordinates()[0].y, DELTA);
		Assert.assertEquals("", recorder.matches(CacheCategory.FEATURE,
				"Got item from cache"));
	}
}
