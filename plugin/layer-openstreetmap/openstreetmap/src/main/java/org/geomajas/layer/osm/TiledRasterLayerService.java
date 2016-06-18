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

package org.geomajas.layer.osm;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.CrsTransform;
import org.geomajas.annotation.Api;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.service.GeoService;
import org.geotools.geometry.jts.JTS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility service which helps to handle a tiled raster layer.
 *
 * @author Joachim Van der Auwera
 * @since 1.8.0
 */
@Api
@Component
public class TiledRasterLayerService {

	private final Logger log = LoggerFactory.getLogger(TiledRasterLayerService.class);

	public static final String MERCATOR = "EPSG:900913";
	
	public static final double EQUATOR_IN_METERS = 40075016.686;
	public static final double HALF_EQUATOR_IN_METERS = 40075016.686 / 2;
	public static final double MAP_UNIT_PER_GOOGLE_METER_DEFAULT = 0.653;

	public static final int MAX_ZOOM_LEVEL = 31;

	private static final int[] POWERS_OF_TWO;

	@Autowired
	private GeoService geoService;

	static {
		POWERS_OF_TWO = new int[MAX_ZOOM_LEVEL + 1];
		int b = 1;
		for (int c = 0; c < POWERS_OF_TWO.length; c++) {
			POWERS_OF_TWO[c] = b;
			b *= 2;
		}
	}

	/**
	 * Paint raster layer.
	 *
	 * @param tileServiceState state (as this is a singleton)
	 * @param boundsCrs crs for bounds
	 * @param bounds bounds for which tiles are needed
	 * @param scale scale for rendering
	 * @return list of raster tiles
	 * @throws GeomajasException oops
	 * @since 1.8.0
	 */
	@Api
	public List<RasterTile> paint(TiledRasterLayerServiceState tileServiceState, CoordinateReferenceSystem boundsCrs,
			Envelope bounds, double scale) throws GeomajasException {
		try {
			CrsTransform layerToMap = geoService.getCrsTransform(tileServiceState.getCrs(), boundsCrs);
			CrsTransform mapToLayer = geoService.getCrsTransform(boundsCrs, tileServiceState.getCrs());

			bounds = clipBounds(tileServiceState, bounds);
			if (bounds.isNull()) {
				return new ArrayList<RasterTile>();
			}

			// find the center of the map in map coordinates (positive y-axis)
			Coordinate boundsCenter = new Coordinate((bounds.getMinX() + bounds.getMaxX()) / 2,
					(bounds.getMinY() + bounds.getMaxY()) / 2);

			// find zoomlevel
			// scale in pix/m should just above the given scale so we have at least one
			// screen pixel per google pixel ! (otherwise text unreadable)
			int zoomLevel = getBestZoomLevelForScaleInPixPerMeter(tileServiceState, mapToLayer, boundsCenter, scale);
			log.debug("zoomLevel={}", zoomLevel);

			// find the google indices for the center
			// google indices determine the row and column of the 256x256 image
			// in the big google square for the given zoom zoomLevel
			// the resulting indices are floating point values as the center
			// is not coincident with an image corner !!!!
			Coordinate indicesCenter;
			indicesCenter = getTileIndicesFromMap(mapToLayer, boundsCenter, zoomLevel);

			// Calculate the width in map units of the image that contains the
			// center
			Coordinate indicesUpperLeft = new Coordinate(Math.floor(indicesCenter.x), Math.floor(indicesCenter.y));
			Coordinate indicesLowerRight = new Coordinate(indicesUpperLeft.x + 1, indicesUpperLeft.y + 1);
			Coordinate mapUpperLeft = getMapFromTileIndices(layerToMap, indicesUpperLeft, zoomLevel);
			Coordinate mapLowerRight =
					getMapFromTileIndices(layerToMap, indicesLowerRight, zoomLevel);
			double width = Math.abs(mapLowerRight.x - mapUpperLeft.x);
			if (0 == width) {
				width = 1.0;
			}
			double height = Math.abs(mapLowerRight.y - mapUpperLeft.y);
			if (0 == height) {
				height = 1.0;
			}

			// Calculate the position and indices of the center image corner
			// in map space
			double xCenter = boundsCenter.x - (indicesCenter.x - indicesUpperLeft.x) * width;
			double yCenter = boundsCenter.y + (indicesCenter.y - indicesUpperLeft.y) * height;
			int iCenter = (int) indicesUpperLeft.x;
			int jCenter = (int) indicesUpperLeft.y;

			// Calculate the position and indices of the upper left image corner
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
			// Calculate the indices of the lower right corner
			// that just falls off the screen
			int levelMax = POWERS_OF_TWO[zoomLevel] - 1;
			double xMax = xCenter;
			int iMax = iCenter;
			while (xMax < bounds.getMaxX() && iMax <= levelMax) {
				xMax += width;
				iMax++;
			}
			double yMin = yCenter;
			int jMax = jCenter;
			while (yMin > bounds.getMinY() && jMax <= levelMax) {
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
					int y = yScreenUpperLeft - (j - jMin) * screenHeight;

					RasterTile image = new RasterTile(new Bbox(x, -y, screenWidth, screenHeight),
							tileServiceState.getId() + "." + zoomLevel + "." + i + "," + j);
					image.setCode(new TileCode(zoomLevel, i, j));
					String url = tileServiceState.getUrlSelectionStrategy().next();
					url = url.replace("${level}", Integer.toString(zoomLevel));
					url = url.replace("${x}", Integer.toString(i));
					url = url.replace("${y}", Integer.toString(j));
					image.setUrl(url);
					log.debug("adding image {}", image);
					result.add(image);
				}
			}
			return result;
		} catch (TransformException e) {
			throw new GeomajasException(e, ExceptionCode.RENDER_TRANSFORMATION_FAILED);
		}
	}

	private Envelope clipBounds(TiledRasterLayerServiceState tileServiceState, Envelope bounds) {
		return bounds.intersection(tileServiceState.getMaxBounds());
	}

	private int getBestZoomLevelForScaleInPixPerMeter(TiledRasterLayerServiceState tileServiceState,
			CrsTransform layerToGoogle, Coordinate mapPosition,
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
		double[] resolutions = tileServiceState.getResolutions();
		if (screenResolution >= resolutions[0]) {
			return 0;
		} else if (screenResolution <= resolutions[tileServiceState.getMaxZoomLevel()]) {
			return tileServiceState.getMaxZoomLevel();
		} else {
			for (int i = 0; i < tileServiceState.getMaxZoomLevel(); i++) {
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
		return MAX_ZOOM_LEVEL;
	}

	private Coordinate getMapFromTileIndices(CrsTransform mapToLayer, Coordinate indices, int zoomLevel)
			throws TransformException {
		double xMeter = EQUATOR_IN_METERS * indices.x / POWERS_OF_TWO[zoomLevel] - HALF_EQUATOR_IN_METERS;
		double yMeter = -EQUATOR_IN_METERS * indices.y / POWERS_OF_TWO[zoomLevel] + HALF_EQUATOR_IN_METERS;
		return JTS.transform(new Coordinate(xMeter, yMeter), new Coordinate(), mapToLayer);
	}

	private Coordinate getTileIndicesFromMap(CrsTransform mapToLayer, Coordinate centerMapCoor, int zoomLevel)
			throws TransformException {
		Coordinate centerLayerCoor = JTS.transform(centerMapCoor, new Coordinate(), mapToLayer);
		double xIndex = (centerLayerCoor.x + HALF_EQUATOR_IN_METERS) * POWERS_OF_TWO[zoomLevel] / EQUATOR_IN_METERS;
		double yIndex = (-centerLayerCoor.y + HALF_EQUATOR_IN_METERS) * POWERS_OF_TWO[zoomLevel] / EQUATOR_IN_METERS;
		return new Coordinate(xIndex, yIndex);
	}

}
