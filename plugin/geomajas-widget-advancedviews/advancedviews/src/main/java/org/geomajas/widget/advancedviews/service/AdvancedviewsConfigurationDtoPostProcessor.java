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
import org.geomajas.internal.configuration.ConfigurationDtoPostProcessor;
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

	@Autowired
	protected ConfigurationDtoPostProcessor geomajasPostProcessor;
	
	@Autowired(required = false)
	protected Map<String, ClientMapInfo> mapInfos;

	public AdvancedviewsConfigurationDtoPostProcessor() {

	}

	@PostConstruct
	protected void processConfiguration() {
		System.out.println("processConfiguration");
		if (mapInfos != null) {
			for (ClientMapInfo map : mapInfos.values()) {
				double pixPerUnit = map.getUnitLength() / map.getPixelLength();
				ThemesInfo info = (ThemesInfo) map.getWidgetInfo(ThemesInfo.IDENTIFIER);
				if (info != null) {
					for (ViewConfig view : info.getThemeConfigs()) {
						for (RangeConfig range : view.getRangeConfigs()) {
							geomajasPostProcessor.completeScale(range.getMaximumScale(), pixPerUnit);
							geomajasPostProcessor.completeScale(range.getMinimumScale(), pixPerUnit);
						}
					}
				}
			}
		}
	}
}
