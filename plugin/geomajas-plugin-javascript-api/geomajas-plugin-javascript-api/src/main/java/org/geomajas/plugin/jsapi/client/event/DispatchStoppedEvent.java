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
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * This event is thrown when the command dispatcher stops dispatching - when the response has returned.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
@Export
@ExportPackage("org.geomajas.jsapi.event")
public class DispatchStoppedEvent extends JsEvent<DispatchStoppedHandler> implements Exportable {

	/** No-arguments constructor. */
	public DispatchStoppedEvent() {
	}

	/** {@inheritDoc} */
	protected void dispatch(DispatchStoppedHandler handler) {
		handler.onDispatchStopped(this);
	}

	/** {@inheritDoc} */
	public Class<DispatchStoppedHandler> getType() {
		return DispatchStoppedHandler.class;
	}
}