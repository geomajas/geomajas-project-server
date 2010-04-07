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

import javax.validation.constraints.NotNull;

/**
 * Linked attribute configuration information.
 * 
 * @author Jan De Moerloose
 */
public class AssociationAttributeInfo extends AttributeInfo implements Serializable {

	private static final long serialVersionUID = 151L;

	@NotNull
	private AssociationType type;

	@NotNull
	private FeatureInfo feature;

	/**
	 * returns the type of the association (many-to-one, one-to-many).
	 * 
	 * @return association type
	 */
	public AssociationType getType() {
		return type;
	}

	/**
	 * Set the type of the association (many-to-one, one-to-many).
	 *
	 * @param type association type
	 */
	public void setType(AssociationType type) {
		this.type = type;
	}

	/**
	 * Get the feature information of this attribute (represented as feature).
	 * 
	 * @return feature information
	 */
	public FeatureInfo getFeature() {
		return feature;
	}

	/**
	 * Set the feature information.
	 *
	 * @param featureInfo feature info
	 */
	public void setFeature(FeatureInfo featureInfo) {
		this.feature = featureInfo;
	}
}
