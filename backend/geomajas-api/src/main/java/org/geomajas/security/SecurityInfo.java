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

import java.util.List;

/**
 * Configuration for the security.
 *
 * @author Joachim Van der Auwera
 */
public class SecurityInfo {

	private boolean loopAllServices;

	private List<SecurityService> securityServices;

	/**
	 * Should all security services try to validate the authentication token and be allowed to add
	 * {@link org.geomajas.security.Authorization} info or should the security manager stop after the first one?
	 *
	 * @return check all security services or stop after first result
	 */
	public boolean isLoopAllServices() {
		return loopAllServices;
	}

	/**
	 * Set whether all security services should have a chance to validate the authentication token and add
	 * {@link org.geomajas.security.Authorization} info. When false, the security manager stops after the first match.
	 *
	 * @param loopAllServices new status
	 */
	public void setLoopAllServices(boolean loopAllServices) {
		this.loopAllServices = loopAllServices;
	}

	/**
	 * Get the list of security services which need to be queried to validate an authentication token and get
	 * relevant {@link org.geomajas.security.UserInfo} and {@link org.geomajas.security.Authorization} information.
	 *
	 * @return list of {@link org.geomajas.security.SecurityService}s
	 */
	public List<SecurityService> getSecurityServices() {
		return securityServices;
	}

	/**
	 * Set the list of security services which need to be queried to validate an authentication token and get
	 * relevant {@link org.geomajas.security.UserInfo} and {@link org.geomajas.security.Authorization} information.
	 *
	 * @param securityServices list of security services
	 */
	public void setSecurityServices(List<SecurityService> securityServices) {
		this.securityServices = securityServices;
	}
}
