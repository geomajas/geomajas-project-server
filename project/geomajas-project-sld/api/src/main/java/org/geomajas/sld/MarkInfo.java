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
 * @since 1.0.0
 */
@Api(allMethods = true)
public class MarkInfo implements Serializable {

	private static final long serialVersionUID = 100;

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
	 * @param wellKnownName well known name
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
	 * @param fill fill
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
	 * @param stroke stroke
	 */
	public void setStroke(StrokeInfo stroke) {
		this.stroke = stroke;
	}

	@Override
	public java.lang.String toString() {
		return "MarkInfo(wellKnownName=" + this.getWellKnownName() + ", fill=" + this.getFill() + ", stroke="
				+ this.getStroke() + ")";
	}

	@Override
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof MarkInfo)) {
			return false;
		}
		final MarkInfo other = (MarkInfo) o;
		if (!other.canEqual(this)) {
			return false;
		}
		if (this.getWellKnownName() == null ? other.getWellKnownName() != null : !this.getWellKnownName().equals(
				other.getWellKnownName())) {
			return false;
		}
		if (this.getFill() == null ? other.getFill() != null : !this.getFill().equals(other.getFill())) {
			return false;
		}
		if (this.getStroke() == null ? other.getStroke() != null : !this.getStroke().equals(other.getStroke())) {
			return false;
		}
		return true;
	}

	/**
	 * Check whether the object can be compared with this.
	 * 
	 * @param other other object
	 * @return true when object can be compared
	 */
	public boolean canEqual(final Object other) {
		return other instanceof MarkInfo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getWellKnownName() == null ? 0 : this.getWellKnownName().hashCode());
		result = result * prime + (this.getFill() == null ? 0 : this.getFill().hashCode());
		result = result * prime + (this.getStroke() == null ? 0 : this.getStroke().hashCode());
		return result;
	}
}