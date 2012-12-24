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
package org.geomajas.gwt.client.util;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.handler.NotificationHandler;

import com.google.gwt.user.client.Window;

/**
 * Notification class for sending messages to the user trough a notification handler. Defaults to Window.alert().
 *
 * @author Kristof Heirwegh
 * @author Oliver May
 * @since 1.2.0
 */
@Api
public final class Notify {

	private static Notify instance;

	private NotificationHandler handler;

	private Notify() {
		handler = getDefaultHandler();
	}
	
	/**
	 * Get instance of the notifier.
	 * 
	 * @return the notifier.
	 */
	public static Notify getInstance() {
		if (instance == null) {
			instance = new Notify();
		}
		return instance;
	}

	/**
	 * Send an info message.
	 * 
	 * @param message the message
	 */
	public static void info(String message) {
		getInstance().getHandler().handleInfo(message);
	}

	/**
	 * Send an error message.
	 * 
	 * @param message the message
	 */
	public static void error(String message) {
		getInstance().getHandler().handleError(message);
	}

	/**
	 * Get the current notification handler.
	 * 
	 * @return the notification handler
	 */
	public NotificationHandler getHandler() {
		return handler;
	}

	/**
	 * Set the notification handler.
	 * 
	 * @param handler the notification handler
	 */
	public void setHandler(NotificationHandler handler) {
		this.handler = (handler == null ? getDefaultHandler() : handler);
	}

	private NotificationHandler getDefaultHandler() {
		return new NotificationHandler() {
			public void handleInfo(String message) {
				Window.alert(message);
			}

			public void handleError(String message) {
				Window.alert(message);
			}
		};
	}
}
