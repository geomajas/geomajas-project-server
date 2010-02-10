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

package org.geomajas.plugin.springsecurity.command;

import org.geomajas.plugin.springsecurity.command.dto.LoginRequest;
import org.geomajas.plugin.springsecurity.command.dto.LoginResponse;
import org.geomajas.plugin.springsecurity.security.AuthenticationTokenService;
import org.geomajas.security.Authentication;
import org.geomajas.security.BaseAuthorization;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Verify the functioning of the LoginCommand class.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/springsecurity/security.xml"})
public class LoginCommandTest {

	@Autowired
	private LoginCommand loginCommand;

	@Autowired
	private AuthenticationTokenService tokenService;

	@Test
	public void testValidLogin() throws Exception {
		LoginRequest request = new LoginRequest();
		request.setLogin("luc");
		request.setPassword("luc");
		LoginResponse response = loginCommand.getEmptyCommandResponse();
		loginCommand.execute(request, response);
		String token = response.getToken();
		Assert.assertNotNull(token);

		Authentication auth = tokenService.getAuthentication(token);
		Assert.assertEquals(1, auth.getAuthorizations().length);
		BaseAuthorization authorizaton = auth.getAuthorizations()[0];
		Assert.assertTrue(authorizaton.isToolAuthorized("bla"));
		Assert.assertTrue(authorizaton.isCommandAuthorized("bla"));
		Assert.assertTrue(authorizaton.isLayerVisible("bla"));
		Assert.assertTrue(authorizaton.isLayerVisible("roads"));
		Assert.assertTrue(authorizaton.isLayerVisible("rivers"));
		Assert.assertTrue(authorizaton.isLayerUpdateAuthorized("bla"));
		Assert.assertTrue(authorizaton.isLayerCreateAuthorized("bla"));
		Assert.assertTrue(authorizaton.isLayerDeleteAuthorized("bla"));
	}

	@Test
	public void testValidLogin2() throws Exception {
		LoginRequest request = new LoginRequest();
		request.setLogin("marino");
		request.setPassword("marino");
		LoginResponse response = loginCommand.getEmptyCommandResponse();
		loginCommand.execute(request, response);
		String token = response.getToken();
		Assert.assertNotNull(token);

		Authentication auth = tokenService.getAuthentication(token);
		Assert.assertEquals(1, auth.getAuthorizations().length);
		BaseAuthorization authorizaton = auth.getAuthorizations()[0];
		Assert.assertFalse(authorizaton.isToolAuthorized("bla"));
		Assert.assertTrue(authorizaton.isCommandAuthorized("bla"));
		Assert.assertFalse(authorizaton.isLayerVisible("bla"));
		Assert.assertTrue(authorizaton.isLayerVisible("roads"));
		Assert.assertTrue(authorizaton.isLayerVisible("rivers"));
		Assert.assertFalse(authorizaton.isLayerUpdateAuthorized("bla"));
		Assert.assertFalse(authorizaton.isLayerCreateAuthorized("bla"));
		Assert.assertFalse(authorizaton.isLayerDeleteAuthorized("bla"));
	}

	@Test
	public void testInvalidLogin() throws Exception {
		LoginRequest request = new LoginRequest();
		request.setLogin("chris");
		request.setPassword("chris");
		LoginResponse response = loginCommand.getEmptyCommandResponse();
		loginCommand.execute(request, response);
		Assert.assertNull(response.getToken());

		request.setLogin("luc");
		request.setPassword("some");
		loginCommand.execute(request, response);
		Assert.assertNull(response.getToken());

		request.setLogin(null);
		request.setPassword("luc");
		loginCommand.execute(request, response);
		Assert.assertNull(response.getToken());

		request.setLogin("luc");
		request.setPassword(null);
		loginCommand.execute(request, response);
		Assert.assertNull(response.getToken());

		request.setLogin("empty");
		request.setPassword("");
		loginCommand.execute(request, response);
		Assert.assertNull(response.getToken());
	}
}
