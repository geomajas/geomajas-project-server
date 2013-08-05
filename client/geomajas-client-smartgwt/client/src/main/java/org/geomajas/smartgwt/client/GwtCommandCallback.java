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

package org.geomajas.smartgwt.client;

import com.smartgwt.client.util.SC;
import org.geomajas.command.CommandResponse;
import org.geomajas.gwt.client.command.CommandExceptionCallback;
import org.geomajas.gwt.client.command.CommunicationExceptionCallback;
import org.geomajas.smartgwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.util.Log;
import org.geomajas.smartgwt.client.widget.ExceptionWindow;

/**
 * Default handling of exceptions in {@link org.geomajas.gwt.client.command.GwtCommandDispatcher}.
 *
 * @author Joachim Van der Auwera
 */
public class GwtCommandCallback implements CommandExceptionCallback, CommunicationExceptionCallback {

	/**
	 * Default behaviour for handling a communication exception. Shows a warning window to the user.
	 *
	 * @param error error to report
	 */
	public void onCommunicationException(Throwable error) {
		String msg = I18nProvider.getGlobal().commandCommunicationError() + ":\n" + error.getMessage();
		Log.logWarn(msg, error);
		SC.warn(msg, null);
	}

	/**
	 * Default behaviour for handling a command execution exception. Shows an exception report to the user.
	 *
	 * @param response command response with error
	 */
	public void onCommandException(CommandResponse response) {
		String message = I18nProvider.getGlobal().commandError() + ":";
		for (String error : response.getErrorMessages()) {
			message += "\n" + error;
		}
		Log.logWarn(message);
		if (response.getExceptions() == null || response.getExceptions().size() == 0) {
			SC.warn(message, null);
		} else {
			// The error messaging window only supports 1 exception to display:
			ExceptionWindow window = new ExceptionWindow(response.getExceptions().get(0));
			window.show();
		}
	}

}
