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

package org.geomajas.smartgwt.client.util;

import org.geomajas.configuration.client.UnitType;
import org.geomajas.smartgwt.client.widget.MapWidget;

import com.google.gwt.i18n.client.NumberFormat;

/**
 * General formatter for distances and areas.
 * 
 * @author Pieter De Graef
 */
public final class DistanceFormat {

	private static final double METERS_IN_MILE = 1609.344d;

	private static final double METERS_IN_YARD = 0.9144d;

	private static final double FEET_IN_METER = 3.2808399d;

	private static final double FEET_IN_MILE = 5280d;

	private static final double METERS_IN_KM = 1000;

	private DistanceFormat() {
		// Private default constructor. This is a utility class after all!
	}

	/**
	 * Distance formatting method. Requires a length as parameter, expressed in the CRS units of the given map.
	 * 
	 * @param map
	 *            The map for which a distance should be formatted. This map may be configured to use the metric system
	 *            or the English system.
	 * @param length
	 *            The original length, expressed in the coordinate reference system of the given map.
	 * @return Returns a string that is the formatted distance of the given length. Preference goes to meters or yards
	 *         (depending on the configured unit type), but when the number is larger than 10000, it will switch
	 *         automatically to kilometer/mile.
	 */
	public static String asMapLength(MapWidget map, double length) {
		double unitLength = map.getUnitLength();
		double distance = length * unitLength;

		String unit = "m";
		if (map.getMapModel().getMapInfo().getDisplayUnitType() == UnitType.METRIC) {
			// Right now, the distance is expressed in meter. Switch to km?
			if (distance > 10000) {
				distance /= METERS_IN_KM;
				unit = "km";
			}
		} else if (map.getMapModel().getMapInfo().getDisplayUnitType() == UnitType.ENGLISH) {
			if (distance / METERS_IN_YARD > 10000) {
				// More than 10000 yard; switch to mile:
				distance = distance / METERS_IN_MILE;
				unit = "mi";
			} else {
				distance /= METERS_IN_YARD; // use yards.
				unit = "yd";
			}
		} else if (map.getMapModel().getMapInfo().getDisplayUnitType() == UnitType.ENGLISH_FOOT) {
			if (distance * FEET_IN_METER > FEET_IN_MILE) {
				// More than 1 mile (5280 feet); switch to mile:
				distance = distance / METERS_IN_MILE;
				unit = "mi";
			} else {
				distance *= FEET_IN_METER; // use feet.
				unit = "ft";
			}
		}  else if (map.getMapModel().getMapInfo().getDisplayUnitType() == UnitType.CRS) {
			unit = "u";
		}

		String formatted = NumberFormat.getDecimalFormat().format(distance);
		return formatted + unit;
	}

	/**
	 * Area formatting method. Requires an area as parameter, expressed in the CRS units of the given map.
	 * 
	 * @param map
	 *            The map for which an area should be formatted. This map may be configured to use the metric system or
	 *            the English system.
	 * @param area
	 *            The original area, expressed in the coordinate reference system of the given map.
	 * @return Returns a string that is the formatted area of the given area. Preference goes to meters or yards
	 *            (depending on the configured unit type), but when the number is larger than meters (or yards), it will
	 *            switch automatically to kilometers/miles.
	 */
	public static String asMapArea(MapWidget map, double area) {
		double unitLength = map.getUnitLength();
		double distance = area * unitLength * unitLength;

		String unit = "m";
		if (map.getMapModel().getMapInfo().getDisplayUnitType() == UnitType.METRIC) {
			// Right now, the distance is expressed in meter. Switch to km?
			if (distance > (METERS_IN_KM * METERS_IN_KM)) {
				distance /= (METERS_IN_KM * METERS_IN_KM);
				unit = "km";
			}
		} else if (map.getMapModel().getMapInfo().getDisplayUnitType() == UnitType.ENGLISH) {
			if (distance > (METERS_IN_MILE * METERS_IN_MILE)) {
				// Switch to mile:
				distance = distance / (METERS_IN_MILE * METERS_IN_MILE);
				unit = "mi";
			} else {
				distance /= (METERS_IN_YARD * METERS_IN_YARD); // use yards.
				unit = "yd";
			}
		} else if (map.getMapModel().getMapInfo().getDisplayUnitType() == UnitType.CRS) {
			unit = "u";
		}

		String formatted = NumberFormat.getDecimalFormat().format(distance);
		return formatted + unit + "&sup2;";
	}
}
