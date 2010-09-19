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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.geomajas.global.Api;

/**
 * <p>
 * Definition of a value for association attributes. The {@link ManyToOneAttribute} will contain one of these, while the
 * {@link OneToManyAttribute} will contain a whole list of these values.
 * </p>
 * <p>
 * It basically defines the bean that holds the attribute value. These beans all have an identifier and a list of
 * attributes themselves, so that is represented in the value object.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
public class AssociationValue implements Serializable {

	private static final long serialVersionUID = 151L;

	private PrimitiveAttribute<?> id;

	private Map<String, PrimitiveAttribute<?>> attributes;

	/**
	 * Constructor, create attribute without value (needed for GWT).
	 */
	public AssociationValue() {
	}

	/**
	 * Create attribute with specified value.
	 * 
	 * @param id
	 *            id attribute for association
	 * @param attributes
	 *            values for the attributes
	 */
	public AssociationValue(PrimitiveAttribute<?> id, Map<String, PrimitiveAttribute<?>> attributes) {
		this.id = id;
		this.attributes = attributes;
	}

	/**
	 * Create a clone of this object.
	 * 
	 * @since 1.7.0
	 * @return A new AssociationValue with the same contents.
	 */
	public Object clone() {
		PrimitiveAttribute<?> idClone = (PrimitiveAttribute<?>) id.clone();
		Map<String, PrimitiveAttribute<?>> attrClone = new HashMap<String, PrimitiveAttribute<?>>();
		if (attributes != null) {
			for (Entry<String, PrimitiveAttribute<?>> entry : attributes.entrySet()) {
				attrClone.put(entry.getKey(), (PrimitiveAttribute<?>) entry.getValue().clone());
			}
		}
		return new AssociationValue(idClone, attrClone);
	}

	/**
	 * Get the id for the associated object.
	 * 
	 * @return id for associated object
	 */
	public PrimitiveAttribute<?> getId() {
		return id;
	}

	/**
	 * Set the id for the associated object.
	 * 
	 * @param id
	 *            id for associated object
	 */
	public void setId(PrimitiveAttribute<?> id) {
		this.id = id;
	}

	/**
	 * Get the attributes for the associated object.
	 * 
	 * @return attributes for associated objects
	 */
	public Map<String, PrimitiveAttribute<?>> getAttributes() {
		return attributes;
	}

	/**
	 * Set the attributes for the associated object.
	 * 
	 * @param attributes
	 *            attributes for associated objects
	 */
	public void setAttributes(Map<String, PrimitiveAttribute<?>> attributes) {
		this.attributes = attributes;
	}
}
