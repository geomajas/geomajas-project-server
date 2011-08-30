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
package org.geomajas.gwt.client.util;

import org.geomajas.annotation.Api;
import org.geomajas.command.dto.LogRequest;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;

/**
 * SmartClient-based logger functionality.
 * 
 * @author Jan De Moerloose
 * @author Joachim Van der Auwera
 * @since 1.10.0
 */
@Api(allMethods = true)
public final class Log {

	public static final int LEVEL_DEBUG = LogRequest.LEVEL_DEBUG;
	public static final int LEVEL_INFO = LogRequest.LEVEL_INFO;
	public static final int LEVEL_WARN = LogRequest.LEVEL_WARN;
	public static final int LEVEL_ERROR = LogRequest.LEVEL_ERROR;

	private static final String SEP = ", ";

	private Log() {
		// hide constructor
	}

	public static native void logDebug(String message) /*-{
														$wnd.isc.Log.logDebug(message);
														}-*/;

	public static native void logInfo(String message) /*-{
														$wnd.isc.Log.logInfo(message);
														}-*/;

	public static native void logWarn(String message) /*-{
														$wnd.isc.Log.logWarn(message);
														}-*/;

	public static native void logError(String message) /*-{
														$wnd.isc.Log.logError(message);
														}-*/;

	public static void logDebug(String message, Throwable t) {
		logDebug(message + SEP + getMessage(t));
	}

	public static void logInfo(String message, Throwable t) {
		logInfo(message + SEP + getMessage(t));
	}

	public static void logWarn(String message, Throwable t) {
		logWarn(message + SEP + getMessage(t));
	}

	public static void logError(String message, Throwable t) {
		logError(message + SEP + getMessage(t));
	}

	private static String getMessage(Throwable throwable) {
		StringBuilder sb = new StringBuilder();
		if (null != throwable) {
			addMessageAndStackTrace(sb, throwable);
			Throwable cause = throwable.getCause();
			while (null != cause) {
				sb.append("\ncaused by ");
				addMessageAndStackTrace(sb, cause);
			}
		}
		return sb.toString();
	}

	private static void addMessageAndStackTrace(StringBuilder sb, Throwable throwable) {
		sb.append(throwable.getClass().getName());
		sb.append(": ");
		sb.append(throwable.getMessage());
		for (StackTraceElement ste : throwable.getStackTrace()) {
			sb.append("\n   ");
			sb.append(ste.toString());
		}
	}

	/**
	 * Log a message in the server log.
	 *
	 * @param logLevel log level
	 * @param message message to log
	 */
	public static void logServer(int logLevel, String message) {
		logServer(logLevel, message, null);
	}

	/**
	 * Log a message in the server log.
	 *
	 * @param logLevel log level
	 * @param message message to log
	 * @param throwable exception to include in message
	 */
	public static void logServer(int logLevel, String message, Throwable throwable) {
		String logMessage  = message;
		if (null == logMessage) {
			logMessage = "";
		}
		if (null != throwable) {
			logMessage += "\n" + getMessage(throwable);
		}
		LogRequest logRequest = new LogRequest();
		logRequest.setLevel(logLevel);
		logRequest.setStatement(message);
		GwtCommand command = new GwtCommand(LogRequest.COMMAND);
		command.setCommandRequest(logRequest);
		GwtCommandDispatcher.getInstance().execute(command);
	}

}
