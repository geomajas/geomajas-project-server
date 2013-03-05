/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.client.gwt.manager.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.plugin.deskmanager.client.gwt.common.GdmLayout;
import org.geomajas.plugin.deskmanager.configuration.UserApplicationInfo;
import org.geomajas.plugin.deskmanager.domain.dto.BaseGeodeskDto;
import org.geomajas.plugin.deskmanager.domain.dto.BlueprintDto;
import org.geomajas.plugin.deskmanager.domain.dto.GeodeskDto;
import org.geomajas.plugin.deskmanager.domain.dto.LayerDto;

/**
 * Utility class that provides easy access to geodesk components.
 * 
 * @author Oliver May
 * 
 */
public final class GeodeskDtoUtil {

	private GeodeskDtoUtil() {
	}

	/**
	 * Retrieve the list of ClientWidgetInfo's from the blueprint's and geodesks main map. If clientWidgetInfo's are
	 * defined on both blueprint and geodesk level, that defined in the geodesk is used.
	 * 
	 * @param geodesk
	 *            the blueprint
	 * @return List of ClientWidgetInfo as defined in the blueprint
	 */
	public static Map<String, ClientWidgetInfo> getMainMapClientWidgetInfo(BaseGeodeskDto geodesk) {
		Map<String, ClientWidgetInfo> widgetInfos = new HashMap<String, ClientWidgetInfo>();

		if (geodesk instanceof GeodeskDto) {
			widgetInfos.putAll(getMainMapClientWidgetInfo(((GeodeskDto) geodesk).getBlueprint()));
		}
		if (geodesk instanceof BlueprintDto) {
			widgetInfos.putAll(getMainMapClientWidgetInfo(geodesk.getUserApplicationInfo()));
		}

		widgetInfos.putAll(geodesk.getMainMapClientWidgetInfos());
		return widgetInfos;
	}

	/**
	 * Retrieve the list of ClientWidgetInfo's from a userapplications main map. This consists of the widget infos
	 * defined in the UserApplication itself, and those defined in the {@link UserApplicationInfo#getApplicationInfo()}.
	 * 
	 * @param uai
	 *            the user application info
	 * @return List of ClientWidgetInfo as defined in the user application
	 */
	public static Map<String, ClientWidgetInfo> getMainMapClientWidgetInfo(UserApplicationInfo uai) {
		Map<String, ClientWidgetInfo> widgetInfos = new HashMap<String, ClientWidgetInfo>();

		for (ClientMapInfo cmi : uai.getApplicationInfo().getMaps()) {
			if (GdmLayout.MAPMAIN_ID.equals(cmi.getId())) {
				widgetInfos.putAll(cmi.getWidgetInfo());
			}
		}
		widgetInfos.putAll(uai.getMainMapWidgetInfos());
		return widgetInfos;
	}

	/**
	 * Retrieve the list of ClientWidgetInfo's from the blueprint's and geodesks overview map. If clientWidgetInfo's are
	 * defined on both blueprint and geodesk level, that defined in the geodesk is used.
	 * 
	 * @param geodesk
	 *            the blueprint
	 * @return List of ClientWidgetInfo as defined in the blueprint
	 */
	public static Map<String, ClientWidgetInfo> getOverviewMapClientWidgetInfo(BaseGeodeskDto geodesk) {
		Map<String, ClientWidgetInfo> widgetInfos = new HashMap<String, ClientWidgetInfo>();
		if (geodesk instanceof GeodeskDto) {
			widgetInfos.putAll(getOverviewMapClientWidgetInfo(((GeodeskDto) geodesk).getBlueprint()));
		}
		if (geodesk instanceof BlueprintDto) {
			widgetInfos.putAll(getOverviewMapClientWidgetInfo(geodesk.getUserApplicationInfo()));
		}
		widgetInfos.putAll(geodesk.getOverviewMapClientWidgetInfos());
		return widgetInfos;
	}

	/**
	 * Retrieve the list of ClientWidgetInfo's from a userapplications overview map. This consists of the widget infos
	 * defined in the UserApplication itself, and those defined in the {@link UserApplicationInfo#getApplicationInfo()}.
	 * 
	 * @param uai
	 *            the user application info
	 * @return List of ClientWidgetInfo as defined in the user application
	 */
	public static Map<String, ClientWidgetInfo> getOverviewMapClientWidgetInfo(UserApplicationInfo uai) {
		Map<String, ClientWidgetInfo> widgetInfos = new HashMap<String, ClientWidgetInfo>();

		for (ClientMapInfo cmi : uai.getApplicationInfo().getMaps()) {
			if (GdmLayout.MAPOVERVIEW_ID.equals(cmi.getId())) {
				widgetInfos.putAll(cmi.getWidgetInfo());
			}
		}
		widgetInfos.putAll(uai.getMainMapWidgetInfos());
		return widgetInfos;
	}

	/**
	 * Retrieve the list of ClientWidgetInfo's from the blueprint's and geodesks application info. If clientWidgetInfo's
	 * are defined on both blueprint and geodesk level, that defined in the geodesk is used.
	 * 
	 * @param geodesk
	 *            the blueprint
	 * @return List of ClientWidgetInfo as defined in the blueprint
	 */
	public static Map<String, ClientWidgetInfo> getApplicationClientWidgetInfo(BaseGeodeskDto geodesk) {
		Map<String, ClientWidgetInfo> widgetInfos = new HashMap<String, ClientWidgetInfo>();
		if (geodesk instanceof GeodeskDto) {
			widgetInfos.putAll(getApplicationClientWidgetInfo(((GeodeskDto) geodesk).getBlueprint()));
		}
		if (geodesk instanceof BlueprintDto) {
			widgetInfos.putAll(getApplicationClientWidgetInfo(geodesk.getUserApplicationInfo()));
		}
		widgetInfos.putAll(geodesk.getApplicationClientWidgetInfos());
		return widgetInfos;
	}

	/**
	 * Retrieve the list of ClientWidgetInfo's from a userapplications application info. This consists of the widget infos
	 * defined in the UserApplication itself, and those defined in the {@link UserApplicationInfo#getApplicationInfo()}.
	 * 
	 * @param uai
	 *            the user application info
	 * @return List of ClientWidgetInfo as defined in the user application
	 */
	public static Map<String, ClientWidgetInfo> getApplicationClientWidgetInfo(UserApplicationInfo uai) {
		Map<String, ClientWidgetInfo> widgetInfos = new HashMap<String, ClientWidgetInfo>();

		widgetInfos.putAll(uai.getApplicationInfo().getWidgetInfo());
		widgetInfos.putAll(uai.getMainMapWidgetInfos());
		return widgetInfos;
	}

	/**
	 * Retrieve the mapinfo from the geodesk's main map.
	 * 
	 * @param geodesk
	 *            the geodesk
	 * @return the mapInfo
	 */
	public static ClientMapInfo getMainMap(BaseGeodeskDto geodesk) {
		return UserApplicationDtoUtil.getMainMap(geodesk.getUserApplicationInfo());
	}

	/**
	 * Retrieve the main map layers from the geodesk. If it's a geodesk without layers, the layers from the blueprint
	 * are retrieved.
	 * 
	 * @param geodesk
	 *            the geodesk
	 * @return a list of layers
	 */
	public static List<LayerDto> getMainMapLayers(BaseGeodeskDto geodesk) {
		if (geodesk instanceof GeodeskDto) {
			if (geodesk.getMainMapLayers() == null || geodesk.getMainMapLayers().isEmpty()) {
				return getMainMapLayers(((GeodeskDto) geodesk).getBlueprint());
			}
		}
		return geodesk.getMainMapLayers();
	}
}
