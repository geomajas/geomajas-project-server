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
 A "PolygonSymbolizer" specifies the rendering of a polygon or area geometry, including its interior fill and border
 * stroke.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:sld="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" substitutionGroup="sld:Symbolizer" name="PolygonSymbolizer">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="sld:SymbolizerType">
 *         &lt;xs:sequence>
 *           &lt;xs:element ref="sld:Geometry" minOccurs="0"/>
 *           &lt;xs:element ref="sld:Fill" minOccurs="0"/>
 *           &lt;xs:element ref="sld:Stroke" minOccurs="0"/>
 *         &lt;/xs:sequence>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class PolygonSymbolizerInfo extends SymbolizerTypeInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private GeometryInfo geometry;

	private FillInfo fill;

	private StrokeInfo stroke;

	/**
	 * Get the 'Geometry' element value.
	 * 
	 * @return value
	 */
	public GeometryInfo getGeometry() {
		return geometry;
	}

	/**
	 * Set the 'Geometry' element value.
	 * 
	 * @param geometry
	 */
	public void setGeometry(GeometryInfo geometry) {
		this.geometry = geometry;
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

	@Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "PolygonSymbolizerInfo(geometry=" + this.getGeometry() + ", fill=" + this.getFill() + ", stroke="
				+ this.getStroke() + ")";
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof PolygonSymbolizerInfo)) {
			return false;
		}
		final PolygonSymbolizerInfo other = (PolygonSymbolizerInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		if (this.getGeometry() == null ? other.getGeometry() != null : !this.getGeometry().equals(
				(java.lang.Object) other.getGeometry())) {
			return false;
		}
		if (this.getFill() == null ? other.getFill() != null : !this.getFill().equals(
				(java.lang.Object) other.getFill())) {
			return false;
		}
		if (this.getStroke() == null ? other.getStroke() != null : !this.getStroke().equals(
				(java.lang.Object) other.getStroke())) {
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
		return other instanceof PolygonSymbolizerInfo;
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + super.hashCode();
		result = result * prime + (this.getGeometry() == null ? 0 : this.getGeometry().hashCode());
		result = result * prime + (this.getFill() == null ? 0 : this.getFill().hashCode());
		result = result * prime + (this.getStroke() == null ? 0 : this.getStroke().hashCode());
		return result;
	}
}