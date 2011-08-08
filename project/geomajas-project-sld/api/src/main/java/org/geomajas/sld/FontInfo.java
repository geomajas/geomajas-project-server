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
 A "Font" element specifies the text font to use. The allowed CssParameters are: "font-family", "font-style",
 * "font-weight", and "font-size".
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="Font">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
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
public class FontInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	private List<CssParameterInfo> cssParameterList = new ArrayList<CssParameterInfo>();

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
