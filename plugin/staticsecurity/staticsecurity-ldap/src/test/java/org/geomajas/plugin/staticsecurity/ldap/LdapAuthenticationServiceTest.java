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

import static org.fest.assertions.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.geomajas.plugin.staticsecurity.configuration.AuthorizationInfo;
import org.geomajas.plugin.staticsecurity.configuration.LayerAuthorizationInfo;
import org.geomajas.plugin.staticsecurity.configuration.NamedRoleInfo;
import org.geomajas.plugin.staticsecurity.security.dto.AllUserFilter;
import org.geomajas.plugin.staticsecurity.security.dto.RoleUserFilter;
import org.geomajas.security.UserInfo;
import org.junit.Before;
import org.junit.Test;

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
		service.setAllUsersDn("dc=staticsecurity,dc=geomajas,dc=org");
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
		List<AuthorizationInfo> auths2 = new ArrayList<AuthorizationInfo>();
		LayerAuthorizationInfo auth2 = new LayerAuthorizationInfo();
		List<String> includes2 = new ArrayList<String>();
		includes2.add(".*");
		auth2.setCommandsInclude(includes2);
		auths2.add(auth2);
		roles.put("cn=devgroup,dc=roles,dc=geomajas,dc=org", auths);
		roles.put("cn=testgroup,dc=roles,dc=geomajas,dc=org", auths2);		
		service.setRoles(roles);
	}

	@Test
	public void testLdapAuthenticationService() throws Exception {
		String password = "bladibla";
		assertThat(service.convertPassword("me", password)).isEqualTo(password);

		assertThat(service.isAuthenticated("wrong", "wrong")).isNull();

		String userId = "test";
		org.geomajas.plugin.staticsecurity.configuration.UserInfo authResult = service.isAuthenticated(userId, "cred");
		assertThat(authResult).isNotNull();
		assertThat(authResult.getUserId()).isEqualTo(userId);
		assertThat(authResult.getUserName()).isEqualTo("Joe Tester");
		assertThat(authResult.getUserLocale()).isEqualTo(new Locale("nl_BE"));
		assertThat(authResult.getUserOrganization()).isEqualTo("test");
		assertThat(authResult.getUserDivision()).isEqualTo("person");
		List<AuthorizationInfo> auths = authResult.getAuthorizations();
		assertThat(auths).hasSize(1);
		List<NamedRoleInfo> authits = authResult.getRoles();
		assertThat(authits).hasSize(1);
		assertThat(authits.get(0).getName()).isEqualTo("testgroup");
	}

	@Test
	public void testUserService() throws Exception {
		List<UserInfo> users = service.getUsers(new RoleUserFilter("testgroup"));
		assertThat(users).isNotNull();
		assertThat(users.size()).isEqualTo(1);
		assertThat(users.get(0).getUserName()).isEqualTo("Joe Tester");
		
		users = service.getUsers(new AllUserFilter());
		assertThat(users).isNotNull();
		assertThat(users.size()).isEqualTo(2);
		
	}

}
