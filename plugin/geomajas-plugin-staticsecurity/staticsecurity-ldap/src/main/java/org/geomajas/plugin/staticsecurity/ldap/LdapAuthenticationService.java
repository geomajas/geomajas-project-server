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

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.staticsecurity.configuration.AuthorizationInfo;
import org.geomajas.plugin.staticsecurity.configuration.NamedRoleInfo;
import org.geomajas.plugin.staticsecurity.configuration.UserInfo;
import org.geomajas.plugin.staticsecurity.security.AuthenticationService;
import org.geomajas.plugin.staticsecurity.security.UserDirectoryService;
import org.geomajas.plugin.staticsecurity.security.dto.AllUserFilter;
import org.geomajas.plugin.staticsecurity.security.dto.AndUserFilter;
import org.geomajas.plugin.staticsecurity.security.dto.OrUserFilter;
import org.geomajas.plugin.staticsecurity.security.dto.RoleUserFilter;
import org.geomajas.plugin.staticsecurity.security.dto.UserFilter;
import org.geomajas.plugin.staticsecurity.security.dto.UserFilterVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unboundid.ldap.sdk.BindResult;
import com.unboundid.ldap.sdk.DN;
import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;
import com.unboundid.util.ssl.SSLUtil;
import com.unboundid.util.ssl.TrustAllTrustManager;

/**
 * {@link AuthenticationService} for linking to an LDAP store of users.
 *
 * @author Joachim Van der Auwera
 * @author Jan De Moerloose
 * @since 1.10.0 (actually already from 1.9.0 but annotations was missing)
 */
@Api
public class LdapAuthenticationService implements AuthenticationService, UserDirectoryService {

	private final Logger log = LoggerFactory.getLogger(LdapAuthenticationService.class);

	@NotNull
	private String serverHost;

	private int serverPort = 636; // default to secure port

	private boolean allowAllSocketFactory;

	@NotNull
	private String userDnTemplate;

	private String allUsersDn;

	private String givenNameAttribute;
	private String surNameAttribute;
	private String localeAttribute;
	private String organizationAttribute;
	private String divisionAttribute;
	private String rolesAttribute;

	private List<AuthorizationInfo> defaultRole;
	private Map<String, List<NamedRoleInfo>> namedRoles;
	private Map<String, Set<String>> ldapRoleMapping;
	
	/**
	 * Filter that allows all users
	 */
	@Api
	public static final Filter ALL = Filter.createEqualityFilter("objectclass", "person");
	
	/**
	 * Filter that allows no users
	 */
	@Api
	public static final Filter NONE = Filter.createNOTFilter(ALL);

	
	/**
	 * Default role name
	 */
	@Api
	public static final String DEFAULT_ROLE_NAME = "ROLE_DEFAULT";

	/**
	 * Set the server host for the LDAP service.
	 *
	 * @param serverHost server host
	 * @since 1.10.0 (actually already from 1.9.0 but annotations was missing)
	 */
	@Api
	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	/**
	 * Set the port on which the LDAP server can be found.
	 *
	 * @param serverPort server port
	 * @since 1.10.0 (actually already from 1.9.0 but annotations was missing)
	 */
	@Api
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	/**
	 * Set to true to make sure that all SSL checks are ignored. While this setting is insecure, it avoids the need
	 * to install root certificates etc. Setting this to true forces the use of SSL to connect to the server.
	 *
	 * @param allowAllSocketFactory should all SSL checks be ignored
	 */
	@Api
	public void setAllowAllSocketFactory(boolean allowAllSocketFactory) {
		this.allowAllSocketFactory = allowAllSocketFactory;
	}

	/**
	 * Set the template to build the DN for the user. Any "{}" in the string is replaced by the user name.
	 *
	 * @param userDnTemplate template to build the user DN
	 * @since 1.10.0 (actually already from 1.9.0 but annotations was missing)
	 */
	@Api
	public void setUserDnTemplate(String userDnTemplate) {
		this.userDnTemplate = userDnTemplate;
	}

	/**
	 * Set the template to build the DN for querying all users.
	 * 
	 * @param allUsersDn all users DN
	 * @since 1.10.0
	 */
	@Api
	public void setAllUsersDn(String allUsersDn) {
		this.allUsersDn = allUsersDn;
	}
	
	/**
	 * Attribute for the given name (if any).
	 *
	 * @param givenNameAttribute attribute name
	 * @since 1.10.0 (actually already from 1.9.0 but annotations was missing)
	 */
	@Api
	public void setGivenNameAttribute(String givenNameAttribute) {
		this.givenNameAttribute = givenNameAttribute;
	}

	/**
	 * Attribute for the surname (if any).
	 *
	 * @param surNameAttribute attribute name
	 * @since 1.10.0 (actually already from 1.9.0 but annotations was missing)
	 */
	@Api
	public void setSurNameAttribute(String surNameAttribute) {
		this.surNameAttribute = surNameAttribute;
	}

	/**
	 * Attribute for locale (if any).
	 *
	 * @param localeAttribute attribute name
	 * @since 1.10.0 (actually already from 1.9.0 but annotations was missing)
	 */
	@Api
	public void setLocaleAttribute(String localeAttribute) {
		this.localeAttribute = localeAttribute;
	}

	/**
	 * Attribute for the organization (if any).
	 *
	 * @param organizationAttribute attribute name
	 * @since 1.10.0 (actually already from 1.9.0 but annotations was missing)
	 */
	@Api
	public void setOrganizationAttribute(String organizationAttribute) {
		this.organizationAttribute = organizationAttribute;
	}

	/**
	 * Attribute for the division (if any).
	 *
	 * @param divisionAttribute attribute name
	 * @since 1.10.0 (actually already from 1.9.0 but annotations was missing)
	 */
	@Api
	public void setDivisionAttribute(String divisionAttribute) {
		this.divisionAttribute = divisionAttribute;
	}

	/**
	 * Attribute for the roles (if any).
	 *
	 * @param rolesAttribute attribute name
	 * @since 1.10.0 (actually already from 1.9.0 but annotations was missing)
	 */
	@Api
	public void setRolesAttribute(String rolesAttribute) {
		this.rolesAttribute = rolesAttribute;
	}
	
	public String getRolesAttribute() {
		return rolesAttribute;
	}

	/**
	 * Get the authorizations which apply for all users who can successfully authenticate using LDAP.
	 *
	 * @return authentications for all users authenticated through LDAP
	 */
	public List<AuthorizationInfo> getDefaultRole() {
		return defaultRole;
	}

	/**
	 * Set the authorizations which apply for all users who can successfully authenticate using LDAP.
	 *
	 * @param defaultRole list of authentications for all users who can authenticate through LDAP
	 * @since 1.10.0 (actually already from 1.9.0 but annotations was missing)
	 */
	@Api
	public void setDefaultRole(List<AuthorizationInfo> defaultRole) {
		this.defaultRole = defaultRole;
	}

	/**
	 * Set the authorizations for the roles which may be defined. If the keys are DN values, the application role names
	 * are taken from the leftmost RDN value. Use {@link LdapAuthenticationService#setNamedRoles(Map)} instead of this
	 * method to explicitly define application role names.
	 * 
	 * @param roles map with roles, keys are the values for {@link #rolesAttribute}, probably DN values
	 * @since 1.10.0 (actually already from 1.9.0 but annotations was missing)
	 */
	@Api
	public void setRoles(Map<String, List<AuthorizationInfo>> roles) {
		Map<String, List<NamedRoleInfo>> namedRoles = new HashMap<String, List<NamedRoleInfo>>();
		for (String ldapRole : roles.keySet()) {
			DN dn;
			List<AuthorizationInfo> auth = roles.get(ldapRole);
			NamedRoleInfo role = new NamedRoleInfo();
			role.setAuthorizations(auth);
			try {
				dn = new DN(ldapRole);
				role.setName(dn.getRDN().getAttributeValues()[0]);
			} catch (LDAPException e) {
				role.setName(ldapRole);
			}
			namedRoles.put(ldapRole, Collections.singletonList(role));
		}
		setNamedRoles(namedRoles);
	}
	
	/**
	 * Set the named roles which may be defined.
	 *
	 * @param roles map with roles, keys are the values for {@link #rolesAttribute}, probably DN values
	 * @since 1.10.0
	 */
	@Api
	public void setNamedRoles(Map<String, List<NamedRoleInfo>> namedRoles) {
		this.namedRoles = namedRoles;
		ldapRoleMapping = new HashMap<String, Set<String>>();
		for (String roleName : namedRoles.keySet()) {
			if (!ldapRoleMapping.containsKey(roleName)) {
				ldapRoleMapping.put(roleName, new HashSet<String>());
			}
			for (NamedRoleInfo role : namedRoles.get(roleName)) {
				ldapRoleMapping.get(roleName).add(role.getName());
			}
		}
	}
	
	/**
	 * Get the named roles which may be defined.
	 * 
	 * @return the roles
	 * @since 1.10.0
	 */
	@Api
	public Map<String, List<NamedRoleInfo>> getNamedRoles() {
		return namedRoles;
	}

	/** {@inheritDoc} */
	public String convertPassword(String user, String password) {
		return password;
	}

	/** {@inheritDoc} */
	public UserInfo isAuthenticated(String user, String password) {
		String userDn = userDnTemplate.replace("{}", user);
		SearchRequest request = createSearchRequest(userDn);
		SearchResult result;
		result = execute(request, userDn, password);
		if (result != null && !result.getSearchEntries().isEmpty()) {
			return getUserInfo(result.getSearchEntries().get(0));
		} else {
			return null;
		}
	}
	
	

	@Override
	public List<org.geomajas.security.UserInfo> getUsers(UserFilter userFilter) {
		List<org.geomajas.security.UserInfo> users = new ArrayList<org.geomajas.security.UserInfo>();
		if (allUsersDn == null) {
			log.warn("Getting users from LDAP requires configuration of allUsersDn property");
		} else {
			SearchRequest request = createSearchRequest(allUsersDn);
			LdapFilterVisitor visitor = new LdapFilterVisitor();
			Filter ldapFilter = (Filter) userFilter.accept(visitor, null);
			if (ldapFilter != null) {
				Filter f = request.getFilter();
				request.setFilter(Filter.createANDFilter(f, ldapFilter));
			}
			SearchResult result = execute(request, null, null);
			if (result != null) {
				for (SearchResultEntry entry : result.getSearchEntries()) {
					users.add(getUserInfo(entry));
				}
			}
		}
		return users;
	}
	
	/**
	 * Convert the specified filter to an LDAP filter. This method is called when this {@link UserDirectoryService} is
	 * extended with a custom filter class. Override this method to return the correct value.
	 * 
	 * @param filter the custom user filter
	 * @return the LDAP filter
	 * @since 1.10.0
	 */
	@Api
	public Filter convert(UserFilter filter) {
		log.warn("You should override the convert() method to support custom filtering!");
		return NONE;
	}
	
	protected SearchResult execute(SearchRequest request, String bindDN, String password) {
		LDAPConnection connection = null;
		try {
			if (allowAllSocketFactory) {
				SSLUtil sslUtil = new SSLUtil(new TrustAllTrustManager());
				connection = new LDAPConnection(sslUtil.createSSLSocketFactory(), serverHost, serverPort);
			} else {
				connection = new LDAPConnection(serverHost, serverPort);
			}
			if (bindDN != null) {
				BindResult auth = connection.bind(bindDN, password);
				if (!auth.getResultCode().isConnectionUsable()) {
					log.error("Connection not usable, result code : " + auth.getResultCode());
				}
			}
			return connection.search(request);
		} catch (LDAPException le) {
			String message = le.getMessage();
			if (!message.startsWith("Unable to bind as user ")) {
				log.error(le.getMessage(), le);
			}
		} catch (GeneralSecurityException gse) {
			log.error(gse.getMessage(), gse);
		} finally {
			if (null != connection) {
				connection.close();
			}
		}
		return null;
	}


	private SearchRequest createSearchRequest(String searchDN) {
		List<String> attributes = new ArrayList<String>();
		attributes.add("cn");
		addAttribute(attributes, givenNameAttribute);
		addAttribute(attributes, surNameAttribute);
		addAttribute(attributes, localeAttribute);
		addAttribute(attributes, organizationAttribute);
		addAttribute(attributes, divisionAttribute);
		addAttribute(attributes, rolesAttribute);
		SearchRequest request = new SearchRequest(searchDN, SearchScope.SUB, Filter.createEqualityFilter(
				"objectclass", "person"), attributes.toArray(new String[attributes.size()]));
		return request;
	}

	private UserInfo getUserInfo(SearchResultEntry entry) {
		UserInfo result = new UserInfo();
		result.setUserId(entry.getAttributeValue("cn"));		
		String name = entry.getAttributeValue(givenNameAttribute);
		String name2 = entry.getAttributeValue(surNameAttribute);
		if (null != name) {
			if (null != name2) {
				name = name + " " + name2;
			}
		} else {
			name = name2;
		}
		result.setUserName(name);
		result.setUserLocale(entry.getAttributeValue(localeAttribute));
		result.setUserOrganization(entry.getAttributeValue(organizationAttribute));
		result.setUserDivision(entry.getAttributeValue(divisionAttribute));
		result.setRoles(getRoles(entry));
		return result;
	}
	
	private List<NamedRoleInfo> getRoles(SearchResultEntry entry) {
		List<NamedRoleInfo> auths = new ArrayList<NamedRoleInfo>();
		if (null != defaultRole) {
			NamedRoleInfo defaultAuthority = new NamedRoleInfo();
			defaultAuthority.setName(DEFAULT_ROLE_NAME);
			defaultAuthority.setAuthorizations(defaultRole);
			auths.add(defaultAuthority);
		}
		String[] attributes = entry.getAttributeValues(rolesAttribute);
		if (null != attributes) {
			for (String attr : attributes) {
				List<NamedRoleInfo> auth = namedRoles.get(attr);
				if (auth != null) {
					auths.addAll(auth);
				}
			}
		}
		return auths;
	}

	private void addAttribute(List<String> attributes, String attribute) {
		if (null != attribute) {
			attributes.add(attribute);
		}
	}
	
	/**
	 * {@link UserFilterVisitor} that creates an LDAP filter.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class LdapFilterVisitor implements UserFilterVisitor {

		@Override
		public Object visit(UserFilter filter, Object extraData) {
			return convert(filter);
		}

		@Override
		public Object visit(AndUserFilter and, Object extraData) {
			List<Filter> filters = new ArrayList<Filter>();
			for (UserFilter filter : and.getChildren()) {
				Filter f = (Filter) filter.accept(this, extraData);
				filters.add(f);
			}
			return Filter.createANDFilter(filters);
		}

		@Override
		public Object visit(OrUserFilter or, Object extraData) {
			List<Filter> filters = new ArrayList<Filter>();
			for (UserFilter filter : or.getChildren()) {
				Filter f = (Filter) filter.accept(this, extraData);
				filters.add(f);
			}
			return Filter.createORFilter(filters);
		}

		@Override
		public Object visit(RoleUserFilter role, Object extraData) {
			List<Filter> filters = new ArrayList<Filter>();
			for (String ldapRole : ldapRoleMapping.keySet()) {
				if (ldapRoleMapping.get(ldapRole).contains(role.getName())) {
					filters.add(Filter.createEqualityFilter(rolesAttribute, ldapRole));
				}
			}
			return Filter.createORFilter(filters);
		}

		@Override
		public Object visit(AllUserFilter all, Object extraData) {
			return ALL;
		}

	}
	
}
