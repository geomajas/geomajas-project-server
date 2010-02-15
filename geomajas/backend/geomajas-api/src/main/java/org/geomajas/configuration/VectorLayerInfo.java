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
 * Information about a vector layer.
 * 
 * @author Joachim Van der Auwera
 */
public class VectorLayerInfo extends LayerInfo implements Serializable {

	private static final long serialVersionUID = 151L;

	private String filter;

	@NotNull
	private FeatureInfo featureInfo;

	private List<NamedStyleInfo> namedStyleInfos = new ArrayList<NamedStyleInfo>();

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public FeatureInfo getFeatureInfo() {
		return featureInfo;
	}

	public void setFeatureInfo(FeatureInfo featureInfo) {
		this.featureInfo = featureInfo;
	}

	public List<NamedStyleInfo> getNamedStyleInfos() {
		return namedStyleInfos;
	}

	public void setNamedStyleInfos(List<NamedStyleInfo> namedStyleInfos) {
		this.namedStyleInfos = namedStyleInfos;
	}

	public NamedStyleInfo getNamedStyleInfo(String name) {
		for (NamedStyleInfo info : namedStyleInfos) {
			if (info.getName().equals(name)) {
				return info;
			}
		}
		return null;
	}

}
