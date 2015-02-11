/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.printing.component;

import org.apache.commons.lang.LocaleUtils;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Util class for print components.
 *
 * @author Jan Venstermans
 */
public final class ComponentUtil {

	private static final String BUNDLE_NAME = "org/geomajas/plugin/printing/PrintingMessages"; //$NON-NLS-1$

	private ComponentUtil() {
	}

	//-----------------------------------
	//  methods for obtaining internationalised messages
	//-----------------------------------

	/**
	 * Returns the resource bundle for current Locale, i.e. locale set in the PageComponent.
	 * Always create a new instance, this avoids getting the incorrect locale information.
	 *
	 * @return resourcebundle for internationalized messages
	 */
	public static ResourceBundle getCurrentResourceBundle(String locale) {
		try {
			if (null != locale && !locale.isEmpty()) {
				return getCurrentResourceBundle(LocaleUtils.toLocale(locale));
			}
		} catch (IllegalArgumentException ex) {
			// do nothing
		}
		return getCurrentResourceBundle((Locale) null);
	}

	public static ResourceBundle getCurrentResourceBundle(Locale locale) {
		if (null != locale) {
			return ResourceBundle.getBundle(BUNDLE_NAME, locale);
		} else {
			return ResourceBundle.getBundle(BUNDLE_NAME);
		}
	}

}
