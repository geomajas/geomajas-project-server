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

import org.geomajas.global.GeomajasConstant;
import org.geomajas.internal.layer.feature.InternalFeatureImpl;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheManagerServiceImpl;
import org.geomajas.service.GeoService;
import org.geomajas.service.TestRecorder;
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
		"/pipelineContext.xml", "/org/geomajas/testdata/layerBeans.xml",
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
				"Got item from cache",
				"Put item in cache"));

		// update one item
		recorder.clear();
		InternalFeature org = features.get(0);
		InternalFeature upd = new InternalFeatureImpl(org);
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
