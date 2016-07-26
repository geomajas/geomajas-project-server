/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer.feature;

import java.util.Map;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.layer.VectorLayer;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * <p>
 * This is the main feature class, internally used on the server. Even though each
 * {@link org.geomajas.layer.VectorLayer} may have it's own specific feature objects, this is the common feature type
 * that comes out of the {@link org.geomajas.layer.VectorLayerService} class and is used throughout the back-end.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
public interface InternalFeature extends Cloneable, Comparable<InternalFeature> {

	/**
	 * Get the feature's bounding box.
	 * 
	 * @return feature bounds
	 */
	Envelope getBounds();

	/**
	 * This function compares style ID's between features. Features are usually sorted by style.
	 *
	 * @param other {@link org.geomajas.layer.feature.InternalFeature} to compare with
	 * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than
	 * the specified object.
	 */
	int compareTo(InternalFeature other);

	/**
	 * Set the feature id.
	 *
	 * @param id feature id
	 */
	void setId(String id);

	/**
	 * Get the feature id.
	 *
	 * @return feature id
	 */
	String getId();

	/**
	 * Get the attributes for the feature (can be null when the attributes are lazy loaded).
	 *
	 * @return attributes map
	 */
	Map<String, Attribute> getAttributes();

	/**
	 * Set the map with the attributes, keyed on the attribute name.
	 *
	 * @param attributes attributes map
	 */
	void setAttributes(Map<String, Attribute> attributes);

	/**
	 * Get the feature geometry (or null when it is lazy loaded).
	 *
	 * @return feature geometry
	 */
	Geometry getGeometry();

	/**
	 * Set the geometry for the feature.
	 *
	 * @param geometry geometry for the feature
	 */
	void setGeometry(Geometry geometry);

	/**
	 * Get the style info for this feature (or null when it is lazy loaded).
	 *
	 * @return style info
	 */
	FeatureStyleInfo getStyleInfo();

	/**
	 * Set the style info for this feature.
	 *
	 * @param style style
	 */
	void setStyleDefinition(FeatureStyleInfo style);

	/**
	 * Get the layer this feature is part of.
	 *
	 * @return layer which contains the feature
	 */
	VectorLayer getLayer();

	/**
	 * Set the layer this feature is part of.
	 * <p/>
	 * Setting this does not move the feature to a different layer!
	 *
	 * @param layer layer this feature is part of
	 */
	void setLayer(VectorLayer layer);

	/**
	 * Get the label for the feature (if any).
	 *
	 * @return label
	 */
	String getLabel();

	/**
	 * Ser the label for the feature.
	 *
	 * @param label label for feature
	 */
	void setLabel(String label);

	/**
	 * Has the feature geometry been clipped ?
	 *
	 * @return is geometry clipped
	 */
	boolean isClipped();

	/**
	 * Set clipped status for feature.
	 *
	 * @param clipped clipped status
	 */
	void setClipped(boolean clipped);

	/**
	 * Get the clipped geometry.
	 *
	 * @return clipped geometry
	 */
	Geometry getClippedGeometry();

	/**
	 * Set the clipped geometry.
	 *
	 * @param clippedGeometry clipped geometry
	 */
	void setClippedGeometry(Geometry clippedGeometry);

	/**
	 * Is the logged in user allowed to edit this feature?
	 *
	 * @return true when edit/update is allowed for this feature
	 */
	boolean isEditable();

	/**
	 * Set whether the logged in user is allowed to edit/update this feature.
	 *
	 * @param editable true when edit/update is allowed for this feature
	 */
	void setEditable(boolean editable);

	/**
	 * Is the logged in user allowed to delete this feature?
	 *
	 * @return true when delete is allowed for this feature
	 */
	boolean isDeletable();

	/**
	 * Set whether the logged in user is allowed to delete this feature.
	 *
	 * @param deletable true when deleting this feature is allowed
	 */
	void setDeletable(boolean deletable);

	/**
	 * Create a deep clone of this internal feature.
	 *
	 * @return deep cloned object (attributes and geometry are deep cloned).
	 */
	InternalFeature clone();

	/**
	 * Create a deep clone of this internal feature but without cloning the geometry (stays null).
	 *
	 * @return deep cloned object (attributes are deep cloned).
	 * @since 1.9.0
	 */
	InternalFeature cloneWithoutGeometry();
}
