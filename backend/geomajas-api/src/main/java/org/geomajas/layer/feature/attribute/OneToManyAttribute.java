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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.AssociationType;
import org.geomajas.global.Api;

/**
 * <p>
 * Definition of the one-to-many association attribute. This type of attribute is not a simple primitive attribute with
 * a single value, but instead holds the values of an entire list of beans. This list of bean values has been defined in
 * the form of a list of {@link AssociationValue} objects.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
public class OneToManyAttribute extends AssociationAttribute<List<AssociationValue>> {

	private static final long serialVersionUID = 154L;

	private List<AssociationValue> value;

	// Constructors:

	/**
	 * Create attribute without value (needed for GWT).
	 */
	public OneToManyAttribute() {
	}

	/**
	 * Create attribute with specified value.
	 * 
	 * @param value
	 *            value for attribute
	 */
	public OneToManyAttribute(List<AssociationValue> value) {
		this.value = value;
	}

	/**
	 * {@inheritDoc}
	 */
	public AssociationType getType() {
		return AssociationType.ONE_TO_MANY;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<AssociationValue> getValue() {
		return value;
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
	public void setValue(List<AssociationValue> value) {
		this.value = value;
	}

	/**
	 * Create a clone of this attribute object.
	 * 
	 * @since 1.7.0
	 * @return A copy of this ManyToOne attribute.
	 */
	public Object clone() {
		List<AssociationValue> clones = new ArrayList<AssociationValue>();
		for (AssociationValue v : value) {
			clones.add((AssociationValue) v.clone());
		}
		OneToManyAttribute clone = new OneToManyAttribute(clones);
		clone.setEditable(isEditable());
		return clone;
	}
}
