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
package org.geomajas.puregwt.client.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * Global localized constants.
 * 
 * @author Emiel Ackermann
 */
public interface GlobalMessages extends Messages {

	String commandError();

	String commandCommunicationError();

	String missingI18n();

	// ActivityMonitor messages:

	/** @return waiting for an activity to finish */
	String activityBusyText();

	/** @return not waiting for an activity to finish */
	String activityNotBusyText();
	
	// Exception dialog messages
	String exceptionDialogShowDetails();
	
	String exceptionDialogCloseDetails();
	
	String exceptionDialogCaptionText();
	
	String exceptionDialogCloseTitle();

}