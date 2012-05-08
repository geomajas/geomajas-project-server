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

package org.geomajas.plugin.jsapi.client.event;

import org.geomajas.annotation.Api;
import org.timepedia.exporter.client.Exportable;

/**
 * Base Event object for all events to be exported to JavaScript.
 * 
 * @param <H>
 *            interface implemented by handlers of this kind of event
 * @author Pieter De Graef
 * @since 1.0.0
 */
//@Export
//@ExportPackage("org.geomajas.jsapi.event")
@Api(allMethods = true)
public abstract class JsEvent<H extends JsHandler> implements Exportable {

	/** No-arguments constructor. */
	public JsEvent() {
	}

	/**
	 * Effectively execute the handler method using this event. This is kept protected, so that only authorized classes
	 * (@link EventBus}) can call upon this.
	 * 
	 * @param handler
	 *            The registered handler that should be called.
	 */
	protected abstract void dispatch(H handler);

	/**
	 * Get the type of handler associated with this type of event.
	 * 
	 * @return The type of handler associated with this type of event.
	 */
	public abstract Class<H> getType();
}