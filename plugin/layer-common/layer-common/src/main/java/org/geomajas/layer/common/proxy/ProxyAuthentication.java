/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer.common.proxy;

import org.geomajas.annotation.Api;

/**
 * Authentication settings for proxying of layers that implement {@link ProxyLayerSupport}. This object supports both
 * BASIC and DIGEST (HTTP) authentication. Replaces {@link LayerAuthentication} for uniformity between TMS and WMS
 * layers.
 * 
 * @author Jan De Moerloose
 * @since 1.16.0
 */
@Api(allMethods = true)
public interface ProxyAuthentication {

	/**
	 * Get the authentication method to use.
	 * 
	 * @return authentication method
	 */
	ProxyAuthenticationMethod getMethod();

	/**
	 * Get the key which is used for the user name in the URL. Defaults to "user"
	 * <p/>
	 * Only used when {@link #getMethod()} is {@link ProxyAuthenticationMethod#URL}.
	 * 
	 * @return key for the user name parameter
	 */
	String getUserKey();

	/**
	 * Get the key which is used for the password in the URL. Defaults to "password".
	 * <p/>
	 * Only used when {@link #getMethod()} is {@link ProxyAuthenticationMethod#URL}.
	 * 
	 * @return key for the user name parameter
	 */
	String getPasswordKey();

	/**
	 * Get the user name.
	 * 
	 * @return user name
	 */
	String getUser();

	/**
	 * Get the password.
	 * 
	 * @return password.
	 */
	String getPassword();

	/**
	 * Get the HTTP authentication realm. This value is optional.
	 * 
	 * @return authentication realm or null.
	 */
	String getRealm();
}
