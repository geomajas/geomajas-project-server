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

package org.geomajas.plugin.jsapi.client.event;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.Exportable;

/**
 * JavaScript exportable handler for catching events thrown when the command dispatcher stops issuing commands to the
 * server - when the response has returned.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Export
@ExportClosure
@Api(allMethods = true)
@UserImplemented
public interface DispatchStoppedHandler extends JsHandler, Exportable {

	/**
	 * Executed when the command dispatch has stopped dispatching commands to the server.
	 * 
	 * @param event
	 *            The dispatch stop event.
	 */
	void onDispatchStopped(DispatchStoppedEvent event);
}