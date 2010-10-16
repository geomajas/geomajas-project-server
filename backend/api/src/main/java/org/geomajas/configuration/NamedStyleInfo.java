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

import org.geomajas.global.Api;
import org.geomajas.global.CacheableObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * A named layer style for vector layers. The layer style consists of a list of feature styles. Each style has a unique
 * name. Should be equivalent to SLD.
 * 
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
public class NamedStyleInfo implements Serializable, CacheableObject {

	private static final long serialVersionUID = 154L;

	private List<FeatureStyleInfo> featureStyles = new ArrayList<FeatureStyleInfo>();
	
	@NotNull
	private LabelStyleInfo labelStyle = new LabelStyleInfo();

	@NotNull
	private String name;

	/**
	 * Get possible styles for features. These are traversed from the beginning, the first style for which the
	 * formula evaluates successfully is applied.
	 *
	 * @return list of feature styles
	 */
	public List<FeatureStyleInfo> getFeatureStyles() {
		return featureStyles;
	}

	/**
	 * Set list of possible styles for features. These are traversed from the beginning, the first style for which the
	 * formula evaluates successfully is applied.
	 *
	 * @param featureStyles list of feature styles
	 */
	public void setFeatureStyles(List<FeatureStyleInfo> featureStyles) {
		this.featureStyles = featureStyles;
	}

	/**
	 * Name for style info.
	 *
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set name for style info.
	 *
	 * @param name name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get label style.
	 *
	 * @return label style
	 */
	public LabelStyleInfo getLabelStyle() {
		return labelStyle;
	}

	/**
	 * Set label style.
	 *
	 * @param labelStyleInfo label style
	 */
	public void setLabelStyle(LabelStyleInfo labelStyleInfo) {
		this.labelStyle = labelStyleInfo;
	}

	/**
	 * String identifier which is guaranteed to include sufficient information to assure to be different for two
	 * instances which could produce different result. It is typically used as basis for calculation of hash
	 * codes (like MD5, SHA1, SHA2 etc) of (collections of) objects.
	 *
	 * @return cacheId
	 * @since 1.8.0
	 */
	public String getCacheId() {
		return "NamedStyleInfo{" +
				"featureStyles=" + featureStyles +
				", labelStyle=" + labelStyle +
				", name='" + name + '\'' +
				'}';
	}

	/**
	 * String representation of object.
	 *
	 * @return string representation of object
	 * @since 1.8.0
	 */
	@Override
	public String toString() {
		return getCacheId();
	}
}
