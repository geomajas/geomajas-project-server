/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.sld.filter.FilterTypeInfo;

/**
 * 
 A FeatureTypeConstraint identifies a specific feature type and supplies fitlering.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/ogc"
 * xmlns:ns1="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="FeatureTypeConstraint">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element ref="ns1:FeatureTypeName" minOccurs="0"/>
 *       &lt;xs:element ref="ns:Filter" minOccurs="0"/>
 *       &lt;xs:element ref="ns1:Extent" minOccurs="0" maxOccurs="unbounded"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 *
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Api(allMethods = true)
public class FeatureTypeConstraintInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	private FeatureTypeNameInfo featureTypeName;

	private FilterTypeInfo filter;

	private List<ExtentInfo> extentList = new ArrayList<ExtentInfo>();

	/**
	 * Get the 'FeatureTypeName' element value.
	 * 
	 * @return value
	 */
	public FeatureTypeNameInfo getFeatureTypeName() {
		return featureTypeName;
	}

	/**
	 * Set the 'FeatureTypeName' element value.
	 * 
	 * @param featureTypeName
	 */
	public void setFeatureTypeName(FeatureTypeNameInfo featureTypeName) {
		this.featureTypeName = featureTypeName;
	}

	/**
	 * Get the 'Filter' element value.
	 * 
	 * @return value
	 */
	public FilterTypeInfo getFilter() {
		return filter;
	}

	/**
	 * Set the 'Filter' element value.
	 * 
	 * @param filter
	 */
	public void setFilter(FilterTypeInfo filter) {
		this.filter = filter;
	}

	/**
	 * Get the list of 'Extent' element items.
	 * 
	 * @return list
	 */
	public List<ExtentInfo> getExtentList() {
		return extentList;
	}

	/**
	 * Set the list of 'Extent' element items.
	 * 
	 * @param list
	 */
	public void setExtentList(List<ExtentInfo> list) {
		extentList = list;
	}
}
