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

import org.geomajas.configuration.PrimitiveType;
import org.geomajas.global.Json;
import org.geomajas.layer.feature.Attribute;

/**
 * Primitive value attribute. This is the base class for simple attribute types such as integer, float, string,...
 *
 * @param <VALUE_TYPE> type which is represented by the primitive attribute
 * 
 * @author Jan De Moerloose
 * @author Pieter De Graef
 */
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
	 * @param type type of primitive attribute
	 */
	protected PrimitiveAttribute(PrimitiveType type) {
		this.type = type;
	}

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
	 * @param type type of primitive attribute
	 */
	public void setType(PrimitiveType type) {
		this.type = type;
	}

	/**
	 * Return true, as this type is the very definition of a primitive attribute.
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

	public VALUE_TYPE getValue() {
		return value;
	}

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
}
