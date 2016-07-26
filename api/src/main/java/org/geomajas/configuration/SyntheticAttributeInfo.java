/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.configuration;

import org.geomajas.annotation.Api;
import org.geomajas.global.Json;
import org.geomajas.layer.feature.SyntheticAttributeBuilder;

/**
 * A synthetic attribute has its value calculated and not stored in the database. It is typically built from the normal
 * attribute values. The value can be built using Spring expression language or in code.
 * <p/>
 * A synthetic attribute cannot be edited.
 *
 * @author Joachim Van der Auwera
 * @since 1.10.0
 */
@Api(allMethods = true)
public class SyntheticAttributeInfo extends AbstractReadOnlyAttributeInfo {

	private static final long serialVersionUID = 1100L;

	private String expression;
	private transient SyntheticAttributeBuilder<?> attributeBuilder; // do not serialize to client

	/**
	 * Expression (using Spring Expression Language) to build the attribute value).
	 * <p/>
	 * This will always result in a String attribute value. It is only used if no {@link #attributeBuilder} is given.
	 * 
	 * @return expression for building the attribute
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * Expression (using Spring Expression Language) to build the attribute value).
	 * <p/>
	 * This will always result in a String attribute value. It is only used if no {@link #attributeBuilder} is given.
	 * 
	 * @param expression attribute expression in Spring Expression Language
	 */
	public void setExpression(String expression) {
		this.expression = expression;
	}

	/**
	 * Set the class which should be used to build an {@link org.geomajas.layer.feature.Attribute} object for this
	 * description.
	 * 
	 * @return attribute builder
	 */
	@Json(serialize = false)
	public SyntheticAttributeBuilder<?> getAttributeBuilder() {
		return attributeBuilder;
	}

	/**
	 * Get the class which should be used to build an {@link org.geomajas.layer.feature.Attribute} object for this
	 * description.
	 *
	 * @param attributeBuilder attribute builder
	 */
	public void setAttributeBuilder(SyntheticAttributeBuilder<?> attributeBuilder) {
		this.attributeBuilder = attributeBuilder;
	}
}
