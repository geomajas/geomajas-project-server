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
package org.geomajas.plugin.deskmanager.domain.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.plugin.deskmanager.configuration.UserApplicationInfo;


/**
 * Common base for geodesks and blueprints.
 * 
 * @author Oliver May
 *
 */
public interface BaseGeodeskDto extends Serializable {

	/**
	 * Get the id of this AbstractGeodesk.
	 * @return the id.
	 */
	String getId();
	
	/**
	 * Set the id of this AbstractGeodesk.
	 * @param id
	 */
	void setId(String id);
	
	/**
	 * Get the name of this AbstractGeodesk.
	 * @return
	 */
	String getName();

	/**
	 * Set the name of this AbstractGeodesk.
	 * @param name
	 */
	void setName(String name);

	/**
	 * Get a map of application clientwidgetinfos.
	 * 
	 * @return
	 */
	Map<String, ClientWidgetInfo> getApplicationClientWidgetInfos();
	
	/**
	 * Set a map of application clientwidgetinfos.
	 * 
	 * @param clientWidgetInfos
	 */
	void setApplicationClientWidgetInfos(Map<String, ClientWidgetInfo> clientWidgetInfos);
	
	/**
	 * Get a map of overviewmap clientwidgetinfos.
	 * 
	 * @return
	 */
	Map<String, ClientWidgetInfo> getOverviewMapClientWidgetInfos();

	/**
	 * Set a map of overviewmap clientwidgetinfos.
	 * 
	 * @param clientWidgetInfos
	 */
	void setOverviewMapClientWidgetInfos(Map<String, ClientWidgetInfo> clientWidgetInfos);
	
	/**
	 * Get a map of mainmap clientwidgetinfos.
	 * 
	 * @return
	 */
	Map<String, ClientWidgetInfo> getMainMapClientWidgetInfos();

	/**
	 * Set a map of mainmap clientwidgetinfos.
	 * 
	 * @param clientWidgetInfos
	 */
	void setMainMapClientWidgetInfos(Map<String, ClientWidgetInfo> clientWidgetInfos);
	
	/**
	 * Get the userapplicationinfo.
	 * 
	 * @return
	 */
	UserApplicationInfo getUserApplicationInfo();
	
	/**
	 * Set the userapplicationinfo.
	 * 
	 * @param userApplicationInfo
	 */
	void setUserApplicationInfo(UserApplicationInfo userApplicationInfo);
	
	/**
	 * Get a list of main map layers.
	 * 
	 * @return
	 */
	List<LayerDto> getMainMapLayers();
	
	/**
	 * Set the list of main map layers.
	 * 
	 * @param mainMapLayers
	 */
	void setMainMapLayers(List<LayerDto> mainMapLayers);
	
	/**
	 * Get a list of overview map layers.
	 * 
	 * @return
	 */
	List<LayerDto> getOverviewMapLayers();
	
	/**
	 * Set the list of overview map layers.
	 * 
	 * @param overviewMapLayers
	 */
	void setOverviewMapLayers(List<LayerDto> overviewMapLayers);

	/**
	 * If the geodesk is public.
	 * @return if public
	 */
	boolean isPublic();
	
	/**
	 * Set if the geodesk is public.
	 * @param publicc
	 */
	void setPublic(boolean publicc);
	
}
