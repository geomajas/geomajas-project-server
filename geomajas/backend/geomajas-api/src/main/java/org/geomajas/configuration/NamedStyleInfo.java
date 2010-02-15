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
package org.geomajas.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * A named layer style for vector layers. The layer style consists of a list of feature styles. Each style has a unique
 * name. Should be equivalent to SLD.
 * 
 * @author Jan De Moerloose
 * 
 */
public class NamedStyleInfo implements Serializable {

	private List<FeatureStyleInfo> featureStyles = new ArrayList<FeatureStyleInfo>();
	
	@NotNull
	private LabelStyleInfo labelStyle;

	@NotNull
	private String name;

	public List<FeatureStyleInfo> getFeatureStyles() {
		return featureStyles;
	}

	public void setFeatureStyles(List<FeatureStyleInfo> featureStyles) {
		this.featureStyles = featureStyles;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public LabelStyleInfo getLabelStyle() {
		return labelStyle;
	}

	
	public void setLabelStyle(LabelStyleInfo labelStyleInfo) {
		this.labelStyle = labelStyleInfo;
	}
	
	

}
