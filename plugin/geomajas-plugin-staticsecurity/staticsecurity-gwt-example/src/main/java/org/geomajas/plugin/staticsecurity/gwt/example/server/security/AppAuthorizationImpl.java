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

import org.geomajas.plugin.staticsecurity.configuration.LayerAuthorization;

/**
 * Custom authorization implementation.
 *
 * @author Joachim Van der Auwera
 */
// @extract-start AppAuthorizationImpl, Implementation for your authorization
public class AppAuthorizationImpl extends LayerAuthorization implements AppAuthorization {

	private AppAuthorizationInfo appAuthorizationInfo;

	protected AppAuthorizationImpl() {
		// for deserialization
		super();
	}

	public AppAuthorizationImpl(AppAuthorizationInfo info) {
		super(info);
		appAuthorizationInfo = info;
	}

	public boolean isBlablaButtonAllowed() {
		return appAuthorizationInfo.isBlablaButtonAllowed();
	}
}
// @extract-end
