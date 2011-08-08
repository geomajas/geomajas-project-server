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
 A "Mark" specifies a geometric shape and applies coloring to it.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="Mark">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element ref="ns:WellKnownName" minOccurs="0"/>
 *       &lt;xs:element ref="ns:Fill" minOccurs="0"/>
 *       &lt;xs:element ref="ns:Stroke" minOccurs="0"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 *
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Api(allMethods = true)
public class MarkInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	private WellKnownNameInfo wellKnownName;

	private FillInfo fill;

	private StrokeInfo stroke;

	/**
	 * Get the 'WellKnownName' element value.
	 * 
	 * @return value
	 */
	public WellKnownNameInfo getWellKnownName() {
		return wellKnownName;
	}

	/**
	 * Set the 'WellKnownName' element value.
	 * 
	 * @param wellKnownName
	 */
	public void setWellKnownName(WellKnownNameInfo wellKnownName) {
		this.wellKnownName = wellKnownName;
	}

	/**
	 * Get the 'Fill' element value.
	 * 
	 * @return value
	 */
	public FillInfo getFill() {
		return fill;
	}

	/**
	 * Set the 'Fill' element value.
	 * 
	 * @param fill
	 */
	public void setFill(FillInfo fill) {
		this.fill = fill;
	}

	/**
	 * Get the 'Stroke' element value.
	 * 
	 * @return value
	 */
	public StrokeInfo getStroke() {
		return stroke;
	}

	/**
	 * Set the 'Stroke' element value.
	 * 
	 * @param stroke
	 */
	public void setStroke(StrokeInfo stroke) {
		this.stroke = stroke;
	}
}
