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

package org.geomajas.gwt.client;

import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.handler.NotificationHandler;
import org.geomajas.gwt.client.util.Notify;

import com.google.gwt.core.client.EntryPoint;
import com.smartgwt.client.util.SC;

/**
 * Entry point for the Geomajas GWT face.
 *
 * @author Joachim Van der Auwera
 */
public class GeomajasEntryPoint implements EntryPoint {

	public void onModuleLoad() {
		GwtCommandDispatcher dispatcher =  GwtCommandDispatcher.getInstance();
		GwtCommandCallback callback = new GwtCommandCallback();
		dispatcher.setCommandExceptionCallback(callback);
		dispatcher.setCommunicationExceptionCallback(callback);
		
		//Set smartgwt notification handler
		Notify.getInstance().setHandler(new NotificationHandler() {
			@Override
			public void handleInfo(String message) {
				SC.say(message);
			}
			@Override
			public void handleError(String message) {
				SC.say(message);
			}
		});
	}
}
