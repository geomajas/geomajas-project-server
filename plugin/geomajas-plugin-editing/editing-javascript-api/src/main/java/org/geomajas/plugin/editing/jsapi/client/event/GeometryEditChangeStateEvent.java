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

package org.geomajas.plugin.editing.jsapi.client.event;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.jsapi.client.event.JsEvent;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Event that reports the changing of the general geometry editing state.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
@Export
@ExportPackage("org.geomajas.plugin.editing.event")
public class GeometryEditChangeStateEvent extends JsEvent<GeometryEditChangeStateHandler> implements Exportable {

	private String state;

	/**
	 * Main constructor.
	 * 
	 * @param state state
	 */
	public GeometryEditChangeStateEvent(String state) {
		this.state = state;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<GeometryEditChangeStateHandler> getType() {
		return GeometryEditChangeStateHandler.class;
	}

	protected void dispatch(GeometryEditChangeStateHandler handler) {
		handler.onChangeEditingState(this);
	}

	/**
	 * Get the current editing state.
	 * 
	 * @return Returns the current editing state.
	 */
	public String getEditingState() {
		return state;
	}
}