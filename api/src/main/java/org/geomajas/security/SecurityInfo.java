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

import java.util.List;

import org.geomajas.annotation.Api;

/**
 * Configuration for the security.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
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
