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
 * @since 1.10.0
 */
@Api(allMethods = true)
public class ImageOutlineInfo implements Serializable {

	private static final long serialVersionUID = 1100;

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
}
