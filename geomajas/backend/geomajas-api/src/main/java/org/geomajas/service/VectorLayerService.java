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

import java.util.List;

import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.global.GeomajasConstant;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.TileMetadata;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Service which allows accessing data from a vector layer.
 * <p/>
 * All access to vector layers should be done through this service, not by accessing the layer directly as this
 * adds possible caching, security etc. These services are implemented using pipelines
 * (see {@link org.geomajas.service.pipeline.PipelineService}) to make them configurable.
 *
 * @author Joachim Van der Auwera
 */
public interface VectorLayerService extends GeomajasConstant {

	/**
	 * Update an existing feature of the model or creates a new feature.
	 *
	 * @param layerId id of layer which contains feature to update
	 * @param crs crs which should be used for the geometry in the feature
	 * @param oldFeatures old features to update or delete, in the same order as in newFeatures (delete when null in
	 * newFeatures). The list may have some null elements added when it is shorter than newFeatures.
	 * @param newFeatures new version of features, indicating a need to create or update (create when entry is null in
	 * oldFeatures). The items in the list are updated with the updated {@link InternalFeature}. The list may have
	 * some null elements added when it is shorter than oldFeatures.
	 * @throws GeomajasException oops
	 */
	void saveOrUpdate(String layerId, CoordinateReferenceSystem crs, List<InternalFeature> oldFeatures,
			List<InternalFeature> newFeatures) throws GeomajasException;

	/**
	 * Retrieve all the features from the model that this filter accepts.
	 *
	 * @param layerId id of layer to get features from
	 * @param crs which should be used for the geometries in the features
	 * @param filter filter to be applied
	 * @param style style to apply
	 * @param featureIncludes indicate which data to include in the features
	 * @return reader of feature value objects
	 * @throws GeomajasException oops
	 */
	List<InternalFeature> getFeatures(String layerId, CoordinateReferenceSystem crs, Filter filter,
			NamedStyleInfo style, int featureIncludes)
			throws GeomajasException;

	/**
	 * <p><b>Note that not all vector layers may support the offset or maxResultSize parameters! Check the individual
	 * layer documentation to be certain.</b></p>
	 * <p>Retrieve all the features from the model that this filter accepts.</p>
	 *
	 * @param layerId id of layer to get features from
	 * @param crs which should be used for the geometries in the features
	 * @param filter filter to be applied
	 * @param style style to apply
	 * @param featureIncludes indicate which data to include in the features
	 * @param offset Skip the first 'offset' features in the result. This is meant for paging.
	 * @param maxResultSize Limit the result to a maximum number of features. Can be used for paging.
	 * @return reader of feature value objects
	 * @throws GeomajasException oops
	 */
	List<InternalFeature> getFeatures(String layerId, CoordinateReferenceSystem crs, Filter filter,
			NamedStyleInfo style, int featureIncludes, int offset, int maxResultSize)
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
	Envelope getBounds(String layerId, CoordinateReferenceSystem crs, Filter filter) throws GeomajasException;

	/**
	 * Return the list of possible attribute values.
	 *
	 * @param layerId id of layer to get objects from
	 * @param attributeName attribute to get objects for
	 * @param filter filter to be applied
	 * @return possible object values
	 * @throws GeomajasException oops
	 */
	List<Attribute<?>> getAttributes(String layerId, String attributeName, Filter filter) throws GeomajasException;
	
	/**
	 * Get a vector tile for the request tile.
	 *
	 * @param tileMetadata description of the tile
	 * @return internal vector tile
	 * @throws GeomajasException oops
	 */
	InternalTile getTile(TileMetadata tileMetadata) throws GeomajasException;

	/*
	void getTileImage(String layerId, InternalTile tile, OutputStream destination) throws GeomajasException;
	*/
}
