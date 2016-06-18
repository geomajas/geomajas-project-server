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

package org.geomajas.plugin.staticsecurity.configuration;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.staticsecurity.security.AuthenticationService;
import org.geomajas.plugin.staticsecurity.security.StaticAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Base configuration data for the staticsecurity security service.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class SecurityServiceInfo {

	private int tokenLifetime = 4 * 60 * 60; // 4 hours // NOSONAR
	private List<UserInfo> users;
	private List<AuthenticationService> authenticationServices;
	private boolean excludeDefault;

	@Autowired
	private StaticAuthenticationService staticAuthenticationService;

	/**
	 * Get information about existing users.
	 *
	 * @return list of {@link UserInfo} objects
	 */
	public List<UserInfo> getUsers() {
		return users;
	}

	/**
	 * Set the list of users which can be used by staticsecurity.
	 *
	 * @param users list of {@link UserInfo} objects
	 */
	public void setUsers(List<UserInfo> users) {
		this.users = users;
	}

	/**
	 * Get how long a token remains valid (in seconds). Default is 4 hours.
	 *
	 * @return lifetime for a token
	 * @since 1.9.0
	 */
	public int getTokenLifetime() {
		return tokenLifetime;
	}

	/**
	 * Set how a long a token can remain valid (in seconds).
	 *
	 * @param tokenLifetime lifetime for a token
	 * @since 1.9.0
	 */
	public void setTokenLifetime(int tokenLifetime) {
		this.tokenLifetime = tokenLifetime;
	}

	/**
	 * Get the list of authentication services for this configuration.
	 *
	 * @return list of authentication services
	 * @since 1.9.0
	 */
	public List<AuthenticationService> getAuthenticationServices() {
		return authenticationServices;
	}

	/**
	 * Set the list of authentication services for this configuration.
	 *
	 * @param authenticationServices list of authentication services
	 * @since 1.9.0
	 */
	public void setAuthenticationServices(List<AuthenticationService> authenticationServices) {
		this.authenticationServices = authenticationServices;
	}
	
	/**
	 * Returns true if the default static authentication service at the end should be excluded.
	 * @return true if excluded, false otherwise 
	 * @since 1.10.0
	 */
	public boolean isExcludeDefault() {
		return excludeDefault;
	}
	
	/**
	 * If set to true, the default static authentication service at the end will be excluded.  
	 * @param excludeDefault
	 * @since 1.10.0
	 */
	public void setExcludeDefault(boolean excludeDefault) {
		this.excludeDefault = excludeDefault;
	}

	/**
	 * Finish initialization of the configuration.
	 */
	@PostConstruct
	protected void postConstruct() {
		if (null == authenticationServices) {
			authenticationServices = new ArrayList<AuthenticationService>();
		}
		if (!excludeDefault) {
			authenticationServices.add(staticAuthenticationService);
		}
	}
}
