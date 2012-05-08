/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.configuration;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.geomajas.annotation.Api;

/**
 * Information about a Feature, indicates how an object can be converted to a feature.

 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class FeatureInfo implements IsInfo {

	private static final long serialVersionUID = 151L;
	@NotNull
	private String dataSourceName;
	private PrimitiveAttributeInfo identifier;
	private String sortAttributeName;
	private String displayAttributeName;
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
	 * Set the name of the sort attribute.
	 * 
	 * @param sortAttributeName sort attribute name
	 */
	public void setSortAttributeName(String sortAttributeName) {
		this.sortAttributeName = sortAttributeName;
	}

	/**
	 * Get name of the display attribute (attribute to be used for display to end users).
	 *
	 * @return display attribute name
	 * @since 1.9.0
	 */
	public String getDisplayAttributeName() {
		return displayAttributeName;
	}

	/**
	 * Set the name of the display attribute.
	 * 
	 * @param displayAttributeName display attribute name
	 * @since 1.9.0
	 */
	public void setDisplayAttributeName(String displayAttributeName) {
		this.displayAttributeName = displayAttributeName;
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
