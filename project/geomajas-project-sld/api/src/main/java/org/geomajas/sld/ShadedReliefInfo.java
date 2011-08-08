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
 "ShadedRelief" specifies the application of relief shading (or "hill shading") to a DEM raster to give it somewhat of
 * a three-dimensional effect and to make elevation changes more visible.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="ShadedRelief">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element ref="ns:BrightnessOnly" minOccurs="0"/>
 *       &lt;xs:element ref="ns:ReliefFactor" minOccurs="0"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 *
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Api(allMethods = true)
public class ShadedReliefInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	private BrightnessOnlyInfo brightnessOnly;

	private ReliefFactorInfo reliefFactor;

	/**
	 * Get the 'BrightnessOnly' element value.
	 * 
	 * @return value
	 */
	public BrightnessOnlyInfo getBrightnessOnly() {
		return brightnessOnly;
	}

	/**
	 * Set the 'BrightnessOnly' element value.
	 * 
	 * @param brightnessOnly
	 */
	public void setBrightnessOnly(BrightnessOnlyInfo brightnessOnly) {
		this.brightnessOnly = brightnessOnly;
	}

	/**
	 * Get the 'ReliefFactor' element value.
	 * 
	 * @return value
	 */
	public ReliefFactorInfo getReliefFactor() {
		return reliefFactor;
	}

	/**
	 * Set the 'ReliefFactor' element value.
	 * 
	 * @param reliefFactor
	 */
	public void setReliefFactor(ReliefFactorInfo reliefFactor) {
		this.reliefFactor = reliefFactor;
	}
}
