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
package org.geomajas.configuration.client;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.IsInfo;

import javax.annotation.PostConstruct;

/**
 * Represents a single scale. This class is needed to support 1:x notation because annotation-based formatters only
 * support fields (no lists) and custom bean editors only support classes.
 * 
 * @author Jan De Moerloose
 * @since 1.7.0
 */
@Api(allMethods = true)
public class ScaleInfo implements IsInfo {

	private static final long serialVersionUID = 170L;

	private double pixelPerUnit;

	private double numerator;

	private double denominator;
	
	private boolean pixelPerUnitBased;

	private double conversionFactor = PIXEL_PER_METER;

	/**
	 * Default conversion factor between map unit and pixel size (based on meter and 96 DPI).
	 * @since 1.11.1
	 */
	public static final double PIXEL_PER_METER = 96 / 0.0254;

	/**
	 * A minimum pixelPerUnit value which is suitable for all purposes but avoids zero and is in float range.
	 */
	public static final double MINIMUM_PIXEL_PER_UNIT = 1e-25;

	/**
	 * A maximum pixelPerUnit value which is suitable for all purposes but avoids infinity and is in float range.
	 */
	public static final double MAXIMUM_PIXEL_PER_UNIT = 1e25;

	/**
	 * Default constructor for GWT serialization.
	 */
	public ScaleInfo() {
		this(MINIMUM_PIXEL_PER_UNIT);
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
		if (pixelPerUnit < MINIMUM_PIXEL_PER_UNIT) {
			pixelPerUnit = MINIMUM_PIXEL_PER_UNIT;
		}
		if (pixelPerUnit > MAXIMUM_PIXEL_PER_UNIT) {
			pixelPerUnit = MAXIMUM_PIXEL_PER_UNIT;
		}
		setPixelPerUnit(pixelPerUnit);
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
		setNumerator(numerator);
		setDenominator(denominator);
	}
	
	/**
	 * Copy constructor. Creates a deep copy of the specified {@link ScaleInfo} object.
	 * 
	 * @param other the scale info to copy
	 * @since 1.11.0
	 */
	public ScaleInfo(ScaleInfo other) {
		setDenominator(other.getDenominator());
		setNumerator(other.getNumerator());
		setPixelPerUnit(other.getPixelPerUnit());
		// must copy all state
		setPixelPerUnitBased(other.isPixelPerUnitBased());
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
		if (pixelPerUnit < MINIMUM_PIXEL_PER_UNIT) {
			pixelPerUnit = MINIMUM_PIXEL_PER_UNIT;
		}
		if (pixelPerUnit > MAXIMUM_PIXEL_PER_UNIT) {
			pixelPerUnit = MAXIMUM_PIXEL_PER_UNIT;
		}
		this.pixelPerUnit = pixelPerUnit;
		setPixelPerUnitBased(true);
		postConstruct();
	}
	
	/**
	 * Returns whether this scale is based on pixel per unit. If true, recalculations should use {@link #pixelPerUnit}
	 * instead of nominator/denominator.
	 * 
	 * @return true if in pixel per unit, false otherwise
	 * @since 1.11.1
	 */
	public boolean isPixelPerUnitBased() {
		return pixelPerUnitBased;
	}

	/**
	 * Set this scale to be based on pixel per unit. If true, recalculations should use {@link #pixelPerUnit} instead of
	 * nominator/denominator.
	 * 
	 * @param pixelPerUnitBased true if based on {@link #pixelPerUnit}
	 * @since 1.11.1
	 */
	public void setPixelPerUnitBased(boolean pixelPerUnitBased) {
		this.pixelPerUnitBased = pixelPerUnitBased;
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
		setPixelPerUnitBased(false);
		if (denominator != 0) {
			postConstruct();
		}
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
		setPixelPerUnitBased(false);
		postConstruct();
	}

	/**
	 * Get the conversion factor between map units and pixel size.
	 *
	 * @return the conversion factor
	 * @since 1.14.0
	 */
	public double getConversionFactor() {
		return conversionFactor;
	}

	/**
	 * Set the conversion factor betweeen map units and pixel size. Defaults to {@link PIXEL_PER_METER}.
	 *
	 * @param conversionFactor the conversion factor
	 * @since 1.14.0
	 * @deprecated fixed to default (96 DPI) to avoid conflict with {@link ClientApplicationInfo#getScreenDpi()}
	 */
	@Deprecated
	public void setConversionFactor(double conversionFactor) {
		this.conversionFactor = conversionFactor;
	}

	/** Finish configuration. */
	@PostConstruct
	protected void postConstruct() {
		if (pixelPerUnitBased) {
			//	Calculate numerator and denominator
			if (pixelPerUnit > PIXEL_PER_METER) {
				this.numerator = pixelPerUnit / conversionFactor;
				this.denominator = 1;
			} else {
				this.numerator = 1;
				this.denominator = PIXEL_PER_METER / pixelPerUnit;
			}
			setPixelPerUnitBased(false);
		} else {
			// Calculate PPU
			this.pixelPerUnit = numerator / denominator * conversionFactor;
			setPixelPerUnitBased(true);
		}
	}
}
