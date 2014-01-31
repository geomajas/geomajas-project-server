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
package org.geomajas.plugin.deskmanager.domain.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.plugin.deskmanager.configuration.UserApplicationInfo;
import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;

/**
 * Dto object for a blueprint.
 * 
 * @author Kristof Heirwegh
 * @author Oliver May
 */
public class BlueprintDto implements BaseGeodeskDto {

	private static final long serialVersionUID = 1L;

	private String id; // UUID

	private String name;

	private String userApplicationKey;

	private Date creationDate;

	private String creationBy; // naam

	private Date lastEditDate;

	private String lastEditBy; // naam

	private boolean active = true;

	private boolean geodesksActive = true;

	private boolean limitToLoketTerritory;

	private boolean limitToUserTerritory;

	private boolean publiek;

	private boolean deleted;
	
	private List<LayerDto> mainMapLayers = new ArrayList<LayerDto>();

	private List<LayerDto> overviewMapLayers = new ArrayList<LayerDto>();

	private List<TerritoryDto> groups = new ArrayList<TerritoryDto>();
	
	private Map<String, ClientWidgetInfo> applicationClientWidgetInfos = new HashMap<String, ClientWidgetInfo>();

	private Map<String, ClientWidgetInfo> overviewMapClientWidgetInfos = new HashMap<String, ClientWidgetInfo>();

	private Map<String, ClientWidgetInfo> mainMapClientWidgetInfos = new HashMap<String, ClientWidgetInfo>();
	
	private UserApplicationInfo userApplicationInfo;

	// ------------------------------------------------------------------

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserApplicationKey() {
		return userApplicationKey;
	}

	public void setUserApplicationKey(String userApplicationKey) {
		this.userApplicationKey = userApplicationKey;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreationBy() {
		return creationBy;
	}

	public void setCreationBy(String creationBy) {
		this.creationBy = creationBy;
	}

	public Date getLastEditDate() {
		return lastEditDate;
	}

	public void setLastEditDate(Date lastEditDate) {
		this.lastEditDate = lastEditDate;
	}

	public String getLastEditBy() {
		return lastEditBy;
	}

	public void setLastEditBy(String lastEditBy) {
		this.lastEditBy = lastEditBy;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isLimitToCreatorTerritory() {
		return limitToLoketTerritory;
	}

	public void setLimitToLoketTerritory(boolean limitToLoketTerritory) {
		this.limitToLoketTerritory = limitToLoketTerritory;
	}

	public boolean isLimitToUserTerritory() {
		return limitToUserTerritory;
	}

	public void setLimitToUserTerritory(boolean limitToUserTerritory) {
		this.limitToUserTerritory = limitToUserTerritory;
	}

	public boolean isPublic() {
		return publiek;
	}

	public void setPublic(boolean publiek) {
		this.publiek = publiek;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public List<TerritoryDto> getTerritories() {
		return groups;
	}

	public void setGroups(List<TerritoryDto> groups) {
		this.groups = groups;
	}

	public boolean isGeodesksActive() {
		return geodesksActive;
	}

	public void setGeodesksActive(boolean geodesksActive) {
		this.geodesksActive = geodesksActive;
	}

	/**
	 * @param applicationClientWidgetInfos the applicationClientWidgetInfos to set
	 */
	public void setApplicationClientWidgetInfos(Map<String, ClientWidgetInfo> applicationClientWidgetInfos) {
		this.applicationClientWidgetInfos = applicationClientWidgetInfos;
	}

	/**
	 * @return the applicationClientWidgetInfos
	 */
	public Map<String, ClientWidgetInfo> getApplicationClientWidgetInfos() {
		return applicationClientWidgetInfos;
	}

	/**
	 * @param overviewMapClientWidgetInfos the overviewMapClientWidgetInfos to set
	 */
	public void setOverviewMapClientWidgetInfos(Map<String, ClientWidgetInfo> overviewMapClientWidgetInfos) {
		this.overviewMapClientWidgetInfos = overviewMapClientWidgetInfos;
	}

	/**
	 * @return the overviewMapClientWidgetInfos
	 */
	public Map<String, ClientWidgetInfo> getOverviewMapClientWidgetInfos() {
		return overviewMapClientWidgetInfos;
	}

	/**
	 * @param mainMapClientWidgetInfos the mainMapClientWidgetInfos to set
	 */
	public void setMainMapClientWidgetInfos(Map<String, ClientWidgetInfo> mainMapClientWidgetInfos) {
		this.mainMapClientWidgetInfos = mainMapClientWidgetInfos;
	}

	/**
	 * @return the mainMapClientWidgetInfos
	 */
	public Map<String, ClientWidgetInfo> getMainMapClientWidgetInfos() {
		return mainMapClientWidgetInfos;
	}

	/**
	 * @param userApplicationInfo the userApplicationInfo to set
	 */
	public void setUserApplicationInfo(UserApplicationInfo userApplicationInfo) {
		this.userApplicationInfo = userApplicationInfo;
	}

	/**
	 * @return the userApplicationInfo
	 */
	public UserApplicationInfo getUserApplicationInfo() {
		return userApplicationInfo;
	}

	/**
	 * @param mainMapLayers the mainMapLayers to set
	 */
	public void setMainMapLayers(List<LayerDto> mainMapLayers) {
		this.mainMapLayers = mainMapLayers;
	}

	/**
	 * @return the mainMapLayers
	 */
	public List<LayerDto> getMainMapLayers() {
		return mainMapLayers;
	}

	/**
	 * @param overviewMapLayers the overviewMapLayers to set
	 */
	public void setOverviewMapLayers(List<LayerDto> overviewMapLayers) {
		this.overviewMapLayers = overviewMapLayers;
	}

	/**
	 * @return the overviewMapLayers
	 */
	public List<LayerDto> getOverviewMapLayers() {
		return overviewMapLayers;
	}
}
