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
package org.geomajas.layer.common.proxy;

import org.geomajas.annotation.Api;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * Configuration object that adds authentication support to the raster layer. If a user configures a layer with
 * such an authentication object, then the layer will use a proxy controller to talk to the raster server (tms/wms).
 * This ensures that the credentials are not leaked to the user.
 * </p>
 * <p>
 * This object supports both BASIC and DIGEST (HTTP) authentication.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class LayerAuthentication implements Serializable {

	private static final long serialVersionUID = 110L;

	@NotNull
	private String user;

	@NotNull
	private String password;

	private String realm;
	private String userKey = "user";
	private String passwordKey = "password";

	private LayerAuthenticationMethod authenticationMethod = LayerAuthenticationMethod.BASIC;

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

	/**
	 * Get the key which is used for the user name in the URL. Defaults to "user"
	 * <p/>
	 * Only used when {@link #getAuthenticationMethod()} is {@link LayerAuthenticationMethod#URL}.
	 *
	 * @return key for the user name parameter
	 */
	public String getUserKey() {
		return userKey;
	}

	/**
	 * Set the key which is used for the user name in the URL. Defaults to "user"
	 * <p/>
	 * Only used when {@link #getAuthenticationMethod()} is {@link LayerAuthenticationMethod#URL}.
	 *
	 * @param userKey key for the user name parameter
	 */
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	/**
	 * Get the key which is used for the password in the URL. Defaults to "password".
	 * <p/>
	 * Only used when {@link #getAuthenticationMethod()} is {@link LayerAuthenticationMethod#URL}.
	 *
	 * @return key for the user name parameter
	 */
	public String getPasswordKey() {
		return passwordKey;
	}

	/**
	 * Set the key which is used for the user name in the URL. Defaults to "password"
	 * <p/>
	 * Only used when {@link #getAuthenticationMethod()} is {@link LayerAuthenticationMethod#URL}.
	 *
	 * @param passwordKey key for the user name parameter
	 */
	public void setPasswordKey(String passwordKey) {
		this.passwordKey = passwordKey;
	}

	/**
	 * Get the authentication method to use.
	 *
	 * @return authentication method
	 */
	public LayerAuthenticationMethod getAuthenticationMethod() {
		return authenticationMethod;
	}

	/**
	 * Set the authentication method to use.
	 *
	 * @param authenticationMethod authentication method
	 */
	public void setAuthenticationMethod(LayerAuthenticationMethod authenticationMethod) {
		this.authenticationMethod = authenticationMethod;
	}
}