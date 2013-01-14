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
import org.geomajas.annotation.Api;

/**
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:string" name="SemanticTypeIdentifier"/>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class SemanticTypeIdentifierInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private String semanticTypeIdentifier;

	/**
	 * Get the 'SemanticTypeIdentifier' element value.
	 * 
	 * @return value
	 */
	public String getSemanticTypeIdentifier() {
		return semanticTypeIdentifier;
	}

	/**
	 * Set the 'SemanticTypeIdentifier' element value.
	 * 
	 * @param semanticTypeIdentifier
	 */
	public void setSemanticTypeIdentifier(String semanticTypeIdentifier) {
		this.semanticTypeIdentifier = semanticTypeIdentifier;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "SemanticTypeIdentifierInfo(semanticTypeIdentifier=" + this.getSemanticTypeIdentifier() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof SemanticTypeIdentifierInfo)) {
			return false;
		}
		final SemanticTypeIdentifierInfo other = (SemanticTypeIdentifierInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getSemanticTypeIdentifier() == null ? other.getSemanticTypeIdentifier() != null : !this
				.getSemanticTypeIdentifier().equals((java.lang.Object) other.getSemanticTypeIdentifier())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof SemanticTypeIdentifierInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime
				+ (this.getSemanticTypeIdentifier() == null ? 0 : this.getSemanticTypeIdentifier().hashCode());
		return result;
	}
}