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

package org.geomajas.gwt.client.action;

import org.geomajas.global.Api;

import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * Abstract class which serves as a template for selectable buttons in a tool bar. These selectable buttons can be
 * selected and deselected. With each of these actions a different method is executed. Usually this type of tool bar
 * button is used to set a new controller onto the {@link org.geomajas.gwt.client.widget.MapWidget}. If you are looking
 * for an action that should be executed immediately when clicking on it, have a look at the
 * {@link org.geomajas.gwt.client.action.ToolbarAction} class.
 *
 * @author Pieter De Graef
 */
@Api(allMethods = true)
public abstract class ToolbarModalAction extends ToolbarBaseAction {

	public ToolbarModalAction(String icon, String tooltip) {
		super(icon, tooltip);
	}

	// Class specific actions:

	/**
	 * When the tool bar button is selected, this method will be called.
	 */
	public abstract void onSelect(ClickEvent event);

	/**
	 * When the tool bar button is deselected, this method will be called.
	 */
	public abstract void onDeselect(ClickEvent event);
}
