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

package org.geomajas.service.pipeline;

/**
 * Constants for the pipelines. These include pipeline names and keys for data put in the context.
 *
 * @author Joachim Van der Auwera
 */
public interface PipelineCode {

	String PIPELINE_GET_RASTER_TILES = "rasterLayer.getTiles";
	String PIPELINE_SAVE_OR_UPDATE = "vectorLayer.saveOrUpdate";
	String PIPELINE_SAVE_OR_UPDATE_ONE = "vectorLayer.saveOrUpdateOne";
	String PIPELINE_GET_FEATURES = "vectorLayer.getFeatures";
	String PIPELINE_GET_BOUNDS = "vectorLayer.getBounds";
	String PIPELINE_GET_OBJECTS = "vectorLayer.getObjects";
	String PIPELINE_GET_VECTOR_TILE = "vectorLayer.getTile";

	String LAYER_ID_KEY = "layerId"; // String
	String LAYER_KEY = "layer"; // Layer
	String FILTER_KEY = "filter"; // Filter
	String CRS_TRANSFORM_KEY = "crsTransform"; // MathTransform
	String OLD_FEATURE_KEY = "oldFeature"; // InternalFeature
	String FEATURE_KEY = "feature"; // InternalFeature
	String OLD_FEATURES_KEY = "oldFeatures"; // List<InternalFeature>
	String NEW_FEATURES_KEY = "newFeatures"; // List<InternalFeature>
	String BOUNDS_KEY = "bounds"; // Envelope
	String SCALE_KEY = "scale"; // Double
	String CRS_KEY = "crs"; // CoordinateReferenceSystem
	String CRS_CODE_KEY = "crsCode"; // String
	String STYLE_KEY = "style"; // NamedStyleInfo
	String FEATURE_INCLUDES_KEY = "featureIncludes"; // Integer
	String OFFSET_KEY = "offset"; // Integer
	String MAX_RESULT_SIZE_KEY = "maxResultSize"; // Integer
	String ATTRIBUTE_NAME_KEY = "attributeName"; // String
	String INDEX_KEY = "index"; // Integer
	String FEATURE_DATA_OBJECT_KEY = "featureDataObject"; // Object
	String TILE_METADATA_KEY = "tileMetadata"; // TileMetadata

}
