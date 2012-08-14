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

import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.global.ConfigurationDtoPostProcess;
import org.geomajas.global.ConfigurationHelper;
import org.geomajas.widget.advancedviews.configuration.client.ThemesInfo;
import org.geomajas.widget.advancedviews.configuration.client.themes.RangeConfig;
import org.geomajas.widget.advancedviews.configuration.client.themes.ViewConfig;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Post-processes configuration DTOs.
 * 
 * @author Oliver May
 * 
 */
public class AdvancedviewsConfigurationDtoPostProcessor implements ConfigurationDtoPostProcess {

	@Autowired(required = false)
	protected Map<String, ClientApplicationInfo> applicationMap;
	
	@Override
	public void processConfiguration(ConfigurationHelper configurationHelper) {
		if (applicationMap != null) {
			for (ClientApplicationInfo applicationInfo : applicationMap.values()) {
				for (ClientMapInfo map : applicationInfo.getMaps()) {
					double pixPerUnit = map.getUnitLength() / map.getPixelLength();
					ThemesInfo info = (ThemesInfo) map.getWidgetInfo(ThemesInfo.IDENTIFIER);
					if (info != null) {
						for (ViewConfig view : info.getThemeConfigs()) {
							for (RangeConfig range : view.getRangeConfigs()) {
								configurationHelper.completeScale(range.getMaximumScale(), pixPerUnit);
								configurationHelper.completeScale(range.getMinimumScale(), pixPerUnit);
							}
						}
					}
				}
			}
		}
	}

}
