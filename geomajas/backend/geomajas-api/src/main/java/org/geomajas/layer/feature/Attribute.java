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
package org.geomajas.layer.feature;

import java.io.Serializable;

/**
 * Attribute definition.
 * 
 * @author Jan De Moerloose
 * @author Joachim Van der Auwera
 */
public interface Attribute extends Serializable {

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

	/** set whether the feature is editable or not.
	 * <p/>
	 * The value can only be true when the layer/feature model has the capability to edit the attribute and the logged
	 * in user is authorized.
	 *
	 * @param editable editable status
	 */
	void setEditable(boolean editable);
}
