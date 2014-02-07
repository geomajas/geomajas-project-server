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
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.tile.RasterTile;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Service which allows accessing data from a raster layer.
 * <p/>
 * All access to raster layers should be done through this service, not by accessing the layer directly as this
 * adds possible modifications in the data. These services are implemented using pipelines
 * (see {@link org.geomajas.service.pipeline.PipelineService}) to make them configurable.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public interface RasterLayerService extends LayerService {

	/**
	 * Get the raster tiles for the specified bounds, optimized for the scale in pixels/unit.
	 *
	 * @param layerId layer id
	 * @param crs Coordinate reference system used for bounds
	 * @param bounds bounds to request images for
	 * @param scale scale or zoom level (unit?)
	 * @return a list of raster images that covers the bounds
	 * @throws GeomajasException oops
	 */
	List<RasterTile> getTiles(String layerId, CoordinateReferenceSystem crs, Envelope bounds, double scale)
			throws GeomajasException;

}
