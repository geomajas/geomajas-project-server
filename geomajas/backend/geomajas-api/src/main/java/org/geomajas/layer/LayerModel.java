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

import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.global.ExpectAlternatives;
import org.geomajas.layer.feature.FeatureModel;
import org.opengis.filter.Filter;

import java.util.Iterator;

/**
 * <p>
 * General interface for the model of a {@link Layer}. A model should hold all the elements that belong to a layer
 * within certain bounds. These bounds are determined by the client's view.
 * </p>
 * <p>
 * Also a model should be able to fetch elements from it's source, using the bounds.
 * </p>
 *
 * @author check subversion
 */
@ExpectAlternatives
public interface LayerModel {

	/**
	 * Set the layer info for this layer model.
	 *
	 * @param vectorLayerInfo vector layer info
	 * @throws LayerException oops
	 */
	void setLayerInfo(VectorLayerInfo vectorLayerInfo) throws LayerException;

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
	 *
	 * @param filter filter to be applied
	 * @return reader of feature value objects
	 * @throws LayerException oops
	 */
	Iterator<?> getElements(Filter filter) throws LayerException;

	/**
	 * Retrieve the bounds of the specified features.
	 *
	 * @param filter filter to be applied
	 * @return the bounds of the specified features
	 * @throws LayerException oops
	 */
	Bbox getBounds(Filter filter) throws LayerException;

	/**
	 * Return the bounds of this model.
	 *
	 * @return bounds of this model
	 * @throws LayerException oops
	 */
	Bbox getBounds() throws LayerException;

	/**
	 * Return the list of possible object values.
	 *
	 * @param attributeName attribute to get objects for
	 * @param filter filter to be applied
	 * @return possible object values
	 * @throws LayerException oops
	 */
	Iterator<?> getObjects(String attributeName, Filter filter) throws LayerException;

	void setDefaultFilter(Filter filter);

	Filter getDefaultFilter();
}
