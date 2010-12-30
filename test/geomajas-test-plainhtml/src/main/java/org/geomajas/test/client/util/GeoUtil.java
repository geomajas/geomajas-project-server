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
		double y = (coordinate.getX() / 180.0) * Math.PI;
		y = MERCATOR_WIDTH / Math.PI * Math.log(Math.tan((90.0 + coordinate.getX()) * Math.PI / 360.0));
		return new Coordinate(x, y);
	}

	public static double getScaleForZoomLevel(int zoomLevel) {
		return 256 * Math.pow(2.0, zoomLevel) / (2 * MERCATOR_WIDTH);
	}
}
