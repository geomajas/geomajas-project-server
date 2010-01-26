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

/**
 * Handle the mapping between an individual authentication store and policy store.
 *
 * @author Joachim Van der Auwera
 */
public interface SecurityService {

	/**
	 * Get the id of this security service. This can be used to check which security service authenticated a token,
	 * which may be useful when additional queries need to be made to backing (authentication or policy) services.
	 *
	 * @return id for this service
	 */
	String getId();

	/**
	 * Check whether the authentication token is valid. If so, get the authorization info and (optionally) some user
	 * information.
	 *
	 * @param authenticationToken authentication token
	 * @return {@link org.geomajas.security.Authentication} object
	 */
	Authentication getAuthentication(String authenticationToken);
}
