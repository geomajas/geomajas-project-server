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
import org.geomajas.layer.feature.Attribute;

/**
 * <p>
 * Attribute type that holds an array of other attributes. Note that not all vector layers support this, as it is not
 * counted as a primitive type.
 * </p>
 * 
 * @param <VALUE_TYPE>
 *            type for the attribute value
 * 
 * @author Pieter De Graef
 * @since 1.6.0
 * @deprecated use {@link OneToManyAttribute} instead
 */
@Api(allMethods = true)
@Deprecated 
public class ArrayAttribute<VALUE_TYPE> implements Attribute<VALUE_TYPE[]> {

	private static final long serialVersionUID = 151L;

	private boolean editable;

	private VALUE_TYPE[] value;

	/**
	 * Constructor, create attribute without value (needed for GWT).
	 */
	public ArrayAttribute() {
	}

	/**
	 * Create array attribute with given array as value.
	 * 
	 * @param value
	 *            value for array
	 */
	public ArrayAttribute(VALUE_TYPE[] value) {
		this.value = value;
	}

	/**
	 * {@inheritDoc}
	 */
	public VALUE_TYPE[] getValue() {
		return value;
	}

	/**
	 * Apply a new array of values for this attribute.
	 * 
	 * @param value
	 *            The new array of values.
	 * @since 1.7.0
	 */
	public void setValue(VALUE_TYPE[] value) {
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
	public boolean isEmpty() {
		return value == null;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isPrimitive() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	/**
	 * Create a clone of this attribute object.
	 * 
	 * @since 1.7.0
	 * @return A copy of this array attribute.
	 */
	@SuppressWarnings("unchecked")
	@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "CN_IDIOM_NO_SUPER_CALL",
			justification = "needed for GWT")
	public Object clone() { // NOSONAR
		ArrayAttribute clone = new ArrayAttribute();
		if (value != null) {
			Object[] clones = new Object[value.length];
			System.arraycopy(value, 0, clones, 0, value.length);
			clone.setValue(clones);
		}
		clone.setEditable(isEditable());
		return clone;
	}
}