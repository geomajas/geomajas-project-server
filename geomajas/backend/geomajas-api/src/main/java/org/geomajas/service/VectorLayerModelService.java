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

package org.geomajas.service;

import org.geomajas.geometry.Bbox;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.feature.RenderedFeature;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.util.List;

/**
 * Service which allows accessing data from a vector layer model.
 * <p/>
 * All access to layer models should be done through this service, not by accessing the layer models directly as this
 * adds possible caching, security etc. These services are implemented using pipelines
 * (see {@link org.geomajas.rendering.pipeline.PipelineService}) to make them configurable.
 *
 * @author Joachim Van der Auwera
 */
public interface VectorLayerModelService {

	/**
	 * Update an existing feature of the model or creates a new feature.
	 *
	 * @param layerId id of layer which contains feature to update
	 * @param crs crs which should be used for the geometry in the feature
	 * @param oldFeatures old features to update or delete, in the same order as in newFeatures (delete when null in
	 * newFeatures). The list may have some null elements added when it is shorter than newFeatures.
	 * @param newFeatures new version of features, indicating a need to create or update (create when entry is null in
	 * oldFeatures). The items in the list are updated with the updated {@link RenderedFeature}. The list may have
	 * some null elements added when it is shorter than oldFeatures.
	 * @throws GeomajasException oops
	 */
	void saveOrUpdate(String layerId, CoordinateReferenceSystem crs, List<RenderedFeature> oldFeatures,
			List<RenderedFeature> newFeatures) throws GeomajasException;

	/**
	 * Retrieve all the features from the model that this filter accepts.
	 *
	 * @param layerId id of layer to get features from
	 * @param crs which should be used for the geometries in the features
	 * @param filter filter to be applied                                                                		o
	 * @return reader of feature value objects
	 * @throws GeomajasException oops
	 */
	Iterable<RenderedFeature> getElements(String layerId, CoordinateReferenceSystem crs, Filter filter)
			throws GeomajasException;

	/**
	 * Retrieve the bounds of the specified features.
	 *
	 * @param layerId id of layer to get bounds for
	 * @param crs crs which should be used for the bounding box
	 * @param filter filter to be applied
	 * @return the bounds of the specified features
	 * @throws GeomajasException oops
	 */
	Bbox getBounds(String layerId, CoordinateReferenceSystem crs, Filter filter) throws GeomajasException;

	/**
	 * Return the list of possible attribute values.
	 *
	 * @param layerId id of layer to get objects from
	 * @param attributeName attribute to get objects for
	 * @param filter filter to be applied
	 * @return possible object values
	 * @throws GeomajasException oops
	 */
	Iterable<?> getObjects(String layerId, String attributeName, Filter filter) throws GeomajasException;
}
