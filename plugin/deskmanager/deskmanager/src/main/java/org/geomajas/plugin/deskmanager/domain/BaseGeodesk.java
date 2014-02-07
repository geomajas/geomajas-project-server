/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.domain;

import java.util.List;
import java.util.Map;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.client.ClientWidgetInfo;

/**
 * Shared interface of Blueprint an Geodesk.
 * 
 * @author Oliver May
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface BaseGeodesk {

	/**
	 * Get the id of this geodesk.
	 * 
	 * @return the id.
	 */
	String getId();

	/**
	 * Set the id of this geodesk.
	 * 
	 * @param id the id to set.
	 */
	void setId(String id);

	/**
	 * Get the nema of this geodesk.
	 * 
	 * @return the name.
	 */
	String getName();

	
	/**
	 * Set the name of this geodesk.
	 * @param name the name to set.
	 */
	void setName(String name);

	/**
	 * Get the key of the user application where this geodesk is based on.
	 * @return the user application key
	 */
	String getUserApplicationKey();

	/**
	 * Set the key of the user application where this geodesk is based on.
	 * @param userApplicationkey the key
	 */
	void setUserApplicationKey(String userApplicationkey);

	/**
	 * Set the client widget configurations that are configured on the application level.
	 * 
	 * @param applicationClientWidgetInfos the applicationClientWidgetInfos to set
	 */
	void setApplicationClientWidgetInfos(Map<String, ClientWidgetInfo> applicationClientWidgetInfos);

	/**
	 * Get the client widget configurations that are configured on the applicaiton level.
	 * 
	 * @return the applicationClientWidgetInfos
	 */
	Map<String, ClientWidgetInfo> getApplicationClientWidgetInfos();

	/**
	 * Set the client widget configurations that are configured on the overview maop.
	 * 
	 * @param overviewMapClientWidgetInfos the overviewMapClientWidgetInfos to set
	 */
	void setOverviewMapClientWidgetInfos(Map<String, ClientWidgetInfo> overviewMapClientWidgetInfos);

	/**
	 * Get the client widget configurations that are configured on the overview map.
	 * @return the overviewMapClientWidgetInfos
	 */
	Map<String, ClientWidgetInfo> getOverviewMapClientWidgetInfos();

	/**
	 * Set the client widget configurations that are configured on the main map.
	 * 
	 * @param mainMapClientWidgetInfos the mainMapClientWidgetInfos to set
	 */
	void setMainMapClientWidgetInfos(Map<String, ClientWidgetInfo> mainMapClientWidgetInfos);

	/**
	 * Get the client widget configurations that are configured on the main map.
	 * 
	 * @return the mainMapClientWidgetInfos
	 */
	Map<String, ClientWidgetInfo> getMainMapClientWidgetInfos();

	/**
	 * Set the main map layers.
	 * 
	 * @param mainMapLayers the mainMapLayers to set
	 */
	void setMainMapLayers(List<ClientLayer> layers);

	/**
	 * Get the main map layers.
	 * 
	 * @return the mainMapLayers
	 */
	List<ClientLayer> getMainMapLayers();

	/**
	 * Set the overview map layers.
	 * 
	 * @param mainMapLayers the overview map Layers to set
	 */
	void setOverviewMapLayers(List<ClientLayer> layers);

	/** 
	 * Get the overview map layers.
	 * 
	 * @return the overview map layers
	 */
	List<ClientLayer> getOverviewMapLayers();

}