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

import org.geomajas.configuration.NamedStyleInfo;

/**
 * DTO object for VectorLayerComponent.
 * 
 * @author Jan De Moerloose
 * @see org.geomajas.plugin.printing.component.VectorLayerComponent
 *
 */
public class VectorLayerComponentInfo extends BaseLayerComponentInfo implements Serializable {

	/** Style for this layer. */
	private NamedStyleInfo styleInfo;

	/** CQL filter */
	private String filter;

	/** Array of selected feature id's for this layer. */
	private String[] selectedFeatureIds = new String[0];

	/** True if labels are visible. */
	private boolean labelsVisible;

	public NamedStyleInfo getStyleInfo() {
		return styleInfo;
	}

	public void setStyleInfo(NamedStyleInfo styleInfo) {
		this.styleInfo = styleInfo;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String[] getSelectedFeatureIds() {
		return selectedFeatureIds;
	}

	public void setSelectedFeatureIds(String[] selectedFeatureIds) {
		this.selectedFeatureIds = selectedFeatureIds;
	}

	public boolean isLabelsVisible() {
		return labelsVisible;
	}

	public void setLabelsVisible(boolean labelsVisible) {
		this.labelsVisible = labelsVisible;
	}

}
