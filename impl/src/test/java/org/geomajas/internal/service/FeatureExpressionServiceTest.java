package org.geomajas.internal.service;

import java.util.List;

import junit.framework.Assert;

import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.bean.BeanLayer;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.feature.attribute.AssociationValue;
import org.geomajas.security.SecurityManager;
import org.geomajas.service.FeatureExpressionService;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests the {@link FeatureExpressionService}.
 * 
 * @author Jan De Moerloose
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/testdata/layerBeans.xml",
		"/org/geomajas/internal/service/featureExpressionServiceContext.xml" })
public class FeatureExpressionServiceTest {

	private static final String LAYER_ID = "beans";

	@Autowired
	private FeatureExpressionService expressionService;

	@Autowired
	private VectorLayerService layerService;

	@Autowired
	@Qualifier(LAYER_ID)
	private BeanLayer beanLayer;

	@Autowired
	private GeoService geoService;

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
	public void testPrimitiveExpressions() throws LayerException, GeomajasException {
		Filter filter = filterService.createFidFilter(new String[] { "1" });
		List<InternalFeature> features = layerService.getFeatures(LAYER_ID,
				geoService.getCrs2(beanLayer.getLayerInfo().getCrs()), filter, null,
				VectorLayerService.FEATURE_INCLUDE_ALL);
		Assert.assertEquals(1, features.size());
		String expression1 = "stringAttr"; // simple attribute
		String expression2 = "doubleAttr"; // simple double attribute
		String expression3 = "stringAttr?:'Elvis'"; // elvis expression (equivalent to #stringAttr.value != null ?
													// #stringAttr.value : 'Elvis')
		String expression4 = "'String attribute is '+stringAttr+' !'"; // concatenation
		String expression5 = "#dateFormat.format(dateAttr)"; // formatted date
		String expression6 = "id"; // id (type = string for internal features)

		Assert.assertEquals("bean1", expressionService.evaluate(expression1, features.get(0)));
		Assert.assertEquals(123.456, expressionService.evaluate(expression2, features.get(0)));
		Assert.assertEquals("bean1", expressionService.evaluate(expression3, features.get(0)));
		Assert.assertEquals("String attribute is bean1 !", expressionService.evaluate(expression4, features.get(0)));
		Assert.assertEquals("23/02/2010", expressionService.evaluate(expression5, features.get(0)));
		Assert.assertEquals("1", expressionService.evaluate(expression6, features.get(0)));
	}

	@Test
	public void testManyToOneExpressions() throws LayerException, GeomajasException {
		Filter filter = filterService.createFidFilter(new String[] { "1" });
		List<InternalFeature> features = layerService.getFeatures(LAYER_ID,
				geoService.getCrs2(beanLayer.getLayerInfo().getCrs()), filter, null,
				VectorLayerService.FEATURE_INCLUDE_ALL);
		Assert.assertEquals(1, features.size());

		String expression1 = "manyToOneAttr.toString()"; // returns object, apply toString
		String expression2 = "manyToOneAttr.id"; // id
		String expression3 = "manyToOneAttr.stringAttr"; // string attr
		String expression4 = "manyToOneAttr == null ? 'Yes' : 'No'"; // false if value not null

		Assert.assertEquals("{id=1, stringAttr=manyToOne - bean1}",
				expressionService.evaluate(expression1, features.get(0)));
		Assert.assertEquals(1L, expressionService.evaluate(expression2, features.get(0)));
		Assert.assertEquals("manyToOne - bean1", expressionService.evaluate(expression3, features.get(0)));
		Assert.assertEquals("No", expressionService.evaluate(expression4, features.get(0)));
	}

	@Test
	public void testOneToManyExpressions() throws LayerException, GeomajasException {
		Filter filter = filterService.createFidFilter(new String[] { "1" });
		List<InternalFeature> features = layerService.getFeatures(LAYER_ID,
				geoService.getCrs2(beanLayer.getLayerInfo().getCrs()), filter, null,
				VectorLayerService.FEATURE_INCLUDE_ALL);
		Assert.assertEquals(1, features.size());

		String expression1 = "oneToManyAttr.size()"; // methods can be called on the list
		String expression2 = "#test.evaluate(oneToManyAttr)"; // example of custom function

		Assert.assertEquals(2, expressionService.evaluate(expression1, features.get(0)));
		Assert.assertEquals("<oneToMany - 1,oneToMany - 2>", expressionService.evaluate(expression2, features.get(0)));
	}

	@Test
	public void testExceptions() throws LayerException, GeomajasException {
		Filter filter = filterService.createFidFilter(new String[] { "1" });
		List<InternalFeature> features = layerService.getFeatures(LAYER_ID,
				geoService.getCrs2(beanLayer.getLayerInfo().getCrs()), filter, null,
				VectorLayerService.FEATURE_INCLUDE_ALL);
		Assert.assertEquals(1, features.size());
		try {
			expressionService.evaluate("?", features.get(0));
			Assert.fail("Expected invalid expression for '?'");
		} catch (LayerException e) {
			Assert.assertEquals(ExceptionCode.EXPRESSION_INVALID, e.getExceptionCode());
		}
		try {
			expressionService.evaluate("missingAttribute", features.get(0));
			Assert.fail("Expected expression evaluation error for missing attribute");
		} catch (LayerException e) {
			Assert.assertEquals(ExceptionCode.EXPRESSION_EVALUATION_FAILED, e.getExceptionCode());
		}
	}

	/**
	 * This method can be called by adding our test class to the context variables, see
	 * '/org/geomajas/internal/service/attributeExpressionServiceContext.xml'
	 * 
	 * @param oneToMany
	 * @return
	 */
	public String evaluate(List<AssociationValue> oneToMany) {
		StringBuilder sb = new StringBuilder("<");
		for (AssociationValue associationValue : oneToMany) {
			if (sb.length() > 1) {
				sb.append(",");
			}
			sb.append(associationValue.getAttributeValue("stringAttr"));
		}
		sb.append(">");
		return sb.toString();
	}

}
