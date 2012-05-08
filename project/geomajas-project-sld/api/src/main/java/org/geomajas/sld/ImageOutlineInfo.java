/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
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
 "ImageOutline" specifies how individual source rasters in a multi-raster set (such as a set of satellite-image
 * scenes) should be outlined to make the individual-image locations visible.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="ImageOutline">
 *   &lt;xs:complexType>
 *     &lt;xs:choice>
 *       &lt;xs:element ref="ns:LineSymbolizer"/>
 *       &lt;xs:element ref="ns:PolygonSymbolizer"/>
 *     &lt;/xs:choice>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class ImageOutlineInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private int imageOutlineSelect = -1;

	private static final int LINE_SYMBOLIZER_CHOICE = 0;

	private static final int POLYGON_SYMBOLIZER_CHOICE = 1;

	private LineSymbolizerInfo lineSymbolizer;

	private PolygonSymbolizerInfo polygonSymbolizer;

	private void setImageOutlineSelect(int choice) {
		if (imageOutlineSelect == -1) {
			imageOutlineSelect = choice;
		} else if (imageOutlineSelect != choice) {
			throw new IllegalStateException("Need to call clearImageOutlineSelect() before changing existing choice");
		}
	}

	/**
	 * Clear the choice selection.
	 */
	public void clearImageOutlineSelect() {
		imageOutlineSelect = -1;
	}

	/**
	 * Check if LineSymbolizer is current selection for choice.
	 * 
	 * @return <code>true</code> if selection, <code>false</code> if not
	 */
	public boolean ifLineSymbolizer() {
		return imageOutlineSelect == LINE_SYMBOLIZER_CHOICE;
	}

	/**
	 * Get the 'LineSymbolizer' element value.
	 * 
	 * @return value
	 */
	public LineSymbolizerInfo getLineSymbolizer() {
		return lineSymbolizer;
	}

	/**
	 * Set the 'LineSymbolizer' element value.
	 * 
	 * @param lineSymbolizer
	 */
	public void setLineSymbolizer(LineSymbolizerInfo lineSymbolizer) {
		setImageOutlineSelect(LINE_SYMBOLIZER_CHOICE);
		this.lineSymbolizer = lineSymbolizer;
	}

	/**
	 * Check if PolygonSymbolizer is current selection for choice.
	 * 
	 * @return <code>true</code> if selection, <code>false</code> if not
	 */
	public boolean ifPolygonSymbolizer() {
		return imageOutlineSelect == POLYGON_SYMBOLIZER_CHOICE;
	}

	/**
	 * Get the 'PolygonSymbolizer' element value.
	 * 
	 * @return value
	 */
	public PolygonSymbolizerInfo getPolygonSymbolizer() {
		return polygonSymbolizer;
	}

	/**
	 * Set the 'PolygonSymbolizer' element value.
	 * 
	 * @param polygonSymbolizer
	 */
	public void setPolygonSymbolizer(PolygonSymbolizerInfo polygonSymbolizer) {
		setImageOutlineSelect(POLYGON_SYMBOLIZER_CHOICE);
		this.polygonSymbolizer = polygonSymbolizer;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "ImageOutlineInfo(imageOutlineSelect=" + this.imageOutlineSelect + ", lineSymbolizer="
				+ this.getLineSymbolizer() + ", polygonSymbolizer=" + this.getPolygonSymbolizer() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof ImageOutlineInfo)) {
			return false;
		}
		final ImageOutlineInfo other = (ImageOutlineInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.imageOutlineSelect != other.imageOutlineSelect) {
			return false;
		}
		if (this.getLineSymbolizer() == null ? other.getLineSymbolizer() != null : !this.getLineSymbolizer().equals(
				(java.lang.Object) other.getLineSymbolizer())) {
			return false;
		}
		if (this.getPolygonSymbolizer() == null ? other.getPolygonSymbolizer() != null : !this.getPolygonSymbolizer()
				.equals((java.lang.Object) other.getPolygonSymbolizer())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof ImageOutlineInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + this.imageOutlineSelect;
		result = result * prime + (this.getLineSymbolizer() == null ? 0 : this.getLineSymbolizer().hashCode());
		result = result * prime + (this.getPolygonSymbolizer() == null ? 0 : this.getPolygonSymbolizer().hashCode());
		return result;
	}
}