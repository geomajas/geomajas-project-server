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

import java.util.Map;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.layer.VectorLayer;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * <p>
 * This is the main feature class, internally used on the server. Even though each
 * {@link org.geomajas.layer.VectorLayer} may have it's own specific feature objects, this is the common feature type
 * that comes out of the {@link org.geomajas.service.VectorLayerService} class and is used throughout the back-end.
 * </p>
 * 
 * @author Pieter De Graef
 */
public interface InternalFeature extends Cloneable, Comparable<InternalFeature> {

	/**
	 * Retrieve the local feature ID. That is the ID without the layer ID attached to it. This ID will still be unique
	 * within the layer.
	 * 
	 * @return local feature id
	 */
	String getLocalId();

	/**
	 * Get the feature's bounding box.
	 * 
	 * @return feature bounds
	 */
	Envelope getBounds();

	/**
	 * Is this a new feature or not? This is tested by the ID. If the ID is null, then the feature is new.
	 * 
	 * @return true when new
	 */
	boolean isNew();

	/**
	 * This function compares style ID's between features. Features are usually sorted by style.
	 */
	int compareTo(InternalFeature o);

	void setId(String id);

	String getId();

	Map<String, Object> getAttributes();

	void setAttributes(Map<String, Object> attributes);

	Geometry getGeometry();

	void setGeometry(Geometry geometry);

	FeatureStyleInfo getStyleInfo();

	void setStyleDefinition(FeatureStyleInfo style);

	VectorLayer getLayer();

	void setLayer(VectorLayer layer);

	String getLabel();

	void setLabel(String label);

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
}
