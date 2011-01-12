/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
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