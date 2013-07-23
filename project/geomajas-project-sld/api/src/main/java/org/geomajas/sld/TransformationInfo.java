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

import org.geomajas.sld.expression.FunctionTypeInfo;

/**
 * 
 A Transformation specifies a Rendering Transformation to be applied to the
 * input data before styling.
 * 
 * 
 * Schema fragment(s) for this class:
 * 
 * <pre>
 * &lt;xs:element xmlns:ns="http://www.opengis.net/ogc" 
 *   xmlns:ns1="http://www.opengis.net/sld" xmlns:xs="http://www.w3.org/2001/XMLSchema" 
 *   name="Transformation">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element ref="ns:Function"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 * @author Jan De Moerloose
 * @since 1.2.0
 */
public class TransformationInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	private FunctionTypeInfo function;

	/**
	 * Get the 'Function' element value.
	 * 
	 * @return value
	 */
	public FunctionTypeInfo getFunction() {
		return function;
	}

	/**
	 * Set the 'Function' element value.
	 * 
	 * @param function
	 */
	public void setFunction(FunctionTypeInfo function) {
		this.function = function;
	}
	
	
}
