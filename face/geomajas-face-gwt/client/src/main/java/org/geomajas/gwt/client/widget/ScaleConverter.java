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

package org.geomajas.gwt.client.widget;

import com.google.gwt.i18n.client.NumberFormat;

/**
 * Utility class for converting scales to string and the reverse.
 *
 * @author Joachim Van der Auwera
 */
public final class ScaleConverter {

	public static final String ERROR_SCALE = "Negative or zero scale not allowed, did you use a correct pixel length?";

	private ScaleConverter() {
		// do not allow instantiation
	}

	/**
	 * Convert a scale to a string representation.
	 *
	 * @param scale scale to convert
	 * @param precision precision for the scale, or 0
	 * @param significantDigits maximum number of significant digits
	 * @return string representation for the scale
	 */
	public static String scaleToString(double scale, int precision, int significantDigits) {
		NumberFormat numberFormat = NumberFormat.getFormat("###,###");
		if (scale > 0 && scale < 1.0) {
			int denominator = round((int) Math.round(1.0 / scale), precision, significantDigits);
			return "1 : " + numberFormat.format(denominator);
		} else if (scale >= 1.0) {
			int nominator = round((int) Math.round(scale), precision, significantDigits);
			return numberFormat.format(nominator) + " : 1";
		} else {
			return ERROR_SCALE;
		}
	}

	/**
	 * Parse scale from string representation.
	 *
	 * @param value to parse
	 * @return scale value
	 */
	public static Double stringToScale(String value) {
		NumberFormat numberFormat = NumberFormat.getFormat("###,###");
		String[] scale2 = value.split(":");
		if (scale2.length == 1) {
			return 1.0 / numberFormat.parse(scale2[0].trim());
		} else {
			return numberFormat.parse(scale2[0].trim()) / numberFormat.parse(scale2[1].trim());
		}
	}

	/**
	 * Round integer number by applying precision and maximum significant digits.
	 *
	 * @param value value to round
	 * @param precision precision
	 * @param significantDigits significant digits
	 * @return rounded value
	 */
	static int round(int value, int precision, int significantDigits) {
		if (precision > 0) {
			value = (value + precision / 2) / precision * precision;
		}
		if (significantDigits > 0) {
			int forRounding = value;
			int precisionMax = (int) Math.pow(10, significantDigits);
			int multiplier = 1;
			while (value > precisionMax) {
				forRounding = value;
				multiplier *= 10;
				value /= 10;
			}
			if (forRounding % 10 >= 5) {
				value++;
			}
			value *= multiplier;
		}
		return value;
	}
}
