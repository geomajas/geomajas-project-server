/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld.editor.common.client.view;

/**
 * View independent base class for utility functions.
 * 
 * @author Jan De Moerloose
 * 
 */
public abstract class BaseViewUtil implements ViewUtil {

	public String numericalToString(Object value, String defaultStringValue) {
		String stringValue = defaultStringValue;

		if (value != null) {
			stringValue = value.toString();
		}
		return stringValue;
	}

	public float numericalToFloat(Object value, float defaultValue) {
		float floatValue = defaultValue;

		if (null != value) {
			if (value.getClass().equals(Float.class)) {
				floatValue = ((Float) value).floatValue();
			} else if (value.getClass().equals(Integer.class)) {
				Integer intValue = (Integer) value;
				floatValue = (float) intValue;
			}
		}

		return floatValue;
	}

	public int factorToPercentage(String value) {
		float factor = Float.parseFloat(value);

		return (int) Math.round(factor * 100.0);
	}

	public String percentageToFactor(int percentage) {
		if (percentage > 100) {
			percentage = 100;
		} else if (percentage < 0) {
			percentage = 0;
		}
		// Note alternative using String.format() cannot be compiled by GWT
		if (percentage == 100) {
			return "1.0";
		} else if (percentage >= 10) { /* 10 to 99 -> 0.10 to 0.99 */
			return "0." + Integer.toString(percentage);
		} else { /* 0 to 9 -> 0.00 to 0.09 */
			return "0.0" + Integer.toString(percentage);
		}
	}

	public String percentageToFactor(float percentage) {
		if (percentage > 100.0) {
			percentage = 100.0F;
		} else if (percentage < 0.0) {
			percentage = 0.0F;
		}
		// Cannot be compiled by GWT: Float factor = new Float((float)percentage / 100.0F);
		// String format = "%.3f"; // decimal fraction notation with fractional part of 3 digits
		//
		// return String.format(format, factor);
		int percentageRounded = Math.round(percentage);
		if (percentageRounded == 100) {
			return "1.0";
		} else if (percentageRounded >= 10) { /* 10 to 99 -> 0.10 to 0.99 */
			return "0." + Integer.toString(percentageRounded);
		} else { /* 0 to 9 -> 0.00 to 0.09 */
			return "0.0" + Integer.toString(percentageRounded);
		}
	}

	public int numericalToInteger(String numerical) {
		if (null == numerical) {
			return 0;
		}
		try {
			double d = Double.parseDouble(numerical);
			return (int) d;
		} catch (NumberFormatException e) {
			return 0;
		}
	}

}
