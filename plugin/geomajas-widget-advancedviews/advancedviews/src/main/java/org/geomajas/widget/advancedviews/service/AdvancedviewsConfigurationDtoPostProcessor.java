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
package org.geomajas.widget.advancedviews.service;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.widget.advancedviews.configuration.client.ThemesInfo;
import org.geomajas.widget.advancedviews.configuration.client.themes.RangeConfig;
import org.geomajas.widget.advancedviews.configuration.client.themes.ViewConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Post-processes configuration DTOs.
 * 
 * @author Oliver May
 * 
 */
@Component
public class AdvancedviewsConfigurationDtoPostProcessor {

	@Autowired(required = false)
	protected Map<String, ClientMapInfo> mapInfos;

	public AdvancedviewsConfigurationDtoPostProcessor() {

	}

	@PostConstruct
	protected void processConfiguration() {
		if (mapInfos != null) {
			for (ClientMapInfo map : mapInfos.values()) {
				double pixPerUnit = map.getUnitLength() / map.getPixelLength();
				ThemesInfo info = (ThemesInfo) map.getWidgetInfo(ThemesInfo.IDENTIFIER);
				if (info != null) {
					for (ViewConfig view : info.getThemeConfigs()) {
						for (RangeConfig range : view.getRangeConfigs()) {
							completeScale(range.getMaximumScale(), pixPerUnit);
							completeScale(range.getMinimumScale(), pixPerUnit);
						}
					}
				}
			}
		}
	}

	/**
	 * Convert the scale in pixels per unit or relative values, which ever is missing.
	 * 
	 * @param scaleInfo scaleInfo object which needs to be completed
	 * @param mapUnitInPixels the number of pixels in a map unit
	 */
	public void completeScale(ScaleInfo scaleInfo, double mapUnitInPixels) {
		if (0 == mapUnitInPixels) {
			throw new IllegalArgumentException("ScaleInfo.completeScale mapUnitInPixels should never be zero.");
		}
		double denominator = scaleInfo.getDenominator();
		double numerator = scaleInfo.getNumerator();
		if (denominator != 0) {
			scaleInfo.setPixelPerUnit(numerator / denominator * mapUnitInPixels);
		} else {
			double pixelPerUnit = scaleInfo.getPixelPerUnit();
			if (pixelPerUnit > mapUnitInPixels) {
				scaleInfo.setNumerator(pixelPerUnit / mapUnitInPixels);
				scaleInfo.setDenominator(1);
			} else {
				scaleInfo.setNumerator(1);
				scaleInfo.setDenominator(mapUnitInPixels / pixelPerUnit);
			}
		}
	}

}
