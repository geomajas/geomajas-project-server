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
package org.geomajas.gwt.client.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * Global localized constants.
 * 
 * @author Jan De Moerloose
 */
public interface GlobalMessages extends Messages {

	String commandError();

	String missingI18n();

	String saveEditingAborted();

	String validationActivityError();

	String confirmDeleteFeature(String featureLabel, String layerLabel);

	// ActivityMonitor messages:

	String activityBusyText();

	String activityNotBusyText();

	// Loading screen:

	String loadScreenDownLoadText();

	String loadScreenLoadText();

	String loadScreenReadyText();

	// MapWidget, aboutAction:

	String aboutMenuTitle();

	String aboutCopyRight();

	String aboutVisit();

	String licensedAs();

	String copyrightListTitle();

	// LocaleSelect:

	String localeTitle();

	String localeReload();

}
