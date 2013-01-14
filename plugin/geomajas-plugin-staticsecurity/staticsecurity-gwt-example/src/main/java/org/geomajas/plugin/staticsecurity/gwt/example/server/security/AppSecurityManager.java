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

package org.geomajas.plugin.staticsecurity.gwt.example.server.security;

import org.geomajas.internal.security.DefaultSecurityManager;
import org.geomajas.security.Authentication;
import org.geomajas.security.SavedAuthorization;
import org.geomajas.security.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Custom security context for this application. This needs to be overwritten as the SecurityManager builds/fills the
 * {@link org.geomajas.security.SecurityContext}.
 *
 * @author Joachim Van der Auwera
 */
// @extract-start AppSecurityManager, Security manager for new security context
public class AppSecurityManager extends DefaultSecurityManager {

	@Autowired
	private SecurityContext securityContext;

	/** @inheritDoc */
	public boolean setSecurityContext(String authenticationToken, List<Authentication> authentications) {
		if (!authentications.isEmpty()) {
			// build authorization and build thread local SecurityContext
			((AppSecurityContext) securityContext).setAuthentications(authenticationToken, authentications);
			return true;
		}
		return false;
	}

	/** @inheritDoc */
	public void clearSecurityContext() {
		((AppSecurityContext) securityContext).setAuthentications(null, null);
	}

	/** @inheritDoc */
	public void restoreSecurityContext(SavedAuthorization authorizations) {
		if (null == authorizations) {
			clearSecurityContext();
		} else {
			((AppSecurityContext) securityContext).restoreSecurityContext(authorizations);
		}
	}

}
// @extract-end
