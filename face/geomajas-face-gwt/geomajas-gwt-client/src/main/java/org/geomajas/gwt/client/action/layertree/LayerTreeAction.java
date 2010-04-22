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

package org.geomajas.gwt.client.action.layertree;

import org.geomajas.global.Api;
import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.map.layer.Layer;

// @extract-start LayerTreeAction, LayerTreeAction
/**
 * Base definition of an action for the LayerTree toolbar.
 *
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
public abstract class LayerTreeAction extends ToolbarBaseAction {

	private String disabledIcon;

	/**
	 * Constructor setting all values.
	 *
	 * @param icon
	 *            The default icon for the button.
	 * @param tooltip
	 *            The default tooltip for the button.
	 * @param disabledIcon
	 *            The icon used when the button is disabled.
	 */
	public LayerTreeAction(String icon, String tooltip, String disabledIcon) {
		super(icon, tooltip);
		this.disabledIcon = disabledIcon;
	}

	/**
	 * This method will be called when the user clicks on the button.
	 *
	 * @param layer
	 *            The currently selected layer.
	 */
	public abstract void onClick(Layer<?> layer);

	/**
	 * Is the this action enabled for the layer?
	 *
	 * @param layer layer to test
	 * @return enabled status of action for layer
	 */
	public abstract boolean isEnabled(Layer<?> layer);

	/**
	 * Set icon to display when button is disabled.
	 *
	 * @return icon shown when the button is disabled
	 */
	public String getDisabledIcon() {
		return disabledIcon;
	}

	/**
	 * Set icon for disabled state.
	 *
	 * @param disabledIcon icon for disabled state
	 */
	public void setDisabledIcon(String disabledIcon) {
		this.disabledIcon = disabledIcon;
	}
}
// @extract-end
