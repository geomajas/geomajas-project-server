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

package org.geomajas.internal.security;

import junit.framework.Assert;
import org.geomajas.internal.layer.feature.InternalFeatureImpl;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.security.AttributeAuthorization;
import org.geomajas.security.Authentication;
import org.geomajas.security.BaseAuthorization;
import org.geomajas.security.FeatureAuthorization;
import org.geomajas.security.SecurityContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Testing of AttributeAuthorization related data in the security context.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/layer/bean/beanContext.xml", "/org/geomajas/layer/bean/layerBeans.xml"})
public class SecurityContextAttributeAuthorizationTest {

	private static final String LAYER_ID = "beans";
	private static final String ATTRIBUTE_ID = "attr";

	private static final String SECURITY_SERVICE_ID = "ss";
	private static final String USER_ID = "auth";

	@Autowired
	private SecurityContext securityContext;

	@Test
	public void testBaseAuthorization() throws Exception {
		SecurityContextImpl securityContext = (SecurityContextImpl)this.securityContext;
		List<Authentication> authentications = new ArrayList<Authentication>();
		Authentication auth1 = getAuthentication(0); // allow nothing
		Authentication auth2 = getAuthentication(1); // base, allow all
		authentications.add(auth1);
		authentications.add(auth2);
		securityContext.setAuthentications("token", authentications);

		InternalFeature feature = new InternalFeatureImpl();
		Map<String, Object> attributes = new HashMap<String, Object>();
		feature.setAttributes(attributes);
		attributes.put(ATTRIBUTE_ID, "bla");
		Assert.assertTrue(securityContext.isAttributeReadable(LAYER_ID, feature, ATTRIBUTE_ID));
		Assert.assertTrue(securityContext.isAttributeWritable(LAYER_ID, feature, ATTRIBUTE_ID));
	}

	@Test
	public void testAllAndFeatureAuthorization() throws Exception {
		SecurityContextImpl securityContext = (SecurityContextImpl)this.securityContext;
		List<Authentication> authentications = new ArrayList<Authentication>();
		Authentication auth1 = getAuthentication(1); // base, allow all
		Authentication auth2 = getAttributeAuthentication();
		authentications.add(auth1);
		authentications.add(auth2);
		securityContext.setAuthentications("token", authentications);

		InternalFeature feature = new InternalFeatureImpl();
		Map<String, Object> attributes = new HashMap<String, Object>();
		feature.setAttributes(attributes);
		attributes.put(ATTRIBUTE_ID, "bla");
		Assert.assertTrue(securityContext.isAttributeReadable(LAYER_ID, feature, ATTRIBUTE_ID));
		Assert.assertTrue(securityContext.isAttributeWritable(LAYER_ID, feature, ATTRIBUTE_ID));
	}

	@Test
	public void testAttributeAuthorization() throws Exception {
		SecurityContextImpl securityContext = (SecurityContextImpl)this.securityContext;
		List<Authentication> authentications = new ArrayList<Authentication>();
		Authentication auth1 = getAuthentication(0); // allow nothing
		Authentication auth2 = getAttributeAuthentication();
		authentications.add(auth1);
		authentications.add(auth2);
		securityContext.setAuthentications("token", authentications);

		InternalFeature feature = new InternalFeatureImpl();
		Map<String, Object> attributes = new HashMap<String, Object>();
		feature.setAttributes(attributes);
		attributes.put(ATTRIBUTE_ID, "bla");
		Assert.assertTrue(securityContext.isAttributeReadable(LAYER_ID, feature, ATTRIBUTE_ID));
		Assert.assertTrue(securityContext.isAttributeWritable(LAYER_ID, feature, ATTRIBUTE_ID));

		feature.getAttributes().put(ATTRIBUTE_ID, "rea");
		Assert.assertFalse(securityContext.isAttributeReadable(LAYER_ID, feature, ATTRIBUTE_ID));
		Assert.assertTrue(securityContext.isAttributeWritable(LAYER_ID, feature, ATTRIBUTE_ID));

		feature.getAttributes().put(ATTRIBUTE_ID, "wri");
		Assert.assertTrue(securityContext.isAttributeReadable(LAYER_ID, feature, ATTRIBUTE_ID));
		Assert.assertFalse(securityContext.isAttributeWritable(LAYER_ID, feature, ATTRIBUTE_ID));
	}

	@Test
	public void testFeatureAuthorization() throws Exception {
		SecurityContextImpl securityContext = (SecurityContextImpl)this.securityContext;
		List<Authentication> authentications = new ArrayList<Authentication>();
		Authentication auth1 = getAuthentication(0); // allow nothing
		Authentication auth2 = getFeatureAuthentication();
		authentications.add(auth1);
		authentications.add(auth2);
		securityContext.setAuthentications("token", authentications);

		InternalFeature feature = new InternalFeatureImpl();
		Map<String, Object> attributes = new HashMap<String, Object>();
		feature.setAttributes(attributes);
		attributes.put(ATTRIBUTE_ID, "bla");
		Assert.assertTrue(securityContext.isAttributeReadable(LAYER_ID, feature, ATTRIBUTE_ID));
		Assert.assertTrue(securityContext.isAttributeWritable(LAYER_ID, feature, ATTRIBUTE_ID));

		feature.getAttributes().put(ATTRIBUTE_ID, "vis");
		Assert.assertFalse(securityContext.isAttributeReadable(LAYER_ID, feature, ATTRIBUTE_ID));
		Assert.assertTrue(securityContext.isAttributeWritable(LAYER_ID, feature, ATTRIBUTE_ID));

		feature.getAttributes().put(ATTRIBUTE_ID, "upd");
		Assert.assertTrue(securityContext.isAttributeReadable(LAYER_ID, feature, ATTRIBUTE_ID));
		Assert.assertFalse(securityContext.isAttributeWritable(LAYER_ID, feature, ATTRIBUTE_ID));
	}

	private Authentication getAuthentication(int which) {
		Authentication auth = new Authentication();
		auth.setAuthorizations(
				new BaseAuthorization[] {which == 1 ? new AllowAllAuthorization() : new AllowNoneAuthorization()});
		auth.setSecurityServiceId(SECURITY_SERVICE_ID + which);
		auth.setUserId(USER_ID);
		return auth;
	}

	private Authentication getAttributeAuthentication() {
		Authentication auth = new Authentication();
		auth.setAuthorizations(new BaseAuthorization[]{new TestAuthorization()});
		auth.setSecurityServiceId(SECURITY_SERVICE_ID);
		auth.setUserId(USER_ID);
		return auth;
	}

	private class TestAuthorization extends AllowAllAuthorization implements AttributeAuthorization {

		public boolean isAttributeReadable(String layerId, InternalFeature feature, String attribute) {
			return !"rea".equals(feature.getAttributes().get(attribute));
		}

		public boolean isAttributeWritable(String layerId, InternalFeature feature, String attribute) {
			return !"wri".equals(feature.getAttributes().get(attribute));
		}
	}

	private Authentication getFeatureAuthentication() {
		Authentication auth = new Authentication();
		auth.setAuthorizations(new BaseAuthorization[]{new TestFeatureAuthorization()});
		auth.setSecurityServiceId(SECURITY_SERVICE_ID);
		auth.setUserId(USER_ID);
		return auth;
	}

	private class TestFeatureAuthorization extends AllowAllAuthorization implements FeatureAuthorization {

		public boolean isFeatureVisible(String layerId, InternalFeature feature) {
			return !"vis".equals(feature.getAttributes().get(ATTRIBUTE_ID));
		}

		public boolean isFeatureUpdateAuthorized(String layerId, InternalFeature feature) {
			return !"upd".equals(feature.getAttributes().get(ATTRIBUTE_ID));
		}

		public boolean isFeatureUpdateAuthorized(String layerId, InternalFeature orgFeature,
				InternalFeature newFeature) {
			return !"org".equals(orgFeature.getAttributes().get(ATTRIBUTE_ID)) &&
					!"new".equals(newFeature.getAttributes().get(ATTRIBUTE_ID));
		}

		public boolean isFeatureDeleteAuthorized(String layerId, InternalFeature feature) {
			return !"del".equals(feature.getAttributes().get(ATTRIBUTE_ID));
		}

		public boolean isFeatureCreateAuthorized(String layerId, InternalFeature feature) {
			return !"cre".equals(feature.getAttributes().get(ATTRIBUTE_ID));
		}
	}
}
