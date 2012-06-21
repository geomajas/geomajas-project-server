/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.rasterizing.api;

import org.geomajas.annotation.Api;

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
	 * Pipeline context key for geometry image id (=rebuild and raster image cache id).
	 */
	String IMAGE_ID_KEY = "rasterizing.geometry.imageId"; // String

	/**
	 * Pipeline context key for geometry image id context (=rebuild and raster image cache id).
	 */
	String IMAGE_ID_CONTEXT = "rasterizing.geometry.imageId"; // CacheContext

	/**
	 * Pipeline context key for label image id (=rebuild and raster image cache id).
	 */
	String IMAGE_ID_LABEL_KEY = "rasterizing.label.imageId"; // String

	/**
	 * Pipeline context key for label image id context (=rebuild and raster image cache id).
	 */
	String IMAGE_ID_LABEL_CONTEXT = "rasterizing.label.imageId"; // CacheContext

	/**
	 * Pipeline context key for storing the image container.
	 */
	String CONTAINER_KEY = "rasterizing.container"; // rasterizingContainer

	/**
	 * Pipeline context key for storing the rendered image.
	 */
	String RENDERED_IMAGE = "rasterizing.renderedImage"; // RenderedImage

	/**
	 * Pipeline context key for storing the java 2D rendering hints.
	 * 
	 * @see org.geotools.renderer.lite.StreamingRenderer#setJava2DHints(java.awt.RenderingHints)
	 */
	String RENDERING_HINTS = "rasterizing.renderingHints"; // RenderingHints

	/**
	 * Pipeline context key for storing the Geotools renderer hints.
	 * 
	 * @see org.geotools.renderer.lite.StreamingRenderer#setRendererHints(java.util.Map)
	 * 
	 */
	String RENDERER_HINTS = "rasterizing.rendererHints"; // Map<Object, Object>

	/**
	 * Pipeline context key for storing the map context metadata.
	 */
	String CLIENT_MAP_INFO_KEY = "rasterizing.clientMapInfo"; // ClientMapInfo

	/**
	 * Pipeline context key for storing the map context.
	 */
	String MAP_CONTEXT_KEY = "rasterizing.mapContext"; // MapContext

}
