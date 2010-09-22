/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
