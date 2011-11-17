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

package org.geomajas.plugin.editing.jsapi.client.event;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;
import org.geomajas.plugin.jsapi.client.event.JsEvent;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Event that reports mouse move events when moving tentatively. These move events don't have to commit to anything.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
@Export
@ExportPackage("org.geomajas.plugin.editing.event")
public class GeometryEditTentativeMoveEvent extends JsEvent<GeometryEditTentativeMoveHandler> implements Exportable {

	private Coordinate origin;

	private Coordinate currentPosition;

	public GeometryEditTentativeMoveEvent(Coordinate origin, Coordinate currentPosition) {
		this.origin = origin;
		this.currentPosition = currentPosition;
	}

	public Class<GeometryEditTentativeMoveHandler> getType() {
		return GeometryEditTentativeMoveHandler.class;
	}

	protected void dispatch(GeometryEditTentativeMoveHandler handler) {
		handler.onInsertMove(this);
	}

	public Coordinate getOrigin() {
		return origin;
	}

	public Coordinate getCurrentPosition() {
		return currentPosition;
	}
}