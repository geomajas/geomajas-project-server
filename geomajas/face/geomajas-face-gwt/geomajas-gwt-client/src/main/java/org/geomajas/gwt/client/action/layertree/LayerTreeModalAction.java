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

import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.map.layer.Layer;

/**
 * Base template for modal action in the layer tree toolbar.
 *
 * @author Pieter De Graef
 */
public abstract class LayerTreeModalAction extends ToolbarBaseAction {

	private String selectedIcon;

	private String disabledIcon;

	private String selectedTooltip;

	// Constructors:

	public LayerTreeModalAction(String selectedIcon, String deselectedIcon, String disabledIcon,
			String selectedTooltip, String deselectedTooltip) {
		super(deselectedIcon, deselectedTooltip);
		this.selectedIcon = selectedIcon;
		this.disabledIcon = disabledIcon;
		this.selectedTooltip = selectedTooltip;
	}

	// Class specific methods:

	/**
	 * When the toolbar button is selected, this method will be called.
	 */
	public abstract boolean isEnabled(Layer<?> layer);

	/**
	 * When the toolbar button is deselected, this method will be called.
	 */
	public abstract boolean isSelected(Layer<?> layer);

	public abstract void onDeselect(Layer<?> layer);

	public abstract void onSelect(Layer<?> layer);

	// Getters and setters:

	public String getSelectedIcon() {
		return selectedIcon;
	}

	public void setSelectedIcon(String selectedIcon) {
		this.selectedIcon = selectedIcon;
	}

	public String getDeselectedIcon() {
		return getIcon();
	}

	public void setDeselectedIcon(String deselectedIcon) {
		setIcon(deselectedIcon);
	}

	public String getSelectedTooltip() {
		return selectedTooltip;
	}

	public void setSelectedTooltip(String selectedTooltip) {
		this.selectedTooltip = selectedTooltip;
	}

	public String getDeselectedTooltip() {
		return getTooltip();
	}

	public void setDeselectedTooltip(String deselectedTooltip) {
		setTooltip(deselectedTooltip);
	}

	public String getDisabledIcon() {
		return disabledIcon;
	}

	public void setDisabledIcon(String disabledIcon) {
		this.disabledIcon = disabledIcon;
	}
}
