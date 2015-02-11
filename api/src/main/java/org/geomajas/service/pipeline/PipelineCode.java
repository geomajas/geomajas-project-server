/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.service.pipeline;

import org.geomajas.annotation.Api;

/**
 * Constants for the pipelines. These include pipeline names and keys for data put in the context.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0  quick check
 */
@Api(allMethods = true)
public interface PipelineCode {
	/** Vector layer get attributes pipeline key. */
	String PIPELINE_GET_ATTRIBUTES = "vectorLayer.getAttributes";
	/** Vector layer get bounds pipeline key. */
	String PIPELINE_GET_BOUNDS = "vectorLayer.getBounds";
	/** Vector layer get features pipeline key. */
	String PIPELINE_GET_FEATURES = "vectorLayer.getFeatures";
	/** Raster layer get tiles pipeline key. */
	String PIPELINE_GET_RASTER_TILES = "rasterLayer.getTiles";
	/** Vector layer get tile pipeline key. */
	String PIPELINE_GET_VECTOR_TILE = "vectorLayer.getTile";
	/** Vector layer save or update pipeline key. */
	String PIPELINE_SAVE_OR_UPDATE = "vectorLayer.saveOrUpdate";
	/** Vector layer save or update sub-pipeline key for one feature. */
	String PIPELINE_SAVE_OR_UPDATE_ONE = "vectorLayer.saveOrUpdateOne";

	/** Attribute name as {@link String}. */
	String ATTRIBUTE_NAME_KEY = "attributeName"; 
	/** Bounds as {@link com.vividsolutions.jts.geom.Envelope}. */
	String BOUNDS_KEY = "bounds"; 
	/** CRS (as {@link org.geomajas.geometry.Crs}. */
	String CRS_KEY = "crs"; 
	/** Crs transformation from request to layer, {@link org.geomajas.geometry.CrsTransform}. */
	String CRS_TRANSFORM_KEY = "crsTransform";
	/** Feature data object , {@link Object}. */
	String FEATURE_DATA_OBJECT_KEY = "featureDataObject";
	/** What should be included in the feature, {@link Integer}. */
	String FEATURE_INCLUDES_KEY = "featureIncludes";
	/** Feature as {@link org.geomajas.layer.feature.InternalFeature}. */
	String FEATURE_KEY = "feature";
	/** Filter object, {@link org.opengis.filter.Filter}. */
	String FILTER_KEY = "filter";
	/** Index, {@link Integer}. */
	String INDEX_KEY = "index";
	/** Are we creating a new feature, {@link Boolean}. */
	String IS_CREATE_KEY = "isCreate";
	/** Layer id (server side), as {@link String}. */
	String LAYER_ID_KEY = "layerId"; 
	/** Layer as {@link org.geomajas.layer.Layer}. */
	String LAYER_KEY = "layer"; 
	/** Maximum size for result, {@link Integer}. */
	String MAX_RESULT_SIZE_KEY = "maxResultSize"; 
	/** List of new features, {@link java.util.List< org.geomajas.layer.feature.InternalFeature>}. */
	String NEW_FEATURES_KEY = "newFeatures"; 
	 /** Offset for result set, {@link Integer}. */
	String OFFSET_KEY = "offset";
	/** Old feature (state before change), {@link org.geomajas.layer.feature.InternalFeature}. */
	String OLD_FEATURE_KEY = "oldFeature";
	/** List of old features, {@link java.util.List< org.geomajas.layer.feature.InternalFeature>}. */
	String OLD_FEATURES_KEY = "oldFeatures"; 
	/** Scale {@link Double}. */
	String SCALE_KEY = "scale"; 
	/** Style as {@link org.geomajas.configuration.NamedStyleInfo}. */
	String STYLE_KEY = "style"; 
	/** Maximum extent for tile, {@link com.vividsolutions.jts.geom.Envelope}. */
	String TILE_MAX_EXTENT_KEY = "tileMaxExtent"; 
	/** Tile metadata as {@link org.geomajas.layer.tile.TileMetadata}. */
	String TILE_METADATA_KEY = "tileMetadata";
	/**
	 * CrsTransform for converting from the layer to the request.
	 *
	 * @since 1.9.0 */
	String CRS_BACK_TRANSFORM_KEY = "crsBackTransform"; // CrsTransform (layer -> request)
	/**
	 * Should paging be forced?
	 *
	 * @since 1.10.0 */
	String FORCE_PAGING_KEY = "forcePaging"; // Boolean (default false)
}
