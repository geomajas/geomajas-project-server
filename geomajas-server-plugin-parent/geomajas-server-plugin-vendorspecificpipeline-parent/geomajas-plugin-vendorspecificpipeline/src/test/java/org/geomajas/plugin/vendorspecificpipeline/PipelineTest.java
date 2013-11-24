package org.geomajas.plugin.vendorspecificpipeline;

import java.util.List;

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.feature.InternalFeature;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/vendorspecificpipeline/DefaultDelaySecurityPipelines.xml",
		"/org/geomajas/testdata/layerCountries.xml", "/org/geomajas/testdata/allowAll.xml",
		"/org/geomajas/spring/testRecorder.xml" })
public class PipelineTest {

	@Autowired
	private VectorLayerService vectorLayerService;

	@Autowired
	private org.geomajas.security.SecurityManager securityManager;

	@Autowired
	@Qualifier("countries")
	private VectorLayer layerCountries;

	@Autowired
	private GeoService geoService;

	@Autowired
	private TestRecorder recorder;

	@Before
	public void before() throws LayerException {
		securityManager.createSecurityContext(null); // assure a security context exists for this thread
		recorder.clear();
	}

	@After
	public void after() {
		ThreadScopeContextHolder.clear();
		recorder.clear();
	}

	@Test
	public void testDefaultDelaySecurityPipeline() throws LayerException, GeomajasException {
		List<InternalFeature> features = vectorLayerService.getFeatures(layerCountries.getId(),
				geoService.getCrs2("EPSG:4326"), Filter.INCLUDE, null, VectorLayerService.FEATURE_INCLUDE_ALL);
		Assert.assertNotNull(features);
		Assert.assertEquals(4, features.size());
		Assert.assertEquals("", recorder.matches("layer", "removed security filter before layer",
				"applied security filter after layer"));
	}
}
