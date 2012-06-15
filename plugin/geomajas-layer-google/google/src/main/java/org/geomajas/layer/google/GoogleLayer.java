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
package org.geomajas.layer.google;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.global.Api;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.osm.TiledRasterLayerService;
import org.geomajas.layer.osm.TiledRasterLayerServiceState;
import org.geomajas.layer.osm.UrlSelectionStrategy;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Layer for displaying Google Maps images. Caution: you must comply with the Google terms of service to display the
 * calculated tiles. For the GWT face, a special map add-on (<code>GoogleAddOn</code>) is available.
 * 
 * WARNING: because of change in <a href="https://developers.google.com/maps/terms">Google Maps Terms of Service</a>,
 * the default behavior for this layer is to not send tiles to the client.
 * 
 * @author Jan De Moerloose
 * @author Joachim Van der Auwera
 * @author Frank Wijnants
 * @author Oliver May
 * @since 1.6.0
 */
@Api
public class GoogleLayer implements RasterLayer {

	public static final String DATA_SOURCE_GOOGLE_INDICATOR = "@GoogleLayer";

	public static final String LAYER_NAME_NORMAL = "G_NORMAL_MAP";
	public static final String LAYER_NAME_SATELLITE = "G_SATELLITE_MAP";
	//public static final String LAYER_NAME_HYBRID = "G_HYBID_MAP"; // doesn't seem to work
	public static final String LAYER_NAME_PHYSICAL = "G_PHYSICAL_MAP";

	public static final int DEFAULT_MAX_ZOOM_LEVEL = 19;

	public static final int TILE_SIZE = 256; // tile size in pixels

	public static final List<String> NORMAL_URLS = new ArrayList<String>();
	public static final List<String> SATELLITE_URLS = new ArrayList<String>();
	public static final List<String> PHYSICAL_URLS = new ArrayList<String>();

	private boolean satellite;

	private boolean physical;
	
	private boolean tilesEnabled;

	@Autowired
	private DtoConverterService converterService;

	@Autowired
	private GeoService geoService;

	@Autowired
	private TiledRasterLayerService tileService;

	private final TiledRasterLayerServiceState tileServiceState =
			new TiledRasterLayerServiceState(NORMAL_URLS, TILE_SIZE, DEFAULT_MAX_ZOOM_LEVEL);

	static {
		NORMAL_URLS.add("http://mt0.google.com/vt?v=w2.95&x=${x}&y=${y}&z=${level}");
		NORMAL_URLS.add("http://mt1.google.com/vt?v=w2.95&x=${x}&y=${y}&z=${level}");
		NORMAL_URLS.add("http://mt2.google.com/vt?v=w2.95&x=${x}&y=${y}&z=${level}");
		NORMAL_URLS.add("http://mt3.google.com/vt?v=w2.95&x=${x}&y=${y}&z=${level}");
		SATELLITE_URLS.add("http://khm0.google.com/kh?v=87&x=${x}&y=${y}&z=${level}");
		SATELLITE_URLS.add("http://khm1.google.com/kh?v=87&x=${x}&y=${y}&z=${level}");
		SATELLITE_URLS.add("http://khm2.google.com/kh?v=87&x=${x}&y=${y}&z=${level}");
		SATELLITE_URLS.add("http://khm3.google.com/kh?v=87&x=${x}&y=${y}&z=${level}");
		PHYSICAL_URLS.add("http://mt0.google.com/vt?lyrs=t@127,r@156000000&x=${x}&y=${y}&z=${level}");
		PHYSICAL_URLS.add("http://mt1.google.com/vt?lyrs=t@127,r@156000000&x=${x}&y=${y}&z=${level}");
		PHYSICAL_URLS.add("http://mt2.google.com/vt?lyrs=t@127,r@156000000&x=${x}&y=${y}&z=${level}");
		PHYSICAL_URLS.add("http://mt3.google.com/vt?lyrs=t@127,r@156000000&x=${x}&y=${y}&z=${level}");
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

	/** {@inheritDoc} */
	public CoordinateReferenceSystem getCrs() {
		return tileServiceState.getCrs();
	}

	/**
	 * Set the maximum zoom level which is supported by this layer. The levels are specific for this layer. The first
	 * level has one tile for the world, the second four etc.
	 *
	 * @param maxZoomLevel max zoom level
	 * @since 1.6.0
	 */
	@Api
	public void setMaxZoomLevel(int maxZoomLevel) {
		tileServiceState.setMaxZoomLevel(maxZoomLevel);
	}

	/** {@inheritDoc} */
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
	 * Set the strategy ({@link UrlSelectionStrategy})for selecting the URL to use for the tiles.
	 *
	 * @param strategy a tile URL builder
	 * @since 1.8.0
	 */
	@Api
	public void setUrlSelectionStrategy(UrlSelectionStrategy strategy) {
		tileServiceState.setUrlSelectionStrategy(strategy);
	}

	/**
	 * Check whether this should be satellite pictures.
	 *
	 * @return true when satellite images are requested
	 */
	public boolean isSatellite() {
		return satellite;
	}

	/**
	 * Set whether to use satellite images.
	 *
	 * @param satellite use satellite ?
	 * @since 1.6.0
	 */
	@Api
	public void setSatellite(boolean satellite) {
		if (satellite) {
			physical = false;
		}
		this.satellite = satellite;
	}

	/**
	 * Check whether this should be physical pictures.
	 *
	 * @return true when physical images are requested
	 */
	public boolean isPhysical() {
		return physical;
	}

	/**
	 * Set whether to use physical images.
	 *
	 * @param physical use physical ?
	 * @since 1.7.0
	 */
	@Api
	public void setPhysical(boolean physical) {
		if (physical) {
			satellite = false;
		}
		this.physical = physical;
	}
	
	/**
	 * Check whether tiles should be sent to the client. WARNING: deprecated because of change in <a
	 * href="https://developers.google.com/maps/terms">Google Maps Terms of Service</a>.
	 * 
	 * @return
	 * @since 1.9.0
	 * @deprecated use default setting of false
	 */
	@Deprecated
	public boolean isTilesEnabled() {
		return tilesEnabled;
	}
	
	/**
	 * Set whether tiles should be sent to the client. Defaults to false. WARNING: deprecated because of change in <a
	 * href="https://developers.google.com/maps/terms">Google Maps Terms of Service</a>.
	 * 
	 * @param tilesEnabled
	 * @since 1.9.0
	 * @deprecated use default setting of false
	 */
	@Deprecated
	public void setTilesEnabled(boolean tilesEnabled) {
		this.tilesEnabled = tilesEnabled;
	}

	@PostConstruct
	protected void postConstruct() throws GeomajasException {
		tileServiceState.postConstruct(geoService, converterService);

		String layerName = getLayerInfo().getDataSourceName();
		if (null == layerName) {
			if (isSatellite()) {
				getLayerInfo().setDataSourceName(LAYER_NAME_SATELLITE + DATA_SOURCE_GOOGLE_INDICATOR);
				tileServiceState.setTileUrls(SATELLITE_URLS);
			} else if (isPhysical()) {
				getLayerInfo().setDataSourceName(LAYER_NAME_PHYSICAL + DATA_SOURCE_GOOGLE_INDICATOR);
				tileServiceState.setTileUrls(PHYSICAL_URLS);
			} else {
				getLayerInfo().setDataSourceName(LAYER_NAME_NORMAL + DATA_SOURCE_GOOGLE_INDICATOR);
			}
		} else if (!layerName.endsWith(DATA_SOURCE_GOOGLE_INDICATOR)) {
			getLayerInfo().setDataSourceName(layerName + DATA_SOURCE_GOOGLE_INDICATOR);
			if (layerName.equals(LAYER_NAME_SATELLITE)) {
				setSatellite(true);
				tileServiceState.setTileUrls(SATELLITE_URLS);
			}
			if (layerName.equals(LAYER_NAME_PHYSICAL)) {
				setPhysical(true);
				tileServiceState.setTileUrls(PHYSICAL_URLS);
			}
		}
	}

	/** {@inheritDoc} */
	public List<RasterTile> paint(CoordinateReferenceSystem boundsCrs, Envelope bounds, double scale)
			throws GeomajasException {
		if (isTilesEnabled()) {
			return tileService.paint(tileServiceState, boundsCrs, bounds, scale);
		} else {
			return Collections.emptyList();
		}
	}

}
