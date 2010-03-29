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
 * Information about a Feature, indicates how an object can be converted to a feature.

 * @author Joachim Van der Auwera
 */
public class FeatureInfo implements Serializable {

	private static final long serialVersionUID = 151L;
	@NotNull
	private String dataSourceName;
	private PrimitiveAttributeInfo identifier;
	private String sortAttributeName;
	private SortType sortType;
	private GeometryAttributeInfo geometryType;
	private List<AttributeInfo> attributes = new ArrayList<AttributeInfo>();

	/**
	 * Get the data source name. This is used by the layer to know which data source to contact.
	 *
	 * @return data source name
	 */
	public String getDataSourceName() {
		return dataSourceName;
	}

	/**
	 * Set the data source name. This is used by the layer to know which data source to contact.
	 *
	 * @param dataSourceName data source name
	 */
	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	/**
	 * Get identifier description.
	 *
	 * @return identifier description
	 */
	public PrimitiveAttributeInfo getIdentifier() {
		return identifier;
	}

	/**
	 * Set the identifier description.
	 *
	 * @param identifier identifier description
	 */
	public void setIdentifier(PrimitiveAttributeInfo identifier) {
		this.identifier = identifier;
	}

	/**
	 * Get name of the sort attribute.
	 *
	 * @return sort attribute name
	 */
	public String getSortAttributeName() {
		return sortAttributeName;
	}

	/**
	 * Set the name of the sort attribute
	 * @param sortAttributeName sort attribute name
	 */
	public void setSortAttributeName(String sortAttributeName) {
		this.sortAttributeName = sortAttributeName;
	}

	/**
	 * Get sort type (which indicates ascending or descending).
	 *
	 * @return sort type
	 */
	public SortType getSortType() {
		return sortType;
	}

	/**
	 * Set sort type (ascending or descending).
	 *
	 * @param sortType sort type
	 */
	public void setSortType(SortType sortType) {
		this.sortType = sortType;
	}

	/**
	 * Get geometry type, information about the geometry attribute.
	 *
	 * @return geometry attribute info
	 */
	public GeometryAttributeInfo getGeometryType() {
		return geometryType;
	}

	/**
	 * Set information about the geometry attribute.
	 *
	 * @param geometryType geometry attribute info
	 */
	public void setGeometryType(GeometryAttributeInfo geometryType) {
		this.geometryType = geometryType;
	}

	/**
	 * Get the attribute definitions.
	 *
	 * @return list of attribute info
	 */
	public List<AttributeInfo> getAttributes() {
		return attributes;
	}

	/**
	 * Set the list with attribute definitions.
	 *
	 * @param attributes attributes list
	 */
	public void setAttributes(List<AttributeInfo> attributes) {
		this.attributes = attributes;
	}
}
