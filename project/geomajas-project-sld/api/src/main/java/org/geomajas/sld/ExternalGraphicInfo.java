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
 * 
 An "ExternalGraphic" gives a reference to an external raster or vector graphical object.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="ExternalGraphic">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element ref="ns:OnlineResource"/>
 *       &lt;xs:element ref="ns:Format"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class ExternalGraphicInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private OnlineResourceInfo onlineResource;

	private FormatInfo format;

	/**
	 * Get the 'OnlineResource' element value.
	 * 
	 * @return value
	 */
	public OnlineResourceInfo getOnlineResource() {
		return onlineResource;
	}

	/**
	 * Set the 'OnlineResource' element value.
	 * 
	 * @param onlineResource
	 */
	public void setOnlineResource(OnlineResourceInfo onlineResource) {
		this.onlineResource = onlineResource;
	}

	/**
	 * Get the 'Format' element value.
	 * 
	 * @return value
	 */
	public FormatInfo getFormat() {
		return format;
	}

	/**
	 * Set the 'Format' element value.
	 * 
	 * @param format
	 */
	public void setFormat(FormatInfo format) {
		this.format = format;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "ExternalGraphicInfo(onlineResource=" + this.getOnlineResource() + ", format=" + this.getFormat() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof ExternalGraphicInfo)) {
			return false;
		}
		final ExternalGraphicInfo other = (ExternalGraphicInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getOnlineResource() == null ? other.getOnlineResource() != null : !this.getOnlineResource().equals(
				(java.lang.Object) other.getOnlineResource())) {
			return false;
		}
		if (this.getFormat() == null ? other.getFormat() != null : !this.getFormat().equals(
				(java.lang.Object) other.getFormat())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof ExternalGraphicInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getOnlineResource() == null ? 0 : this.getOnlineResource().hashCode());
		result = result * prime + (this.getFormat() == null ? 0 : this.getFormat().hashCode());
		return result;
	}
}