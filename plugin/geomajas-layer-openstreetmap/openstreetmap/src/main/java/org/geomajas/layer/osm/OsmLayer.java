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
package org.geomajas.layer.osm;

import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.global.Api;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Layer for displaying OpenStreetMap images.
 *
 * @author Jan De Moerloose
 * @author Joachim Van der Auwera
 * @since 1.7.1
 */
@Api
public class OsmLayer implements RasterLayer {

	public static final int DEFAULT_MAX_ZOOM_LEVEL = 19;

	public static final int TILE_SIZE = 256; // tile size in pixels

	public static final List<String> OPEN_STREET_MAP_URLS = new ArrayList<String>();
	public static final List<String> OPEN_CYCLE_MAP_URLS = new ArrayList<String>();
	public static final List<String> TILES_AT_HOME_MAP_URLS = new ArrayList<String>();

	@Autowired
	private DtoConverterService converterService;

	@Autowired
	private GeoService geoService;

	@Autowired
	private TiledRasterLayerService tileService;

	private TiledRasterLayerServiceState tileServiceState =
			new TiledRasterLayerServiceState(OPEN_STREET_MAP_URLS, TILE_SIZE, DEFAULT_MAX_ZOOM_LEVEL);

	static {
		OPEN_STREET_MAP_URLS.add("http://a.tile.openstreetmap.org/${level}/${x}/${y}.png");
		OPEN_STREET_MAP_URLS.add("http://b.tile.openstreetmap.org/${level}/${x}/${y}.png");
		OPEN_STREET_MAP_URLS.add("http://c.tile.openstreetmap.org/${level}/${x}/${y}.png");
		OPEN_CYCLE_MAP_URLS.add("http://a.tile.opencyclemap.org/${level}/${x}/${y}.png");
		OPEN_CYCLE_MAP_URLS.add("http://b.tile.opencyclemap.org/${level}/${x}/${y}.png");
		OPEN_CYCLE_MAP_URLS.add("http://c.tile.opencyclemap.org/${level}/${x}/${y}.png");
		TILES_AT_HOME_MAP_URLS.add("http://a.tah.openstreetmap.org/Tiles/tile/${level}/${x}/${y}.png");
		TILES_AT_HOME_MAP_URLS.add("http://b.tah.openstreetmap.org/Tiles/tile/${level}/${x}/${y}.png");
		TILES_AT_HOME_MAP_URLS.add("http://c.tah.openstreetmap.org/Tiles/tile/${level}/${x}/${y}.png");
	}

	public String getId() {
		return tileServiceState.getId();
	}

	/**
	 * Set the id for this layer.
	 *
	 * @param id id
	 * @since 1.8.0
	 */
	@Api
	public void setId(String id) {
		tileServiceState.setId(id);
	}

	public CoordinateReferenceSystem getCrs() {
		return tileServiceState.getCrs();
	}

	/**
	 * Set the maximum zoom level which is supported by this layer. The levels are specific for this layer. The first
	 * level has one tile for the world, the second four etc.
	 *
	 * @param maxZoomLevel max zoom level
	 * @since 1.8.0
	 */
	@Api
	public void setMaxZoomLevel(int maxZoomLevel) {
		tileServiceState.setMaxZoomLevel(maxZoomLevel);
	}

	public RasterLayerInfo getLayerInfo() {
		return tileServiceState.getLayerInfo();
	}

	/**
	 * Set the layer configuration.
	 *
	 * @param layerInfo layer information
	 * @throws LayerException oops
	 * @since 1.7.1
	 */
	@Api
	public void setLayerInfo(RasterLayerInfo layerInfo) throws LayerException {
		tileServiceState.setLayerInfo(layerInfo);
	}

	/**
	 * Set a list of tile URLs. The zoom level, and tile coordinates can be indicated using ${level}, ${x} and ${y},
	 * for example "http://a.tile.openstreetmap.org/${level}/${x}/${y}.png",
	 *
	 * @param tileUrls list of tile URLs
	 * @since 1.8.0
	 */
	@Api
	public void setTileUrls(List<String> tileUrls) {
		tileServiceState.setTileUrls(tileUrls);
	}

	/**
	 * Set the strategy ({@see UrlSelectionStrategy})for selecting the URL to use for the tiles.
	 *
	 * @param strategy a tile URL builder
	 * @since 1.8.0
	 */
	@Api
	public void setUrlSelectionStrategy(UrlSelectionStrategy strategy) {
		tileServiceState.setUrlSelectionStrategy(strategy);
	}

	@PostConstruct
	private void postConstruct() throws Exception {
		tileServiceState.postConstruct(geoService, converterService);
	}

	public List<RasterTile> paint(CoordinateReferenceSystem boundsCrs, Envelope bounds, double scale)
			throws GeomajasException {
		return tileService.paint(tileServiceState, boundsCrs, bounds, scale);
	}

}
