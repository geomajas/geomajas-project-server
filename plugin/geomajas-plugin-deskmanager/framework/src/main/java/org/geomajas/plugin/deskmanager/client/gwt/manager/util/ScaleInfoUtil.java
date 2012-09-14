/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.client.gwt.manager.util;

import org.geomajas.configuration.client.ScaleInfo;


/**
 * Helper class that allows changing of scaleInfo values without losing inconsistency.
 *
 * @author Oliver May
 *
 */
public final class ScaleInfoUtil {

	private ScaleInfoUtil() { }
	
	/**
	 * Update the scaleInfo with a new value for the denominator.
	 * 
	 * @param scaleInfo the scaleInfo to update
	 * @param newDenominator the new denominator, may not be zero!
	 */
	public static void changeDenominator(ScaleInfo scaleInfo, double newDenominator) {
		double mapUnitInPixels = scaleInfo.getDenominator() / scaleInfo.getNumerator() * scaleInfo.getPixelPerUnit();
		scaleInfo.setNumerator(1);
		scaleInfo.setDenominator(newDenominator);
		scaleInfo.setPixelPerUnit(scaleInfo.getNumerator() / scaleInfo.getDenominator() * mapUnitInPixels);
	}
	
	/**
	 * Update the scaleInfo with a new value for the pixelperunits.
	 * 
	 * @param scaleInfo the scaleInfo to update
	 * @param newPixelPerUnit the new pixel per unit, may not be zero!
	 */
	public static void changePixelPerUnit(ScaleInfo scaleInfo, double newPixelPerUnit) {
		double mapUnitInPixels = scaleInfo.getDenominator() / scaleInfo.getNumerator() * scaleInfo.getPixelPerUnit();
		scaleInfo.setNumerator(1);
		scaleInfo.setPixelPerUnit(newPixelPerUnit);
		scaleInfo.setDenominator(mapUnitInPixels / scaleInfo.getPixelPerUnit());

		
	}
	
	/**
	 * Generate a scaleinfo object with the given pixelPerunit and map unit in pixels.
	 * @param pixelPerUnit the scale pixels per unit
	 * @param mapUnitPixels the map pixels per unit
	 * @return the scaleinfo
	 */
	public static ScaleInfo createScaleInfo(double pixelPerUnit, double mapUnitPixels) {
		ScaleInfo info = new ScaleInfo(pixelPerUnit);
		info.setNumerator(1);
		info.setDenominator(mapUnitPixels / info.getPixelPerUnit());
		return info;
	}
	
	
	/**
	 * Generate a scaleinfo object with the given numenator and denominator and map unit in pixels.
	 * @param numerator the scale numerator
	 * @param denominator the scale denominator
	 * @param mapUnitPixels the map pixels per unit
	 * @return the scaleinfo
	 */
	public static ScaleInfo createScaleInfo(double numerator, double denominator, double mapUnitPixels) {
		ScaleInfo info = new ScaleInfo(numerator, denominator);
		info.setPixelPerUnit(info.getNumerator() / info.getDenominator() * mapUnitPixels);
		return info;
	}
}
