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
package org.geomajas.gwt.client.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * Global localized constants.
 * 
 * @author Jan De Moerloose
 */
public interface GlobalMessages extends Messages {

	String commandError();

	String commandCommunicationError();

	String missingI18n();

	String saveEditingAborted();

	String validationActivityError();

	String confirmDeleteFeature(String featureLabel, String layerLabel);

	// ActivityMonitor messages:

	/** @return waiting for an activity to finish */
	String activityBusyText();

	/** @return not waiting for an activity to finish */
	String activityNotBusyText();

	// Loading screen:

	String loadScreenDownLoadText();

	String loadScreenLoadText();

	String loadScreenReadyText();

	// MapWidget, aboutAction:

	/** @return about menu title */
	String aboutMenuTitle();

	/** @return copyright title in about dialog */
	String aboutCopyRight();

	String aboutVisit();

	/** @return license name/URL label */
	String licensedAs();

	/** @return source code URL label */
	String sourceUrl();

	String copyrightListTitle();

	// LocaleSelect:

	String localeTitle();

	String localeReload();

	// ExceptionWindow

	/** @return view details */
	String exceptionDetailsView();

	/** @return hide details */
	String exceptionDetailsHide();

}
