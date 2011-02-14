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
	// controller pipeline
	String PIPELINE_RASTERIZING = "rasterizing.rasterize";

	// command pipeline
	String PIPELINE_GET_VECTOR_TILE_RASTERIZING = "rasterizing.getVectorTile"; // command pipeline

	String IMAGE_ID_KEY = "rasterizing.imageId"; // String
	String CONTAINER_KEY = "rasterizing.container"; // rasterizingContainer
}
