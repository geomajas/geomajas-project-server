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
package org.geomajas.sld.filter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.sld.expression.ExpressionInfo;

/**
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:complexType
 * xmlns:ns="http://www.opengis.net/ogc"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="BinaryComparisonOpType">
 *   &lt;xs:complexContent>
 *     &lt;xs:extension base="ns:ComparisonOpsType">
 *       &lt;xs:sequence>
 *         &lt;xs:element ref="ns:expression" minOccurs="2" maxOccurs="2"/>
 *       &lt;/xs:sequence>
 *     &lt;/xs:extension>
 *   &lt;/xs:complexContent>
 * &lt;/xs:complexType>
 * </pre>
 *
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Api(allMethods = true)
public class BinaryComparisonOpTypeInfo extends ComparisonOpsTypeInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	private List<ExpressionInfo> expressionList = new ArrayList<ExpressionInfo>();

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
}
