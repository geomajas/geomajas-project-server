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
import java.util.List;

import junit.framework.Assert;

import org.geomajas.security.Authentication;
import org.geomajas.security.BaseAuthorization;
import org.geomajas.security.allowall.AllowAllAuthorization;
import org.junit.Test;

/**
 * Testing of BaseAuthorization related data in the security context.
 *
 * @author Joachim Van der Auwera
 */
public class SecurityContextBaseAuthorizationTest {

	private static final String TOOL_ID = "Tool";
	private static final String COMMAND_NAME = "Command";
	private static final String LAYER_ID = "layer";

	@Test
	public void testAuthorizedOne() {
		DefaultSecurityContext securityContext = new DefaultSecurityContext();
		List<Authentication> authentications = new ArrayList<Authentication>();
		Authentication authentication;
		authentication = new Authentication();
		authentication.setAuthorizations(
				new BaseAuthorization[]{new AllowAllAuthorization(), new AllowNoneAuthorization()});
		authentications.add(authentication);
		securityContext.setAuthentications("token", authentications);
		Assert.assertTrue(securityContext.isToolAuthorized(TOOL_ID));
		Assert.assertTrue(securityContext.isCommandAuthorized(COMMAND_NAME));
		Assert.assertTrue(securityContext.isLayerVisible(LAYER_ID));
		Assert.assertTrue(securityContext.isLayerCreateAuthorized(LAYER_ID));
		Assert.assertTrue(securityContext.isLayerUpdateAuthorized(LAYER_ID));
		Assert.assertTrue(securityContext.isLayerDeleteAuthorized(LAYER_ID));
	}

	@Test
	public void testAuthorizedOther() {
		DefaultSecurityContext securityContext = new DefaultSecurityContext();
		List<Authentication> authentications = new ArrayList<Authentication>();
		Authentication authentication;
		authentication = new Authentication();
		authentication.setAuthorizations(
				new BaseAuthorization[]{new AllowNoneAuthorization(), new AllowAllAuthorization()});
		authentications.add(authentication);
		securityContext.setAuthentications("token", authentications);
		Assert.assertTrue(securityContext.isToolAuthorized(TOOL_ID));
		Assert.assertTrue(securityContext.isCommandAuthorized(COMMAND_NAME));
		Assert.assertTrue(securityContext.isLayerVisible(LAYER_ID));
		Assert.assertTrue(securityContext.isLayerCreateAuthorized(LAYER_ID));
		Assert.assertTrue(securityContext.isLayerUpdateAuthorized(LAYER_ID));
		Assert.assertTrue(securityContext.isLayerDeleteAuthorized(LAYER_ID));
	}

	@Test
	public void testAuthorizedTwoAuthentications() {
		DefaultSecurityContext securityContext = new DefaultSecurityContext();
		List<Authentication> authentications = new ArrayList<Authentication>();
		Authentication authentication;
		authentication = new Authentication();
		authentication.setAuthorizations(new BaseAuthorization[]{new AllowNoneAuthorization()});
		authentications.add(authentication);
		authentication = new Authentication();
		authentication.setAuthorizations(new BaseAuthorization[]{new AllowAllAuthorization()});
		authentications.add(authentication);
		securityContext.setAuthentications("token", authentications);
		Assert.assertTrue(securityContext.isToolAuthorized(TOOL_ID));
		Assert.assertTrue(securityContext.isCommandAuthorized(COMMAND_NAME));
		Assert.assertTrue(securityContext.isLayerVisible(LAYER_ID));
		Assert.assertTrue(securityContext.isLayerCreateAuthorized(LAYER_ID));
		Assert.assertTrue(securityContext.isLayerUpdateAuthorized(LAYER_ID));
		Assert.assertTrue(securityContext.isLayerDeleteAuthorized(LAYER_ID));
	}

	@Test
	public void testAuthorizedNeither() {
		DefaultSecurityContext securityContext = new DefaultSecurityContext();
		Assert.assertFalse(securityContext.isToolAuthorized(TOOL_ID));
		Assert.assertFalse(securityContext.isCommandAuthorized(COMMAND_NAME));
		Assert.assertFalse(securityContext.isLayerVisible(LAYER_ID));
		Assert.assertFalse(securityContext.isLayerCreateAuthorized(LAYER_ID));
		Assert.assertFalse(securityContext.isLayerUpdateAuthorized(LAYER_ID));
		Assert.assertFalse(securityContext.isLayerDeleteAuthorized(LAYER_ID));
	}

	@Test
	public void testAuthorizedNone() {
		DefaultSecurityContext securityContext = new DefaultSecurityContext();
		Assert.assertFalse(securityContext.isToolAuthorized(TOOL_ID));
		Assert.assertFalse(securityContext.isCommandAuthorized(COMMAND_NAME));
		Assert.assertFalse(securityContext.isLayerVisible(LAYER_ID));
		Assert.assertFalse(securityContext.isLayerCreateAuthorized(LAYER_ID));
		Assert.assertFalse(securityContext.isLayerUpdateAuthorized(LAYER_ID));
		Assert.assertFalse(securityContext.isLayerDeleteAuthorized(LAYER_ID));
	}
}
