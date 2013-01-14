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

import org.geomajas.plugin.staticsecurity.configuration.LayerAuthorizationInfo;
import org.geomajas.security.BaseAuthorization;

/**
 * Configuration object which includes the custom policy.
 *
 * @author Joachim Van der Auwera
 */
// @extract-start AppAuthorizationInfo, Configuration for your authorization
public class AppAuthorizationInfo extends LayerAuthorizationInfo {

	private boolean blablaButtonAllowed;

	/**
	 * Set whether the blabla button is allowed.
	 *
	 * @param blablaButtonAllowed is blabla button allowed
	 */
	public void setBlablaButtonAllowed(boolean blablaButtonAllowed) {
		this.blablaButtonAllowed = blablaButtonAllowed;
	}

	/**
	 * Does the user have access to the "blabla" button?
	 *
	 * @return true when button is allowed
	 */
	boolean isBlablaButtonAllowed() {
		return blablaButtonAllowed;
	}

	@Override
	public BaseAuthorization getAuthorization() {
		return new AppAuthorizationImpl(this);
	}
}
// @extract-end
