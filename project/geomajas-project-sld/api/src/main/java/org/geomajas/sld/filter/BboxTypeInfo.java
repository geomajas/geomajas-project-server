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
package org.geomajas.sld.filter;

import java.io.Serializable;
import org.geomajas.annotation.Api;
import org.geomajas.sld.expression.PropertyNameInfo;
import org.geomajas.sld.geometry.BoxTypeInfo;

/**
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:complexType
 * xmlns:ns="http://www.opengis.net/ogc"
 * xmlns:ns1="http://www.opengis.net/gml"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="BBOXType">
 *   &lt;xs:complexContent>
 *     &lt;xs:extension base="ns:SpatialOpsType">
 *       &lt;xs:sequence>
 *         &lt;xs:element ref="ns:PropertyName"/>
 *         &lt;xs:element ref="ns1:Box"/>
 *       &lt;/xs:sequence>
 *     &lt;/xs:extension>
 *   &lt;/xs:complexContent>
 * &lt;/xs:complexType>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class BboxTypeInfo extends SpatialOpsTypeInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private PropertyNameInfo propertyName;

	private BoxTypeInfo box;

	/**
	 * Get the 'PropertyName' element value.
	 * 
	 * @return value
	 */
	public PropertyNameInfo getPropertyName() {
		return propertyName;
	}

	/**
	 * Set the 'PropertyName' element value.
	 * 
	 * @param propertyName
	 */
	public void setPropertyName(PropertyNameInfo propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * Get the 'Box' element value.
	 * 
	 * @return value
	 */
	public BoxTypeInfo getBox() {
		return box;
	}

	/**
	 * Set the 'Box' element value.
	 * 
	 * @param box
	 */
	public void setBox(BoxTypeInfo box) {
		this.box = box;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "BboxTypeInfo(propertyName=" + this.getPropertyName() + ", box=" + this.getBox() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof BboxTypeInfo)) {
			return false;
		}
		final BboxTypeInfo other = (BboxTypeInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		if (this.getPropertyName() == null ? other.getPropertyName() != null : !this.getPropertyName().equals(
				(java.lang.Object) other.getPropertyName())) {
			return false;
		}
		if (this.getBox() == null ? other.getBox() != null : !this.getBox().equals((java.lang.Object) other.getBox())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof BboxTypeInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + super.hashCode();
		result = result * prime + (this.getPropertyName() == null ? 0 : this.getPropertyName().hashCode());
		result = result * prime + (this.getBox() == null ? 0 : this.getBox().hashCode());
		return result;
	}
}