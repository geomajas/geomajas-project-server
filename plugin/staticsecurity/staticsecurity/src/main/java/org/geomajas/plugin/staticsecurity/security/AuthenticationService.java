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

package org.geomajas.plugin.staticsecurity.security;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.staticsecurity.configuration.UserInfo;

/**
 * Service which handles the authentication of the users.
 *
 * @author Joachim Van der Auwera
 * @since 1.9.0
 */
@Api(allMethods = true)
public interface AuthenticationService {

	/**
	 * Convert the original password to something which can be used for comparing in
	 * {@link #isAuthenticated(String, String)}.
	 *
	 * @param user user for which the password needs to be converted
	 * @param password original password
	 * @return converted password which can be used for comparing
	 */
	String convertPassword(String user, String password);

	/**
	 * Check whether the user is authenticated and return user info if authentication succeeded.
	 *
	 * @param user user id
	 * @param convertedPassword converted password, @see {@link #convertPassword(String, String)}
	 * @return user info (includes authentications) if login was successful
	 */
	UserInfo isAuthenticated(String user, String convertedPassword);

}
