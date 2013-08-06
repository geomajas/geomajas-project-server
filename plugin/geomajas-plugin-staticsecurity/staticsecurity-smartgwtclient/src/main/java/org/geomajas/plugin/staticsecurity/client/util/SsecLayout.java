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

package org.geomajas.plugin.staticsecurity.client.util;

import org.geomajas.annotation.Api;

/**
 * Class which helps to provide consistent sizes and names for layout purposes, see
 * {@link org.geomajas.gwt.client.util.WidgetLayout}.
 * <p/>
 * Implemented as static class to allow overwriting values at application start, thus allowing skinning.
 *
 * @author Joachim Van der Auwera
 * @since 1.9.0
 */
@Api(allMethods = true)
public final class SsecLayout {

	// CHECKSTYLE VISIBILITY MODIFIER: OFF

	/** Icon for login button. */
	public static String iconLogin = "[ISOMORPHIC]/geomajas/staticsecurity/key_go.png";
	/** Icon for logout button. */
	public static String iconLogout = "[ISOMORPHIC]/geomajas/staticsecurity/key_delete.png";

	/** Width for the token request window. */
	public static String tokenRequestWindowWidth = "500";
	/** Height for the token request window. */
	public static String tokenRequestWindowHeight = "300";
	/** Field width for the token request window. */
	public static String tokenRequestWindowFieldWidth = "*";
	/** Height of the slogan area in the token request window. */
	public static String tokenRequestWindowSloganHeight = "24";
	/** Height of the error area in the token request window. */
	public static String tokenRequestWindowErrorHeight = "14";
	/** View/hide details button width. */
	public static String tokenRequestWindowButtonWidth = "80";
	/** View/hide details button layout width. */
	public static String tokenRequestWindowButtonLayoutWidth = "50%";
	/** Logo to be displayed on the token request window. */
	public static String tokenRequestWindowLogo = "[ISOMORPHIC]/geomajas/geomajas_logo.png";
	/** Width of the logo to be displayed on the token request window. */
	public static String tokenRequestWindowLogoWidth = "300";
	/** Height of the logo to be displayed on the token request window. */
	public static String tokenRequestWindowLogoHeight = "80";
	/** Background to be displayed on the token request window. */
	public static String tokenRequestWindowBackground =
			"[ISOMORPHIC]/geomajas/staticsecurity/login_background_grey.jpg";
	/** Should the window be confined to stay within the parent rectangle? */
	public static boolean tokenRequestWindowKeepInScreen = true;
	/** Style used for the error display in the token request window. */
	public static String tokenRequestWindowErrorStyle = "color:#FFAA00;";

	// CHECKSTYLE VISIBILITY MODIFIER: ON

	private SsecLayout() {
		// do not allow instantiation.
	}
	
}
