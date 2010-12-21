package org.geomajas.gwt.client.map.feature;

import java.util.Map;

import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.layer.feature.Attribute;

public interface Feature {

	String getId();

	Object getAttributeValue(String attributeName);

	String getLabel();

	/**
	 * Get the attributes map, null when it needs to be lazy loaded.
	 * 
	 * @return attributes map
	 * @throws IllegalStateException
	 *             attributes not present because of lazy loading
	 */
	Map<String, Attribute> getAttributes() throws IllegalStateException;

	/**
	 * Check whether the attributes are already available or should be lazy
	 * loaded.
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

	boolean isSelected();

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