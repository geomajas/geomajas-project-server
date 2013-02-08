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
package org.geomajas.plugin.deskmanager.client.gwt.common;

import java.util.LinkedHashMap;
import java.util.Map;

import org.geomajas.annotation.Api;

/**
 * Registry where different {@link UserApplication}s can be registered. UserApplications registered here are available
 * for use in the management interface.
 * 
 * @author Oliver May
 * @since 1.0.0
 */
@Api
public final class UserApplicationRegistry {

	private static final UserApplicationRegistry INSTANCE = new UserApplicationRegistry();

	private Map<String, UserApplication> userApplications = new LinkedHashMap<String, UserApplication>();

	private UserApplicationRegistry() {
	}

	/**
	 * Register a user application. UserApplication.getClientApplicationKey() is used as key to fetch the application
	 * later.
	 * 
	 * @param userApplication
	 *            the user application to register.
	 */
	public void register(UserApplication userApplication) {
		if (null != userApplication) {
			register(userApplication.getClientApplicationKey(), userApplication);
		}
	}

	/**
	 * Register a user application.
	 * 
	 * @param key
	 *            the key
	 * @param userApplication
	 *            the user application
	 */
	public void register(String key, UserApplication userApplication) {
		userApplications.put(key, userApplication);
	}

	/**
	 * Fetch a user application for with the given key.
	 * 
	 * @param key the key
	 * @return the user applications.
	 */
	public UserApplication get(String key) {
		return userApplications.get(key);
	}

	/**
	 * Get the only instance of the user application registry.
	 *  
	 * @return the registry.
	 */
	public static UserApplicationRegistry getInstance() {
		return INSTANCE;
	}

	/**
	 * Get a map of user application keys mapped to the user applications.
	 * 
	 * @return the user applications.
	 */
	public Map<String, UserApplication> getUserApplications() {
		return userApplications;
	}

	/**
	 * Get a map of user application keys mapped to the user application names. 
	 * 
	 * @return the names
	 */
	public LinkedHashMap<String, String> getUserApplicationNames() {
		LinkedHashMap<String, String> loketNames = new LinkedHashMap<String, String>();
		for (UserApplication ca : getUserApplications().values()) {
			loketNames.put(ca.getClientApplicationKey(), ca.getClientApplicationName());
		}
		return loketNames;
	}
}
