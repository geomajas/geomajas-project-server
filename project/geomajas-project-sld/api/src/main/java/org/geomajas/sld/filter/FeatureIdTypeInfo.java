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
 * @since 1.10.0
 */
@Api(allMethods = true)
public class FeatureIdTypeInfo implements Serializable {

	private static final long serialVersionUID = 1100;

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
}
