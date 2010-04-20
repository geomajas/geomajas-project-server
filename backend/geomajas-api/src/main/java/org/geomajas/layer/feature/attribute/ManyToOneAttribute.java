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

/**
 * <p>
 * Definition of the many-to-one association attribute. This type of attribute is not a simple primitive attribute with
 * a single value, but instead holds the value of an entire bean. This value has been defined in the form of a
 * {@link AssociationValue}.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
public class ManyToOneAttribute extends AssociationAttribute<AssociationValue> {

	private static final long serialVersionUID = 151L;

	private AssociationValue value;


	/**
	 * Create attribute without value (needed for GWT).
	 */
	public ManyToOneAttribute() {
	}

	/**
	 * Create attribute with specified value.
	 *
	 * @param value value for attribute
	 */
	public ManyToOneAttribute(AssociationValue value) {
		this.value = value;
	}

	/**
	 * {@inheritDoc}
	 */
	public AssociationType getType() {
		return AssociationType.MANY_TO_ONE;
	}

	/**
	 * {@inheritDoc}
	 */
	public AssociationValue getValue() {
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isEmpty() {
		return value == null || value.getAttributes() == null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setValue(AssociationValue value) {
		this.value = value;
	}
}
