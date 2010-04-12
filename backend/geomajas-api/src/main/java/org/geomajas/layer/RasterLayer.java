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
package org.geomajas.layer;

import java.util.List;

import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.global.Api;
import org.geomajas.global.GeomajasException;
import org.geomajas.global.UserImplemented;
import org.geomajas.layer.tile.RasterTile;

import com.vividsolutions.jts.geom.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A layer of raster data (could be based on local files, WMS or some non-standard image server).
 *
 * @author Jan De Moerloose
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
