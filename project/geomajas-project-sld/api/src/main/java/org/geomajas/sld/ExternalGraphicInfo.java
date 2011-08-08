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

import org.geomajas.annotations.Api;

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
 * @since 1.10.0
 */
@Api(allMethods = true)
public class ExternalGraphicInfo implements Serializable {

	private static final long serialVersionUID = 1100;

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
}
