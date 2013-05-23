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
import org.geomajas.sld.geometry.AbstractGeometryInfo;
import org.geomajas.sld.geometry.BoxTypeInfo;

/**
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:complexType
 * xmlns:ns="http://www.opengis.net/ogc"
 * xmlns:ns1="http://www.opengis.net/gml"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="BinarySpatialOpType">
 *   &lt;xs:complexContent>
 *     &lt;xs:extension base="ns:SpatialOpsType">
 *       &lt;xs:sequence>
 *         &lt;xs:element ref="ns:PropertyName"/>
 *         &lt;xs:choice>
 *           &lt;xs:element ref="ns1:_Geometry"/>
 *           &lt;xs:element ref="ns1:Box"/>
 *         &lt;/xs:choice>
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
public class BinarySpatialOpTypeInfo extends SpatialOpsTypeInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private PropertyNameInfo propertyName;

	private int choiceSelect = -1;

	private static final int GEOMETRY_CHOICE = 0;

	private static final int BOX_CHOICE = 1;

	private AbstractGeometryInfo geometry;

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

	private void setChoiceSelect(int choice) {
		if (choiceSelect == -1) {
			choiceSelect = choice;
		} else if (choiceSelect != choice) {
			throw new IllegalStateException("Need to call clearChoiceSelect() before changing existing choice");
		}
	}

	/**
	 * Clear the choice selection.
	 */
	public void clearChoiceSelect() {
		choiceSelect = -1;
	}

	/**
	 * Check if Geometry is current selection for choice.
	 * 
	 * @return <code>true</code> if selection, <code>false</code> if not
	 */
	public boolean ifGeometry() {
		return choiceSelect == GEOMETRY_CHOICE;
	}

	/**
	 * Get the '_Geometry' element value.
	 * 
	 * @return value
	 */
	public AbstractGeometryInfo getGeometry() {
		return geometry;
	}

	/**
	 * Set the '_Geometry' element value.
	 * 
	 * @param _Geometry
	 */
	public void setGeometry(AbstractGeometryInfo geometry) {
		setChoiceSelect(GEOMETRY_CHOICE);
		this.geometry = geometry;
	}

	/**
	 * Check if Box is current selection for choice.
	 * 
	 * @return <code>true</code> if selection, <code>false</code> if not
	 */
	public boolean ifBox() {
		return choiceSelect == BOX_CHOICE;
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
		setChoiceSelect(BOX_CHOICE);
		this.box = box;
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "BinarySpatialOpTypeInfo(propertyName=" + this.getPropertyName() + ", choiceSelect=" + this.choiceSelect
				+ ", geometry=" + this.getGeometry() + ", box=" + this.getBox() + ")";
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof BinarySpatialOpTypeInfo)) {
			return false;
		}
		final BinarySpatialOpTypeInfo other = (BinarySpatialOpTypeInfo) o;
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
		if (this.choiceSelect != other.choiceSelect) {
			return false;
		}
		if (this.getGeometry() == null ? other.getGeometry() != null : !this.getGeometry().equals(
				(java.lang.Object) other.getGeometry())) {
			return false;
		}
		if (this.getBox() == null ? other.getBox() != null : !this.getBox().equals((java.lang.Object) other.getBox())) {
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
		return other instanceof BinarySpatialOpTypeInfo;
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + super.hashCode();
		result = result * prime + (this.getPropertyName() == null ? 0 : this.getPropertyName().hashCode());
		result = result * prime + this.choiceSelect;
		result = result * prime + (this.getGeometry() == null ? 0 : this.getGeometry().hashCode());
		result = result * prime + (this.getBox() == null ? 0 : this.getBox().hashCode());
		return result;
	}
}