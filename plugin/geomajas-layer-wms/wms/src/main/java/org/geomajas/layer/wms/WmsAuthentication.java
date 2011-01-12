/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer.wms;

import org.geomajas.global.Api;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * Configuration object that adds authentication support to the WMS layer. If a user configures a WMS layer with
 * such an authentication object, then the WMS layer will use a proxy controller to talk to the WMS server.
 * This ensures that the credentials are not leaked to the user.
 * </p>
 * <p>
 * This object supports both BASIC and DIGEST (HTTP) authentication.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.8.0
 */
@Api(allMethods = true)
public class WmsAuthentication implements Serializable {

	private static final long serialVersionUID = 180L;

	@NotNull
	private String user;

	@NotNull
	private String password;

	private String realm;

	/**
	 * Get the user name.
	 * 
	 * @return user name
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Set the user name.
	 * 
	 * @param user
	 *            user name
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Get the password.
	 * 
	 * @return password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Set the password.
	 * 
	 * @param password
	 *            password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Get the HTTP authentication realm. This value is optional.
	 * 
	 * @return authentication realm or null.
	 */
	public String getRealm() {
		return realm;
	}

	/**
	 * Set the HTTP authentication realm. This value is optional.
	 * 
	 * @param realm
	 *            authentication realm
	 */
	public void setRealm(String realm) {
		this.realm = realm;
	}
}