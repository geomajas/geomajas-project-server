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
package org.geomajas.layer;

import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.layer.feature.FeatureModel;
import org.opengis.filter.Filter;

import java.util.Iterator;

/**
 * A layer which contains vector data.
 * <p/>
 * It hold all the elements that belong to a layer within the bounds determined by the client's view. It is able to
 * manipulate the objects which are represented as features on the map.
 *
 * @author Pieter De Graef
 * @author Jan De Moerloose
 */
public interface VectorLayer extends Layer<VectorLayerInfo> {

	/**
	 * Indicate whether the layer is capable of creating new features.
	 *
	 * @return true when creating new features is possible for this layer
	 */
	boolean isCreateCapable();

	/**
	 * Indicate whether the layer is capable of updating features.
	 *
	 * @return true when updating features is possible for this layer
	 */
	boolean isUpdateCapable();

	/**
	 * Indicate whether the layer is capable of creating new features.
	 *
	 * @return true when deleting features is possible for this layer
	 */
	boolean isDeleteCapable();

	/**
	 * Return the feature model of this layer model.
	 *
	 * @return feature model for this layer
	 */
	FeatureModel getFeatureModel();

	/**
	 * Creates a new feature.
	 *
	 * @param feature feature value object
	 * @return the new feature (with assigned or generated ID)
	 * @throws LayerException oops
	 */
	Object create(Object feature) throws LayerException;

	/**
	 * Update an existing feature of the model or creates a new feature.
	 *
	 * @param feature feature value object
	 * @return the feature itself
	 * @throws LayerException oops
	 */
	Object saveOrUpdate(Object feature) throws LayerException;

	/**
	 * Reads an existing feature of the model.
	 *
	 * @param featureId unique id of the feature
	 * @return feature value object
	 * @throws LayerException oops
	 */
	Object read(String featureId) throws LayerException;

	/**
	 * Deletes a feature of the model.
	 *
	 * @param featureId unique id of the feature
	 * @throws LayerException oops
	 */
	void delete(String featureId) throws LayerException;

	/**
	 * Retrieve all the features from the model that this filter accepts.
	 * <p><b>Note that not all vector layers may support the offset or maxResultSize parameters! Check the individual
	 * layer documentation to be certain.</b></p>
	 *
	 * @param filter filter to be applied
	 * @param offset Skip the first 'offset' features in the result. This is meant for paging.
	 * @param maxResultSize Limit the result to a maximum number of features. Can be used for paging.
	 * @return reader of feature value objects
	 * @throws LayerException oops
	 */
	Iterator<?> getElements(Filter filter, int offset, int maxResultSize) throws LayerException;

	/**
	 * Retrieve the bounds of the specified features.
	 *
	 * @param filter filter to be applied
	 * @return the bounds of the specified features
	 * @throws LayerException oops
	 */
	Envelope getBounds(Filter filter) throws LayerException;

	/**
	 * Return the bounds of this model.
	 *
	 * @return bounds of this model
	 * @throws LayerException oops
	 */
	Envelope getBounds() throws LayerException;

	/**
	 * Return the list of possible object values.
	 *
	 * @param attributeName attribute to get objects for
	 * @param filter filter to be applied
	 * @return possible object values
	 * @throws LayerException oops
	 */
	Iterator<?> getObjects(String attributeName, Filter filter) throws LayerException;
}
