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

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.LogRequest;
import org.geomajas.plugin.springsecurity.command.dto.LoginRequest;
import org.geomajas.plugin.springsecurity.command.dto.LoginResponse;
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
		"/org/geomajas/test/security/commandDispatcher.xml"})
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
		CommandResponse response = commandDispatcher.execute("command.Login", request, null, null);
		Assert.assertTrue(response instanceof LoginResponse);
		Assert.assertFalse(response.isError());
		String token = ((LoginResponse)response).getToken();
		Assert.assertNotNull(token);
		Assert.assertNull(securityContext.getToken());

		LogRequest logRequest = new LogRequest();
		logRequest.setStatement("test log command access");
		response = commandDispatcher.execute("command.general.Log", logRequest, token, "en");
		Assert.assertFalse(response.isError());
		Assert.assertNull(securityContext.getToken());

		// this test to verify the command itself (should fail here as "luc" should be logged in).
		response = commandDispatcher.execute("command.MarinoLoggedIn", null, token, "en");
		Assert.assertTrue(response.isError());
		Assert.assertEquals("java.lang.AssertionError", response.getErrorMessages().get(0));
		Assert.assertNull(securityContext.getToken());
	}

	@Test
	public void testLogNotAllowed() throws Exception {
		LoginRequest request = new LoginRequest();
		request.setLogin("marino");
		request.setPassword("marino");
		CommandResponse response = commandDispatcher.execute("command.Login", request, null, null);
		Assert.assertTrue(response instanceof LoginResponse);
		Assert.assertFalse(response.isError());
		String token = ((LoginResponse)response).getToken();
		Assert.assertNotNull(token);
		Assert.assertNull(securityContext.getToken());

		LogRequest logRequest = new LogRequest();
		logRequest.setStatement("test log command access");
		response = commandDispatcher.execute("command.general.Log", logRequest, token, "en");
		Assert.assertTrue(response.isError());
		Assert.assertEquals("User marino is not authorized to use the command command.general.Log. ",
				response.getErrorMessages().get(0)); // needs to be updated when messages file is changed
		Assert.assertNull(securityContext.getToken());

		response = commandDispatcher.execute("command.MarinoLoggedIn", null, token, "en");
		Assert.assertFalse(response.isError());
		Assert.assertNull(securityContext.getToken());
	}
}
