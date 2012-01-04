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

package org.geomajas.plugin.staticsecurity.ldap;

import org.geomajas.plugin.staticsecurity.configuration.AuthorizationInfo;
import org.geomajas.plugin.staticsecurity.configuration.LayerAuthorizationInfo;
import org.geomajas.plugin.staticsecurity.configuration.UserInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Test for {@link LdapAuthenticationService}.
 *
 * @author Joachim Van der Auwera
 */
public class LdapAuthenticationServiceTest extends LdapServerProvider {

	private LdapAuthenticationService service = new LdapAuthenticationService();

	@Before
	public void start() throws Exception {
		service.setServerHost("localhost");
		service.setServerPort(PORT);
		service.setAllowAllSocketFactory(false);
		service.setUserDnTemplate("cn={},dc=staticsecurity,dc=geomajas,dc=org");
		service.setGivenNameAttribute("givenName");
		service.setSurNameAttribute("sn");
		service.setOrganizationAttribute("cn");
		service.setLocaleAttribute("locale");
		service.setDivisionAttribute("objectClass");
		service.setRolesAttribute("memberOf");
		Map<String, List<AuthorizationInfo>> roles = new HashMap<String, List<AuthorizationInfo>>();
		List<AuthorizationInfo> auths = new ArrayList<AuthorizationInfo>();
		LayerAuthorizationInfo auth = new LayerAuthorizationInfo();
		List<String> includes = new ArrayList<String>();
		includes.add(".*");
		auth.setCommandsInclude(includes);
		auths.add(auth);
		roles.put("cn=testgroup,dc=roles,dc=geomajas,dc=org", auths);
		service.setRoles(roles);
	}

	@Test
	public void testLdapAuthenticationService() throws Exception {
		String password = "bladibla";
		assertThat(service.convertPassword("me", password)).isEqualTo(password);

		assertThat(service.isAuthenticated("wrong", "wrong")).isNull();

		String userId = "test";
		UserInfo authResult = service.isAuthenticated(userId, "cred");
		assertThat(authResult).isNotNull();
		assertThat(authResult.getUserId()).isEqualTo(userId);
		assertThat(authResult.getUserName()).isEqualTo("Joe Tester");
		assertThat(authResult.getUserLocale()).isEqualTo(new Locale("nl_BE"));
		assertThat(authResult.getUserOrganization()).isEqualTo("test");
		assertThat(authResult.getUserDivision()).isEqualTo("person");
		List<AuthorizationInfo> auths = authResult.getAuthorizations();
		assertThat(auths).hasSize(1);
	}

}
