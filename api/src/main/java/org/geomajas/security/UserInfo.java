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

package org.geomajas.security;

import java.util.Locale;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

/**
 * Information about the logged in user (all data is optional).
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
@UserImplemented
public interface UserInfo {

	/**
	 * Get the user id if known.
	 *
	 * @return user id
	 */
	String getUserId();

	/**
	 * Get the users name if known.
	 *
	 * @return name of user or null when not known
	 */
	String getUserName();

	/**
	 * Get the users locale if known.
	 *
	 * @return locale for the user or null when not known
	 */
	Locale getUserLocale();

	/**
	 * Set the organization for the user. This value is optional and may be null.
	 *
	 * @return organization for the user or null when not known
	 */
	String getUserOrganization();

	/**
	 * Get the organization's division for the user. This value is optional and may be null.
	 *
	 * @return organizational division for the user or null when not known
	 */
	String getUserDivision();
}
