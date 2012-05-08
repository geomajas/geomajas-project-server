/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
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
 * &lt;xs:complexType
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="SelectedChannelType">
 *   &lt;xs:sequence>
 *     &lt;xs:element ref="ns:SourceChannelName"/>
 *     &lt;xs:element ref="ns:ContrastEnhancement" minOccurs="0"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class SelectedChannelTypeInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private SourceChannelNameInfo sourceChannelName;

	private ContrastEnhancementInfo contrastEnhancement;

	/**
	 * Get the 'SourceChannelName' element value.
	 * 
	 * @return value
	 */
	public SourceChannelNameInfo getSourceChannelName() {
		return sourceChannelName;
	}

	/**
	 * Set the 'SourceChannelName' element value.
	 * 
	 * @param sourceChannelName
	 */
	public void setSourceChannelName(SourceChannelNameInfo sourceChannelName) {
		this.sourceChannelName = sourceChannelName;
	}

	/**
	 * Get the 'ContrastEnhancement' element value.
	 * 
	 * @return value
	 */
	public ContrastEnhancementInfo getContrastEnhancement() {
		return contrastEnhancement;
	}

	/**
	 * Set the 'ContrastEnhancement' element value.
	 * 
	 * @param contrastEnhancement
	 */
	public void setContrastEnhancement(ContrastEnhancementInfo contrastEnhancement) {
		this.contrastEnhancement = contrastEnhancement;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "SelectedChannelTypeInfo(sourceChannelName=" + this.getSourceChannelName() + ", contrastEnhancement="
				+ this.getContrastEnhancement() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof SelectedChannelTypeInfo)) {
			return false;
		}
		final SelectedChannelTypeInfo other = (SelectedChannelTypeInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getSourceChannelName() == null ? other.getSourceChannelName() != null : !this.getSourceChannelName()
				.equals((java.lang.Object) other.getSourceChannelName())) {
			return false;
		}
		if (this.getContrastEnhancement() == null ? other.getContrastEnhancement() != null : !this
				.getContrastEnhancement().equals((java.lang.Object) other.getContrastEnhancement())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof SelectedChannelTypeInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getSourceChannelName() == null ? 0 : this.getSourceChannelName().hashCode());
		result = result * prime
				+ (this.getContrastEnhancement() == null ? 0 : this.getContrastEnhancement().hashCode());
		return result;
	}
}