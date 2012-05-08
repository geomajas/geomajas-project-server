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
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * This event is thrown when the command dispatcher starts dispatching.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
@Export
@ExportPackage("org.geomajas.jsapi.event")
public class DispatchStartedEvent extends JsEvent<DispatchStartedHandler> implements Exportable {

	/** No-arguments constructor. */
	public DispatchStartedEvent() {
	}

	/** {@inheritDoc} */
	public Class<DispatchStartedHandler> getType() {
		return DispatchStartedHandler.class;
	}

	/** {@inheritDoc} */
	protected void dispatch(DispatchStartedHandler handler) {
		handler.onDispatchStarted(this);
	}
}