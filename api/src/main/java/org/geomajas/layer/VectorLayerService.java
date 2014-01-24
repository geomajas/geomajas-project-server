/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.layer;

import java.util.List;

import org.geomajas.annotation.Api;
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
 * @since 1.6.0
 */
@Api(allMethods = true)
public interface VectorLayerService extends LayerService, GeomajasConstant {

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
	 * <p>
	 * <b>Note that not all vector layers may support the offset or maxResultSize parameters! Check the individual layer
	 * documentation to be certain.</b>
	 * </p>
	 * <p>
	 * <b>SECURITY WARNING: if paging is forced at the layer level (forcePaging=true), callers are able to count the
	 * number of invisible features, creating a possible security hazard !</b>
	 * </p>
	 * <p>
	 * Retrieve all the features from the model that this filter accepts.
	 * </p>
	 * 
	 * @param layerId id of layer to get features from
	 * @param crs which should be used for the geometries in the features
	 * @param filter filter to be applied
	 * @param style style to apply
	 * @param featureIncludes indicate which data to include in the features
	 * @param offset Skip the first 'offset' features in the result. This is meant for paging.
	 * @param maxResultSize Limit the result to a maximum number of features. Can be used for paging.
	 * @param forcePaging Forces paging at layer level. This optimizes performance at the expense of (possibly) fewer
	 *        results being returned than asked for because of subsequent security filtering.
	 * @return reader of feature value objects
	 * @throws GeomajasException oops
	 * @since 1.10.0
	 */
	List<InternalFeature> getFeatures(String layerId, CoordinateReferenceSystem crs, Filter filter,
			NamedStyleInfo style, int featureIncludes, int offset, int maxResultSize, boolean forcePaging)
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
	 * @param attributePath attribute path to get objects for
	 * @param filter filter to be applied
	 * @return possible object values
	 * @throws GeomajasException oops
	 */
	List<Attribute<?>> getAttributes(String layerId, String attributePath, Filter filter) throws GeomajasException;
	
	/**
	 * Get a vector tile for the request tile.
	 *
	 * @param tileMetadata description of the tile
	 * @return internal vector tile
	 * @throws GeomajasException oops
	 */
	InternalTile getTile(TileMetadata tileMetadata) throws GeomajasException;
}
