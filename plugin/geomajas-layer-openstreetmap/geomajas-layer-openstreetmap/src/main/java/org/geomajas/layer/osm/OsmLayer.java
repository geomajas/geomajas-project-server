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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.global.Api;
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
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

/**
 * Layer for displaying OpenStreetMap images.
 * 
 * @author Jan De Moerloose
 * @since 1.7.1
 */
@Api
public class OsmLayer implements RasterLayer {

	protected int tileWidth;

	protected int tileHeight;

	protected double maxWidth;

	protected double maxHeight;

	public static final double EQUATOR_IN_METERS = 40075016.686;

	public static final int DEFAULT_MAX_ZOOM_LEVEL = 19;

	public static final int MAX_ZOOM_LEVEL = 31;

	public static final int TILE_SIZE = 256; // tile size in pixels

	private final Logger log = LoggerFactory.getLogger(OsmLayer.class);

	@Autowired
	private DtoConverterService converterService;

	@Autowired
	private GeoService geoService;

	private RasterLayerInfo layerInfo;

	private CoordinateReferenceSystem crs;

	private String id;

	private TileUrlBuilder urlBuilder = new RoundRobinTileUrlBuilder();

	private List<String> baseUrls;

	protected int maxZoomLevel = DEFAULT_MAX_ZOOM_LEVEL;

	protected static final double[] RESOLUTIONS = new double[MAX_ZOOM_LEVEL + 1];

	protected static final int[] POWERS_OF_TWO;

	static {
		POWERS_OF_TWO = new int[MAX_ZOOM_LEVEL + 1];
		int b = 1;
		for (int c = 0; c < POWERS_OF_TWO.length; c++) {
			POWERS_OF_TWO[c] = b;
			b *= 2;
		}
		for (int zoomLevel = 0; zoomLevel <= MAX_ZOOM_LEVEL; zoomLevel++) {
			double resolution = (EQUATOR_IN_METERS) / (TILE_SIZE * POWERS_OF_TWO[zoomLevel]);
			RESOLUTIONS[zoomLevel] = resolution;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RasterLayerInfo getLayerInfo() {
		return layerInfo;
	}

	public CoordinateReferenceSystem getCrs() {
		return crs;
	}

	public Envelope getMaxBounds() {
		return converterService.toInternal(layerInfo.getMaxExtent());
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
		this.maxZoomLevel = maxZoomLevel < MAX_ZOOM_LEVEL ? maxZoomLevel : MAX_ZOOM_LEVEL;
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
	 * Set a list of base URLs. Use this as a shortcut for setting a RoundRobinTileUrlBuilder with the specified base
	 * URLs.
	 * 
	 * @param baseUrls
	 *            list of base URLs (e.g.
	 *            "a.tile.openstreetmap.org","b.tile.openstreetmap.org","c.tile.openstreetmap.org")
	 * @since 1.8.0
	 */
	@Api
	public void setBaseUrls(List<String> baseUrls) {
		this.baseUrls = baseUrls;
	}

	/**
	 * Set the builder that should be used to build a tile URL.
	 * 
	 * @param urlBuilder
	 *            a tile URL builder
	 * @since 1.8.0
	 */
	@Api
	public void setUrlBuilder(TileUrlBuilder urlBuilder) {
		this.urlBuilder = urlBuilder;
	}

	@PostConstruct
	private void postConstruct() throws Exception {
		if (null != layerInfo) {
			crs = geoService.getCrs("EPSG:900913"); // we overrule the declared crs, always use mercator/google
			tileWidth = layerInfo.getTileWidth();
			tileHeight = layerInfo.getTileHeight();
			maxWidth = layerInfo.getMaxExtent().getWidth();
			maxHeight = layerInfo.getMaxExtent().getHeight();
		}
		if (null != baseUrls) {
			RoundRobinTileUrlBuilder rr = new RoundRobinTileUrlBuilder();
			rr.setBaseUrls(baseUrls);
			setUrlBuilder(rr);
		}
	}

	public List<RasterTile> paint(CoordinateReferenceSystem boundsCrs, Envelope bounds, double scale)
			throws GeomajasException {
		try {
			MathTransform layerToMap = geoService.findMathTransform(crs, boundsCrs);
			MathTransform mapToLayer = layerToMap.inverse();

			bounds = clipBounds(bounds);
			if (bounds.isNull()) {
				return new ArrayList<RasterTile>();
			}

			// find the center of the map in map coordinates (positive y-axis)
			Coordinate center = new Coordinate(0.5 * (bounds.getMinX() + bounds.getMaxX()), 0.5 * (bounds
					.getMinY() + bounds.getMaxY()));

			// find zoomlevel
			// scale in pix/m should just above the given scale so we have at least one
			// screen pixel per google pixel ! (otherwise text unreadable)
			int tileLevel = getBestOsmZoomLevelForScaleInPixPerMeter(mapToLayer, center, scale);
			log.debug("zoomLevel={}", tileLevel);

			// find the google indices for the center
			// google indices determine the row and column of the 256x256 image
			// in the big google square for the given zoom zoomLevel
			// the resulting indices are floating point values as the center
			// is not coincident with an image corner !!!!
			Coordinate indicesCenter;
			indicesCenter = getOsmIndicesFromMap(mapToLayer, center, tileLevel);

			// Calculate the width in map units of the image that contains the
			// center
			Coordinate indicesUpperLeft = new Coordinate(Math.floor(indicesCenter.x), Math.floor(indicesCenter.y));
			Coordinate indicesLowerRight = new Coordinate(indicesUpperLeft.x + 1, indicesUpperLeft.y + 1);
			Coordinate mapUpperLeft = getMapFromOsmIndices(layerToMap, indicesUpperLeft, tileLevel);
			Coordinate mapLowerRight = getMapFromOsmIndices(layerToMap, indicesLowerRight, tileLevel);
			double width = Math.abs(mapLowerRight.x - mapUpperLeft.x);
			if (0 == width) {
				width = 1.0;
			}
			double height = Math.abs(mapLowerRight.y - mapUpperLeft.y);
			if (0 == height) {
				height = 1.0;
			}

			// Calculate the position and indexes of the center image corner
			// in map space
			double xCenter = center.x - (indicesCenter.x - indicesUpperLeft.x) * width;
			double yCenter = center.y + (indicesCenter.y - indicesUpperLeft.y) * width;
			int iCenter = (int) indicesUpperLeft.x;
			int jCenter = (int) indicesUpperLeft.y;

			// Calculate the position and indexes of the upper left image corner
			// that just falls off the screen
			double xMin = xCenter;
			int iMin = iCenter;
			while (xMin > bounds.getMinX() && iMin > 0) {
				xMin -= width;
				iMin--;
			}
			double yMax = yCenter;
			int jMin = jCenter;
			while (yMax < bounds.getMaxY() && jMin > 0) {
				yMax += height;
				jMin--;
			}
			// Calculate the indexes of the lower right corner
			// that just falls off the screen
			int levelMax = (int) Math.pow(2, tileLevel) - 1;
			double xMax = xCenter;
			int iMax = iCenter;
			while (xMax < bounds.getMaxX() /*&& iMax < levelMax*/) {
				xMax += width;
				iMax++;
			}
			double yMin = yCenter;
			int jMax = jCenter;
			while (yMin > bounds.getMinY() /*&& jMax < levelMax*/) {
				yMin -= height;
				jMax++;
			}
			Coordinate upperLeft = new Coordinate(xMin, yMax);

			// calculate the images
			List<RasterTile> result = new ArrayList<RasterTile>();
			if (log.isDebugEnabled()) {
				log.debug("bounds =" + bounds);
				log.debug("tilebounds " + xMin + ", " + xMax + ", " + yMin + ", " + yMax);
				log.debug("indices " + iMin + ", " + iMax + ", " + jMin + ", " + jMax);
			}
			int xScreenUpperLeft = (int) Math.round(upperLeft.x * scale);
			int yScreenUpperLeft = (int) Math.round(upperLeft.y * scale);
			int screenWidth = (int) Math.round(scale * width);
			int screenHeight = (int) Math.round(scale * height);
			for (int i = iMin; i < iMax; i++) {
				for (int j = jMin; j < jMax; j++) {
					// Using screen coordinates:
					int x = xScreenUpperLeft + (i - iMin) * screenWidth;
					int y = yScreenUpperLeft - (j - jMin) * screenWidth;

					RasterTile image = new RasterTile(new Bbox(x, -y, screenWidth, screenHeight), getId() + "."
							+ tileLevel + "." + i + "," + j);
					image.setCode(new TileCode(tileLevel, i, j));
					image.setUrl(urlBuilder.buildUrl(tileLevel, i, j));
					log.debug("adding OSM image {}", image);
					result.add(image);
				}
			}
			return result;
		} catch (TransformException e) {
			throw new GeomajasException(e, ExceptionCode.RENDER_TRANSFORMATION_FAILED);
		}
	}

	private Envelope clipBounds(Envelope bounds) {
		return bounds.intersection(getMaxBounds());
	}

	private int getBestOsmZoomLevelForScaleInPixPerMeter(MathTransform layerToGoogle, Coordinate mapPosition,
			double scale) {
		double scaleRatio = 0.653;
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
		if (screenResolution >= RESOLUTIONS[0]) {
			return 0;
		} else if (screenResolution <= RESOLUTIONS[maxZoomLevel]) {
			return maxZoomLevel;
		} else {
			for (int i = 0; i < maxZoomLevel; i++) {
				double upper = RESOLUTIONS[i];
				double lower = RESOLUTIONS[i + 1];
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
		return MAX_ZOOM_LEVEL;
	}

	private Coordinate getMapFromOsmIndices(MathTransform mapToLayer, Coordinate indices, int zoomLevel)
			throws TransformException {
		double xMeter = EQUATOR_IN_METERS * indices.x / POWERS_OF_TWO[zoomLevel] - 0.5 * EQUATOR_IN_METERS;
		double yMeter = -EQUATOR_IN_METERS * indices.y / POWERS_OF_TWO[zoomLevel] + 0.5 * EQUATOR_IN_METERS;
		return JTS.transform(new Coordinate(xMeter, yMeter), new Coordinate(), mapToLayer);
	}

	private Coordinate getOsmIndicesFromMap(MathTransform mapToLayer, Coordinate center, int zoomLevel)
			throws TransformException {
		Coordinate googleCenter = JTS.transform(center, new Coordinate(), mapToLayer);
		double xIndex = (googleCenter.x + 0.5 * EQUATOR_IN_METERS) * POWERS_OF_TWO[zoomLevel] / (EQUATOR_IN_METERS);
		double yIndex = (-googleCenter.y + 0.5 * EQUATOR_IN_METERS) * POWERS_OF_TWO[zoomLevel] / (EQUATOR_IN_METERS);
		return new Coordinate(xIndex, yIndex);
	}

}
