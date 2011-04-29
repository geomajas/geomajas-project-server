/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
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
	 * @param value
	 *            value for attribute
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

	/**
	 * Create a clone of this attribute object.
	 * 
	 * @since 1.7.0
	 * @return A copy of this ManyToOne attribute.
	 */
	@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "CN_IDIOM_NO_SUPER_CALL",
			justification = "needed for GWT")
	public Object clone() {
		ManyToOneAttribute clone = new ManyToOneAttribute();
		if (value != null) {
			clone.setValue((AssociationValue) value.clone());
		}
		clone.setEditable(isEditable());
		return clone;
	}
}
