/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer;

import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;
import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.tile.RasterTile;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;

/**
 * A layer of raster data (could be based on local files, WMS or some non-standard image server).
 *
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
@UserImplemented
public interface RasterLayer extends Layer<RasterLayerInfo> {

	/**
	 * Paints the specified bounds optimized for the specified scale in pixel/unit.
	 *
	 * @param boundsCrs Coordinate reference system used for bounds
	 * @param bounds bounds to request images for
	 * @param scale scale or zoom level (unit?)
	 * @return a list of raster images that covers the bounds
	 * @throws GeomajasException oops
	 */
	List<RasterTile> paint(CoordinateReferenceSystem boundsCrs, Envelope bounds, double scale) throws GeomajasException;
	
}
