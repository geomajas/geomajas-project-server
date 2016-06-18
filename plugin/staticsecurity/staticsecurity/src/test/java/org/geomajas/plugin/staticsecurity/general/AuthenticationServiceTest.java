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

package org.geomajas.plugin.staticsecurity.general;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.LogRequest;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.staticsecurity.command.dto.LoginRequest;
import org.geomajas.plugin.staticsecurity.command.dto.LoginResponse;
import org.geomajas.security.SecurityContext;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test using a custom {@link org.geomajas.plugin.staticsecurity.security.AuthenticationService}.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/staticsecurity/general/customAuthenticationService.xml"})
public class AuthenticationServiceTest {

	@Autowired
	private CommandDispatcher commandDispatcher;

	@Autowired
	private SecurityContext securityContext;

	/**
	 * Test using custom authentication service credentials.
	 *
	 * @throws Exception oops
	 */
	@Test
	public void testLoginAllowed() throws Exception {
		LoginRequest request = new LoginRequest();
		request.setLogin("custom");
		request.setPassword("custom");
		CommandResponse response = commandDispatcher.execute("command.staticsecurity.Login", request, null, null);
		Assert.assertTrue(response instanceof LoginResponse);
		Assert.assertFalse(response.isError());
		String token = ((LoginResponse)response).getToken();
		Assert.assertNotNull(token);
		Assert.assertNull(securityContext.getToken());

		LogRequest logRequest = new LogRequest();
		logRequest.setStatement("test log command access");
		response = commandDispatcher.execute(LogRequest.COMMAND, logRequest, token, "en");
		Assert.assertFalse(response.isError());
		Assert.assertNull(securityContext.getToken());
	}

	/**
	 * The default handling should also be added automatically, so logging in using the passed user info should
	 * also work.
	 *
	 * @throws Exception oops
	 */
	@Test
	public void testLoginChainingDefault() throws Exception {
		LoginRequest request = new LoginRequest();
		request.setLogin("luc");
		request.setPassword("luc");
		CommandResponse response = commandDispatcher.execute("command.staticsecurity.Login", request, null, null);
		Assert.assertTrue(response instanceof LoginResponse);
		Assert.assertFalse(response.isError());
		String token = ((LoginResponse)response).getToken();
		Assert.assertNotNull(token);
		Assert.assertNull(securityContext.getToken());

		LogRequest logRequest = new LogRequest();
		logRequest.setStatement("test log command access");
		response = commandDispatcher.execute(LogRequest.COMMAND, logRequest, token, "en");
		Assert.assertFalse(response.isError());
		Assert.assertNull(securityContext.getToken());
	}

	/**
	 * Custom service should not accept everything!
	 *
	 * @throws Exception oops
	 */
	@Test
	public void testLoginNotAllowed() throws Exception {
		LoginRequest request = new LoginRequest();
		request.setLogin("bla");
		request.setPassword("bla");
		CommandResponse response = commandDispatcher.execute(LoginRequest.COMMAND, request, null, null);
		Assert.assertTrue(response instanceof LoginResponse);
		Assert.assertFalse(response.isError());
		String token = ((LoginResponse)response).getToken();
		Assert.assertNull(token);
		Assert.assertNull(securityContext.getToken());
	}

}
