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

package org.geomajas.plugin.editing.jsapi.client.event;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;
import org.geomajas.plugin.jsapi.client.event.JsHandler;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.Exportable;

/**
 * Interface for handling mouse move events when inserting vertices. This will point to the current temporary location
 * for insertion.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Export
@ExportClosure
@Api(allMethods = true)
@UserImplemented
public interface GeometryEditTentativeMoveHandler extends JsHandler, Exportable {

	/**
	 * Executed during vertex insertion when (mouse) move events occur.
	 * 
	 * @param event
	 *            The geometry edit insert move event.
	 */
	void onInsertMove(GeometryEditTentativeMoveEvent event);
}