/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
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
 * Attribute with value of type <code>PrimitiveType.CURRENCY</code>. GWT has no support for BigDecimal, so we have to
 * work with the String representation and validate correctly where necessary.
 * 
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
public class CurrencyAttribute extends PrimitiveAttribute<String> {

	private static final long serialVersionUID = 151L;

	/**
	 * Constructor, create attribute without value (needed for GWT).
	 */
	public CurrencyAttribute() {
		this(null);
	}

	/**
	 * Constructor which accepts the value.
	 * 
	 * @param value
	 *            value
	 */
	public CurrencyAttribute(String value) {
		super(PrimitiveType.CURRENCY);
		setValue(value);
	}

	/**
	 * Create a clone of this attribute object.
	 * 
	 * @since 1.7.0
	 * @return A copy of this currency attribute.
	 */
	@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "CN_IDIOM_NO_SUPER_CALL",
			justification = "needed for GWT")
	public Object clone() { // NOSONAR super.clone() not supported by GWT
		CurrencyAttribute clone = new CurrencyAttribute();
		if (getValue() != null) {
			clone.setValue(getValue());
		}
		clone.setEditable(isEditable());
		return clone;
	}
}