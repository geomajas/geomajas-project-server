/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.geomajas.internal.layer.feature.InternalFeatureImpl;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.security.Authentication;
import org.geomajas.security.BaseAuthorization;
import org.geomajas.security.FeatureAuthorization;
import org.geomajas.security.SecurityContext;
import org.geomajas.security.allowall.AllowAllAuthorization;
import org.geomajas.testdata.ReloadContext;
import org.geomajas.testdata.ReloadContextTestExecutionListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * Testing of FeatureAuthorization related data in the security context.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(listeners = {ReloadContextTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class})
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/testdata/layerBeans.xml"})
@ReloadContext
public class SecurityContextFeatureAuthorizationTest {

	private static final String LAYER_ID = "beans";
	private static final String ATTRIBUTE_ID = "attr";

	private static final String SECURITY_SERVICE_ID = "ss";
	private static final String USER_ID = "auth";

	@Autowired
	private SecurityContext securityContext;

	@Test
	@DirtiesContext
	public void testBaseAuthorization() throws Exception {
		DefaultSecurityContext securityContext = (DefaultSecurityContext)this.securityContext;
		securityContext.setAuthentications(null, null);
		List<Authentication> authentications = new ArrayList<Authentication>();
		Authentication auth1 = getAuthentication(0); // allow nothing
		Authentication auth2 = getAuthentication(1); // base, allow all
		authentications.add(auth1);
		authentications.add(auth2);
		securityContext.setAuthentications("token", authentications);

		InternalFeature feature = new InternalFeatureImpl();
		Map<String, Attribute> attributes = new HashMap<String, Attribute>();
		feature.setAttributes(attributes);
		attributes.put(ATTRIBUTE_ID, new StringAttribute("bla"));
		Assert.assertTrue("1", securityContext.isFeatureVisible(LAYER_ID, feature));
		Assert.assertTrue("2", securityContext.isFeatureUpdateAuthorized(LAYER_ID, feature));
		Assert.assertTrue("3", securityContext.isFeatureUpdateAuthorized(LAYER_ID, feature, feature));
		Assert.assertTrue("4", securityContext.isFeatureDeleteAuthorized(LAYER_ID, feature));
		Assert.assertTrue("5", securityContext.isFeatureCreateAuthorized(LAYER_ID, feature));
	}

	@Test
	@DirtiesContext
	public void testAllAndFeatureAuthorization() throws Exception {
		DefaultSecurityContext securityContext = (DefaultSecurityContext)this.securityContext;
		List<Authentication> authentications = new ArrayList<Authentication>();
		Authentication auth1 = getAuthentication(1); // base, allow all
		Authentication auth2 = getFeatureAuthentication();
		authentications.add(auth1);
		authentications.add(auth2);
		securityContext.setAuthentications("token", authentications);

		InternalFeature feature = new InternalFeatureImpl();
		Map<String, Attribute> attributes = new HashMap<String, Attribute>();
		feature.setAttributes(attributes);
		attributes.put(ATTRIBUTE_ID, new StringAttribute("bla"));
		Assert.assertTrue(securityContext.isFeatureVisible(LAYER_ID, feature));
		Assert.assertTrue(securityContext.isFeatureUpdateAuthorized(LAYER_ID, feature));
		Assert.assertTrue(securityContext.isFeatureUpdateAuthorized(LAYER_ID, feature, feature));
		Assert.assertTrue(securityContext.isFeatureDeleteAuthorized(LAYER_ID, feature));
		Assert.assertTrue(securityContext.isFeatureCreateAuthorized(LAYER_ID, feature));
	}

	@Test
	@DirtiesContext
	public void testFeatureAuthorization() throws Exception {
		DefaultSecurityContext securityContext = (DefaultSecurityContext)this.securityContext;
		List<Authentication> authentications = new ArrayList<Authentication>();
		Authentication auth1 = getAuthentication(0); // allow nothing
		Authentication auth2 = getFeatureAuthentication();
		authentications.add(auth1);
		authentications.add(auth2);
		securityContext.setAuthentications("token", authentications);

		InternalFeature feature = new InternalFeatureImpl();
		Map<String, Attribute> attributes = new HashMap<String, Attribute>();
		feature.setAttributes(attributes);
		attributes.put(ATTRIBUTE_ID, new StringAttribute("bla"));
		Assert.assertTrue(securityContext.isFeatureVisible(LAYER_ID, feature));
		Assert.assertTrue(securityContext.isFeatureUpdateAuthorized(LAYER_ID, feature));
		Assert.assertTrue(securityContext.isFeatureUpdateAuthorized(LAYER_ID, feature, feature));
		Assert.assertTrue(securityContext.isFeatureDeleteAuthorized(LAYER_ID, feature));
		Assert.assertTrue(securityContext.isFeatureCreateAuthorized(LAYER_ID, feature));

		feature.getAttributes().put(ATTRIBUTE_ID, new StringAttribute("vis"));
		Assert.assertFalse(securityContext.isFeatureVisible(LAYER_ID, feature));
		Assert.assertTrue(securityContext.isFeatureUpdateAuthorized(LAYER_ID, feature));
		Assert.assertTrue(securityContext.isFeatureUpdateAuthorized(LAYER_ID, feature, feature));
		Assert.assertTrue(securityContext.isFeatureDeleteAuthorized(LAYER_ID, feature));
		Assert.assertTrue(securityContext.isFeatureCreateAuthorized(LAYER_ID, feature));

		feature.getAttributes().put(ATTRIBUTE_ID, new StringAttribute("cre"));
		Assert.assertTrue(securityContext.isFeatureVisible(LAYER_ID, feature));
		Assert.assertTrue(securityContext.isFeatureUpdateAuthorized(LAYER_ID, feature));
		Assert.assertTrue(securityContext.isFeatureUpdateAuthorized(LAYER_ID, feature, feature));
		Assert.assertTrue(securityContext.isFeatureDeleteAuthorized(LAYER_ID, feature));
		Assert.assertFalse(securityContext.isFeatureCreateAuthorized(LAYER_ID, feature));

		feature.getAttributes().put(ATTRIBUTE_ID, new StringAttribute("upd"));
		Assert.assertTrue(securityContext.isFeatureVisible(LAYER_ID, feature));
		Assert.assertFalse(securityContext.isFeatureUpdateAuthorized(LAYER_ID, feature));
		Assert.assertTrue(securityContext.isFeatureUpdateAuthorized(LAYER_ID, feature, feature));
		Assert.assertTrue(securityContext.isFeatureDeleteAuthorized(LAYER_ID, feature));
		Assert.assertTrue(securityContext.isFeatureCreateAuthorized(LAYER_ID, feature));

		feature.getAttributes().put(ATTRIBUTE_ID, new StringAttribute("org"));
		Assert.assertTrue(securityContext.isFeatureVisible(LAYER_ID, feature));
		Assert.assertTrue(securityContext.isFeatureUpdateAuthorized(LAYER_ID, feature));
		Assert.assertFalse(securityContext.isFeatureUpdateAuthorized(LAYER_ID, feature, feature));
		Assert.assertTrue(securityContext.isFeatureDeleteAuthorized(LAYER_ID, feature));
		Assert.assertTrue(securityContext.isFeatureCreateAuthorized(LAYER_ID, feature));

		feature.getAttributes().put(ATTRIBUTE_ID, new StringAttribute("new"));
		Assert.assertTrue(securityContext.isFeatureVisible(LAYER_ID, feature));
		Assert.assertTrue(securityContext.isFeatureUpdateAuthorized(LAYER_ID, feature));
		Assert.assertFalse(securityContext.isFeatureUpdateAuthorized(LAYER_ID, feature, feature));
		Assert.assertTrue(securityContext.isFeatureDeleteAuthorized(LAYER_ID, feature));
		Assert.assertTrue(securityContext.isFeatureCreateAuthorized(LAYER_ID, feature));

		feature.getAttributes().put(ATTRIBUTE_ID, new StringAttribute("del"));
		Assert.assertTrue(securityContext.isFeatureVisible(LAYER_ID, feature));
		Assert.assertTrue(securityContext.isFeatureUpdateAuthorized(LAYER_ID, feature));
		Assert.assertTrue(securityContext.isFeatureUpdateAuthorized(LAYER_ID, feature, feature));
		Assert.assertFalse(securityContext.isFeatureDeleteAuthorized(LAYER_ID, feature));
		Assert.assertTrue(securityContext.isFeatureCreateAuthorized(LAYER_ID, feature));
	}

	private Authentication getAuthentication(int which) {
		Authentication auth = new Authentication();
		auth.setAuthorizations(
				new BaseAuthorization[] {which == 1 ? new AllowAllAuthorization() : new AllowNoneAuthorization()});
		auth.setSecurityServiceId(SECURITY_SERVICE_ID + which);
		auth.setUserId(USER_ID);
		return auth;
	}

	private Authentication getFeatureAuthentication() {
		Authentication auth = new Authentication();
		auth.setAuthorizations(new BaseAuthorization[]{new TestAuthorization()});
		auth.setSecurityServiceId(SECURITY_SERVICE_ID);
		auth.setUserId(USER_ID);
		return auth;
	}

	private class TestAuthorization extends AllowAllAuthorization implements FeatureAuthorization {

		public boolean isFeatureVisible(String layerId, InternalFeature feature) {
			return !"vis".equals(feature.getAttributes().get(ATTRIBUTE_ID).getValue());
		}

		public boolean isFeatureUpdateAuthorized(String layerId, InternalFeature feature) {
			return !"upd".equals(feature.getAttributes().get(ATTRIBUTE_ID).getValue());
		}

		public boolean isFeatureUpdateAuthorized(String layerId, InternalFeature orgFeature,
				InternalFeature newFeature) {
			return !"org".equals(orgFeature.getAttributes().get(ATTRIBUTE_ID).getValue()) &&
					!"new".equals(newFeature.getAttributes().get(ATTRIBUTE_ID).getValue());
		}

		public boolean isFeatureDeleteAuthorized(String layerId, InternalFeature feature) {
			return !"del".equals(feature.getAttributes().get(ATTRIBUTE_ID).getValue());
		}

		public boolean isFeatureCreateAuthorized(String layerId, InternalFeature feature) {
			return !"cre".equals(feature.getAttributes().get(ATTRIBUTE_ID).getValue());
		}
	}
}
