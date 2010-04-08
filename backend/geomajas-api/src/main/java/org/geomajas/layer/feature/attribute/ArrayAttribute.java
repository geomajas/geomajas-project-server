/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.layer.feature.attribute;

import org.geomajas.global.Api;
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
 */
@Api(allMethods = true)
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
	 * @param value value for array
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
}