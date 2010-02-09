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

package org.geomajas.internal.security;

import org.geomajas.security.Authentication;
import org.geomajas.security.SecurityContext;
import org.geomajas.security.SecurityInfo;
import org.geomajas.security.SecurityManager;
import org.geomajas.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link org.geomajas.security.SecurityManager} implementation.
 * <p/>
 * The security manager tries to find the authorization objects for an authentication token.
 * <p/>
 * It can be used to create or clear the security context for the current thread.
 *
 * @author Joachim Van der Auwera
 */
@Component
public class SecurityManagerImpl implements SecurityManager {

	@Autowired
	private SecurityInfo securityInfo;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private SecurityContext securityContext;

	/** @inheritDoc */
	public boolean createSecurityContext(String authenticationToken) {
		clearSecurityContext(); // assure there is no authenticated user in case of problems during creation
		List<SecurityService> services = securityInfo.getSecurityServices();
		List<Authentication> authentications = new ArrayList<Authentication>();
		for (SecurityService service : services) {
			Authentication auth = service.getAuthentication(authenticationToken);
			if (null != auth) {
				authentications.add(auth);
				auth.setSecurityServiceId(service.getId());
				if (!securityInfo.isLoopAllServices()) {
					break;
				}
			}
		}
		if (!authentications.isEmpty()) {
			// build authorization and build thread local SecurityContext
			((SecurityContextImpl) securityContext).setAuthentications("token", authentications);
			return true;
		}
		return false;
	}

	/** @inheritDoc */
	public void clearSecurityContext() {
		((SecurityContextImpl) securityContext).setAuthentications(null, null);
	}
}