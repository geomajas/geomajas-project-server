/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.staticsecurity.command;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.LogRequest;
import org.geomajas.plugin.staticsecurity.command.dto.GetUsersRequest;
import org.geomajas.plugin.staticsecurity.command.dto.GetUsersResponse;
import org.geomajas.plugin.staticsecurity.command.dto.LoginRequest;
import org.geomajas.plugin.staticsecurity.command.dto.LoginResponse;
import org.geomajas.plugin.staticsecurity.command.dto.LogoutRequest;
import org.geomajas.plugin.staticsecurity.command.staticsecurity.GetUsersCommand;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Verify the functioning of the {@link org.geomajas.plugin.staticsecurity.command.staticsecurity.LoginCommand} class.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/staticsecurity/security.xml"})
public class GetUsersCommandTest {

	@Autowired
	private CommandDispatcher dispatcher;

	@Autowired
	private GetUsersCommand command;

	@Test
	public void directTest() throws Exception {
		GetUsersRequest request = new GetUsersRequest();
		GetUsersResponse response = command.getEmptyCommandResponse();
		command.execute(request, response);
		checkResult(response);

	}

	@Test
	public void roleTest() throws Exception {
		GetUsersRequest request = new GetUsersRequest();
		request.setRoles(Collections.singleton("someRole"));
		GetUsersResponse response = command.getEmptyCommandResponse();
		command.execute(request, response);
		Set<String> users = response.getUsers();
		Assert.assertEquals(1, users.size());
		Assert.assertTrue(users.contains("marino"));
	}

	private void checkResult(GetUsersResponse response) {
		Assert.assertNotNull(response);
		Set<String> users = response.getUsers();
		Assert.assertEquals(3, users.size());
		Assert.assertTrue(users.contains("luc"));
		Assert.assertTrue(users.contains("marino"));
		Assert.assertTrue(users.contains("empty"));
	}

	@Test
	public void dispatchedTest() throws Exception {
		// login
		LoginRequest login = new LoginRequest();
		login.setLogin("luc");
		login.setPassword("luc");
		LoginResponse loginResponse = (LoginResponse) dispatcher.execute(LoginRequest.COMMAND, login, null, "en");
		String token = loginResponse.getToken();

		GetUsersRequest request = new GetUsersRequest();
		CommandResponse response = dispatcher.execute(GetUsersRequest.COMMAND, request, token, "en");
		if (response.isError()) {
			response.getErrors().get(0).printStackTrace();
		}
		Assert.assertFalse(response.isError());
		Assert.assertTrue(response instanceof GetUsersResponse);
		checkResult((GetUsersResponse) response);

		// now logout
		dispatcher.execute(LogoutRequest.COMMAND, null, token, "en");
	}

}
