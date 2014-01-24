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
package org.geomajas.layer.google;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.CrsTransform;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.geotools.geometry.jts.JTS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vividsolutions.jts.geom.Coordinate;
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

	private final Logger log = LoggerFactory.getLogger(GoogleLayer.class);

	public static final String DATA_SOURCE_GOOGLE_INDICATOR = "@GoogleLayer";

	public static final String LAYER_NAME_NORMAL = "G_NORMAL_MAP";

	public static final String LAYER_NAME_SATELLITE = "G_SATELLITE_MAP";

	// public static final String LAYER_NAME_HYBRID = "G_HYBID_MAP"; // doesn't seem to work
	public static final String LAYER_NAME_PHYSICAL = "G_PHYSICAL_MAP";

	public static final int DEFAULT_MAX_ZOOM_LEVEL = 19;

	public static final int DEFAULT_TILE_SIZE = 512; // tile size in pixels

	public static final String NORMAL_URL = "http://maps.googleapis.com/maps/api/staticmap?center=${center}"
			+ "&zoom=${level}&sensor=false&maptype=roadmap";

	public static final String SATELLITE_URL = "http://maps.googleapis.com/maps/api/staticmap?center=${center}"
			+ "&zoom=${level}&sensor=false&maptype=satellite";

	public static final String PHYSICAL_URL = "http://maps.googleapis.com/maps/api/staticmap?center=${center}"
			+ "&zoom=${level}&sensor=false&maptype=terrain";

	public static final String MERCATOR = "EPSG:900913";

	public static final String WSG_84 = "EPSG:4326";

	public static final double EQUATOR_IN_METERS = 40075016.686;

	public static final double HALF_EQUATOR_IN_METERS = 40075016.686 / 2;

	public static final double MAP_UNIT_PER_GOOGLE_METER_DEFAULT = 0.653;

	private int tileSize = DEFAULT_TILE_SIZE;

	//default api key, registered with geomajas.apikey@gmail.com.
	private String apiKey = "AIzaSyDRzQiQFHTC6FZw4DAwD2-KIUT18u99fDE";

	private boolean satellite;

	private boolean physical;

	private boolean tilesEnabled = true;

	@Autowired
	private DtoConverterService converterService;

	@Autowired
	private GeoService geoService;

	private String id;

	private CoordinateReferenceSystem crs;

	private int maxZoomlevel = DEFAULT_MAX_ZOOM_LEVEL;

	private RasterLayerInfo layerInfo;

	private String tileUrl;

	private double[] resolutions;

	private Envelope maxBounds;

	public String getId() {
		return id;
	}

	/**
	 * Set the id for this layer.
	 * 
	 * @param id
	 *            id
	 * @since 1.8.0
	 */
	@Api
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public CoordinateReferenceSystem getCrs() {
		return crs;
	}

	/**
	 * Set the maximum zoom level which is supported by this layer. The levels are specific for this layer. The first
	 * level has one tile for the world, the second four etc.
	 * 
	 * @param maxZoomLevel
	 *            max zoom level
	 * @since 1.6.0
	 */
	@Api
	public void setMaxZoomLevel(int maxZoomLevel) {
		this.maxZoomlevel = maxZoomLevel;
	}

	@Override
	public RasterLayerInfo getLayerInfo() {
		return layerInfo;
	}

	/**
	 * Set the layer configuration.
	 * 
	 * @param layerInfo
	 *            layer information
	 * @throws LayerException
	 *             oops
	 * @since 1.7.1
	 */
	@Api
	public void setLayerInfo(RasterLayerInfo layerInfo) throws LayerException {
		this.layerInfo = layerInfo;
	}

	/**
	 * Set a list of tile URLs. The zoom level, and tile coordinates can be indicated using ${level} and ${center}, for
	 * example:
	 * "http://maps.googleapis.com/maps/api/staticmap?center=${center}&zoom=${level}&sensor=false&maptype=roadmap".
	 * 
	 * @param tileUrl
	 *            list of tile URLs
	 * @since 2.0.0
	 */
	@Api
	public void setTileUrl(String tileUrls) {
		this.tileUrl = tileUrls;
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
	 * @param satellite
	 *            use satellite ?
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
	 * @param physical
	 *            use physical ?
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
	 * Check whether tiles should be sent to the client.
	 * 
	 * @return true when tiles are enabled
	 * @since 2.0.0
	 */
	public boolean isTilesEnabled() {
		return tilesEnabled;
	}

	/**
	 * Set whether tiles should be sent to the client. Defaults to true.
	 * 
	 * @param tilesEnabled
	 *            true when tiles should be sent to the client
	 * @since 2.0.0
	 */
	@Api
	public void setTilesEnabled(boolean tilesEnabled) {
		this.tilesEnabled = tilesEnabled;
	}

	/**
	 * The google API key used to retrieve static images. If no API key is set, no static images will be retrieved.
	 * 
	 * @param apiKey
	 *            the apiKey to set
	 * @since 2.0.0
	 */
	@Api
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * The google API key used to retrieve static images.
	 * 
	 * @return the apiKey
	 * @since 2.0.0
	 */
	public String getApiKey() {
		return apiKey;
	}

	@PostConstruct
	protected void postConstruct() throws GeomajasException {
		String layerName = null;
		if (getLayerInfo() != null) {
			layerName = getLayerInfo().getDataSourceName();
		}
		crs = geoService.getCrs2(MERCATOR);

		if (null == layerInfo) {
			layerInfo = new RasterLayerInfo();
			layerInfo.setTileHeight(tileSize);
			layerInfo.setTileWidth(tileSize);
		}
		Bbox bbox = new Bbox(-20026376.393709917, -20026376.393709917, 40052752.787419834, 40052752.787419834);
		layerInfo.setMaxExtent(bbox);
		layerInfo.setCrs(MERCATOR);
		maxBounds = converterService.toInternal(bbox);
		if (layerInfo.getTileHeight() != 0) {
			// We choose tile height, width and height should be the same anyway
			tileSize = layerInfo.getTileHeight();
		}

		resolutions = new double[maxZoomlevel + 1];
		double powerOfTwo = 1;
		for (int zoomLevel = 0; zoomLevel <= maxZoomlevel; zoomLevel++) {
			double resolution = (EQUATOR_IN_METERS) / (256 * powerOfTwo);
			resolutions[zoomLevel] = resolution;
			powerOfTwo *= 2;
		}

		String url = null;
		// Init layer name and url's
		if (null == layerName) {
			if (isSatellite()) {
				getLayerInfo().setDataSourceName(LAYER_NAME_SATELLITE + DATA_SOURCE_GOOGLE_INDICATOR);
				url = completeTileUrl(SATELLITE_URL);
			} else if (isPhysical()) {
				getLayerInfo().setDataSourceName(LAYER_NAME_PHYSICAL + DATA_SOURCE_GOOGLE_INDICATOR);
				url = completeTileUrl(PHYSICAL_URL);
			} else {
				getLayerInfo().setDataSourceName(LAYER_NAME_NORMAL + DATA_SOURCE_GOOGLE_INDICATOR);
				url = completeTileUrl(NORMAL_URL);
			}
		} else if (!layerName.endsWith(DATA_SOURCE_GOOGLE_INDICATOR)) {
			getLayerInfo().setDataSourceName(layerName + DATA_SOURCE_GOOGLE_INDICATOR);
			if (layerName.equals(LAYER_NAME_SATELLITE)) {
				setSatellite(true);
				url = completeTileUrl(SATELLITE_URL);
			} else if (layerName.equals(LAYER_NAME_PHYSICAL)) {
				setPhysical(true);
				url = completeTileUrl(PHYSICAL_URL);
			} else {
				url = completeTileUrl(NORMAL_URL);
			}
		}
		// If tileUrl is set, use it.
		if (tileUrl != null) {
			tileUrl = completeTileUrl(tileUrl);
		} else {
			tileUrl = url;
		}
	}

	private String completeTileUrl(String source) {
		String sizeString = "&size=" + tileSize + "x" + tileSize;
		String apiString = "";
		if (apiKey != null && !"".equals(apiKey)) {
			apiString = "&key=" + apiKey;
		}
		return source + apiString + sizeString;
	}

	@Override
	public List<RasterTile> paint(CoordinateReferenceSystem targetCrs, Envelope bounds, double scale)
			throws GeomajasException {
		if (isTilesEnabled()) {
			try {
				CrsTransform layerToMap = geoService.getCrsTransform(crs, targetCrs);
				CrsTransform mapToLayer = geoService.getCrsTransform(targetCrs, crs);
				CrsTransform layerToWsg84 = geoService.getCrsTransform(crs, geoService.getCrs2(WSG_84));

				// find the center of the map in map coordinates (positive y-axis)
				Coordinate boundsCenter = new Coordinate((bounds.getMinX() + bounds.getMaxX()) / 2,
						(bounds.getMinY() + bounds.getMaxY()) / 2);

				// Translate the map coordinates to layer coordinates, assumes equal x-y orientation
				Envelope layerBounds = geoService.transform(bounds, mapToLayer);
				// double layerScale = bounds.getWidth() * scale / layerBounds.getWidth();
				layerBounds = clipBounds(layerBounds);
				if (layerBounds.isNull()) {
					return new ArrayList<RasterTile>(0);
				}

				// find zoomlevel
				// scale in pix/m should just above the given scale so we have at least one
				// screen pixel per google pixel ! (otherwise text unreadable)
				int zoomLevel = getBestZoomLevelForScaleInPixPerMeter(mapToLayer, boundsCenter, scale);
				log.debug("zoomLevel={}", zoomLevel);

				RasterGrid grid = getRasterGrid(layerBounds, tileSize * resolutions[zoomLevel], tileSize
						* resolutions[zoomLevel]);

				// We calculate the first tile's screen box with this assumption
				List<RasterTile> result = new ArrayList<RasterTile>();
				for (int i = grid.getXmin(); i < grid.getXmax(); i++) {
					for (int j = grid.getYmin(); j < grid.getYmax(); j++) {
						double x = grid.getLowerLeft().x + (i - grid.getXmin()) * grid.getTileWidth();
						double y = grid.getLowerLeft().y + (j - grid.getYmin()) * grid.getTileHeight();
						// layer coordinates
						Bbox worldBox;
						Bbox layerBox;
						layerBox = new Bbox(x, y, grid.getTileWidth(), grid.getTileHeight());
						// Transforming back to map coordinates will only result in a proper grid if the transformation
						// is nearly affine
						worldBox = geoService.transform(layerBox, layerToMap);

						// Rounding to avoid white space between raster tiles lower-left becomes upper-left in inverted
						// y-space
						Bbox screenBox = new Bbox(Math.round(scale * worldBox.getX()), -Math.round(scale
								* worldBox.getMaxY()), Math.round(scale * worldBox.getMaxX())
								- Math.round(scale * worldBox.getX()), Math.round(scale * worldBox.getMaxY())
								- Math.round(scale * worldBox.getY()));

						RasterTile image = new RasterTile(screenBox, getId() + "." + zoomLevel + "." + i + "," + j);

						String url = tileUrl;

						Coordinate center = new Coordinate((layerBox.getX() + layerBox.getMaxX()) / 2,
								(layerBox.getY() + layerBox.getMaxY()) / 2);
						Coordinate centerInWsg84 = JTS.transform(center, new Coordinate(), layerToWsg84);
						url = url.replace("${center}",
								Double.toString(centerInWsg84.y) + "," + Double.toString(centerInWsg84.x));
						url = url.replace("${level}", Integer.toString(zoomLevel));

						// When we are trying to display the tiles on a different coordinate system, use double scaled
						// images so that renderings are more smooth. This will return an image tileSize*2 x tileSize*2.
						// Disabled becausd the printing plugin can't handle this!
						// if (!layerToMap.isIdentity()) {
						// url += "&scale=2";
						// }

						image.setCode(new TileCode(zoomLevel, i, j));
						image.setUrl(url);
						log.debug("adding image {}", image);
						result.add(image);
					}
				}

				return result;

			} catch (TransformException e) {
				throw new GeomajasException(e, ExceptionCode.RENDER_TRANSFORMATION_FAILED);
			}
		} else {
			return Collections.emptyList();
		}
	}

	private int getBestZoomLevelForScaleInPixPerMeter(CrsTransform layerToGoogle, Coordinate mapPosition, 
			double scale) {
		double scaleRatio = MAP_UNIT_PER_GOOGLE_METER_DEFAULT;
		try {
			Coordinate mercatorCenter = JTS.transform(mapPosition, new Coordinate(), layerToGoogle);
			Coordinate dx = JTS.transform(new Coordinate(mapPosition.x + 1, mapPosition.y), new Coordinate(),
					layerToGoogle);
			scaleRatio = 1.0 / (dx.x - mercatorCenter.x);
		} catch (TransformException e) {
			log.warn("calculateMapUnitPerGoogleMeter() : transformation failed", e);
		}
		double scaleInPixPerMeter = scale * scaleRatio;
		double screenResolution = 1.0 / scaleInPixPerMeter;
		if (screenResolution >= resolutions[0]) {
			return 0;
		} else if (screenResolution <= resolutions[maxZoomlevel]) {
			return maxZoomlevel;
		} else {
			for (int i = 0; i < maxZoomlevel; i++) {
				double upper = resolutions[i];
				double lower = resolutions[i + 1];
				if (screenResolution <= upper && screenResolution >= lower) {
					if ((upper - screenResolution) > 2 * (screenResolution - lower)) {
						return i + 1;
					} else {
						return i;
					}
				}
			}
		}
		// should not occur !!!!
		return maxZoomlevel;
	}

	private RasterGrid getRasterGrid(Envelope bounds, double width, double height) {
		Bbox bbox = getLayerInfo().getMaxExtent();
		int ymin = (int) Math.floor((bounds.getMinY() - bbox.getY()) / height);
		int ymax = (int) Math.ceil((bounds.getMaxY() - bbox.getY()) / height);
		int xmin = (int) Math.floor((bounds.getMinX() - bbox.getX()) / width);
		int xmax = (int) Math.ceil((bounds.getMaxX() - bbox.getX()) / width);

		Coordinate lowerLeft = new Coordinate(bbox.getX() + xmin * width, bbox.getY() + ymin * height);
		return new RasterGrid(lowerLeft, xmin, ymin, xmax, ymax, width, height);
	}

	private Envelope clipBounds(Envelope bounds) {
		return bounds.intersection(maxBounds);
	}

	/**
	 * Grid definition for a WMS layer. It is used internally in the WMS layer.
	 * 
	 * @author Jan De Moerloose
	 * @author Pieter De Graef
	 */
	private static class RasterGrid {

		private final Coordinate lowerLeft;

		private final int xmin;

		private final int ymin;

		private final int xmax;

		private final int ymax;

		private final double tileWidth;

		private final double tileHeight;

		RasterGrid(Coordinate lowerLeft, int xmin, int ymin, int xmax, int ymax, double tileWidth, double tileHeight) {
			super();
			this.lowerLeft = lowerLeft;
			this.xmin = xmin;
			this.ymin = ymin;
			this.xmax = xmax;
			this.ymax = ymax;
			this.tileWidth = tileWidth;
			this.tileHeight = tileHeight;
		}

		public Coordinate getLowerLeft() {
			return lowerLeft;
		}

		public double getTileHeight() {
			return tileHeight;
		}

		public double getTileWidth() {
			return tileWidth;
		}

		public int getXmax() {
			return xmax;
		}

		public int getXmin() {
			return xmin;
		}

		public int getYmax() {
			return ymax;
		}

		public int getYmin() {
			return ymin;
		}
	}
}
