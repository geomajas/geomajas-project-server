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
import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;

/**
 * 
 A "Fill" specifies the pattern for filling an area geometry. The allowed CssParameters are: "fill" (color) and
 * "fill-opacity".
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="Fill">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element ref="ns:GraphicFill" minOccurs="0"/>
 *       &lt;xs:element ref="ns:CssParameter" minOccurs="0" maxOccurs="unbounded"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 *
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Api(allMethods = true)
public class FillInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	private GraphicFillInfo graphicFill;

	private List<CssParameterInfo> cssParameterList = new ArrayList<CssParameterInfo>();

	/**
	 * Get the 'GraphicFill' element value.
	 * 
	 * @return value
	 */
	public GraphicFillInfo getGraphicFill() {
		return graphicFill;
	}

	/**
	 * Set the 'GraphicFill' element value.
	 * 
	 * @param graphicFill
	 */
	public void setGraphicFill(GraphicFillInfo graphicFill) {
		this.graphicFill = graphicFill;
	}

	/**
	 * Get the list of 'CssParameter' element items.
	 * 
	 * @return list
	 */
	public List<CssParameterInfo> getCssParameterList() {
		return cssParameterList;
	}

	/**
	 * Set the list of 'CssParameter' element items.
	 * 
	 * @param list
	 */
	public void setCssParameterList(List<CssParameterInfo> list) {
		cssParameterList = list;
	}
}
