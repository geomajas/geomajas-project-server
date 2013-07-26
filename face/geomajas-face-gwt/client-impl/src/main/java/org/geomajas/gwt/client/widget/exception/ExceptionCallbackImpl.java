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
package org.geomajas.gwt.client.widget.exception;

import org.geomajas.command.CommandResponse;
import org.geomajas.global.ExceptionDto;
import org.geomajas.gwt.client.command.CommandExceptionCallback;
import org.geomajas.gwt.client.command.CommunicationExceptionCallback;
import org.geomajas.gwt.client.util.Log;
import org.geomajas.gwt.client.i18n.GlobalMessages;

import com.google.gwt.core.client.GWT;

/**
 * Callback implementation that writes all exceptions to {@link Log}. It will also show a dialog window.
 * 
 * @author Emiel Ackermann / Jan Venstermans
 */
public class ExceptionCallbackImpl implements CommandExceptionCallback, CommunicationExceptionCallback {

	private GlobalMessages messages = GWT.create(GlobalMessages.class);

	@Override
	public void onCommandException(CommandResponse response) {
		String msg = null;
		String stack = null;
		boolean first = true;
		for (ExceptionDto error : response.getExceptions()) {
			if (first) {
				msg = messages.commandError() + ":\n" + error.getMessage();
				first = false;
			}
			stack = getDetails(error);
		}
		showDialog(msg, stack);
	}

	@Override
	public void onCommunicationException(Throwable error) {
		if (null != error) {
			String msg = messages.commandCommunicationError() + ":\n" + error.getMessage();
			Log.logWarn(msg, error);

			String stack = getDetails(error);

			showDialog(msg, stack);
		}
	}

	/**
	 * Build details message for an exception.
	 * 
	 * @param error error to build message for
	 * @return string with details message
	 */
	private String getDetails(ExceptionDto error) {
		if (null == error) {
			return "";
		}
		StringBuilder content = new StringBuilder();
		content.append(error.getClassName());
		if (error.getExceptionCode() != 0) {
			content.append(" (");
			content.append(error.getExceptionCode());
			content.append(")");
		}
		processStackTrace(error.getStackTrace(), content);
		content.append(getDetails(error.getCause()));
		return content.toString();
	}

	/**
	 * Build details message for an exception.
	 * 
	 * @param error error to build message for
	 * @return string with details message
	 */
	private String getDetails(Throwable error) {
		if (null == error) {
			return "";
		}
		StringBuilder content = new StringBuilder();
		content.append(error.getMessage());
		processStackTrace(error.getStackTrace(), content);
		content.append(getDetails(error.getCause()));
		return content.toString();
	}

	private void processStackTrace(StackTraceElement[] stackTrace, StringBuilder content) {
		for (StackTraceElement el : stackTrace) {
			content.append("  ");
			content.append(el.toString());
			content.append("\n");
		}
	}

	/**
	 * Makes a dialog box to show exception message and stack trace.
	 * 
	 * @param msg error message
	 * @param stack stack trace
	 */
	private void showDialog(String msg, String stack) {
		ExceptionDialog warning = new ExceptionDialog(msg, stack);
		warning.show();
	}
}
