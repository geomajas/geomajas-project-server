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

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.Exportable;

/**
 * Handler for catching events that indicate a layer configuration has been loaded into the
 * {@link org.geomajas.jsapi.map.LayersModel}.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Export
@ExportClosure
@Api(allMethods = true)
@UserImplemented
public interface LayersModelChangedHandler extends JsHandler, Exportable {

	/**
	 * Called when a new layer configuration has been loaded into the {@link org.geomajas.jsapi.map.LayersModel}.
	 * 
	 * @param event
	 *            {@link LayersModelChangedEvent}
	 */
	void onLayersModelChanged(LayersModelChangedEvent event);
}