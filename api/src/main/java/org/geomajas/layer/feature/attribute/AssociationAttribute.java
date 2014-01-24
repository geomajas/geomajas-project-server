/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer.feature.attribute;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.AssociationType;
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
	 * Create a clone of this association object.
	 * 
	 * @since 1.7.0
	 * @return Return an exact copy of this association.
	 */
	public abstract Object clone();

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
