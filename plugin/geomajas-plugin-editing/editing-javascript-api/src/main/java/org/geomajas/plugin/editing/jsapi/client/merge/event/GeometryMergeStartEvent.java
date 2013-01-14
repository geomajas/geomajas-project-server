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

package org.geomajas.plugin.editing.jsapi.client.merge.event;

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
@ExportPackage("org.geomajas.plugin.editing.merge.event")
public class GeometryMergeStartEvent extends JsEvent<GeometryMergeStartHandler> implements Exportable {

	/**
	 * Default constructor.
	 */
	public GeometryMergeStartEvent() {
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<GeometryMergeStartHandler> getType() {
		return GeometryMergeStartHandler.class;
	}

	protected void dispatch(GeometryMergeStartHandler handler) {
		handler.onGeometryMergeStart(this);
	}
}