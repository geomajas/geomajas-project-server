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
package org.geomajas.plugin.deskmanager.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.plugin.deskmanager.domain.dto.GeodeskInfo;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * Blueprints should not (cannot) be *really* deleted as they might still have loketten that depend on them.
 * <p>
 * Use the deleted property to mark a blueprint as deleted.
 * 
 * @author Kristof Heirwegh
 * 
 */
@Entity
@Table(name = "config_blueprints")
public class Blueprint implements GeodeskInfo {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id; // UUID

	@Column(name = "name", nullable = false)
	private String name;

	// -- from Geodesk-Registry
	@Column(name = "user_application_name")
	private String userApplicationKey;

	@Column(name = "creation_date", nullable = false)
	private Date creationDate;

	@Column(name = "creation_by", nullable = false)
	private String creationBy; // naam

	@Column(name = "lastedit_date", nullable = false)
	private Date lastEditDate;

	@Column(name = "lastedit_by", nullable = false)
	private String lastEditBy; // naam

	@Column(name = "active")
	private boolean active = true;

	@Column(name = "loketten_active")
	private boolean lokettenActive = true;

	@Column(name = "limit_to_loket_territory")
	private boolean limitToLoketTerritory;

	@Column(name = "limit_to_user_territory")
	private boolean limitToUserTerritory;

	@Column(name = "public")
	private boolean publiek;

	@Column(name = "deleted")
	private boolean deleted;

	@ManyToOne
	@JoinColumn(name = "layertree_id")
	private LayerTree layerTree;

	/**
	 * The groups that can use this blueprint to create loketten
	 */
	@ManyToMany(cascade = { CascadeType.ALL }, targetEntity = Territory.class, fetch = FetchType.LAZY)
	@JoinTable(name = "tt_groups_blueprints", 
		joinColumns = @JoinColumn(name = "blueprint_id"), inverseJoinColumns = { @JoinColumn(name = "group_id") })
//	@Fetch(FetchMode.JOIN)
	@OrderBy("name desc")
	private List<Territory> groups = new ArrayList<Territory>();

	@ElementCollection(fetch = FetchType.EAGER)
	@Type(type = "org.geomajas.plugin.deskmanager.domain.types.XmlSerialisationType")
	private Map<String, ClientWidgetInfo> applicationClientWidgetInfos = new HashMap<String, ClientWidgetInfo>();
	
	@ElementCollection(fetch = FetchType.EAGER)
	@Type(type = "org.geomajas.plugin.deskmanager.domain.types.XmlSerialisationType")
	@Fetch(FetchMode.SELECT)
	private Map<String, ClientWidgetInfo> overviewMapClientWidgetInfos = new HashMap<String, ClientWidgetInfo>();
	
	@ElementCollection(fetch = FetchType.EAGER)
	@Type(type = "org.geomajas.plugin.deskmanager.domain.types.XmlSerialisationType")
	@Fetch(FetchMode.SELECT)
	private Map<String, ClientWidgetInfo> mainMapClientWidgetInfos = new HashMap<String, ClientWidgetInfo>();
	
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
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

	public void setPublic(boolean publik) {
		this.publiek = publik;
	}

	public List<Territory> getTerritories() {
		return groups;
	}

	public void setGroups(List<Territory> groups) {
		this.groups = groups;
	}

	public LayerTree getLayerTree() {
		return layerTree;
	}

	public void setLayerTree(LayerTree layerTree) {
		this.layerTree = layerTree;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isLokettenActive() {
		return lokettenActive;
	}

	public void setLokettenActive(boolean lokettenActive) {
		this.lokettenActive = lokettenActive;
	}

	public String getUserApplicationKey() {
		return userApplicationKey;
	}

	public void setUserApplicationKey(String userApplicationName) {
		this.userApplicationKey = userApplicationName;
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
}
