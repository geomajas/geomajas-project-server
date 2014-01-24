/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.staticsecurity.ldap;

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

import static org.fest.assertions.Assertions.assertThat;

/**
 * Integration test verifying the normal login procedure using the {@link LdapAuthenticationService}.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/staticsecurity/ldap/ldapConnection.xml"})
public class LdapLoginTest extends LdapServerProvider {

	@Autowired
	private LoginCommand loginCommand;

	@Autowired
	private AuthenticationTokenService tokenService;

	@Test
	public void testValidLogin() throws Exception {
		LoginRequest request = new LoginRequest();
		String userId = "test";
		request.setLogin(userId);
		request.setPassword("cred");
		LoginResponse response = loginCommand.getEmptyCommandResponse();
		loginCommand.execute(request, response);
		String token = response.getToken();
		assertThat(token).isNotNull();
		assertThat(response.getUserId()).isEqualTo(userId);
		assertThat(response.getUserName()).isEqualTo("Joe Tester");
		assertThat(response.getUserLocale()).isEqualTo("nl_be");
		assertThat(response.getUserOrganization()).isNull();
		assertThat(response.getUserDivision()).isNull();

		Authentication auth = tokenService.getAuthentication(token);
		Assert.assertEquals(2, auth.getAuthorizations().length); // defaultRole + assigned role
		BaseAuthorization authorizaton = auth.getAuthorizations()[0]; // default Role
		Assert.assertTrue(authorizaton.isToolAuthorized("bla"));
		Assert.assertFalse(authorizaton.isCommandAuthorized("bla"));
		authorizaton = auth.getAuthorizations()[1]; // assigned Role
		Assert.assertFalse(authorizaton.isToolAuthorized("bla"));
		Assert.assertTrue(authorizaton.isCommandAuthorized("bla"));
	}

	@Test
	public void testInvalidLogin() throws Exception {
		LoginRequest request = new LoginRequest();
		request.setLogin("chris");
		request.setPassword("chris");
		LoginResponse response = loginCommand.getEmptyCommandResponse();
		loginCommand.execute(request, response);
		assertThat(response.getToken()).isNull();

		request.setLogin("test");
		request.setPassword("some");
		loginCommand.execute(request, response);
		assertThat(response.getToken()).isNull();

		request.setLogin(null);
		request.setPassword("cred");
		loginCommand.execute(request, response);
		assertThat(response.getToken()).isNull();

		request.setLogin("test");
		request.setPassword(null);
		loginCommand.execute(request, response);
		assertThat(response.getToken()).isNull();

		request.setLogin("empty");
		request.setPassword("");
		loginCommand.execute(request, response);
		assertThat(response.getToken()).isNull();
	}

}
