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

package org.geomajas.security;

import org.geomajas.global.Api;
import org.geomajas.global.UserImplemented;

import java.util.Locale;

/**
 * Information about the logged in user (all data is optional).
 *
 * @author Joachim Van der Auwera
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
