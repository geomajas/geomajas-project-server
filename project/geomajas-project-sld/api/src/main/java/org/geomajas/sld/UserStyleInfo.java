/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
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
 * @since 1.0.0
 */

@Api(allMethods = true)
public class UserStyleInfo implements Serializable {

	private static final long serialVersionUID = 100;

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

	@Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "UserStyleInfo(name=" + this.getName() + ", title=" + this.getTitle() + ", aAbstract=" + this.aAbstract
				+ ", isDefault=" + this.getIsDefault() + ", featureTypeStyleList=" + this.getFeatureTypeStyleList()
				+ ")";
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof UserStyleInfo)) {
			return false;
		}
		final UserStyleInfo other = (UserStyleInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getName() == null ? other.getName() != null : !this.getName().equals(
				(java.lang.Object) other.getName())) {
			return false;
		}
		if (this.getTitle() == null ? other.getTitle() != null : !this.getTitle().equals(
				(java.lang.Object) other.getTitle())) {
			return false;
		}
		if (this.aAbstract == null ? other.aAbstract != null : !this.aAbstract
				.equals((java.lang.Object) other.aAbstract)) {
			return false;
		}
		if (this.getIsDefault() == null ? other.getIsDefault() != null : !this.getIsDefault().equals(
				(java.lang.Object) other.getIsDefault())) {
			return false;
		}
		if (this.getFeatureTypeStyleList() == null ? other.getFeatureTypeStyleList() != null : !this
				.getFeatureTypeStyleList().equals((java.lang.Object) other.getFeatureTypeStyleList())) {
			return false;
		}
		return true;
	}

	/**
	 * Is there a chance that the object are equal? Verifies that the other object has a comparable type.
	 *
	 * @param other other object
	 * @return true when other is an instance of this type
	 */
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof UserStyleInfo;
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getName() == null ? 0 : this.getName().hashCode());
		result = result * prime + (this.getTitle() == null ? 0 : this.getTitle().hashCode());
		result = result * prime + (this.aAbstract == null ? 0 : this.aAbstract.hashCode());
		result = result * prime + (this.getIsDefault() == null ? 0 : this.getIsDefault().hashCode());
		result = result * prime
				+ (this.getFeatureTypeStyleList() == null ? 0 : this.getFeatureTypeStyleList().hashCode());
		return result;
	}
}