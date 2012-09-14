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
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.GeodeskLayout;
import org.geomajas.plugin.deskmanager.configuration.UserApplicationInfo;

/**
 * Utility class that provides easy access to userapplication components.
 * 
 * @author Oliver May
 * 
 */
public final class UserApplicationDtoUtil {

	private UserApplicationDtoUtil() {
	}

	/**
	 * Retrieve the list of ClientWidgetInfo's from the userApplication's main map.
	 * 
	 * @param userApplication
	 *            the userapplication
	 * @return List of ClientWidgetInfo as defined in the userapplication
	 */
	public static Map<String, ClientWidgetInfo> getMainMapClientWidgetInfo(UserApplicationInfo userApplication) {
		Map<String, ClientWidgetInfo> widgetInfos = new HashMap<String, ClientWidgetInfo>();
		for (ClientMapInfo mapInfo : userApplication.getApplicationInfo().getMaps()) {
			if (mapInfo.getId().equals(GeodeskLayout.MAPMAIN_ID)) {
				widgetInfos.putAll(mapInfo.getWidgetInfo());
			}
		}
		widgetInfos.putAll(userApplication.getMainMapWidgetInfos());
		return widgetInfos;
	}

	/**
	 * Retrieve the list of ClientWidgetInfo's from the userapplication's overview map. used.
	 * 
	 * @param userApplication
	 *            the userapplication
	 * @return List of ClientWidgetInfo as defined in the userapplication
	 */
	public static Map<String, ClientWidgetInfo> getOverviewMapClientWidgetInfo(UserApplicationInfo userApplication) {
		Map<String, ClientWidgetInfo> widgetInfos = new HashMap<String, ClientWidgetInfo>();
		for (ClientMapInfo mapInfo : userApplication.getApplicationInfo().getMaps()) {
			if (mapInfo.getId().equals(GeodeskLayout.MAPOVERVIEW_ID)) {
				widgetInfos.putAll(mapInfo.getWidgetInfo());
			}
		}
		widgetInfos.putAll(userApplication.getOverviewMapWidgetInfos());
		return widgetInfos;
	}

	/**
	 * Retrieve the list of ClientWidgetInfo's from the userapplication's applicationinfo. If clientWidgetInfo's are
	 * defined on both userapplication and userApplication level, that defined in the userapplication is used.
	 * 
	 * @param userApplication
	 *            the userapplication
	 * @return List of ClientWidgetInfo as defined in the userapplication
	 */
	public static Map<String, ClientWidgetInfo> getApplicationClientWidgetInfo(UserApplicationInfo userApplication) {
		Map<String, ClientWidgetInfo> widgetInfos = new HashMap<String, ClientWidgetInfo>();
		widgetInfos.putAll(userApplication.getApplicationInfo().getWidgetInfo());
		widgetInfos.putAll(userApplication.getApplicationWidgetInfos());
		return widgetInfos;
	}

	/**
	 * Retrieve the mapinfo from the userapplication's main map.
	 * 
	 * @param userApplication
	 *            the userapplication
	 * @return the mapInfo
	 */
	public static ClientMapInfo getMainMap(UserApplicationInfo userApplication) {
		for (ClientMapInfo mapInfo : userApplication.getApplicationInfo().getMaps()) {
			if (mapInfo.getId().equals(GeodeskLayout.MAPMAIN_ID)) {
				return mapInfo;
			}
		}
		return null;
	}

}
