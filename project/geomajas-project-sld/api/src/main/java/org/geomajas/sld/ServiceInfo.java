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
 A Service refers to the type of a remote OWS server.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="Service">
 *   &lt;xs:simpleType>
 *     &lt;!-- Reference to inner class ServiceInfoInner -->
 *   &lt;/xs:simpleType>
 * &lt;/xs:element>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class ServiceInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private ServiceInfoInner service;

	/**
	 * Get the 'Service' element value.
	 * 
	 * @return value
	 */
	public ServiceInfoInner getService() {
		return service;
	}

	/**
	 * Set the 'Service' element value.
	 * 
	 * @param service
	 */
	public void setService(ServiceInfoInner service) {
		this.service = service;
	}

	/**
	 * Schema fragment(s) for this class:...
	 * 
	 * <pre>
	 * &lt;xs:simpleType
	 * xmlns:xs="http://www.w3.org/2001/XMLSchema">
	 *   &lt;xs:restriction base="xs:string">
	 *     &lt;xs:enumeration value="WFS"/>
	 *     &lt;xs:enumeration value="WCS"/>
	 *   &lt;/xs:restriction>
	 * &lt;/xs:simpleType>
	 * </pre>
	 */
	public static enum ServiceInfoInner implements Serializable {
		WFS, WCS;

		private static final long serialVersionUID = 100;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "ServiceInfo(service=" + this.getService() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof ServiceInfo)) {
			return false;
		}
		final ServiceInfo other = (ServiceInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getService() == null ? other.getService() != null : !this.getService().equals(
				(java.lang.Object) other.getService())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof ServiceInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getService() == null ? 0 : this.getService().hashCode());
		return result;
	}
}