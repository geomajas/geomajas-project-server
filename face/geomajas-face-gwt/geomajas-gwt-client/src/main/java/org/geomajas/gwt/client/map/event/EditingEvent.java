/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.map.event;

import org.geomajas.global.Api;

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
	};

	private EditingEventType editingEventType;

	public EditingEvent(EditingEventType editingEventType) {
		this.editingEventType = editingEventType;
	}

	public Type<EditingHandler> getAssociatedType() {
		return EditingHandler.TYPE;
	}

	protected void dispatch(EditingHandler editingHandler) {
		editingHandler.onEditingChange(this);
	}

	public EditingEventType getEditingEventType() {
		return editingEventType;
	}
}
