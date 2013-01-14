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
package org.geomajas.layer.feature;

import java.io.Serializable;

import org.geomajas.annotation.Api;

/**
 * Attribute definition.
 * 
 * @param <VALUE_TYPE>
 *            type for the attribute value
 * 
 * @author Jan De Moerloose
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public interface Attribute<VALUE_TYPE> extends Serializable, Cloneable {

	/**
	 * Create a clone from this attribute object.
	 * 
	 * @since 1.7.0
	 * @return Returns an exact copy.
	 */
	Object clone();

	/**
	 * Get the value for this attribute.
	 * 
	 * @return attribute value
	 */
	VALUE_TYPE getValue();

	/**
	 * Is this a primitive attribute?
	 * <p/>
	 * An attribute is not primitive when is is an association attribute.
	 * 
	 * @return true when attribute is primitive
	 */
	boolean isPrimitive();

	/**
	 * Does the attribute have a value?
	 * 
	 * @return true when the attribute has no value
	 */
	boolean isEmpty();

	/**
	 * Indicates whether the user is authorized to edit this attribute.
	 * <p/>
	 * The value can only be true when the layer/feature model has the capability to edit the attribute and the logged
	 * in user is authorized.
	 * 
	 * @return true when attribute is editable
	 */
	boolean isEditable();

	/**
	 * set whether the feature is editable or not.
	 * <p/>
	 * The value can only be true when the layer/feature model has the capability to edit the attribute and the logged
	 * in user is authorized.
	 * 
	 * @param editable
	 *            editable status
	 */
	void setEditable(boolean editable);
}
