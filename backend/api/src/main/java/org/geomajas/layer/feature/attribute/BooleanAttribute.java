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

import org.geomajas.configuration.PrimitiveType;
import org.geomajas.global.Api;

/**
 * Attribute with value of type <code>PrimitiveType.BOOLEAN</code>.
 * 
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
public class BooleanAttribute extends PrimitiveAttribute<Boolean> {

	private static final long serialVersionUID = 151L;

	/**
	 * Constructor, create attribute without value (needed for GWT).
	 */
	public BooleanAttribute() {
		this(null);
	}

	/**
	 * Create attribute with given boolean value.
	 * 
	 * @param value
	 *            value for attribute
	 */
	public BooleanAttribute(Boolean value) {
		super(PrimitiveType.BOOLEAN);
		setValue(value);
	}

	/**
	 * Create a clone of this attribute object.
	 * 
	 * @since 1.7.0
	 * @return A copy of this boolean attribute.
	 */
	public Object clone() { // NOSONAR
		BooleanAttribute clone = new BooleanAttribute();
		if (getValue() != null) {
			clone.setValue(getValue().booleanValue());
		}
		clone.setEditable(isEditable());
		return clone;
	}
}