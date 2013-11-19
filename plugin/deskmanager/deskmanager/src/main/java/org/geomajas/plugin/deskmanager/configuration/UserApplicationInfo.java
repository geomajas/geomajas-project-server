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
package org.geomajas.plugin.deskmanager.configuration;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientWidgetInfo;

/**
 * Configuration object for user applications. This provides the basic application configuration for the deskmanager, 
 * on this configuration the blueprints are based. 
 * 
 * @author Kristof Heirwegh
 * @author Oliver May
 * @since 1.0.0
 */
@Api(allMethods = true)
public class UserApplicationInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String key;

	private Map<String, ClientWidgetInfo> applicationWidgetInfo = new HashMap<String, ClientWidgetInfo>();

	private Map<String, ClientWidgetInfo> mainMapWidgetInfos =
			new HashMap<String, ClientWidgetInfo>();

	private Map<String, ClientWidgetInfo> overviewMapWidgetInfos =
		new HashMap<String, ClientWidgetInfo>();
	
	private ClientApplicationInfo applicationInfo;

	/**
	 * Get a map of application widget configurations.
	 * 
	 * @return the widget configurations.
	 */
	public Map<String, ClientWidgetInfo> getApplicationWidgetInfos() {
		return applicationWidgetInfo;
	}

	/**
	 * Set the application widget configurations. Allows configuration of widgets on the application level.
	 * 
	 * @param applicationWidgetInfo the applicationwidgetinfo
	 */
	public void setApplicationWidgetInfos(Map<String, ClientWidgetInfo> applicationWidgetInfo) {
		this.applicationWidgetInfo = applicationWidgetInfo;
	}

	/**
	 * Get a map of application widget configurations.
	 * 
	 * @return the widget configurations.
	 */
	public Map<String, ClientWidgetInfo> getMainMapWidgetInfos() {
		return mainMapWidgetInfos;
	}

	/**
	 * Set the main map widget configurations. Allows configuration of widgets on the main map level.
	 * 
	 * @param mapWidgetInfos the applicationwidgetinfos
	 */
	public void setMainMapWidgetInfos(Map<String, ClientWidgetInfo> mapWidgetInfos) {
		this.mainMapWidgetInfos = mapWidgetInfos;
	}

	/**
	 * Get a map of application widget configurations.
	 * 
	 * @return the widget configurations.
	 */
	public Map<String, ClientWidgetInfo> getOverviewMapWidgetInfos() {
		return overviewMapWidgetInfos;
	}

	/**
	 * Set the overview map widget configurations. Allows configuration of widgets on the overview map level.
	 * 
	 * @param mapWidgetInfos the applicationwidgetinfos
	 */
	public void setOverviewMapWidgetInfos(Map<String, ClientWidgetInfo> mapWidgetInfos) {
		this.overviewMapWidgetInfos = mapWidgetInfos;
	}

	/**
	 * The key by which this user application is defined.
	 * 
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * The key by which this user application is defined.
	 * 
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Set the default applicaiton configuration of this user application.
	 * 
	 * @param applicationInfo the applicationInfo to set
	 */
	public void setApplicationInfo(ClientApplicationInfo applicationInfo) {
		this.applicationInfo = applicationInfo;
	}

	/**
	 * Get the default applicaiton configuration of this user application.
	 * 
	 * @return the applicationInfo
	 */
	public ClientApplicationInfo getApplicationInfo() {
		return applicationInfo;
	}

}
