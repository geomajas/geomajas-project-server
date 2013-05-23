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
 A RemoteOWS gives a reference to a remote WFS/WCS/other-OWS server.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="RemoteOWS">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element ref="ns:Service"/>
 *       &lt;xs:element ref="ns:OnlineResource"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class RemoteOWSInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private ServiceInfo service;

	private OnlineResourceInfo onlineResource;

	/**
	 * Get the 'Service' element value.
	 * 
	 * @return value
	 */
	public ServiceInfo getService() {
		return service;
	}

	/**
	 * Set the 'Service' element value.
	 * 
	 * @param service
	 */
	public void setService(ServiceInfo service) {
		this.service = service;
	}

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

	@Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "RemoteOWSInfo(service=" + this.getService() + ", onlineResource=" + this.getOnlineResource() + ")";
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof RemoteOWSInfo)) {
			return false;
		}
		final RemoteOWSInfo other = (RemoteOWSInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getService() == null ? other.getService() != null : !this.getService().equals(
				(java.lang.Object) other.getService())) {
			return false;
		}
		if (this.getOnlineResource() == null ? other.getOnlineResource() != null : !this.getOnlineResource().equals(
				(java.lang.Object) other.getOnlineResource())) {
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
		return other instanceof RemoteOWSInfo;
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getService() == null ? 0 : this.getService().hashCode());
		result = result * prime + (this.getOnlineResource() == null ? 0 : this.getOnlineResource().hashCode());
		return result;
	}
}