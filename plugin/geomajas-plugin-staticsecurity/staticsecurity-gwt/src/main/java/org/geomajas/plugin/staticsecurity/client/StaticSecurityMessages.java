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

package org.geomajas.plugin.staticsecurity.client;

import com.google.gwt.i18n.client.Messages;

/**
 * <p>
 * Localization messages for the GWT face of the staticsecurity plugin.
 * </p>
 * 
 * @author Pieter De Graef
 */
public interface StaticSecurityMessages extends Messages {

	// Login window messages:

	String loginWindowTitle();

	String loginUserName();

	String loginPassword();

	String loginBtnLogin();

	String loginBtnReset();

	String loginNoUserName();

	String loginNoPassword();

	String loginFailure();

	// Logout button messages:

	String logoutBtnTitle();
}
