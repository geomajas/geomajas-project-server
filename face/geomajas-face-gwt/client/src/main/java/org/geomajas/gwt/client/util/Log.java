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

/**
 * SmartClient-based logger functionality.
 * 
 * @author Jan De Moerloose
 */
public final class Log {

	private Log() {
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
		logDebug(message);
		logDebug(getMessage(t));
	}

	public static void logInfo(String message, Throwable t) {
		logDebug(message);
		logDebug(getMessage(t));
	}

	public static void logWarn(String message, Throwable t) {
		logDebug(message);
		logDebug(getMessage(t));
	}

	public static void logError(String message, Throwable t) {
		logDebug(message);
		logDebug(getMessage(t));
	}

	public static String getMessage(Throwable t) {
		String st = t.getClass().getName() + ": " + t.getMessage();
		for (StackTraceElement ste : t.getStackTrace()) {
			st += "\n" + ste.toString();
		}
		st += "";
		return st;
	}

}
