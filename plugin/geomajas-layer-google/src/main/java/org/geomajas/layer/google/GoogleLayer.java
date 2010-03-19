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
package org.geomajas.layer.google;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.rendering.RenderException;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.geotools.geometry.DirectPosition2D;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

/**
 * Layer for displaying Google Maps images.
 * 
 * @author Jan De Moerloose
 * @author Joachim Van der Auwera
 * @author Frank Wijnants
 */
public class GoogleLayer implements RasterLayer {

	public static final String DATA_SOURCE_GOOGLE_INDICATOR = "@GoogleLayer";

	public static final String LAYER_NAME_NORMAL = "G_NORMAL_MAP";
	public static final String LAYER_NAME_SATELLITE = "G_SATELLITE_MAP";
	public static final String LAYER_NAME_HYBRID = "G_HYBID_MAP";
	public static final String LAYER_NAME_PHYSICAL = "G_PHYSICAL_MAP";

	public static final double EARTH_RADIUS_IN_METERS = 6378137.0;

	public static final double EQUATOR_IN_METERS = 2 * Math.PI * EARTH_RADIUS_IN_METERS;

	public static final int MAX_ZOOM_LEVEL = 17;

	public static final int TILE_SIZE = 256; // tile size in pixels

	private final Logger log = LoggerFactory.getLogger(GoogleLayer.class);

	protected List<GoogleResolution> resolutions = new ArrayList<GoogleResolution>();

	protected int tileWidth;

	protected int tileHeight;

	protected double maxWidth;

	protected double maxHeight;

	private boolean satellite;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private DtoConverterService converterService;

	@Autowired
	private GeoService geoService;

	private RasterLayerInfo layerInfo;

	private CoordinateReferenceSystem crs;

	private String id;

	protected static final int[] POWERS_OF_TWO;

	static {
		POWERS_OF_TWO = new int[31];
		int b = 1;
		for (int c = 0; c < POWERS_OF_TWO.length; c++) {
			POWERS_OF_TWO[c] = b;
			b *= 2;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CoordinateReferenceSystem getCrs() {
		return crs;
	}

	public Envelope getMaxBounds() {
		return converterService.toInternal(layerInfo.getMaxExtent());
	}

	public void setLayerInfo(RasterLayerInfo layerInfo) throws LayerException {
		this.layerInfo = layerInfo;
		crs = configurationService.getCrs("EPSG:900913"); // we overrule the declared crs, always use mercator/google
		String layerName = layerInfo.getDataSourceName();
		if (null == layerName) {
			layerInfo.setDataSourceName(LAYER_NAME_NORMAL + DATA_SOURCE_GOOGLE_INDICATOR);
		} else if (!layerName.endsWith(DATA_SOURCE_GOOGLE_INDICATOR)) {
			layerInfo.setDataSourceName(layerName + DATA_SOURCE_GOOGLE_INDICATOR);
			if (layerName.equals(LAYER_NAME_SATELLITE)) {
				setSatellite(true);
			}
		}
		tileWidth = layerInfo.getTileWidth();
		tileHeight = layerInfo.getTileHeight();
		maxWidth = layerInfo.getMaxExtent().getWidth();
		maxHeight = layerInfo.getMaxExtent().getHeight();
		this.calculatePredefinedResolutions();
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
	 */
	public void setSatellite(boolean satellite) {
		this.satellite = satellite;
	}

	public List<RasterTile> paint(CoordinateReferenceSystem boundsCrs, Envelope bounds, double scale)
			throws RenderException {
		try {
			MathTransform layerToMap = geoService.findMathTransform(crs, boundsCrs);
			MathTransform mapToLayer = layerToMap.inverse();

			// TODO: if bounds width or height is 0, we run out of memory ?
			bounds = clipBounds(bounds);
			// find the center of the map in map coordinates (positive y-axis)
			DirectPosition2D center = new DirectPosition2D(0.5 * (bounds.getMinX() + bounds.getMaxX()), 0.5 * (bounds
					.getMinY() + bounds.getMaxY()));

			double scaleRatio = calculateMapUnitPerGoogleMeter(mapToLayer, center);

			// find zoomlevel
			// scale in pix/m should just above the given scale so we have at
			// least
			// 1
			// screen pixel per google pixel ! (otherwise text unreadable)
			int tileLevel = getBestGoogleZoomLevelForScaleInPixPerMeter(scale * scaleRatio);
			log.info("zoomLevel=" + tileLevel);

			// find the google indices for the center
			// google indices determine the row and column of the 256x256 image
			// in
			// the big google square for the given zoom zoomLevel
			// the resulting indices are floating point values as the center
			// is not coincident with an image corner !!!!
			Coordinate indicesCenter;
			indicesCenter = getGoogleIndicesFromMap(mapToLayer, center, tileLevel);

			// Calculate the width in map units of the image that contains the
			// center
			Coordinate indicesUpperLeft = new Coordinate(Math.floor(indicesCenter.x), Math.floor(indicesCenter.y));
			Coordinate indicesLowerRight = new Coordinate(indicesUpperLeft.x + 1, indicesUpperLeft.y + 1);
			DirectPosition mapUpperLeft = getMapFromGoogleIndices(layerToMap, indicesUpperLeft, tileLevel);
			DirectPosition mapLowerRight = getMapFromGoogleIndices(layerToMap, indicesLowerRight, tileLevel);
			double width = mapLowerRight.getOrdinate(0) - mapUpperLeft.getOrdinate(0);
			double height = mapLowerRight.getOrdinate(1) - mapUpperLeft.getOrdinate(1);

			// Calculate the position and indices of the center image corner
			// in map space
			double xCenter = center.x - (indicesCenter.x - indicesUpperLeft.x) * width;
			double yCenter = center.y + (indicesCenter.y - indicesUpperLeft.y) * height;
			int iCenter = (int) indicesUpperLeft.x;
			int jCenter = (int) indicesUpperLeft.y;

			// Calculate the position and indices of the upper left image corner
			// that just falls off the screen
			double xMin = xCenter;
			int iMin = iCenter;
			while (xMin > bounds.getMinX()) {
				xMin -= width;
				iMin--;
			}
			double yMax = yCenter;
			int jMin = jCenter;
			while (yMax < bounds.getMaxY()) {
				yMax += height;
				jMin--;
			}
			// Calculate the indices of the lower right corner
			// that just falls off the screen
			double xMax = xCenter;
			int iMax = iCenter;
			while (xMax < bounds.getMaxX()) {
				xMax += width;
				iMax++;
			}
			double yMin = yCenter;
			int jMax = jCenter;
			while (yMin > bounds.getMinY()) {
				yMin -= height;
				jMax++;
			}
			Coordinate upperLeft = new Coordinate(xMin, yMax);

			// calculate the images
			List<RasterTile> result = new ArrayList<RasterTile>();
			log.info("bounds =" + bounds);
			log.info("indices " + iMin + "," + iMax + "," + jMin + "," + jMax);
			int xScreenUpperLeft = (int) Math.round(upperLeft.x * scale);
			int yScreenUpperLeft = (int) Math.round(upperLeft.y * scale);
			int screenWidth = (int) Math.round(scale * width);
			int screenHeight = (int) Math.round(scale * height);
			for (int i = iMin; i <= iMax; i++) {
				for (int j = jMin; j <= jMax; j++) {
					// Using screen coordinates:
					int x = xScreenUpperLeft + (i - iMin) * screenWidth;
					int y = yScreenUpperLeft - (j - jMin) * screenWidth;

					RasterTile image = new RasterTile(new Bbox(x, -y, screenWidth, screenHeight), getId()
							+ "." + tileLevel + "." + i + "," + j);
					image.setCode(new TileCode(tileLevel, i, j));
					if (isSatellite()) {
						image.setUrl(getSatelliteTileUrl(i, j, tileLevel));
					} else {
						image.setUrl(getTileUrl(i, j, tileLevel));
					}
					result.add(image);
				}
			}
			return result;
		} catch (RenderException re) {
			throw re;
		} catch (GeomajasException e) {
			throw new RenderException(e, ExceptionCode.RENDER_TRANSFORMATION_FAILED);
		} catch (TransformException e) {
			throw new RenderException(e, ExceptionCode.RENDER_TRANSFORMATION_FAILED);
		}
	}

	private double calculateMapUnitPerGoogleMeter(MathTransform layerToGoogle, DirectPosition2D mapPosition) {
		try {
			DirectPosition p1 = layerToGoogle.transform(mapPosition, null);
			DirectPosition mapdx = new DirectPosition2D(mapPosition.x + 1, mapPosition.y);
			DirectPosition p2 = layerToGoogle.transform(mapdx, null);
			return 1. / (p2.getOrdinate(0) - p1.getOrdinate(0));
		} catch (MismatchedDimensionException e) {
			log.warn("calculateMapUnitPerGoogleMeter() : transformation failed", e);
			return 0.653;
		} catch (TransformException e) {
			log.warn("calculateMapUnitPerGoogleMeter() : transformation failed", e);
			return 0.653;
		}
	}

	private Envelope clipBounds(Envelope bounds) {
		return bounds.intersection(getMaxBounds());
	}

	private void calculatePredefinedResolutions() {
		for (int zoomLevel = 0; zoomLevel <= MAX_ZOOM_LEVEL; zoomLevel++) {
			double resolution = (EQUATOR_IN_METERS) / (TILE_SIZE * Math.pow(2.0, zoomLevel));
			resolutions.add(new GoogleResolution(resolution, zoomLevel));
		}
	}

	private int getBestGoogleZoomLevelForScaleInPixPerMeter(double scaleInPixPerMeter) {
		double screenResolution = 1.0 / scaleInPixPerMeter;
		if (screenResolution >= resolutions.get(0).getResolution()) {
			return resolutions.get(0).getZoomLevel();
		} else if (screenResolution <= resolutions.get(resolutions.size() - 1).getResolution()) {
			return resolutions.get(resolutions.size() - 1).getZoomLevel();
		} else {
			for (int i = 0; i < resolutions.size() - 1; i++) {
				GoogleResolution upper = resolutions.get(i);
				GoogleResolution lower = resolutions.get(i + 1);
				if (screenResolution <= upper.getResolution() && screenResolution >= lower.getResolution()) {
					if ((upper.getResolution() - screenResolution) > 2 * (screenResolution - lower.getResolution())) {
						return lower.getZoomLevel();
					} else {
						return upper.getZoomLevel();
					}
				}
			}
		}
		// should not occur !!!!
		return resolutions.get(resolutions.size() - 1).getZoomLevel();
	}

	private DirectPosition getMapFromGoogleIndices(MathTransform googleToLayer, Coordinate indices, int zoomLevel)
			throws TransformException {
		double xMeter = EQUATOR_IN_METERS * indices.x / Math.pow(2.0, zoomLevel) - 0.5 * EQUATOR_IN_METERS;
		double yMeter = -EQUATOR_IN_METERS * indices.y / Math.pow(2.0, zoomLevel) + 0.5 * EQUATOR_IN_METERS;
		return googleToLayer.transform(new DirectPosition2D(xMeter, yMeter), null);
	}

	private Coordinate getGoogleIndicesFromMap(MathTransform layerToGoogle, DirectPosition2D map, int zoomLevel)
			throws TransformException {
		DirectPosition google = layerToGoogle.transform(map, null);
		double xIndex = (google.getOrdinate(0) + 0.5 * EQUATOR_IN_METERS) * Math.pow(2.0, zoomLevel)
				/ (EQUATOR_IN_METERS);
		double yIndex = (-google.getOrdinate(1) + 0.5 * EQUATOR_IN_METERS) * Math.pow(2.0, zoomLevel)
				/ (EQUATOR_IN_METERS);
		return new Coordinate(xIndex, yIndex);
	}

	public RasterLayerInfo getLayerInfo() {
		return layerInfo;
	}

	public String getTileUrl(int x, int y, int zoom) {
		if ((zoom < 0) || (zoom > 17)) {
			return null;
		}
		x = roundDownToMultiple(x, TILE_SIZE) / TILE_SIZE;
		y = roundDownToMultiple(y, TILE_SIZE) / TILE_SIZE;
		int d = getPowerOfTwo(zoom);
		if ((y < 0) || (y >= d)) {
			return null;
		}
		x = x % d;
		if (x < 0) {
			x += d;
		}
		String e = "mt" + ((x + y) % 4) + ".google.com";
		return "http://" + e + "/mt?v=w2.95&x=" + x + "&y=" + y + "&z=" + zoom;
	}

	public String getSatelliteTileUrl(int x, int y, int zoom) {
		if ((zoom < 0) || (zoom > 19)) {
			return null;
		}
		x /= TILE_SIZE;
		y /= TILE_SIZE;
		int d = getPowerOfTwo(zoom);
		if ((y < 0) || (y >= d)) {
			return null;
		}
		x = x % d;
		if (x < 0) {
			x += d;
		}
		String e = "kh" + ((x + y) % 4) + ".google.com";
		String f = "t";
		for (int g = 0; g < zoom; g++) {
			d /= 2;
			if (y < d) {
				if (x < d) {
					f += "q";
				} else {
					f += "r";
					x -= d;
				}
			} else {
				y -= d;
				if (x < d) {
					f += "t";
				} else {
					f += "s";
					x -= d;
				}
			}
		}
		return "http://" + e + "/kh?n=404&v=20&t=" + f;
	}

	/**
	 * Returns the largest integer smaller or equal to the specified one which is a multiple of the specified factor.
	 *
	 * @param value value to round down
	 * @param factor factor which result needs to be a multiple of
	 * @return largest multiple of factor <= value
	 */
	public int roundDownToMultiple(int value, int factor) {
		int b = Math.abs(value) % factor;
		if (b == 0) {
			return value;
		} else if (value >= 0) {
			return value - b;
		} else {
			return value - (factor - b);
		}
	}

	/**
	 * Returns 2^n, for values of n between 0 and 31.
	 *
	 * @param n requested power
	 * @return 2^n
	 */
	public int getPowerOfTwo(int n) {
		return POWERS_OF_TWO[n];
	}

	/**
	 * Single resolution in a Google layer.
	 *
	 * @author Jan De Moerloose
	 */
	private class GoogleResolution {

		private double resolution;

		private int zoomLevel;

		public GoogleResolution(double resolution, int zoomLevel) {
			this.resolution = resolution;
			this.zoomLevel = zoomLevel;
		}

		public int getZoomLevel() {
			return zoomLevel;
		}

		public double getResolution() {
			return resolution;
		}

	}

}
