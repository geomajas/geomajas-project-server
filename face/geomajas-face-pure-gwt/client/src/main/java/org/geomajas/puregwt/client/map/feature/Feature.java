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

package org.geomajas.puregwt.client.map.feature;

import java.util.Map;

import org.geomajas.geometry.Geometry;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.puregwt.client.map.layer.Layer;

/**
 * General interface for a GWT client side feature.
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 */
public interface Feature {

	/**
	 * Return the features unique identifier.
	 * 
	 * @return Returns the ID as a string.
	 */
	String getId();

	/**
	 * Get the value of a certain attribute.
	 * 
	 * @param attributeName
	 *            The name of the attribute. These names are configured within a layer.
	 * @return Returns the value of the attribute, or null. If the attribute does not exist, this method will also
	 *         return null.
	 */
	Object getAttributeValue(String attributeName);

	/**
	 * Return the title of this feature. This is usually the value of one of the attributes (or derived from it).
	 * 
	 * @return Returns a readable label.
	 */
	String getLabel();

	/**
	 * Get the attributes map, null when it needs to be lazy loaded.
	 * 
	 * @return attributes map
	 * @throws IllegalStateException
	 *             attributes not present because of lazy loading
	 */
	Map<String, Attribute<?>> getAttributes() throws IllegalStateException;

	/**
	 * Check whether the attributes are already available or should be lazy loaded.
	 * 
	 * @return true when attributes are available
	 */
	boolean isAttributesLoaded();

	/**
	 * Get the feature's geometry, , null when it needs to be lazy loaded.
	 * 
	 * @return geometry
	 * @throws IllegalStateException
	 *             attributes not present because of lazy loading
	 */
	Geometry getGeometry() throws IllegalStateException;

	/**
	 * Check whether the geometry is already available or should be lazy loaded.
	 * 
	 * @return true when geometry are available
	 */
	boolean isGeometryLoaded();

	/**
	 * Has this feature been selected or not?
	 * 
	 * @return Returns true or false.
	 */
	boolean isSelected();

	/**
	 * Return the layer instance associated with this feature. Every feature can belong to only one layer.
	 * 
	 * @return Returns the layer. This value can never be null.
	 */
	Layer<?> getLayer();

	/**
	 * Is the logged in user allowed to edit this feature?
	 * 
	 * @return true when edit/update is allowed for this feature
	 */
	boolean isUpdatable();

	/**
	 * Is the logged in user allowed to delete this feature?
	 * 
	 * @return true when delete is allowed for this feature
	 */
	boolean isDeletable();
}