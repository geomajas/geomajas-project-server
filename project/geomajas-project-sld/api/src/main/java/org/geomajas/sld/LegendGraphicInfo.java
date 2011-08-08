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
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld" 
 *
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="LegendGraphic">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element ref="ns:Graphic"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 *
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Api(allMethods = true)
public class LegendGraphicInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	private GraphicInfo graphic;

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
