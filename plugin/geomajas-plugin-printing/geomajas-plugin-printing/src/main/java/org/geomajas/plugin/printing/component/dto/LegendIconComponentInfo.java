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

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.global.Api;
import org.geomajas.layer.LayerType;

/**
 * DTO object for LegendIconComponent.
 * 
 * @author Jan De Moerloose
 * @see org.geomajas.plugin.printing.component.LegendIconComponent
 * @since 2.0.0
 * 
 */
@Api(allMethods = true)
public class LegendIconComponentInfo extends PrintComponentInfo implements Serializable {

	private static final long serialVersionUID = 200L;

	private String label;

	private LayerType layerType;

	private FeatureStyleInfo styleInfo;

	
	public String getLabel() {
		return label;
	}

	
	public void setLabel(String label) {
		this.label = label;
	}

	
	public LayerType getLayerType() {
		return layerType;
	}

	
	public void setLayerType(LayerType layerType) {
		this.layerType = layerType;
	}

	
	public FeatureStyleInfo getStyleInfo() {
		return styleInfo;
	}

	
	public void setStyleInfo(FeatureStyleInfo styleInfo) {
		this.styleInfo = styleInfo;
	}

}
