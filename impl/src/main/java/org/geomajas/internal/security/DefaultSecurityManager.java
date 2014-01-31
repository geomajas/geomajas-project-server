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

package org.geomajas.internal.security;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.security.Authentication;
import org.geomajas.security.SavedAuthorization;
import org.geomajas.security.SecurityContext;
import org.geomajas.security.SecurityInfo;
import org.geomajas.security.SecurityManager;
import org.geomajas.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * {@link org.geomajas.security.SecurityManager} implementation.
 * <p/>
 * The security manager tries to find the authorization objects for an authentication token.
 * <p/>
 * It can be used to create or clear the security context for the current thread.
 *
 * @author Joachim Van der Auwera
 * @since 1.10.0
 */
@Api
@Component
public class DefaultSecurityManager implements SecurityManager {

	@Autowired
	private SecurityInfo securityInfo;

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
		return setSecurityContext(authenticationToken, authentications);
	}

	/**
	 * Method which sets the authorizations in the {@link SecurityContext}. To be overwritten when adding custom
	 * policies.
	 *
	 * @param authenticationToken authentication token
	 * @param authentications authorizations for this token
	 * @return true when a valid context was created, false when the token is not authenticated
	 */
	@Api
	public boolean setSecurityContext(String authenticationToken, List<Authentication> authentications) {
		if (!authentications.isEmpty()) {
			// build authorization and build thread local SecurityContext
			((DefaultSecurityContext) securityContext).setAuthentications(authenticationToken, authentications);
			return true;
		}
		return false;
	}

	/** @inheritDoc */
	public void clearSecurityContext() {
		((DefaultSecurityContext) securityContext).setAuthentications(null, null);
	}

	/** @inheritDoc */
	public void restoreSecurityContext(SavedAuthorization authorizations) {
		if (null == authorizations) {
			clearSecurityContext();
		} else {
			((DefaultSecurityContext) securityContext).restoreSecurityContext(authorizations);
		}
	}
}