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

	/** Width for the login window. */
	public static String loginWindowWidth = "500";
	/** Height for the login window. */
	public static String loginWindowHeight = "300";
	/** Field width for the login window. */
	public static String loginWindowFieldWidth = "*";
	/** Height of the slogan area in the login window. */
	public static String loginWindowSloganHeight = "24";
	/** Height of the error area in the login window. */
	public static String loginWindowErrorHeight = "14";
	/** View/hide details button width. */
	public static String loginWindowButtonWidth = "80";
	/** Style for main message in login window. */
	public static String loginWindowMessageStyle = "font-size:12px; font-weight:bold;";
	/** Style for detail header in login window. */
	public static String loginWindowDetailHeaderStyle = "font-size:12px; font-weight:bold;";
	/** Style for normal detail stack trace line in login window. */
	public static String loginWindowDetailTraceNormalStyle = "font-size:12px; padding-left:10px;";
	/** Style for likely less important (framework) detail stack trace line in login window. */
	public static String loginWindowDetailTraceLessStyle = "font-size:9px; padding-left:10px;";
	/** Border style for details in login window. */
	public static String loginWindowDetailBorderStyle = "1px solid #A0A0A0;";
	/** Logo to be displayed on the login window. */
	public static String loginWindowLogo = "[ISOMORPHIC]/geomajas/geomajas_logo.png";
	/** Width of the logo to be displayed on the login window. */
	public static String loginWindowLogoWidth = "300";
	/** Height of the logo to be displayed on the login window. */
	public static String loginWindowLogoHeight = "80";
	/** Background to be displayed on the login window. */
	public static String loginWindowBackground = "[ISOMORPHIC]/geomajas/staticsecurity/login_background_grey.jpg";
	/** Should the window be confined to stay within the parent rectangle? */
	public static boolean loginWindowKeepInScreen = true;
	

	// CHECKSTYLE VISIBILITY MODIFIER: ON

	private SsecLayout() {
		// do not allow instantiation.
	}
	
}
