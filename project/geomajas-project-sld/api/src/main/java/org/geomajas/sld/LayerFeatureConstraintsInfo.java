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
 LayerFeatureConstraints define what features &amp; feature types are referenced in a layer.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld" 
 * 
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="LayerFeatureConstraints">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element ref="ns:FeatureTypeConstraint" maxOccurs="unbounded"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class LayerFeatureConstraintsInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private List<FeatureTypeConstraintInfo> featureTypeConstraintList = new ArrayList<FeatureTypeConstraintInfo>();

	/**
	 * Get the list of 'FeatureTypeConstraint' element items.
	 * 
	 * @return list
	 */
	public List<FeatureTypeConstraintInfo> getFeatureTypeConstraintList() {
		return featureTypeConstraintList;
	}

	/**
	 * Set the list of 'FeatureTypeConstraint' element items.
	 * 
	 * @param list
	 */
	public void setFeatureTypeConstraintList(List<FeatureTypeConstraintInfo> list) {
		featureTypeConstraintList = list;
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "LayerFeatureConstraintsInfo(featureTypeConstraintList=" + this.getFeatureTypeConstraintList() + ")";
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof LayerFeatureConstraintsInfo)) {
			return false;
		}
		final LayerFeatureConstraintsInfo other = (LayerFeatureConstraintsInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getFeatureTypeConstraintList() == null ? other.getFeatureTypeConstraintList() != null : !this
				.getFeatureTypeConstraintList().equals((java.lang.Object) other.getFeatureTypeConstraintList())) {
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
		return other instanceof LayerFeatureConstraintsInfo;
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime
				+ (this.getFeatureTypeConstraintList() == null ? 0 : this.getFeatureTypeConstraintList().hashCode());
		return result;
	}
}