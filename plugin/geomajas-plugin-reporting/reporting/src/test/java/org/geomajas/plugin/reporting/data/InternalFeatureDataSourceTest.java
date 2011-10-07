package org.geomajas.plugin.reporting.data;

import java.util.List;

import junit.framework.Assert;

import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;
import net.sf.jasperreports.engine.design.JRDesignField;

import org.geomajas.global.GeomajasConstant;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.security.SecurityManager;
import org.geomajas.service.GeoService;
import org.geomajas.spring.ThreadScopeContextHolder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test for {@link InternalFeatureDataSource}.
 *
 * @author Jan De Moerloose
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/layerCountries.xml", "/org/geomajas/plugin/reporting/security.xml" })
public class InternalFeatureDataSourceTest {

	@Autowired
	VectorLayerService service;

	@Autowired
	GeoService geoService;

	private static final String LAYER_COUNTRIES = "countries";

	@Autowired
	private SecurityManager securityManager;

	@Before
	public void login() {
		// assure security context is set
		securityManager.createSecurityContext(null);
	}

	@After
	public void logoutAndFixSideEffects() {
		// assure security context is set
		securityManager.clearSecurityContext();
		ThreadScopeContextHolder.clear();
	}

	@Test
	public void testIteration() throws Exception {
		List<InternalFeature> features = service.getFeatures(LAYER_COUNTRIES, geoService.getCrs2("EPSG:4326"), null,
				null, GeomajasConstant.FEATURE_INCLUDE_ALL);
		JRRewindableDataSource data = new InternalFeatureDataSource(features);
		data.moveFirst();
		JRField name = createField("name",String.class);
		JRField opec = createField("opec",Integer.class);
		int index = 0;
		while (index < features.size()) {
			Assert.assertTrue(data.next());
			InternalFeature feature = features.get(index++);
			Assert.assertEquals(feature.getAttributes().get("name").getValue(), data.getFieldValue(name));
		}
		Assert.assertFalse(data.next());
		data.moveFirst();
		index = 0;
		while (index < features.size()) {
			Assert.assertTrue(data.next());
			InternalFeature feature = features.get(index++);
			Assert.assertEquals(feature.getAttributes().get("opec").getValue(), data.getFieldValue(opec));
		}
	}

	private JRField createField(String name, Class clss) {
		JRDesignField field = new JRDesignField();
		field.setName(name);
		field.setValueClass(clss);
		return field;
	}
}
