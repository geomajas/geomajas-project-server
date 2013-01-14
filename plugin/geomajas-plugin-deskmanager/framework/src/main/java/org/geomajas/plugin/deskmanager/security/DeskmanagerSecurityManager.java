/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.security;

import java.util.List;

import org.geomajas.internal.security.DefaultSecurityManager;
import org.geomajas.security.Authentication;
import org.geomajas.security.SavedAuthorization;
import org.geomajas.security.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Deskmanager security manager. Extension of the default security manager that adds deskmanager custom 
 * authentications.
 * 
 * @author Oliver May
 * 
 */
public class DeskmanagerSecurityManager extends DefaultSecurityManager {

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

}
