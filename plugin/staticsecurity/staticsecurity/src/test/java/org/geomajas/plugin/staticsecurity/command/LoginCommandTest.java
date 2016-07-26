/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.staticsecurity.command;

import java.util.Collections;

import org.geomajas.plugin.staticsecurity.command.dto.LoginRequest;
import org.geomajas.plugin.staticsecurity.command.dto.LoginResponse;
import org.geomajas.plugin.staticsecurity.command.staticsecurity.LoginCommand;
import org.geomajas.plugin.staticsecurity.security.AuthenticationTokenService;
import org.geomajas.security.Authentication;
import org.geomajas.security.BaseAuthorization;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Verify the functioning of the {@link org.geomajas.plugin.staticsecurity.command.staticsecurity.LoginCommand} class.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/staticsecurity/security.xml"})
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
		Assert.assertEquals("luc", response.getUserId());
		Assert.assertEquals("Luc Van Lierde", response.getUserName());
		Assert.assertEquals("triathlon", response.getUserOrganization());
		Assert.assertEquals("all distances", response.getUserDivision());
		Assert.assertEquals("nl_BE", response.getUserLocale());
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
