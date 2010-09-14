package org.geomajas.rest.server;

import java.util.List;

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.security.SecurityManager;
import org.geomajas.service.FilterService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/testdata/layerBeans.xml" })
public class GeotoolsconverterServiceTest {

	@Autowired
	private GeotoolsConvertorService service;

	@Autowired
	@Qualifier("beans")
	private VectorLayer layer;

	@Autowired
	private VectorLayerService vectorLayerService;

	@Autowired
	private SecurityManager securityManager;

	@Autowired
	private FilterService filterService;

	@Before
	public void login() {
		// assure security context is set
		securityManager.createSecurityContext(null);
	}

	@Test
	public void testLayerBeanstoSimpleFeatureType() throws LayerException {
		SimpleFeatureType type = service.toSimpleFeatureType(layer.getLayerInfo());
		Assert.assertEquals("org.geomajas.layer.bean.FeatureBean", type.getName().getLocalPart());
		AttributeType doubleType = type.getType("doubleAttr");
		Assert.assertEquals(Double.class, doubleType.getBinding());
		Assert.assertEquals("doubleAttr", doubleType.getName().getLocalPart());
	}

	@Test
	public void testLayerBeansToSimpleFeature() throws GeomajasException {
		SimpleFeatureType type = service.toSimpleFeatureType(layer.getLayerInfo());
		List<InternalFeature> features = vectorLayerService.getFeatures("beans", null, filterService
				.createFidFilter(new String[] {"1"}), null, VectorLayerService.FEATURE_INCLUDE_ALL);
		Assert.assertEquals(1, features.size());
		SimpleFeature feature = service.toSimpleFeature(features.get(0), type);
		Assert.assertEquals("bean1", feature.getAttribute(0));
		Assert.assertEquals(true, feature.getAttribute(1));
	}
}
