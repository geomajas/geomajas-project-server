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
import java.util.Map;

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
 */
public class AssociationValue implements Serializable {

	private static final long serialVersionUID = 154L;

	private PrimitiveAttribute<?> id;

	private Map<String, PrimitiveAttribute<?>> attributes;

	// Constructors:

	public AssociationValue() {
	}

	public AssociationValue(PrimitiveAttribute<?> id,  Map<String, PrimitiveAttribute<?>> attributes) {
		this.id = id;
		this.attributes = attributes;
	}

	// Getters and setters:

	public PrimitiveAttribute<?> getId() {
		return id;
	}

	public void setId(PrimitiveAttribute<?> id) {
		this.id = id;
	}

	public  Map<String, PrimitiveAttribute<?>> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, PrimitiveAttribute<?>> attributes) {
		this.attributes = attributes;
	}
}
