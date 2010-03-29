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
package org.geomajas.configuration;

import java.io.Serializable;

/**
 * Primitive attribute configuration information.
 *
 * @author Jan De Moerloose
 */
public class PrimitiveAttributeInfo extends AttributeInfo implements Serializable {

	private static final long serialVersionUID = 151L;
	private PrimitiveType type;

	/**
	 * Default constructor for GWT.
	 */
	public PrimitiveAttributeInfo() {
		this(null, null, null);
	}

	/**
	 * Creates an attribute information for the specified primitive type.
	 *
	 * @param name attribute name
	 * @param label attribute label
	 * @param type the primitive type
	 */
	public PrimitiveAttributeInfo(String name, String label, PrimitiveType type) {
		super(name, label);
		this.type = type;
	}

	/**
	 * Get type for the value of the attribute.
	 *
	 * @return type for the value
	 */
	public PrimitiveType getType() {
		return type;
	}

	/**
	 * Set type for the value of the attribute.
	 *
	 * @param type type
	 */
	public void setType(PrimitiveType type) {
		this.type = type;
	}
}
