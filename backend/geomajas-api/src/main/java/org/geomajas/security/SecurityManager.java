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

/**
 * The security manager tries to find the authorization objects for an authentication token.
 * It can be used to create or clear the security context for the current thread.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public interface SecurityManager {

	/**
	 * Create security context for this thread, given the authentication token.
	 *
	 * @param authenticationToken authentication token which should set the security context
	 * @return true when a valid context was created, false when the token is not authenticated
	 */
	boolean createSecurityContext(String authenticationToken);

	/**
	 * Clear the security context for this thread.
	 */
	void clearSecurityContext();
}
