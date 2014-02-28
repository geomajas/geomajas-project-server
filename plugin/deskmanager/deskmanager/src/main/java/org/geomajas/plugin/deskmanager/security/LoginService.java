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
package org.geomajas.plugin.deskmanager.security;

import org.geomajas.security.GeomajasSecurityException;

/**
 * Contains methods for logging in/off users.
 *
 * @author Jan Venstermans
 */
public interface LoginService {

	/**
	 * Check login and return token.
	 *
	 * @param email
	 * @param password
	 * @return token
	 */
	String checkLogin(String email, String password) throws GeomajasSecurityException;

}
