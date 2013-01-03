/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.security;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.security.Authentication;
import org.geomajas.security.SavedAuthorization;
import org.geomajas.security.SecurityContext;
import org.geomajas.security.SecurityInfo;
import org.geomajas.security.SecurityManager;
import org.geomajas.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Deskmanager security manager. Extension of the default security manager that adds deskmanager custom 
 * authentications.
 * 
 * @author Oliver May
 * 
 */
public class DeskmanagerSecurityManager implements SecurityManager {

	@Autowired
	private SecurityInfo securityInfo;
	
	@Autowired
	private SecurityContext securityContext;

	/** @inheritDoc */
	public boolean setSecurityContext(String authenticationToken, List<Authentication> authentications) {
		if (!authentications.isEmpty()) {
			// build authorization and build thread local SecurityContext
			((DeskmanagerSecurityContext) securityContext).setAuthentications(authenticationToken, authentications);
			return true;
		}
		return false;
	}

	/** @inheritDoc */
	public void clearSecurityContext() {
		((DeskmanagerSecurityContext) securityContext).setAuthentications(null, null);
	}

	/** @inheritDoc */
	public void restoreSecurityContext(SavedAuthorization authorizations) {
		if (null == authorizations) {
			clearSecurityContext();
		} else {
			// System.out.println(authorizations);
			((DeskmanagerSecurityContext) securityContext).restoreSecurityContext(authorizations);
		}
	}

	/* (non-Javadoc)
	 * @see org.geomajas.security.SecurityManager#createSecurityContext(java.lang.String)
	 */
	@Override
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

}
