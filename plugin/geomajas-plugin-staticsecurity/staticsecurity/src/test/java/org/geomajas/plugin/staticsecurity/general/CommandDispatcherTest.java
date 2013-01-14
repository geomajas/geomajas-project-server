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
 * Test {@link org.geomajas.command.CommandDispatcher} security.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/staticsecurity/general/commandDispatcher.xml"})
public class CommandDispatcherTest {

	@Autowired
	private CommandDispatcher commandDispatcher;

	@Autowired
	private SecurityContext securityContext;

	@Test
	public void testLogAllowed() throws Exception {
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

		// this test to verify the command itself (should fail here as "luc" should be logged in).
		response = commandDispatcher.execute("command.MarinoLoggedIn", null, token, "en");
		Assert.assertTrue(response.isError());
		Assert.assertTrue(response.getErrors().get(0) instanceof GeomajasException);
		Assert.assertEquals(ExceptionCode.TEST, ((GeomajasException)response.getErrors().get(0)).getExceptionCode());
		Assert.assertNull(securityContext.getToken());
	}

	@Test
	public void testLogNotAllowed() throws Exception {
		LoginRequest request = new LoginRequest();
		request.setLogin("marino");
		request.setPassword("marino");
		CommandResponse response = commandDispatcher.execute(LoginRequest.COMMAND, request, null, null);
		Assert.assertTrue(response instanceof LoginResponse);
		Assert.assertFalse(response.isError());
		String token = ((LoginResponse)response).getToken();
		Assert.assertNotNull(token);
		Assert.assertNull(securityContext.getToken());

		LogRequest logRequest = new LogRequest();
		logRequest.setStatement("test log command access");
		response = commandDispatcher.execute("command.general.Log", logRequest, token, "en");
		Assert.assertTrue(response.isError());
		Assert.assertEquals("User marino is not authorized to use the command command.general.Log.",
				response.getErrorMessages().get(0)); // needs to be updated when messages file is changed
		Assert.assertNull(securityContext.getToken());

		response = commandDispatcher.execute("command.MarinoLoggedIn", null, token, "en");
		Assert.assertFalse(response.isError());
		Assert.assertNull(securityContext.getToken());
	}
}
