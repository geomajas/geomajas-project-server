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

import org.geomajas.annotation.Api;
import org.geomajas.sld.expression.ExpressionInfo;

/**
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:complexType
 * xmlns:ns="http://www.opengis.net/ogc"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="PropertyIsBetweenType">
 *   &lt;xs:complexContent>
 *     &lt;xs:extension base="ns:ComparisonOpsType">
 *       &lt;xs:sequence>
 *         &lt;xs:element ref="ns:expression"/>
 *         &lt;xs:element type="ns:LowerBoundaryType" name="LowerBoundary"/>
 *         &lt;xs:element type="ns:UpperBoundaryType" name="UpperBoundary"/>
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
public class PropertyIsBetweenTypeInfo extends ComparisonOpsTypeInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	private ExpressionInfo expression;

	private LowerBoundaryTypeInfo lowerBoundary;

	private UpperBoundaryTypeInfo upperBoundary;

	/**
	 * Get the 'expression' element value.
	 * 
	 * @return value
	 */
	public ExpressionInfo getExpression() {
		return expression;
	}

	/**
	 * Set the 'expression' element value.
	 * 
	 * @param expression
	 */
	public void setExpression(ExpressionInfo expression) {
		this.expression = expression;
	}

	/**
	 * Get the 'LowerBoundary' element value.
	 * 
	 * @return value
	 */
	public LowerBoundaryTypeInfo getLowerBoundary() {
		return lowerBoundary;
	}

	/**
	 * Set the 'LowerBoundary' element value.
	 * 
	 * @param lowerBoundary
	 */
	public void setLowerBoundary(LowerBoundaryTypeInfo lowerBoundary) {
		this.lowerBoundary = lowerBoundary;
	}

	/**
	 * Get the 'UpperBoundary' element value.
	 * 
	 * @return value
	 */
	public UpperBoundaryTypeInfo getUpperBoundary() {
		return upperBoundary;
	}

	/**
	 * Set the 'UpperBoundary' element value.
	 * 
	 * @param upperBoundary
	 */
	public void setUpperBoundary(UpperBoundaryTypeInfo upperBoundary) {
		this.upperBoundary = upperBoundary;
	}
}
