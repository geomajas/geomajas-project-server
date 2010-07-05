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
package org.geomajas.configuration.client;

import org.geomajas.global.Api;

import java.io.Serializable;

/**
 * Represents a single scale. This class is needed to support 1:x notation because annotation-based formatters only
 * support fields (no lists) and custom bean editors only support classes.
 * 
 * @author Jan De Moerloose
 * @since 1.7.0
 * @see org.geomajas.internal.configuration.ScaleInfoEditor
 */
@Api(allMethods = true)
public class ScaleInfo implements Serializable {

	private static final long serialVersionUID = 170L;

	private double pixelPerUnit;

	private double numerator;

	private double denominator;

	/**
	 * A maximum scale which is suitable for all purposes but avoids infinity.
	 */
	public static final ScaleInfo MAX_VALUE = new ScaleInfo(10E50);

	/**
	 * The minimum scale which is suitable for all purposes but avoids 0 in calculations.
	 */
	public static final ScaleInfo MIN_VALUE = new ScaleInfo(10E-50);

	/**
	 * Default constructor for GWT serialization.
	 */
	public ScaleInfo() {
	}

	/**
	 * Constructs a scale info object with the specified scale value. The scale value should be interpreted as
	 * pixels/map unit. This is especially useful for raster images for which the image resolution indirectly determines
	 * the optimal view scale of the image.
	 * 
	 * @param pixelPerUnit
	 *            the scale value in pixel per map unit)
	 */
	public ScaleInfo(double pixelPerUnit) {
		this.pixelPerUnit = pixelPerUnit;
	}

	/**
	 * Constructs a scale info object with the specified numerator and denominator values. The scale should be
	 * interpreted as the more familiar pure number scale, i.e. 1 meter on the screen divided by 1 meter on the map.
	 * 
	 * @param numerator
	 *            typically value is 1 or > 1 for large scales
	 * @param denominator
	 *            typically values are 100, 1000, ... or 1 for large scales
	 */
	public ScaleInfo(double numerator, double denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
		if (denominator <= 0) {
			throw new IllegalArgumentException("Scale denominator must be positive");
		}
	}

	/**
	 * Returns the scale value in pixel per map unit.
	 * 
	 * @return the scale value (pix/map unit)
	 */
	public double getPixelPerUnit() {
		return pixelPerUnit;
	}

	/**
	 * Sets the scale value in pixel per map unit.
	 * 
	 * @param pixelPerUnit
	 *            the scale value (pix/map unit)
	 */
	public void setPixelPerUnit(double pixelPerUnit) {
		this.pixelPerUnit = pixelPerUnit;
	}

	/**
	 * Returns the numerator for a relative scale.
	 * 
	 * @return the scale numerator
	 */
	public double getNumerator() {
		return numerator;
	}

	/**
	 * Sets the numerator for a relative scale.
	 * 
	 * @param numerator
	 *            the numerator for a relative scale
	 */
	public void setNumerator(double numerator) {
		this.numerator = numerator;
	}

	/**
	 * Returns the denominator for a relative scale.
	 * 
	 * @return the scale denominator
	 */
	public double getDenominator() {
		return denominator;
	}

	/**
	 * Sets the denominator for a relative scale.
	 * 
	 * @param denominator
	 *            the denominator for a relative scale
	 */
	public void setDenominator(double denominator) {
		this.denominator = denominator;
	}

	/**
	 * Convert the scale in pixels per unit or relative values, which ever is missing.
	 * 
	 * @param mapUnitInPixels
	 *            the number of pixels in a map unit
	 */
	public void convertScale(double mapUnitInPixels) {
		if (denominator != 0) {
			pixelPerUnit = numerator / denominator * mapUnitInPixels;
		} else {
			if (pixelPerUnit / mapUnitInPixels > 1) {
				numerator = pixelPerUnit / mapUnitInPixels;
				denominator = 1.;
			} else {
				numerator = 1.;
				denominator = mapUnitInPixels / pixelPerUnit;
			}
		}

	}

}
