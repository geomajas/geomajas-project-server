/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.client.gwt.geodesk.service;

import org.geomajas.command.CommandResponse;
import org.geomajas.global.ExceptionDto;
import org.geomajas.gwt.client.GwtCommandCallback;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.util.Log;
import org.geomajas.gwt.client.util.Notify;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.i18n.GeodeskMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.StatusCodeException;

/**
 * Overriding the Geomajas default to add some more userfriendly errormessages.
 * 
 * @author Kristof Heirwegh
 */
public class DeskmanagerGwtCommandCallback extends GwtCommandCallback {

	private static final String LAYER_EXCEPTION = "org.geomajas.layer.LayerException";

	private static final String RENDER_EXCEPTION = "org.geomajas.rendering.RenderException";

	private static final String SECURITY_EXCEPTION = "org.geomajas.security.GeomajasSecurityException";

	private static final GeodeskMessages MESSAGES = GWT.create(GeodeskMessages.class);
	
	@Override
	public void onCommunicationException(Throwable error) {
		if (error instanceof StatusCodeException && ((StatusCodeException) error).getStatusCode() == 500) {
			String msg = I18nProvider.getGlobal().commandCommunicationError() + ":\n" + error.getMessage();
			Log.logWarn(msg);
			Notify.error(MESSAGES.userFriendlyCommunicationErrorMessage());
		} else {
			Notify.error(error.getLocalizedMessage());
		}
	}

	@Override
	public void onCommandException(CommandResponse response) {
		if (response.getExceptions() != null && response.getExceptions().size() > 0) {
			ExceptionDto ex = response.getExceptions().get(0);
			String className = ex.getClassName();
			if (className != null && !"".equals(className)) {
				if (LAYER_EXCEPTION.equals(className) || RENDER_EXCEPTION.equals(className)) {
					logWarn(response);
					Notify.error(MESSAGES.userFriendlyLayerErrorMessage());
				} else if (SECURITY_EXCEPTION.equals(className)) {
					logWarn(response);
					Notify.error(MESSAGES.userFriendlySecurityErrorMessage());
				} else {
					for (String error : response.getErrorMessages()) {
						Notify.error(error);
					}
				}
			} else {
				for (String error : response.getErrorMessages()) {
					Notify.error(error);
				}
			}
		} else {
			for (String error : response.getErrorMessages()) {
				Notify.error(error);
			}
		}
	}

	private void logWarn(CommandResponse response) {
		String message = I18nProvider.getGlobal().commandError() + ":";
		for (String error : response.getErrorMessages()) {
			message += "\n" + error;
		}
		Log.logWarn(message);
	}
}
