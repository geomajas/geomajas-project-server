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
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.geomajas.plugin.staticsecurity.configuration.AuthorityInfo;
import org.geomajas.plugin.staticsecurity.configuration.AuthorizationInfo;
import org.geomajas.plugin.staticsecurity.configuration.UserInfo;
import org.geomajas.plugin.staticsecurity.security.AuthenticationService;
import org.geomajas.plugin.staticsecurity.security.UserDirectoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unboundid.ldap.sdk.BindResult;
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
 */
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
	private Map<String, List<AuthorizationInfo>> roles;
	
	public static final String DEFAULT_ROLE_NAME = "ROLE_DEFAULT";

	/**
	 * Set the server host for the LDAP service.
	 *
	 * @param serverHost server host
	 */
	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	/**
	 * Set the port on which the LDAP server can be found.
	 *
	 * @param serverPort server port
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	/**
	 * Set to true to make sure that all SSL checks are ignored. While this setting is insecure, it avoids the need
	 * to install root certificates etc. Setting this to true forces the use of SSL to connect to the server.
	 *
	 * @param allowAllSocketFactory should all SSL checks be ignored
	 */
	public void setAllowAllSocketFactory(boolean allowAllSocketFactory) {
		this.allowAllSocketFactory = allowAllSocketFactory;
	}

	/**
	 * Set the template to build the DN for the user. Any "{}" in the string is replaced by the user name.
	 *
	 * @param userDnTemplate template to build the user DN
	 */
	public void setUserDnTemplate(String userDnTemplate) {
		this.userDnTemplate = userDnTemplate;
	}

	/**
	 * Set the template to build the DN for querying all users.
	 * 
	 * @param allUsersDn all users DN
	 */
	public void setAllUsersDn(String allUsersDn) {
		this.allUsersDn = allUsersDn;
	}

	/**
	 * Attribute for the given name (if any).
	 *
	 * @param givenNameAttribute attribute name
	 */
	public void setGivenNameAttribute(String givenNameAttribute) {
		this.givenNameAttribute = givenNameAttribute;
	}

	/**
	 * Attribute for the surname (if any).
	 *
	 * @param surNameAttribute attribute name
	 */
	public void setSurNameAttribute(String surNameAttribute) {
		this.surNameAttribute = surNameAttribute;
	}

	/**
	 * Attribute for locale (if any).
	 *
	 * @param localeAttribute attribute name
	 */
	public void setLocaleAttribute(String localeAttribute) {
		this.localeAttribute = localeAttribute;
	}

	/**
	 * Attribute for the organization (if any).
	 *
	 * @param organizationAttribute attribute name
	 */
	public void setOrganizationAttribute(String organizationAttribute) {
		this.organizationAttribute = organizationAttribute;
	}

	/**
	 * Attribute for the division (if any).
	 *
	 * @param divisionAttribute attribute name
	 */
	public void setDivisionAttribute(String divisionAttribute) {
		this.divisionAttribute = divisionAttribute;
	}

	/**
	 * Attribute for the roles (if any).
	 *
	 * @param rolesAttribute attribute name
	 */
	public void setRolesAttribute(String rolesAttribute) {
		this.rolesAttribute = rolesAttribute;
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
	 */
	public void setDefaultRole(List<AuthorizationInfo> defaultRole) {
		this.defaultRole = defaultRole;
	}

	/**
	 * Set the authorizations for the roles which may be defined.
	 *
	 * @param roles map with roles, keys are the values for {@link #rolesAttribute}, probably DN values
	 */
	public void setRoles(Map<String, List<AuthorizationInfo>> roles) {
		this.roles = roles;
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
	public List<UserInfo> getUsers(Set<String> roles, Map<String, String> parameters) {
		List<UserInfo> users = new ArrayList<UserInfo>();
		if (allUsersDn == null) {
			log.warn("Getting users from LDAP requires configuration of allUsersDn property");
		} else {
			SearchRequest request = createSearchRequest(allUsersDn);
			if (roles != null && !roles.isEmpty()) {
				List<Filter> roleFilters = new ArrayList<Filter>();
				for (String role : roles) {
					roleFilters.add(Filter.createEqualityFilter(rolesAttribute, role));
				}
				Filter f = request.getFilter();
				request.setFilter(Filter.createANDFilter(f, Filter.createORFilter(roleFilters)));
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
		result.setAuthorities(getAuthorities(entry));
		return result;
	}
	
	private SearchResult execute(SearchRequest request, String bindDN, String password) {
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

	private List<AuthorityInfo> getAuthorities(SearchResultEntry entry) {
		List<AuthorityInfo> auths = new ArrayList<AuthorityInfo>();
		if (null != defaultRole) {
			AuthorityInfo defaultAuthority = new AuthorityInfo();
			defaultAuthority.setName(DEFAULT_ROLE_NAME);
			defaultAuthority.setAuthorizations(defaultRole);
			auths.add(defaultAuthority);
		}
		String[] attributes = entry.getAttributeValues(rolesAttribute);
		if (null != attributes) {
			for (String attr : attributes) {
				List<AuthorizationInfo> auth = roles.get(attr);
				if (null != auth) {
					AuthorityInfo authority = new AuthorityInfo();
					authority.setName(attr);
					authority.setAuthorizations(auth);
					auths.add(authority);
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
}
