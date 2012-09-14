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

import java.util.HashMap;
import java.util.Map;

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.plugin.deskmanager.domain.dto.BlueprintDto;

/**
 * Utility class that provides easy access to blueprint components.
 * 
 * @author Oliver May
 * 
 */
public final class BlueprintDtoUtil {

	private BlueprintDtoUtil() {}
	
	/**
	 * Retrieve the list of ClientWidgetInfo's from the blueprint's and userApplication's main map.
	 * If clientWidgetInfo's are defined on both blueprint and userApplication level, that defined in the blueprint is
	 * used.  
	 * 
	 * @param bp the blueprint
	 * @return List of ClientWidgetInfo as defined in the blueprint
	 */
	public static Map<String, ClientWidgetInfo> getMainMapClientWidgetInfo(BlueprintDto bp) {
		Map<String, ClientWidgetInfo> widgetInfos = new HashMap<String, ClientWidgetInfo>();
		widgetInfos.putAll(UserApplicationDtoUtil.getMainMapClientWidgetInfo(bp.getUserApplicationInfo()));
		widgetInfos.putAll(bp.getMainMapClientWidgetInfos());
		return widgetInfos;
	}

	/**
	 * Retrieve the list of ClientWidgetInfo's from the blueprint's overview map.
	 * If clientWidgetInfo's are defined on both blueprint and userApplication level, that defined in the blueprint is
	 * used.  
	 * 
	 * @param bp the blueprint
	 * @return List of ClientWidgetInfo as defined in the blueprint
	 */
	public static Map<String, ClientWidgetInfo> getOverviewMapClientWidgetInfo(BlueprintDto bp) {
		Map<String, ClientWidgetInfo> widgetInfos = new HashMap<String, ClientWidgetInfo>();
		widgetInfos.putAll(UserApplicationDtoUtil.getOverviewMapClientWidgetInfo(bp.getUserApplicationInfo()));
		widgetInfos.putAll(bp.getOverviewMapClientWidgetInfos());
		return widgetInfos;
	}

	/**
	 * Retrieve the list of ClientWidgetInfo's from the blueprint's applicationinfo.
	 * If clientWidgetInfo's are defined on both blueprint and userApplication level, that defined in the blueprint is
	 * used.  
	 * 
	 * @param bp the blueprint
	 * @return List of ClientWidgetInfo as defined in the blueprint
	 */
	public static Map<String, ClientWidgetInfo> getApplicationClientWidgetInfo(BlueprintDto bp) {
		Map<String, ClientWidgetInfo> widgetInfos = new HashMap<String, ClientWidgetInfo>();
		widgetInfos.putAll(UserApplicationDtoUtil.getApplicationClientWidgetInfo(bp.getUserApplicationInfo()));
		widgetInfos.putAll(bp.getApplicationClientWidgetInfos());
		return widgetInfos;
	}

	/**
	 * Retrieve the mapinfo from the blueprint's main map.
	 * 
	 * @param bp the blueprint
	 * @return the mapInfo
	 */
	public static ClientMapInfo getMainMap(BlueprintDto bp) {
		return UserApplicationDtoUtil.getMainMap(bp.getUserApplicationInfo());
	}
	
}
