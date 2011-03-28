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

package org.geomajas.puregwt.client.map.feature;

import java.util.Map;

import org.geomajas.geometry.Geometry;
import org.geomajas.global.FutureApi;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.puregwt.client.map.layer.Layer;

/**
 * General interface for a GWT client side feature.<br/>
 * TODO what about lazy loading here?
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@FutureApi
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