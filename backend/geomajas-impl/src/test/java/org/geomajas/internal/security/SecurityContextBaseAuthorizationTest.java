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
import org.geomajas.security.Authentication;
import org.geomajas.security.BaseAuthorization;
import org.geomajas.security.allowall.AllowAllAuthorization;
import org.geomajas.security.allowall.SecurityContextImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
		SecurityContextImpl securityContext = new SecurityContextImpl();
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
		SecurityContextImpl securityContext = new SecurityContextImpl();
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
		SecurityContextImpl securityContext = new SecurityContextImpl();
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
		SecurityContextImpl securityContext = new SecurityContextImpl();
		Assert.assertFalse(securityContext.isToolAuthorized(TOOL_ID));
		Assert.assertFalse(securityContext.isCommandAuthorized(COMMAND_NAME));
		Assert.assertFalse(securityContext.isLayerVisible(LAYER_ID));
		Assert.assertFalse(securityContext.isLayerCreateAuthorized(LAYER_ID));
		Assert.assertFalse(securityContext.isLayerUpdateAuthorized(LAYER_ID));
		Assert.assertFalse(securityContext.isLayerDeleteAuthorized(LAYER_ID));
	}

	@Test
	public void testAuthorizedNone() {
		SecurityContextImpl securityContext = new SecurityContextImpl();
		Assert.assertFalse(securityContext.isToolAuthorized(TOOL_ID));
		Assert.assertFalse(securityContext.isCommandAuthorized(COMMAND_NAME));
		Assert.assertFalse(securityContext.isLayerVisible(LAYER_ID));
		Assert.assertFalse(securityContext.isLayerCreateAuthorized(LAYER_ID));
		Assert.assertFalse(securityContext.isLayerUpdateAuthorized(LAYER_ID));
		Assert.assertFalse(securityContext.isLayerDeleteAuthorized(LAYER_ID));
	}
}
