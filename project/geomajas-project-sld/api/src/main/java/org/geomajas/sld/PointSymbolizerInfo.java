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
 A "PointSymbolizer" specifies the rendering of a "graphic symbol" at a point.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:sld="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" substitutionGroup="sld:Symbolizer" name="PointSymbolizer">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="sld:SymbolizerType">
 *         &lt;xs:sequence>
 *           &lt;xs:element ref="sld:Geometry" minOccurs="0"/>
 *           &lt;xs:element ref="sld:Graphic" minOccurs="0"/>
 *         &lt;/xs:sequence>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 *
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Api(allMethods = true)
public class PointSymbolizerInfo extends SymbolizerTypeInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	private GeometryInfo geometry;

	private GraphicInfo graphic;

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
	 * Get the 'Graphic' element value.
	 * 
	 * @return value
	 */
	public GraphicInfo getGraphic() {
		return graphic;
	}

	/**
	 * Set the 'Graphic' element value.
	 * 
	 * @param graphic
	 */
	public void setGraphic(GraphicInfo graphic) {
		this.graphic = graphic;
	}
}
