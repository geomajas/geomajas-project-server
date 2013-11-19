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
import org.geomajas.plugin.staticsecurity.security.dto.UserFilter;
import org.geomajas.security.UserInfo;
import org.junit.Before;
import org.junit.Test;

import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.LDAPException;

public class CustomLdapAuthenticationServiceTest extends LdapServerProvider {

	private CustomLdapAuthenticationService service = new CustomLdapAuthenticationService();

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
	public void testUserService() throws Exception {
		List<UserInfo> users = service.getUsers(new CustomUserFilter("(givenName=Jill)"));
		assertThat(users).isNotNull();
		assertThat(users.size()).isEqualTo(1);
		assertThat(users.get(0).getUserName()).isEqualTo("Jill Developer");
	}

	/**
	 * Custom ldap authentication service that supports {@link CustomUserFilter} queries.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public class CustomLdapAuthenticationService extends LdapAuthenticationService {

		@Override
		public Filter convert(UserFilter filter) {
			try {
				return Filter.create(((CustomUserFilter) filter).getSearchText());
			} catch (LDAPException e) {
				return NONE;
			}
		}

	}

}
