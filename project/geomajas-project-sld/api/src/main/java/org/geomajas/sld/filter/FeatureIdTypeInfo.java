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

/**
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:complexType
 * xmlns:ns="http://www.opengis.net/ogc"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="FeatureIdType">
 *   &lt;xs:attribute type="xs:string" use="required" name="fid"/>
 * &lt;/xs:complexType>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class FeatureIdTypeInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private String fid;

	/**
	 * Get the 'fid' attribute value.
	 * 
	 * @return value
	 */
	public String getFid() {
		return fid;
	}

	/**
	 * Set the 'fid' attribute value.
	 * 
	 * @param fid
	 */
	public void setFid(String fid) {
		this.fid = fid;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "FeatureIdTypeInfo(fid=" + this.getFid() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof FeatureIdTypeInfo)) {
			return false;
		}
		final FeatureIdTypeInfo other = (FeatureIdTypeInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getFid() == null ? other.getFid() != null : !this.getFid().equals((java.lang.Object) other.getFid())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof FeatureIdTypeInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getFid() == null ? 0 : this.getFid().hashCode());
		return result;
	}
}