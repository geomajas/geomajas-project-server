package org.geomajas.plugin.reporting.data;

import junit.framework.Assert;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;
import net.sf.jasperreports.engine.design.JRDesignField;
import org.geomajas.global.GeomajasConstant;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.feature.attribute.ManyToOneAttribute;
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

import java.util.List;

/**
 * Test for {@link InternalFeatureDataSource}.
 *
 * @author Jan De Moerloose
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/layerCountries.xml", "/org/geomajas/testdata/layerBeans.xml",
		"/org/geomajas/plugin/reporting/security.xml"})
public class InternalFeatureDataSourceTest {

	@Autowired
	VectorLayerService service;

	@Autowired
	GeoService geoService;

	private static final String LAYER_COUNTRIES = "countries";
	private static final String LAYER_BEANS = "beans";

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
	public void testIterationAndAttributes() throws Exception {
		List<InternalFeature> features = service.getFeatures(LAYER_COUNTRIES, geoService.getCrs2("EPSG:4326"), null,
				null, GeomajasConstant.FEATURE_INCLUDE_ALL);
		JRRewindableDataSource data = new InternalFeatureDataSource(features);
		data.moveFirst();
		JRField name = createField("name", String.class);
		JRField opec = createField("opec", Integer.class);
		JRField id = createField("@id", Long.class);
		JRField featureField = createField("@", InternalFeature.class);
		int index = 0;
		while (index < features.size()) {
			Assert.assertTrue(data.next());
			InternalFeature feature = features.get(index++);
			Assert.assertEquals(feature.getAttributes().get("name").getValue(), data.getFieldValue(name));
			Assert.assertEquals(feature.getId(), data.getFieldValue(id));
			Assert.assertEquals(feature, data.getFieldValue(featureField));
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

	@Test
	public void testManyToOneAttribute() throws Exception {
		List<InternalFeature> features = service.getFeatures(LAYER_BEANS, geoService.getCrs2("EPSG:4326"), null,
				null, GeomajasConstant.FEATURE_INCLUDE_ALL);
		JRRewindableDataSource data = new InternalFeatureDataSource(features);
		data.moveFirst();
		JRField stringAttribute = createField("stringAttr", String.class);
		JRField many2OneString = createField("manyToOneAttr.stringAttr", String.class);
		int index = 0;
		while (index < features.size()) {
			Assert.assertTrue(data.next());
			InternalFeature feature = features.get(index++);
			Assert.assertEquals(feature.getAttributes().get("stringAttr").getValue(),
					data.getFieldValue(stringAttribute));
			ManyToOneAttribute manyToOne = (ManyToOneAttribute) feature.getAttributes().get("manyToOneAttr");
			if (null != manyToOne.getValue()) {
				Assert.assertEquals(manyToOne.getValue().getAttributeValue("stringAttr"),
						data.getFieldValue(many2OneString));
			} else {
				Assert.assertEquals(null, data.getFieldValue(many2OneString));
			}
		}
	}

	private JRField createField(String name, Class clss) {
		JRDesignField field = new JRDesignField();
		field.setName(name);
		field.setValueClass(clss);
		return field;
	}
}
