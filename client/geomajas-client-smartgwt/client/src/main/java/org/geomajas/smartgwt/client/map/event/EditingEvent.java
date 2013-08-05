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

package org.geomajas.smartgwt.client.map.event;

import org.geomajas.annotation.Api;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event that announces changes in editing state.
 * 
 * @author Pieter De Graef
 * @since 1.8.0
 */
@Api(allMethods = true)
public class EditingEvent extends GwtEvent<EditingHandler> {

	/**
	 * The type of editing event.
	 * 
	 * @author Pieter De Graef
	 */
	public enum EditingEventType {
		/**
		 * This type is fired at the moment editing is started.
		 */
		START_EDITING,
		/**
		 * This type is fired when editing is stopped.
		 */
		STOP_EDITING,
		// /**
		// * This type should be fired when the user switches from drawing points to a normal editing mode.
		// */
		// STOP_DRAWING_POINTS
	}

	private EditingEventType editingEventType;

	/**
	 * Constructor.
	 *
	 * @param editingEventType editing event type
	 */
	public EditingEvent(EditingEventType editingEventType) {
		this.editingEventType = editingEventType;
	}

	@Override
	public Type<EditingHandler> getAssociatedType() {
		return EditingHandler.TYPE;
	}

	@Override
	protected void dispatch(EditingHandler editingHandler) {
		editingHandler.onEditingChange(this);
	}

	/**
	 * Get editing event type.
	 *
	 * @return editing event type
	 */
	public EditingEventType getEditingEventType() {
		return editingEventType;
	}
}
