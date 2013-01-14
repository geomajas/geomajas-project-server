/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer.feature.attribute;

import java.util.Date;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.PrimitiveType;

/**
 * Attribute with value of type <code>PrimitiveType.DATE</code>.
 * <p/>
 * This only stores the date, any time component may be removed.
 * 
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
public class DateAttribute extends PrimitiveAttribute<Date> {

	private static final long serialVersionUID = 151L;

	/**
	 * Create using undefined value (null).
	 */
	public DateAttribute() {
		this(null);
	}

	/**
	 * Create using specific date value.
	 * 
	 * @param value
	 *            value for attribute
	 */
	public DateAttribute(Date value) {
		super(PrimitiveType.DATE);
		setValue(value);
	}

	/**
	 * Create a clone of this attribute object.
	 * 
	 * @since 1.7.0
	 * @return A copy of this date attribute.
	 */
	@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "CN_IDIOM_NO_SUPER_CALL",
			justification = "needed for GWT")
	public Object clone() { // NOSONAR super.clone() not supported by GWT
		DateAttribute clone = new DateAttribute();
		if (getValue() != null) {
			clone.setValue((Date) getValue().clone());
		}
		clone.setEditable(isEditable());
		return clone;
	}
}