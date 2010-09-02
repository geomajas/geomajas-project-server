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
package org.geomajas.plugin.printing.component.dto;

import java.io.Serializable;

import org.geomajas.global.Api;

/**
 * DTO object for BaseLayerComponent.
 * 
 * @author Jan De Moerloose
 * @see org.geomajas.plugin.printing.component.BaseLayerComponent
 *
 */
@Api(allMethods = true)
public class BaseLayerComponentInfo extends PrintComponentInfo implements Serializable {

	private static final long serialVersionUID = 200L;
	/**
	 * True if layer is visible.
	 */
	private boolean visible = true;

	/**
	 * True if layer is selected.
	 */
	private boolean selected;

	/**
	 * ID of this layer (client ID).
	 */
	private String layerId;

	public BaseLayerComponentInfo() {
		getLayoutConstraint().setAlignmentX(LayoutConstraintInfo.JUSTIFIED);
		getLayoutConstraint().setAlignmentY(LayoutConstraintInfo.JUSTIFIED);
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}
 

}
