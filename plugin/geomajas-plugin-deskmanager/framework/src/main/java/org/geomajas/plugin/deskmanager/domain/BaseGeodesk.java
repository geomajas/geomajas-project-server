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
package org.geomajas.plugin.deskmanager.domain;

import java.util.List;
import java.util.Map;

import org.geomajas.configuration.client.ClientWidgetInfo;

/**
 * @author Oliver May
 *
 */
public interface BaseGeodesk {

	String getId();

	void setId(String id);

	String getName();

	void setName(String name);

	String getUserApplicationKey();

	void setUserApplicationKey(String userApplicationName);

	/**
	 * @param applicationClientWidgetInfos the applicationClientWidgetInfos to set
	 */
	void setApplicationClientWidgetInfos(Map<String, ClientWidgetInfo> applicationClientWidgetInfos);

	/**
	 * @return the applicationClientWidgetInfos
	 */
	Map<String, ClientWidgetInfo> getApplicationClientWidgetInfos();

	/**
	 * @param overviewMapClientWidgetInfos the overviewMapClientWidgetInfos to set
	 */
	void setOverviewMapClientWidgetInfos(Map<String, ClientWidgetInfo> overviewMapClientWidgetInfos);

	/**
	 * @return the overviewMapClientWidgetInfos
	 */
	Map<String, ClientWidgetInfo> getOverviewMapClientWidgetInfos();

	/**
	 * @param mainMapClientWidgetInfos the mainMapClientWidgetInfos to set
	 */
	void setMainMapClientWidgetInfos(Map<String, ClientWidgetInfo> mainMapClientWidgetInfos);

	/**
	 * @return the mainMapClientWidgetInfos
	 */
	Map<String, ClientWidgetInfo> getMainMapClientWidgetInfos();

	/**
	 * @param mainMapLayers the mainMapLayers to set
	 */
	void setMainMapLayers(List<ClientLayer> layers);

	/**
	 * @return the mainMapLayers
	 */
	List<ClientLayer> getMainMapLayers();

	/**
	 * @param mainMapLayers the mainMapLayers to set
	 */

	void setOverviewMapLayers(List<ClientLayer> layers);

	/**
	 * @return the mainMapLayers
	 */
	List<ClientLayer> getOverviewMapLayers();

}