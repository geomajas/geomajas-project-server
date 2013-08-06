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

import org.geomajas.security.BaseAuthorization;

/**
 * Custom authorization class for the application.
 *
 * @author Joachim Van der Auwera
 */
// @extract-start AppAuthorization, Define a BaseAuthorization for your policy
public interface AppAuthorization extends BaseAuthorization {

	/**
	 * Does the user have access to the "blabla" button?
	 *
	 * @return true when button is allowed
	 */
	boolean isBlablaButtonAllowed();

}
// @extract-end
