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
import org.geomajas.gwt.client.action.event.ToolbarActionDisabledEvent;
import org.geomajas.gwt.client.action.event.ToolbarActionEnabledEvent;
import org.geomajas.gwt.client.action.event.ToolbarActionHandler;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Base tool bar action, defines the common bits between {@link ToolbarAction} and {@link ToolbarModalAction}.
 * 
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
// @extract-start ToolbarBaseAction, ToolbarBaseAction
public abstract class ToolbarBaseAction {

	private String icon; // Link to the image icon that should represent the action's button in the tool bar.

	private String tooltip; // Text that appears when hovering over the tool bar button.

	/** Is the button for this action disabled or not? */
	private boolean disabled;

	private HandlerManager handlerManager;

	public ToolbarBaseAction(String icon, String tooltip) {
		this.icon = icon;
		this.tooltip = tooltip;
		handlerManager = new HandlerManager(this);
	}

	public HandlerRegistration addToolbarActionHandler(ToolbarActionHandler handler) {
		return handlerManager.addHandler(ToolbarActionHandler.TYPE, handler);
	}


	/**
	 * Link to the image icon that should represent the action's button in the tool bar.
	 *
	 * @return icon link
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * Link to the image icon that should represent the action's button in the tool bar.
	 * 
	 * @param icon
	 *            The new icon value
	 * */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * Text that appears when hovering over the tool bar button.
	 *
	 * @return tool tip
	 */
	public String getTooltip() {
		return tooltip;
	}

	/**
	 * Text that appears when hovering over the tool bar button.
	 * 
	 * @param tooltip
	 *            The new tooltip value
	 */
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	/**
	 * Is the button for this action disabled or not?
	 *
	 * @return true when disabled 
	 */
	public boolean isDisabled() {
		return disabled;
	}

	/**
	 * Is the button for this action disabled or not?
	 * 
	 * @param disabled
	 *            The new value
	 */
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
		if (disabled) {
			handlerManager.fireEvent(new ToolbarActionDisabledEvent());
		} else {
			handlerManager.fireEvent(new ToolbarActionEnabledEvent());
		}
	}
}
// @extract-end
