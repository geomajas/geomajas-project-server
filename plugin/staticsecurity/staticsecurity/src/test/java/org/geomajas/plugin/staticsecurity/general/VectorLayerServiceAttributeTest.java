/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.staticsecurity.general;

import junit.framework.Assert;
import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandResponse;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.bean.BeanLayer;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.feature.attribute.IntegerAttribute;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.plugin.staticsecurity.command.dto.LoginRequest;
import org.geomajas.plugin.staticsecurity.command.dto.LoginResponse;
import org.geomajas.security.SecurityManager;
import org.geomajas.service.FilterService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for proper application of security in {@link VectorLayerServiceInvisibleLayerTest}.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/testdata/layerBeans.xml",
		"/org/geomajas/plugin/staticsecurity/general/VectorLayerSecurityAttribute.xml"})
public class VectorLayerServiceAttributeTest {

	private static final String LAYER_ID = "beans";
	private static final String STRING_ATTR = "stringAttr";
	private static final String BOOLEAN_ATTR = "booleanAttr";
	private static final String INTEGER_ATTR = "integerAttr";

	@Autowired
	private VectorLayerService layerService;

	@Autowired
	@Qualifier("beans")
	private BeanLayer beanLayer;

	@Autowired
	private SecurityManager securityManager;

	@Autowired
	private CommandDispatcher commandDispatcher;

	@Autowired
	private FilterService filterService;

	@After
	public void clearSecurityContext() {
		securityManager.clearSecurityContext();
	}

	// assure we are logged in as a specific user to set correct authorizations
	public void login(String name) {
		LoginRequest request = new LoginRequest();
		request.setLogin(name);
		request.setPassword(name);
		CommandResponse response = commandDispatcher.execute("command.staticsecurity.Login", request, null, "en");
		Assert.assertFalse(response.isError());
		Assert.assertTrue(response instanceof LoginResponse);
		securityManager.createSecurityContext(((LoginResponse)response).getToken());
	}

	@Test
	public void testGetFeaturesAttributeAuthorization() throws Exception {
		List<InternalFeature> features;
		CoordinateReferenceSystem crs = beanLayer.getCrs();
		InternalFeature feature;

		login("luc");
		features = layerService.getFeatures(LAYER_ID, crs, null, null, VectorLayerService.FEATURE_INCLUDE_ATTRIBUTES);
		Assert.assertEquals(3, features.size());
		feature = features.get(0);
		Assert.assertTrue(feature.isEditable());
		Assert.assertTrue(feature.isDeletable());
		Assert.assertTrue(feature.getAttributes().get(STRING_ATTR).isEditable());
		Assert.assertTrue(feature.getAttributes().get(BOOLEAN_ATTR).isEditable());
		Assert.assertTrue(feature.getAttributes().get(INTEGER_ATTR).isEditable());
		feature = features.get(1);
		Assert.assertTrue(feature.isEditable());
		Assert.assertTrue(feature.isDeletable());
		Assert.assertTrue(feature.getAttributes().get(STRING_ATTR).isEditable());
		Assert.assertTrue(feature.getAttributes().get(BOOLEAN_ATTR).isEditable());
		Assert.assertTrue(feature.getAttributes().get(INTEGER_ATTR).isEditable());
		feature = features.get(2);
		Assert.assertTrue(feature.isEditable());
		Assert.assertTrue(feature.isDeletable());
		Assert.assertTrue(feature.getAttributes().get(STRING_ATTR).isEditable());
		Assert.assertTrue(feature.getAttributes().get(BOOLEAN_ATTR).isEditable());
		Assert.assertTrue(feature.getAttributes().get(INTEGER_ATTR).isEditable());

		login("marino");
		features = layerService.getFeatures(LAYER_ID, crs, null, null, VectorLayerService.FEATURE_INCLUDE_ATTRIBUTES);
		Assert.assertEquals(3, features.size());
		for (InternalFeature testFeature : features) {
			if ("1".equals(testFeature.getId())) {
				Assert.assertTrue(testFeature.isEditable());
				Assert.assertFalse(testFeature.isDeletable());
				Assert.assertFalse(testFeature.getAttributes().get(STRING_ATTR).isEditable());
				Assert.assertNull(testFeature.getAttributes().get(BOOLEAN_ATTR));
				Assert.assertTrue(testFeature.getAttributes().get(INTEGER_ATTR).isEditable());
			} else if ("2".equals(testFeature.getId())) {
				Assert.assertTrue(testFeature.isEditable());
				Assert.assertFalse(testFeature.isDeletable());
				Assert.assertNull(testFeature.getAttributes().get(STRING_ATTR));
				Assert.assertNull(testFeature.getAttributes().get(BOOLEAN_ATTR));
				Assert.assertFalse(testFeature.getAttributes().get(INTEGER_ATTR).isEditable());
			} else if ("3".equals(testFeature.getId())) {
				Assert.assertTrue(testFeature.isEditable());
				Assert.assertFalse(testFeature.isDeletable());
				Assert.assertFalse(testFeature.getAttributes().get(STRING_ATTR).isEditable());
				Assert.assertNull(testFeature.getAttributes().get(BOOLEAN_ATTR));
				Assert.assertTrue(testFeature.getAttributes().get(INTEGER_ATTR).isEditable());
			} else {
				Assert.fail("wrong feature returned " + testFeature);
			}
		}
	}

	@Test
	@DirtiesContext
	public void testSaveOrUpdateAttributeWritable() throws Exception {
		Filter filter;
		List<InternalFeature> oldFeatures;
		List<InternalFeature> newFeatures;
		InternalFeature feature;
		CoordinateReferenceSystem crs = beanLayer.getCrs();

		login("marino");
		filter = filterService.createFidFilter(new String[]{"1"});
		oldFeatures = layerService.getFeatures(LAYER_ID, crs, filter, null,
				VectorLayerService.FEATURE_INCLUDE_ATTRIBUTES);
		Assert.assertEquals(1, oldFeatures.size());
		feature = oldFeatures.get(0);
		newFeatures = new ArrayList<InternalFeature>();
		feature = feature.clone();
		newFeatures.add(feature);
		feature.getAttributes().put(STRING_ATTR, new StringAttribute("changed"));
		feature.getAttributes().put(INTEGER_ATTR, new IntegerAttribute(12345));
		layerService.saveOrUpdate(LAYER_ID, crs, oldFeatures, newFeatures);
		// check changes
		filter = filterService.createFidFilter(new String[]{"1"});
		oldFeatures = layerService.getFeatures(LAYER_ID, crs, filter, null,
				VectorLayerService.FEATURE_INCLUDE_ATTRIBUTES);
		Assert.assertEquals(1, oldFeatures.size());
		feature = oldFeatures.get(0);
		Assert.assertEquals("bean1", feature.getAttributes().get(STRING_ATTR).getValue()); // org value, not updated		
		Assert.assertEquals(12345, feature.getAttributes().get(INTEGER_ATTR).getValue()); // updated
	}
}