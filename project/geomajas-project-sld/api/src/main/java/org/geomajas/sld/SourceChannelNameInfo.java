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
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:string" name="SourceChannelName"/>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class SourceChannelNameInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private String sourceChannelName;

	/**
	 * Get the 'SourceChannelName' element value.
	 * 
	 * @return value
	 */
	public String getSourceChannelName() {
		return sourceChannelName;
	}

	/**
	 * Set the 'SourceChannelName' element value.
	 * 
	 * @param sourceChannelName
	 */
	public void setSourceChannelName(String sourceChannelName) {
		this.sourceChannelName = sourceChannelName;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "SourceChannelNameInfo(sourceChannelName=" + this.getSourceChannelName() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof SourceChannelNameInfo)) {
			return false;
		}
		final SourceChannelNameInfo other = (SourceChannelNameInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getSourceChannelName() == null ? other.getSourceChannelName() != null : !this.getSourceChannelName()
				.equals((java.lang.Object) other.getSourceChannelName())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof SourceChannelNameInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getSourceChannelName() == null ? 0 : this.getSourceChannelName().hashCode());
		return result;
	}
}