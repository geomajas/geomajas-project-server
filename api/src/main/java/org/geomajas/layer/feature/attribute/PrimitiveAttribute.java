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
package org.geomajas.layer.feature.attribute;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.PrimitiveType;
import org.geomajas.global.Json;
import org.geomajas.layer.feature.Attribute;

/**
 * Primitive value attribute. This is the base class for simple attribute types such as integer, float, string,...
 * 
 * @param <VALUE_TYPE>
 *            type which is represented by the primitive attribute
 * 
 * @author Jan De Moerloose
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
public abstract class PrimitiveAttribute<VALUE_TYPE> implements Attribute<VALUE_TYPE> {

	private static final long serialVersionUID = 151L;

	private boolean editable;

	/**
	 * The type of attribute.
	 */
	private PrimitiveType type;

	/**
	 * The value this attribute currently holds.
	 */
	private VALUE_TYPE value;

	/**
	 * Default constructor...the attribute will have no type or value, when you use this.
	 */
	protected PrimitiveAttribute() {
		this(null);
	}

	/**
	 * This is the preferred constructor. It immediately sets the attribute's type.
	 * 
	 * @param type
	 *            type of primitive attribute
	 */
	protected PrimitiveAttribute(PrimitiveType type) {
		this.type = type;
	}

	/**
	 * Create a clone of this attribute object.
	 * 
	 * @since 1.7.0
	 * @return Return an exact copy of this attribute.
	 */
	public abstract Object clone(); //NOSONAR

	/**
	 * Get type of primitive attribute.
	 * 
	 * @return {@link org.geomajas.configuration.PrimitiveType}
	 */
	@Json(serialize = false)
	public PrimitiveType getType() {
		return type;
	}

	/**
	 * Set type of primitive attribute.
	 * 
	 * @param type
	 *            type of primitive attribute
	 */
	public void setType(PrimitiveType type) {
		this.type = type;
	}

	/**
	 * Return true, as this type is the very definition of a primitive attribute.
	 * 
	 * @return true as this is a primitive attribute
	 */
	@Json(serialize = false)
	public boolean isPrimitive() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Json(serialize = false)
	public boolean isEmpty() {
		return value == null;
	}

	/**
	 * {@inheritDoc}
	 */
	public VALUE_TYPE getValue() {
		return value;
	}

	/**
	 * Set the actual value for the attribute.
	 * 
	 * @param value
	 *            value for the attribute
	 */
	public void setValue(VALUE_TYPE value) {
		this.value = value;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isEditable() {
		return editable;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	/**
	 * Returns a string representation of the primitive value using {@link Object#toString()}.
	 *
	 * @return string value
	 * @since 1.9.0
	 */
	public String toString() {
		return value == null ? "null" : value.toString();
	}
}
