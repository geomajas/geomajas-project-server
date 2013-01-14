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

import org.geomajas.global.GeomajasConstant;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheManagerServiceImpl;
import org.geomajas.service.GeoService;
import org.geomajas.service.TestRecorder;
import org.geomajas.spring.ThreadScopeContextHolder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for the cached variant of the GetBounds pipeline.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/META-INF/geomajasContext.xml", "/org/geomajas/plugin/caching/DefaultCachedPipelines.xml",
		"/cacheServiceContext.xml", "/dummySecurity.xml", "/org/geomajas/testdata/layerBeans.xml",
		"/org/geomajas/spring/testRecorder.xml"})
public class GetFeaturesInvalidateUpdateTest {

	private static final String LAYER_BEANS = "beans";

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
	@DirtiesContext
	public void testFeaturesInvalidateUpdate() throws Exception {
		List<InternalFeature> features;

		// first run, this should put things in the cache
		recorder.clear();
		features = vectorLayerService.getFeatures(LAYER_BEANS, geoService.getCrs2("EPSG:4326"), null, null,
				GeomajasConstant.FEATURE_INCLUDE_ATTRIBUTES);
		Assert.assertNotNull(features);
		Assert.assertEquals(3, features.size());
		Assert.assertEquals("", recorder.matches(CacheCategory.FEATURE,
				"Put item in cache"));

		// get features again, it should now use the cache
		recorder.clear();
		features = vectorLayerService.getFeatures(LAYER_BEANS, geoService.getCrs2("EPSG:4326"), null, null,
				GeomajasConstant.FEATURE_INCLUDE_ATTRIBUTES);
		Assert.assertNotNull(features);
		Assert.assertEquals(3, features.size());
		Assert.assertEquals("1", features.get(0).getId()); // assure testing same object later
		Assert.assertNotSame("something has changed", features.get(0).getAttributes().get("stringAttr").getValue());
		Assert.assertEquals("", recorder.matches(CacheCategory.FEATURE,
				"Got item from cache"));

		// update one item
		recorder.clear();
		InternalFeature org = features.get(0);
		InternalFeature upd = org.clone();
		upd.getAttributes().put("stringAttr", new StringAttribute("something has changed"));
		List<InternalFeature> orgFeatures = new ArrayList<InternalFeature>();
		orgFeatures.add(org);
		List<InternalFeature> updFeatures = new ArrayList<InternalFeature>();
		updFeatures.add(upd);
		vectorLayerService.saveOrUpdate(LAYER_BEANS, geoService.getCrs2("EPSG:4326"), orgFeatures, updFeatures);
		Assert.assertEquals("", recorder.matches("layer",
				"Invalidate geometry for old version of feature")); // not invalidating on new as not given/changed

		// get features again, it should *not* use the cache
		recorder.clear();
		features = vectorLayerService.getFeatures(LAYER_BEANS, geoService.getCrs2("EPSG:4326"), null, null,
				GeomajasConstant.FEATURE_INCLUDE_ATTRIBUTES);
		Assert.assertNotNull(features);
		Assert.assertEquals(3, features.size());
		Assert.assertEquals("1", features.get(0).getId()); // assure testing same object
		Assert.assertEquals("something has changed", features.get(0).getAttributes().get("stringAttr").getValue());
		Assert.assertEquals("", recorder.matches(CacheCategory.FEATURE,
				"Put item in cache"));
	}
}
