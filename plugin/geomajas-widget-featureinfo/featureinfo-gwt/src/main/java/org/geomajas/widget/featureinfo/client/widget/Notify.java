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
package org.geomajas.widget.featureinfo.client.widget;

import com.smartgwt.client.util.SC;

/**
 * TODO this class should be in geomajas face-GWT.
 *
 * @author Kristof Heirwegh
 */
public final class Notify {

	private static Notify instance;

	private NotificationHandler handler;

	private Notify() {
		handler = getDefaultHandler();
	}

	public static Notify getInstance() {
		if (instance == null) {
			instance = new Notify();
		}
		return instance;
	}

	public static void info(String message) {
		getInstance().getHandler().handleInfo(message);
	}

	public static void error(String message) {
		getInstance().getHandler().handleError(message);
	}

	public NotificationHandler getHandler() {
		return handler;
	}

	public void setHandler(NotificationHandler handler) {
		this.handler = (handler == null ? getDefaultHandler() : handler);
	}

	// ----------------------------------------------------------

	private NotificationHandler getDefaultHandler() {
		return new NotificationHandler() {
			public void handleInfo(String message) {
				SC.say(message);
			}

			public void handleError(String message) {
				SC.say(message);
			}
		};
	}
}
