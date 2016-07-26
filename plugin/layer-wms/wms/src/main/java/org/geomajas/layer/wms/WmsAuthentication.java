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

package org.geomajas.layer.wms;

import org.geomajas.annotation.Api;
import org.geomajas.layer.common.proxy.ProxyAuthentication;
import org.geomajas.layer.common.proxy.ProxyAuthenticationMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

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
 * @deprecated use {@link LayerAuthentication}
 */
@Api(allMethods = true)
@Deprecated
public class WmsAuthentication implements Serializable, ProxyAuthentication {

	private final Logger log = LoggerFactory.getLogger(WmsLayer.class);

	private static final long serialVersionUID = 180L;

	@NotNull
	private String user;

	@NotNull
	private String password;

	private String realm;
	private String userKey = "user";
	private String passwordKey = "password";

	private WmsAuthenticationMethod authenticationMethod = WmsAuthenticationMethod.BASIC;

	/**
	 * This class is deprecated use: LayerAuthentication.
	 */
	public WmsAuthentication() {
		super();
		log.warn("this class is deprecated use: LayerAuthentication.");
	}

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
	 * Only used when {@link #getAuthenticationMethod()} is {@link WmsAuthenticationMethod#URL}.
	 *
	 * @return key for the user name parameter
	 */
	public String getUserKey() {
		return userKey;
	}

	/**
	 * Set the key which is used for the user name in the URL. Defaults to "user"
	 * <p/>
	 * Only used when {@link #getAuthenticationMethod()} is {@link WmsAuthenticationMethod#URL}.
	 *
	 * @param userKey key for the user name parameter
	 */
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	/**
	 * Get the key which is used for the password in the URL. Defaults to "password".
	 * <p/>
	 * Only used when {@link #getAuthenticationMethod()} is {@link WmsAuthenticationMethod#URL}.
	 *
	 * @return key for the user name parameter
	 */
	public String getPasswordKey() {
		return passwordKey;
	}

	/**
	 * Set the key which is used for the user name in the URL. Defaults to "password"
	 * <p/>
	 * Only used when {@link #getAuthenticationMethod()} is {@link WmsAuthenticationMethod#URL}.
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
	public WmsAuthenticationMethod getAuthenticationMethod() {
		return authenticationMethod;
	}

	/**
	 * Set the authentication method to use.
	 *
	 * @param authenticationMethod authentication method
	 */
	public void setAuthenticationMethod(WmsAuthenticationMethod authenticationMethod) {
		this.authenticationMethod = authenticationMethod;
	}

	/**
	 * Get the proxy authentication method.
	 *
	 * @since 1.16.0
	 */
	@Override
	public ProxyAuthenticationMethod getMethod() {
		switch (authenticationMethod) {
			case BASIC:
				return ProxyAuthenticationMethod.BASIC;
			case URL:
				return ProxyAuthenticationMethod.URL;
			default:
				return null;
		}
	}
}
