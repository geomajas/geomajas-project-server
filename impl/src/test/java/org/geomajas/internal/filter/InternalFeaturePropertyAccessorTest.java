package org.geomajas.internal.filter;

import java.util.List;
import java.util.regex.Pattern;

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.feature.attribute.ManyToOneAttribute;
import org.geomajas.security.SecurityManager;
import org.geomajas.service.FilterService;
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
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/testdata/layerBeans.xml" })
public class InternalFeaturePropertyAccessorTest {

	@Autowired
	@Qualifier("beans")
	private VectorLayer layerBeans;

	@Autowired
	private VectorLayerService layerService;

	@Autowired
	private SecurityManager securityManager;

	@Autowired
	private FilterService filterService;

	@Before
	public void login() {
		// assure security context is set
		securityManager.createSecurityContext(null);
	}

	@After
	public void clearSecurityContext() {
		securityManager.clearSecurityContext();
	}

	@Test
	public void testSimpleProperty() throws GeomajasException {
		List<InternalFeature> features = layerService.getFeatures(layerBeans.getId(), null, null, null,
				VectorLayerService.FEATURE_INCLUDE_ALL);
		for (InternalFeature internalFeature : features) {
			Filter filter = filterService.createCompareFilter("stringAttr", "=",
					internalFeature.getAttributes().get("stringAttr").toString());
			Assert.assertTrue(filter.evaluate(internalFeature));
		}

	}

	@Test
	public void testGeometry() throws GeomajasException {
		List<InternalFeature> features = layerService.getFeatures(layerBeans.getId(), null, null, null,
				VectorLayerService.FEATURE_INCLUDE_ALL);
		for (InternalFeature internalFeature : features) {
			Filter filter = filterService.createContainsFilter(internalFeature.getGeometry(), "geometry");
			Assert.assertTrue(filter.evaluate(internalFeature));
		}
	}

	@Test
	public void testId() throws GeomajasException {
		List<InternalFeature> features = layerService.getFeatures(layerBeans.getId(), null, null, null,
				VectorLayerService.FEATURE_INCLUDE_ALL);
		for (InternalFeature internalFeature : features) {
			Filter filter = filterService.createCompareFilter("@id", "=", internalFeature.getId());
			Assert.assertTrue(filter.evaluate(internalFeature));
		}
	}
	
	@Test
	public void testManyToOne() throws GeomajasException {
		List<InternalFeature> features = layerService.getFeatures(layerBeans.getId(), null,
				filterService.createFidFilter(new String[] { "1" }), null, VectorLayerService.FEATURE_INCLUDE_ALL);
		for (InternalFeature internalFeature : features) {
			Filter filter = filterService.createCompareFilter("manyToOneAttr/stringAttr", "=",
					((ManyToOneAttribute) internalFeature.getAttributes().get("manyToOneAttr")).getValue()
							.getAttributeValue("stringAttr").toString());
			Assert.assertTrue(filter.evaluate(internalFeature));
		}
		for (InternalFeature internalFeature : features) {
			Filter filter = filterService.parseFilter("IN( 1 )");
			Assert.assertTrue(filter.evaluate(internalFeature));
		}
	}
	
	@Test
	public void testOneToMany() throws GeomajasException {
		List<InternalFeature> features = layerService.getFeatures(layerBeans.getId(), null,
				filterService.createFidFilter(new String[] { "1" }), null, VectorLayerService.FEATURE_INCLUDE_ALL);
		for (InternalFeature internalFeature : features) {
			Filter filter = filterService.createCompareFilter("oneToManyAttr[1]/stringAttr", "=","oneToMany - 2");
			Assert.assertTrue(filter.evaluate(internalFeature));
		}
	}

}
