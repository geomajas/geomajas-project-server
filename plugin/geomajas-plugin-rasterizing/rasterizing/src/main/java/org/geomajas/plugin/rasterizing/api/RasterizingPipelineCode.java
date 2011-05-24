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
	 * Pipeline for building the map image, invoked by the controller.
	 */
	String PIPELINE_RASTERIZING_GET_MAP_IMAGE = "rasterizing.getMapImage";

	/**
	 * Pipeline for building the legend image, invoked by the controller.
	 */
	String PIPELINE_RASTERIZING_GET_LEGEND_IMAGE = "rasterizing.getLegendImage";

	/**
	 * Pipeline which actually builds the raster image while getting the vector tile.
	 */
	String PIPELINE_GET_VECTOR_TILE_RASTERIZING = "rasterizing.getVectorTile";

	/**
	 * Pipeline context key for image id (=rebuild and raster image cache id).
	 */
	String IMAGE_ID_KEY = "rasterizing.imageId"; // String

	/**
	 * Pipeline context key for image id context (=rebuild and raster image cache id).
	 */
	String IMAGE_ID_CONTEXT = "rasterizing.imageId"; // CacheContext

	/**
	 * Pipeline context key for storing the image container.
	 */
	String CONTAINER_KEY = "rasterizing.container"; // rasterizingContainer

	/**
	 * Pipeline context key for storing the rendered image.
	 */
	String RENDERED_IMAGE = "rasterizing.renderedImage"; // RenderedImage

	/**
	 * Pipeline context key for storing the graphics context.
	 */
	String RENDERING_HINTS = "rasterizing.renderingHints"; // RenderingHints

	/**
	 * Pipeline context key for storing the map context metadata.
	 */
	String CLIENT_MAP_INFO_KEY = "rasterizing.clientMapInfo"; // ClientMapInfo

	/**
	 * Pipeline context key for storing the map context.
	 */
	String MAP_CONTEXT_KEY = "rasterizing.mapContext"; // MapContext

}
