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

package org.geomajas.jsapi.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.geomajas.annotation.Api;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Central event bus for handler registration and event firing.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Export
@ExportPackage("org.geomajas.jsapi.event")
@Api(allMethods = true)
public class JsEventBus implements Exportable {

	private HashMap<Class<?>, List<JsHandler>> handlers = new HashMap<Class<?>, List<JsHandler>>();

	/**
	 * Add a new handler to the event bus registration map. When an associated event is fired, this handler will be
	 * called upon to catch that event.
	 * 
	 * @param handler
	 *            The new handler to register.
	 */
	public void addHandler(JsHandler handler) {
		// Check if the list already exists in the HashMap. If not, create it:
		if (!handlers.containsKey(handler.getClass())) {
			handlers.put(handler.getClass(), new ArrayList<JsHandler>());
		}

		// Now add the handler to the correct list:
		List<JsHandler> handlerList = handlers.get(handler.getClass());
		handlerList.add(handler);
	}

	/**
	 * Fire a certain event. All handlers of the associated type will be called upon (in the correct order) to catch
	 * this event.
	 * 
	 * @param event
	 *            The event to fire.
	 */
	public void fireEvent(JsEvent<JsHandler> event) {
		List<JsHandler> handlerList = handlers.get(event.getClass());
		if (handlerList != null) {
			for (JsHandler handler : handlerList) {
				event.dispatch(handler);
			}
		}
	}
}