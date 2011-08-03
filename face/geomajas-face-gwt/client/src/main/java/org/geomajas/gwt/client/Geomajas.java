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

package org.geomajas.gwt.client;

import com.google.gwt.i18n.client.LocaleInfo;
import org.geomajas.global.Api;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Global settings for the Geomajas GWT face.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
public final class Geomajas {

	private Geomajas() {
	}

	/** Returns the current version of Geomajas as a string. */
	public static String getVersion() {
		return "1.9.0";
	}

	/**
	 * Returns a list of locales that can be used in this version of Geomajas. The default is english, and 'native'
	 * means that your browsers locale should be used (if supported - default otherwise).
	 */
	public static Map<String, String> getSupportedLocales() {
		Map<String, String> locales = new HashMap<String, String>();
		for (String localeName : LocaleInfo.getAvailableLocaleNames()) {
			String displayName = LocaleInfo.getLocaleNativeDisplayName(localeName);
			locales.put(localeName, displayName);
		}
		return locales;
	}

	/**
	 * Return the base directory for the web application.
	 * 
	 * @return
	 */
	public static native String getIsomorphicDir()
	/*-{
		return $wnd.isomorphicDir;
	}-*/;
}