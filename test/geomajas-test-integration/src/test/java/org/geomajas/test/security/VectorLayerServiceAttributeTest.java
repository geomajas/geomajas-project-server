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

package org.geomajas.test.security;

import junit.framework.Assert;
import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandResponse;
import org.geomajas.layer.bean.BeanLayer;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.feature.attribute.IntegerAttribute;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.plugin.springsecurity.command.dto.LoginRequest;
import org.geomajas.plugin.springsecurity.command.dto.LoginResponse;
import org.geomajas.security.SecurityManager;
import org.geomajas.service.FilterService;
import org.geomajas.service.VectorLayerService;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
		"/org/geomajas/layer/bean/beanContext.xml", "/org/geomajas/layer/bean/layerBeans.xml",
		"/org/geomajas/test/security/VectorLayerSecurityAttribute.xml"})
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

	// assure we are logged in as a specific user to set correct authorizations
	public void login(String name) {
		LoginRequest request = new LoginRequest();
		request.setLogin(name);
		request.setPassword(name);
		CommandResponse response = commandDispatcher.execute("command.Login", request, null, "en");
		Assert.assertFalse(response.isError());
		Assert.assertTrue(response instanceof LoginResponse);
		securityManager.createSecurityContext(((LoginResponse)response).getToken());
	}

	@Test
	public void testGetFeaturesAttributeAuthorization() throws Exception {
		List<InternalFeature> features;
		CoordinateReferenceSystem crs = CRS.decode(beanLayer.getLayerInfo().getCrs());
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
	public void testSaveOrUpdateAttributeWritable() throws Exception {
		Filter filter;
		List<InternalFeature> oldFeatures;
		List<InternalFeature> newFeatures;
		InternalFeature feature;
		CoordinateReferenceSystem crs = CRS.decode(beanLayer.getLayerInfo().getCrs());

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