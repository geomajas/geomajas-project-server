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
package org.geomajas.layer.osm;

import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.annotation.Api;
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

	/**
	 * Default tile URLs.
	 *
	 * @since 1.10.0
	 */
	@Api
	public static final List<String> OPEN_STREET_MAP_URLS = new ArrayList<String>();

	/**
	 * Open Cycle Map rendering of OpenStreetMap.
	 *
	 * @since 1.10.0
	 */
	@Api
	public static final List<String> OPEN_CYCLE_MAP_URLS = new ArrayList<String>();

	/**
	 * @deprecated no longer working, use {@link #OPEN_STREET_MAP_URLS} or another set of URLs instead.
	 */
	@Deprecated
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
		OPEN_CYCLE_MAP_URLS.add("http://a.tile.opencyclemap.org/cycle/${level}/${x}/${y}.png");
		OPEN_CYCLE_MAP_URLS.add("http://b.tile.opencyclemap.org/cycle/${level}/${x}/${y}.png");
		OPEN_CYCLE_MAP_URLS.add("http://c.tile.opencyclemap.org/cycle/${level}/${x}/${y}.png");
		TILES_AT_HOME_MAP_URLS.add("http://a.tile.openstreetmap.org/${level}/${x}/${y}.png");
		TILES_AT_HOME_MAP_URLS.add("http://b.tile.openstreetmap.org/${level}/${x}/${y}.png");
		TILES_AT_HOME_MAP_URLS.add("http://c.tile.openstreetmap.org/${level}/${x}/${y}.png");
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
	 * Set the strategy ({@link UrlSelectionStrategy}) for selecting the URL to use for the tiles.
	 *
	 * @param strategy a tile URL builder
	 * @since 1.8.0
	 */
	@Api
	public void setUrlSelectionStrategy(UrlSelectionStrategy strategy) {
		tileServiceState.setUrlSelectionStrategy(strategy);
	}

	@PostConstruct
	protected void postConstruct() throws GeomajasException {
		tileServiceState.postConstruct(geoService, converterService);
	}

	public List<RasterTile> paint(CoordinateReferenceSystem boundsCrs, Envelope bounds, double scale)
			throws GeomajasException {
		return tileService.paint(tileServiceState, boundsCrs, bounds, scale);
	}

}
