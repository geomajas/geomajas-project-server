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
import org.geomajas.plugin.deskmanager.domain.dto.GeodeskDto;

/**
 * Utility class that provides easy access to geodesk components.
 * 
 * @author Oliver May
 * 
 */
public final class GeodeskDtoUtil {

	private GeodeskDtoUtil() {}
	
	/**
	 * Retrieve the list of ClientWidgetInfo's from the blueprint's and geodesks main map.
	 * If clientWidgetInfo's are defined on both blueprint and geodesk level, that defined in the geodesk is
	 * used.  
	 * 
	 * @param geodesk the blueprint
	 * @return List of ClientWidgetInfo as defined in the blueprint
	 */
	public static Map<String, ClientWidgetInfo> getMainMapClientWidgetInfo(GeodeskDto geodesk) {
		Map<String, ClientWidgetInfo> widgetInfos = new HashMap<String, ClientWidgetInfo>();
		widgetInfos.putAll(BlueprintDtoUtil.getMainMapClientWidgetInfo(geodesk.getBlueprint()));
		widgetInfos.putAll(geodesk.getMainMapClientWidgetInfos());
		return widgetInfos;
	}

	/**
	 * Retrieve the list of ClientWidgetInfo's from the blueprint's and geodesks overview map.
	 * If clientWidgetInfo's are defined on both blueprint and geodesk level, that defined in the geodesk is
	 * used.  
	 * 
	 * @param geodesk the blueprint
	 * @return List of ClientWidgetInfo as defined in the blueprint
	 */
	public static Map<String, ClientWidgetInfo> getOverviewMapClientWidgetInfo(GeodeskDto geodesk) {
		Map<String, ClientWidgetInfo> widgetInfos = new HashMap<String, ClientWidgetInfo>();
		widgetInfos.putAll(BlueprintDtoUtil.getOverviewMapClientWidgetInfo(geodesk.getBlueprint()));
		widgetInfos.putAll(geodesk.getOverviewMapClientWidgetInfos());
		return widgetInfos;
	}

	/**
	 * Retrieve the list of ClientWidgetInfo's from the blueprint's and geodesks application info.
	 * If clientWidgetInfo's are defined on both blueprint and geodesk level, that defined in the geodesk is
	 * used.  
	 * 
	 * @param geodesk the blueprint
	 * @return List of ClientWidgetInfo as defined in the blueprint
	 */
	public static Map<String, ClientWidgetInfo> getApplicationClientWidgetInfo(GeodeskDto geodesk) {
		Map<String, ClientWidgetInfo> widgetInfos = new HashMap<String, ClientWidgetInfo>();
		widgetInfos.putAll(BlueprintDtoUtil.getApplicationClientWidgetInfo(geodesk.getBlueprint()));
		widgetInfos.putAll(geodesk.getApplicationClientWidgetInfos());
		return widgetInfos;
	}

	/**
	 * Retrieve the mapinfo from the geodesk's main map.
	 * 
	 * @param geodesk the geodesk
	 * @return the mapInfo
	 */
	public static ClientMapInfo getMainMap(GeodeskDto geodesk) {
		return BlueprintDtoUtil.getMainMap(geodesk.getBlueprint());
	}
}
