/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.wmsclient.client.service;

import java.util.List;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.plugin.wmsclient.client.layer.config.WmsTileConfiguration;
import org.geomajas.puregwt.client.map.ViewPort;

/**
 * Service definition with helper methods for tile calculations. These may help when rendering a tile based layer.
 * 
 * @author Pieter De Graef
 */
public interface WmsTileService {

	// ------------------------------------------------------------------------
	// Methods regarding tiles:
	// ------------------------------------------------------------------------

	/**
	 * Get a list of tile codes for a certain location (bounding box).
	 * 
	 * @param viewPort
	 *            The ViewPort on the map we are calculating tile codes for. We need the ViewPort for scale to
	 *            tile-level translations.
	 * @param tileConfiguration
	 *            The basic tile configuration.
	 * @param bounds
	 *            The bounds in world space (map CRS).
	 * @param scale
	 *            The scale at which to search for tiles.
	 * @return A list of all tiles that lie within the location.
	 */
	List<TileCode> getTileCodesForBounds(ViewPort viewPort, WmsTileConfiguration tileConfiguration, Bbox bounds,
			double scale);

	/**
	 * Given a tile for a layer, what are the tiles bounds in world space.
	 * 
	 * @param viewPort
	 *            The ViewPort on the map we are calculating tile codes for. We need the ViewPort for scale to
	 *            tile-level translations.
	 * @param tileConfig
	 *            The basic tile configuration.
	 * @param tileCode
	 *            The tile code.
	 * @return The tile bounds in map CRS.
	 */
	Bbox getWorldBoundsForTile(ViewPort viewPort, WmsTileConfiguration tileConfig, TileCode tileCode);

	/**
	 * Given a certain location at a certain scale, what tile lies there?
	 * 
	 * @param viewPort
	 *            The ViewPort on the map we are calculating tile codes for. We need the ViewPort for scale to
	 *            tile-level translations.
	 * @param tileConfig
	 *            The basic tile configuration.
	 * @param location
	 *            The location to retrieve a tile for.
	 * @param scale
	 *            The scale at which to retrieve a tile.
	 * @return Returns the tile code for the requested location.
	 */
	TileCode getTileCodeForLocation(ViewPort viewPort, WmsTileConfiguration tileConfig, Coordinate location,
			double scale);
}