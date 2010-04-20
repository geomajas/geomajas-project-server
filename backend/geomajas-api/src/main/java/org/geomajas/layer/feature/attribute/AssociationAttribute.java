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

import org.geomajas.configuration.AssociationType;
import org.geomajas.global.Api;
import org.geomajas.layer.feature.Attribute;

/**
 * Association attribute.
 * 
 * @param <VALUE_TYPE>
 *            type for the attribute value
 * 
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
public abstract class AssociationAttribute<VALUE_TYPE> implements Attribute<VALUE_TYPE> {

	private static final long serialVersionUID = 151L;

	private boolean editable;

	/**
	 * {@inheritDoc}
	 */
	public abstract AssociationType getType();

	/**
	 * {@inheritDoc}
	 */
	public boolean isPrimitive() {
		return false;
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
	 * Set the association value.
	 *
	 * @param value associated value
	 */
	public abstract void setValue(VALUE_TYPE value);
}
