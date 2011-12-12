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

package org.geomajas.plugin.editing.jsapi.client.merging.event;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.jsapi.client.event.JsEvent;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Event that reports the merging process for geometries has begun.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
@Export
@ExportPackage("org.geomajas.plugin.editing.merging.event")
public class GeometryMergingStartEvent extends JsEvent<GeometryMergingStartHandler> implements Exportable {

	public GeometryMergingStartEvent() {
	}

	public Class<GeometryMergingStartHandler> getType() {
		return GeometryMergingStartHandler.class;
	}

	protected void dispatch(GeometryMergingStartHandler handler) {
		handler.onGeometryMergingStart(this);
	}
}