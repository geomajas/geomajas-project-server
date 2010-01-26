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

/**
 * Information about a Feature, indicates how an object can be converted to a feature.

 * @author Joachim Van der Auwera
 */
public class FeatureInfo implements Serializable {

	private static final long serialVersionUID = 151L;
	private String dataSourceName;
	private PrimitiveAttributeInfo identifier;
	private String sortAttributeName;
	private SortType sortType;
	private GeometricAttributeInfo geometryType;
	private List<AttributeInfo> attributes;

	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	public PrimitiveAttributeInfo getIdentifier() {
		return identifier;
	}

	public void setIdentifier(PrimitiveAttributeInfo identifier) {
		this.identifier = identifier;
	}

	public String getSortAttributeName() {
		return sortAttributeName;
	}

	public void setSortAttributeName(String sortAttributeName) {
		this.sortAttributeName = sortAttributeName;
	}

	public SortType getSortType() {
		return sortType;
	}

	public void setSortType(SortType sortType) {
		this.sortType = sortType;
	}

	public GeometricAttributeInfo getGeometryType() {
		return geometryType;
	}

	public void setGeometryType(GeometricAttributeInfo geometryType) {
		this.geometryType = geometryType;
	}

	public List<AttributeInfo> getAttributes() {
		if (null == attributes) {
			attributes = new ArrayList<AttributeInfo>();
		}
		return attributes;
	}

	public void setAttributes(List<AttributeInfo> attributes) {
		this.attributes = attributes;
	}
}
