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

/**
 * 
 A UserStyle allows user-defined styling and is semantically equivalent to a WMS named style.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="UserStyle">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element ref="ns:Name" minOccurs="0"/>
 *       &lt;xs:element ref="ns:Title" minOccurs="0"/>
 *       &lt;xs:element ref="ns:Abstract" minOccurs="0"/>
 *       &lt;xs:element ref="ns:IsDefault" minOccurs="0"/>
 *       &lt;xs:element ref="ns:FeatureTypeStyle" maxOccurs="unbounded"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 *
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Api(allMethods = true)
public class UserStyleInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	private String name;

	private String title;

	private AbstractInfo aAbstract;

	private IsDefaultInfo isDefault;

	private List<FeatureTypeStyleInfo> featureTypeStyleList = new ArrayList<FeatureTypeStyleInfo>();

	/**
	 * Get the 'Name' element value.
	 * 
	 * @return value
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the 'Name' element value.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the 'Title' element value.
	 * 
	 * @return value
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set the 'Title' element value.
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Get the 'Abstract' element value.
	 * 
	 * @return value
	 */
	public AbstractInfo getAbstract() {
		return aAbstract;
	}

	/**
	 * Set the 'Abstract' element value.
	 * 
	 * @param _abstract
	 */
	public void setAbstract(AbstractInfo aAbstract) {
		this.aAbstract = aAbstract;
	}

	/**
	 * Get the 'IsDefault' element value.
	 * 
	 * @return value
	 */
	public IsDefaultInfo getIsDefault() {
		return isDefault;
	}

	/**
	 * Set the 'IsDefault' element value.
	 * 
	 * @param isDefault
	 */
	public void setIsDefault(IsDefaultInfo isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * Get the list of 'FeatureTypeStyle' element items.
	 * 
	 * @return list
	 */
	public List<FeatureTypeStyleInfo> getFeatureTypeStyleList() {
		return featureTypeStyleList;
	}

	/**
	 * Set the list of 'FeatureTypeStyle' element items.
	 * 
	 * @param list
	 */
	public void setFeatureTypeStyleList(List<FeatureTypeStyleInfo> list) {
		featureTypeStyleList = list;
	}
}
