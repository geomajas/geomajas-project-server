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
package org.geomajas.test.client.util;

import org.geomajas.geometry.Coordinate;

/**
 * Converts coordinates and zoom levels for Google CRS.
 * 
 * @author Jan De Moerloose
 * 
 */
public final class GeoUtil {

	private static final double MERCATOR_WIDTH = Math.PI * 6378137.0;

	private GeoUtil() {
	}

	/**
	 * Converts Google coordinate to latlon coordinate.
	 * 
	 * @param coordinate
	 *            Google coordinate unit = meter
	 * @return latlon coordinate
	 */
	public static Coordinate convertToLatLon(Coordinate coordinate) {
		double lat = (coordinate.getY() / MERCATOR_WIDTH) * 180.0;
		double lon = (coordinate.getX() / MERCATOR_WIDTH) * 180.0;
		lat = 180 / Math.PI * (2 * Math.atan(Math.exp(lat * Math.PI / 180.0)) - Math.PI / 2.0);
		return new Coordinate(lat, lon);
	}

	/**
	 * Converts latlon coordinate to Google coordinate.
	 * 
	 * @param coordinate
	 *            latitude first, longitude last, unit = degree
	 * @return google coordinate
	 */
	public static Coordinate convertToGoogle(Coordinate coordinate) {
		double x = (coordinate.getY() / 180.0) * MERCATOR_WIDTH;
		double y = MERCATOR_WIDTH / Math.PI * Math.log(Math.tan((90.0 + coordinate.getX()) * Math.PI / 360.0));
		return new Coordinate(x, y);
	}

	public static double getScaleForZoomLevel(int zoomLevel) {
		return 256 * Math.pow(2.0, zoomLevel) / (2 * MERCATOR_WIDTH);
	}
}
