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
package org.geomajas.gwt.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window.Navigator;

/**
 * Utility class with static methods used to check whether the user uses mobile or desktop browser.
 * 
 * @author Dosi Bingov
 */
public final class Browser {

	// array with the most used mobile user agents
	private static final String[] MOBILE_PLATFORMS = { "iphone", "ipod", "ipad", "android", "blackberry", "fennec" };

	private Browser() {
	}

	/**
	 * Method that returns the platform current device runs.
	 * 
	 * @return platform
	 */
	public static String getPlatform() {
		return Navigator.getPlatform();
	}

	/**
	 * Checks if the user agent belongs to mobile browser. (iPhone, iPad, android blackberry or Nokia) Note that not all
	 * mobile devices are supported see detectmobilebrowsers.com for the most recent mobile user agents
	 * 
	 * @return whether the browser is mobile (true or false)
	 */
	public static boolean isMobile() {
		if (GWT.isClient()) {
			String userAgent = Navigator.getUserAgent();

			for (String platform : MOBILE_PLATFORMS) {
				if (userAgent.toLowerCase().indexOf(platform) != -1) {
					return true;
				}
			}
		}

		return false;
	}

}
