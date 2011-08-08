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
package org.geomajas.sld.expression;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotations.Api;

/**
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:complexType
 * xmlns:ns="http://www.opengis.net/ogc"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="FunctionType">
 *   &lt;xs:complexContent>
 *     &lt;xs:extension base="ns:ExpressionType">
 *       &lt;xs:sequence>
 *         &lt;xs:element ref="ns:expression" minOccurs="0" maxOccurs="unbounded"/>
 *       &lt;/xs:sequence>
 *       &lt;xs:attribute type="xs:string" use="required" name="name"/>
 *     &lt;/xs:extension>
 *   &lt;/xs:complexContent>
 * &lt;/xs:complexType>
 * </pre>
 *
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Api(allMethods = true)
public class FunctionTypeInfo extends ExpressionInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	private List<ExpressionInfo> expressionList = new ArrayList<ExpressionInfo>();

	private String name;

	/**
	 * Get the list of 'expression' element items.
	 * 
	 * @return list
	 */
	public List<ExpressionInfo> getExpressionList() {
		return expressionList;
	}

	/**
	 * Set the list of 'expression' element items.
	 * 
	 * @param list
	 */
	public void setExpressionList(List<ExpressionInfo> list) {
		expressionList = list;
	}

	/**
	 * Get the 'name' attribute value.
	 * 
	 * @return value
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the 'name' attribute value.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
}
