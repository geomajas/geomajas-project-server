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
package org.geomajas.gwt.client.command;

import org.geomajas.command.CommandResponse;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.widget.ExceptionWindow;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.util.SC;

/**
 * Convenience class that implements CommandExceptionCallback and CommunicationExceptionCallback with the default 
 * SC.say behavior. 
 * 
 * @author Oliver May
 */
public abstract class AbstractCommandCallback implements CommandCallback, CommandExceptionCallback,
		CommunicationExceptionCallback {

	public void onCommunicationException(Throwable error) {
		SC.warn(I18nProvider.getGlobal().commandError() + ":\n" + error.getMessage(), null);
	}

	public void onCommandException(CommandResponse response) {
		String message = I18nProvider.getGlobal().commandError() + ":";
		for (String error : response.getErrorMessages()) {
			message += "\n" + error;
		}
		GWT.log(message, null);
		if (response.getExceptions() == null || response.getExceptions().size() == 0) {
			SC.warn(message, null);
		} else {
			// The error messaging window only supports 1 exception to display:
			ExceptionWindow window = new ExceptionWindow(response.getExceptions().get(0));
			window.show();
		}
	}

}
