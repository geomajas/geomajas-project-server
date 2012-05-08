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
package org.geomajas.plugin.editing.client.event;

import org.geomajas.annotation.FutureApi;
import org.geomajas.plugin.editing.client.service.GeometryEditState;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event that reports the changing of the general geometry editing state.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@FutureApi(allMethods = true)
public class GeometryEditChangeStateEvent extends GwtEvent<GeometryEditChangeStateHandler> {

	private final GeometryEditState editingState;

	/**
	 * Main constructor.
	 * 
	 * @param state state
	 */
	public GeometryEditChangeStateEvent(GeometryEditState editingState) {
		this.editingState = editingState;
	}

	/**
	 * Get the type of this class.
	 * 
	 * @return the type of this class
	 */
	@Override
	public Type<GeometryEditChangeStateHandler> getAssociatedType() {
		return GeometryEditChangeStateHandler.TYPE;
	}

	@Override
	protected void dispatch(GeometryEditChangeStateHandler geometryEditChangeStateHandler) {
		geometryEditChangeStateHandler.onChangeEditingState(this);
	}

	/**
	 * Get the current editing state.
	 * 
	 * @return Returns the current editing state.
	 */
	public GeometryEditState getEditingState() {
		return editingState;
	}
}