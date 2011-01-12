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

package org.geomajas.service.pipeline;

import org.geomajas.global.Api;

/**
 * Constants for the pipelines. These include pipeline names and keys for data put in the context.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public interface PipelineCode {
	String PIPELINE_GET_ATTRIBUTES = "vectorLayer.getAttributes";
	String PIPELINE_GET_BOUNDS = "vectorLayer.getBounds";
	String PIPELINE_GET_FEATURES = "vectorLayer.getFeatures";
	String PIPELINE_GET_RASTER_TILES = "rasterLayer.getTiles";
	String PIPELINE_GET_VECTOR_TILE = "vectorLayer.getTile";
	String PIPELINE_SAVE_OR_UPDATE = "vectorLayer.saveOrUpdate";
	String PIPELINE_SAVE_OR_UPDATE_ONE = "vectorLayer.saveOrUpdateOne";

	String ATTRIBUTE_NAME_KEY = "attributeName"; // String
	String BOUNDS_KEY = "bounds"; // Envelope
	String CRS_KEY = "crs"; // Crs
	String CRS_TRANSFORM_KEY = "crsTransform"; // CrsTransform
	String FEATURE_DATA_OBJECT_KEY = "featureDataObject"; // Object
	String FEATURE_INCLUDES_KEY = "featureIncludes"; // Integer
	String FEATURE_KEY = "feature"; // InternalFeature
	String FILTER_KEY = "filter"; // Filter
	String INDEX_KEY = "index"; // Integer
	String IS_CREATE_KEY = "isCreate"; // Boolean (or not set)
	String LAYER_ID_KEY = "layerId"; // String
	String LAYER_KEY = "layer"; // Layer
	String MAX_RESULT_SIZE_KEY = "maxResultSize"; // Integer
	String NEW_FEATURES_KEY = "newFeatures"; // List<InternalFeature>
	String OFFSET_KEY = "offset"; // Integer
	String OLD_FEATURE_KEY = "oldFeature"; // InternalFeature
	String OLD_FEATURES_KEY = "oldFeatures"; // List<InternalFeature>
	String SCALE_KEY = "scale"; // Double
	String STYLE_KEY = "style"; // NamedStyleInfo
	String TILE_MAX_EXTENT_KEY = "tileMaxExtent"; // Envelope
	String TILE_METADATA_KEY = "tileMetadata"; // TileMetadata

}
