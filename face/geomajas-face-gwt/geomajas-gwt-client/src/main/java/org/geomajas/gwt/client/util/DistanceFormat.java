package org.geomajas.gwt.client.util;

import org.geomajas.configuration.client.UnitType;
import org.geomajas.gwt.client.widget.MapWidget;

import com.google.gwt.i18n.client.NumberFormat;

/**
 * General formatter for distances.
 * 
 * @author Pieter De Graef
 */
public final class DistanceFormat {

	private static final double METERS_IN_MILE = 1609.344d;

	private static final double METERS_IN_YARD = 0.9144d;

	private DistanceFormat() {
		// Private default constructor. This is a utility class after all!
	}

	/**
	 * Main formatting method. Requires a length as parameter, expressed in the CRS units of the given map.
	 * 
	 * @param map
	 *            The map for which a distance should be formatted. This map may be configured to use the metric system
	 *            or the English system.
	 * @param length
	 *            The original length, expressed in the coordinate reference system of the given map.
	 * @return Returns a string that is the formatted distance of the given length. Preference goes to meters or yards
	 *         (depending on the configured unit type), but when the number is larger than 10000, it will switch
	 *         automatically to meter/mile.
	 */
	public static String asMapLength(MapWidget map, double length) {
		double unitLength = map.getUnitLength();
		double distance = length * unitLength;

		String unit = "m";
		if (map.getMapModel().getMapInfo().getDisplayUnitType() == UnitType.METRIC) {
			// Right now, the distance is expressed in meter. Switch to km?
			if (distance > 10000) {
				distance /= 1000;
				unit = "km";
			}
		} else if (map.getMapModel().getMapInfo().getDisplayUnitType() == UnitType.ENGLISH) {
			if (distance / METERS_IN_YARD > 10000) {
				// More than 10000 yard; switch to mile:
				distance = distance / METERS_IN_MILE;
				unit = "mi";
			} else {
				distance /= METERS_IN_YARD; // New expressed in yards.
				unit = "yd";
			}
		} else if (map.getMapModel().getMapInfo().getDisplayUnitType() == UnitType.CRS) {
			unit = "u";
		}

		String formatted = NumberFormat.getDecimalFormat().format(distance);
		return formatted + unit;
	}
}
