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
package org.geomajas.plugin.deskmanager.configuration;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientWidgetInfo;

/**
 * @author Kristof Heirwegh
 * @since 1.0.0
 */
@Api(allMethods = true)
public class UserApplicationInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * @deprecated Don't use ID, just use the key
	 */
	@Deprecated
	private String id;

	private String key;

	private Map<String, ClientWidgetInfo> applicationWidgetInfo = new HashMap<String, ClientWidgetInfo>();

	private Map<String, ClientWidgetInfo> mainMapWidgetInfos =
			new HashMap<String, ClientWidgetInfo>();

	private Map<String, ClientWidgetInfo> overviewMapWidgetInfos =
		new HashMap<String, ClientWidgetInfo>();
	
	private ClientApplicationInfo applicationInfo;

	// -------------------------------------------------
	/**
	 * @deprecated Don't use ID, just use the key
	 */
	@Deprecated
	public String getId() {
		return id;
	}
	/**
	 * @deprecated Don't use ID, just use the key
	 */
	@Deprecated
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * TODO.
	 * @return
	 */
	public Map<String, ClientWidgetInfo> getApplicationWidgetInfos() {
		return applicationWidgetInfo;
	}

	/**
	 * TODO.
	 * @param applicationWidgetInfo
	 */
	public void setApplicationWidgetInfos(Map<String, ClientWidgetInfo> applicationWidgetInfo) {
		this.applicationWidgetInfo = applicationWidgetInfo;
	}

	/**
	 * TODO.
	 * @return
	 */
	public Map<String, ClientWidgetInfo> getMainMapWidgetInfos() {
		return mainMapWidgetInfos;
	}

	/**
	 * TODO.
	 * @param mapWidgetInfos
	 */
	public void setMainMapWidgetInfos(Map<String, ClientWidgetInfo> mapWidgetInfos) {
		this.mainMapWidgetInfos = mapWidgetInfos;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param overviewMapWidgetInfos the overviewMapWidgetInfos to set
	 */
	public void setOverviewMapWidgetInfos(Map<String, ClientWidgetInfo> overviewMapWidgetInfos) {
		this.overviewMapWidgetInfos = overviewMapWidgetInfos;
	}

	/**
	 * @return the overviewMapWidgetInfos
	 */
	public Map<String, ClientWidgetInfo> getOverviewMapWidgetInfos() {
		return overviewMapWidgetInfos;
	}

	/**
	 * @param applicationInfo the applicationInfo to set
	 */
	public void setApplicationInfo(ClientApplicationInfo applicationInfo) {
		this.applicationInfo = applicationInfo;
	}

	/**
	 * @return the applicationInfo
	 */
	public ClientApplicationInfo getApplicationInfo() {
		return applicationInfo;
	}

}
