/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
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
 * @since 1.10.0
 */
@Api(allMethods = true)
public class ServiceInfo implements Serializable {

	private static final long serialVersionUID = 1100;

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

		private static final long serialVersionUID = 1100;
	}
}
