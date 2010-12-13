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
package org.geomajas.layer.wms;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * Configuration object that adds HTTP authentication support to the WMS layer. If a user configures a WMS layer with
 * such an authentication object, then the WMS layer will use a proxy controller to talk to the WMS server. This proxy
 * controller will than add these authentication parameters to the WMS requests.
 * </p>
 * <p>
 * This type of authentication supports both BASIC and DIGEST authentication.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class HttpAuthentication implements Serializable {

	private static final long serialVersionUID = 180L;

	@NotNull
	private String user;

	@NotNull
	private String password;

	private String realm;

	private String applicationUrl;

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	/**
	 * Return the digest user name.
	 * 
	 * @return The user name.
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Set the digest user name.
	 * 
	 * @param user
	 *            The new value.
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Get the digest password.
	 * 
	 * @return The digest password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Set the digest password.
	 * 
	 * @param password
	 *            The new value.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Get the HTTP authentication realm. This value is optional, and can be left empty.
	 * 
	 * @return The realm, or null.
	 */
	public String getRealm() {
		return realm;
	}

	/**
	 * Set the HTTP authentication realm. This value is optional, and can be left empty.
	 * 
	 * @param realm
	 *            The new value.
	 */
	public void setRealm(String realm) {
		this.realm = realm;
	}

	/**
	 * Get the URL for this web application. In order for the WMS layer to create URLs that point to the proxy
	 * controller, it needs to know it's own URL. Although not strictly required, it is highly recommended to fill in
	 * this value.
	 * 
	 * @return The application URL.
	 */
	public String getApplicationUrl() {
		return applicationUrl;
	}

	/**
	 * Set the URL for this web application. In order for the WMS layer to create URLs that point to the proxy
	 * controller, it needs to know it's own URL. Although not strictly required, it is highly recommended to fill in
	 * this value.
	 * 
	 * @param applicationUrl
	 *            The new value.
	 */
	public void setApplicationUrl(String applicationUrl) {
		this.applicationUrl = applicationUrl;
	}
}