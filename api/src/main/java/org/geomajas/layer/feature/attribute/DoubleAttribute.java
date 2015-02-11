/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer.feature.attribute;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.PrimitiveType;

/**
 * Attribute with value of type <code>PrimitiveType.DOUBLE</code>.
 * 
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
public class DoubleAttribute extends PrimitiveAttribute<Double> {

	private static final long serialVersionUID = 151L;

	/**
	 * Constructor, create attribute without value (needed for GWT).
	 */
	public DoubleAttribute() {
		this(null);
	}

	/**
	 * Create attribute with specified double value.
	 * 
	 * @param value
	 *            value for attribute
	 */
	public DoubleAttribute(Double value) {
		super(PrimitiveType.DOUBLE);
		setValue(value);
	}

	/**
	 * Create a clone of this attribute object.
	 * 
	 * @since 1.7.0
	 * @return A copy of this double attribute.
	 */
	@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "CN_IDIOM_NO_SUPER_CALL",
			justification = "needed for GWT")
	public Object clone() { // NOSONAR super.clone() not supported by GWT
		DoubleAttribute clone = new DoubleAttribute();
		if (getValue() != null) {
			clone.setValue(Double.valueOf(getValue()));
		}
		clone.setEditable(isEditable());
		return clone;
	}
}