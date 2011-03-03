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

package org.geomajas.plugin.rasterizing.api;

import org.geomajas.global.Api;

/**
 * Codes for the rasterizing pipeline.
 * 
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface RasterizingPipelineCode {

	/**
	 * Pipeline for building the raster image, invoked by the controller.
	 */
	String PIPELINE_RASTERIZING = "rasterizing.rasterize";

	/**
	 * Pipeline for building the raster image, invoked by the controller.
	 */
	String PIPELINE_RASTERIZING_GET_IMAGE = "rasterizing.getImage";

	/**
	 * Pipeline which actually builds the raster image while getting the vector tile.
	 */
	String PIPELINE_GET_VECTOR_TILE_RASTERIZING = "rasterizing.getVectorTile"; // command pipeline

	/**
	 * Pipeline context key for image id (=rebuild and raster image cache id).
	 */
	String IMAGE_ID_KEY = "rasterizing.imageId"; // String

	/**
	 * Pipeline context key for storing the image container.
	 */
	String CONTAINER_KEY = "rasterizing.container"; // rasterizingContainer

	/**
	 * Pipeline context key for storing the buffered image.
	 */
	String BUFFERED_IMAGE = "rasterizing.bufferedImage"; // BufferedImage

	/**
	 * Pipeline context key for storing the graphics context.
	 */
	String GRAPHICS2D = "rasterizing.graphics2D"; // Graphics2D

	/**
	 * Pipeline context key for storing the graphics context.
	 */
	String RENDERING_HINTS = "rasterizing.renderingHints"; // RenderingHints

	/**
	 * Pipeline context key for storing the map context metadata.
	 */
	String MAP_CONTEXT_METADATA_KEY = "rasterizing.mapContextMetadata"; // MapContextMetaData

	/**
	 * Pipeline context key for storing the map context.
	 */
	String MAP_CONTEXT_KEY = "rasterizing.mapContext"; // MapContext

}
