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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyClass;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.MapKey;
import org.hibernate.annotations.Type;

/**
 * Representation of a blueprint. A blueprint contains a full geodesk configuration on which a geodesk is based.
 *  
 * @author Kristof Heirwegh
 * @author Oliver May
 * @since 1.0.0
 */
@Api (allMethods = true)
@Entity
@Table(name = "config_blueprints")
public class Blueprint implements BaseGeodesk {

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

	@Column(name = "creationBy", nullable = false)
	private String creationBy; // naam

	@Column(name = "lastEditDate", nullable = false)
	private Date lastEditDate;

	@Column(name = "lastEditBy", nullable = false)
	private String lastEditBy; // naam

	@Column(name = "active")
	private boolean active = true;

	@Column(name = "geodesksActive")
	private boolean geodesksActive = true;

	private boolean limitToCreatorTerritory;

	@Column(name = "limit_to_user_territory")
	private boolean limitToUserTerritory;

	private boolean publicc;

	private boolean deleted;

	@OneToMany(cascade = CascadeType.ALL/*, orphanRemoval = true*/)
	@OrderColumn(name = "sortorder")
	@JoinTable(name = "config_blueprint_mainlayer")
	private List<ClientLayer> mainMapLayers = new ArrayList<ClientLayer>();

	@OneToMany(cascade = CascadeType.ALL/*, orphanRemoval = true*/)
	@OrderColumn(name = "sortorder")
	@JoinTable(name = "config_blueprint_overviewlayer")
	private List<ClientLayer> overviewMapLayers = new ArrayList<ClientLayer>();

	/**
	 * The groups that can use this blueprint to create loketten
	 */
	@ManyToMany(targetEntity = Territory.class)
	@JoinTable(name = "config_blueprint_territory", 
		joinColumns = @JoinColumn(name = "blueprint_id"), inverseJoinColumns = { @JoinColumn(name = "group_id") })
	@OrderBy("name desc")
	private List<Territory> groups = new ArrayList<Territory>();

	@ElementCollection()
	@MapKeyClass(String.class)
	@MapKey(type = @Type(type = "org.hibernate.type.StringType"))
	@Type(type = "org.geomajas.plugin.deskmanager.domain.types.XmlSerialisationType")
	@JoinTable(name = "config_blueprint_applicationclientwidgetinfos")
	private Map<String, ClientWidgetInfo> applicationClientWidgetInfos = new HashMap<String, ClientWidgetInfo>();
	
	@ElementCollection()
	@MapKeyClass(String.class)
	@MapKey(type = @Type(type = "org.hibernate.type.StringType"))
	@Type(type = "org.geomajas.plugin.deskmanager.domain.types.XmlSerialisationType")
	@JoinTable(name = "config_blueprint_overviewmapclientwidgetinfos")
	private Map<String, ClientWidgetInfo> overviewMapClientWidgetInfos = new HashMap<String, ClientWidgetInfo>();
	
	@ElementCollection()
	@MapKeyClass(String.class)
	@MapKey(type = @Type(type = "org.hibernate.type.StringType"))
	@Type(type = "org.geomajas.plugin.deskmanager.domain.types.XmlSerialisationType")
	@JoinTable(name = "config_blueprint_mainmapclientwidgetinfos")
	private Map<String, ClientWidgetInfo> mainMapClientWidgetInfos = new HashMap<String, ClientWidgetInfo>();
	
	// ------------------------------------------------------------------

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the creation date of this blueprint.
	 * @return the creation date
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * Set the creation date of this blueprint.
	 * @param creationDate
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * Get the creator of this blueprint.
	 * @return the creator
	 */
	public String getCreationBy() {
		return creationBy;
	}

	/**
	 * Set the creator of this blueprint.
	 * @param creationBy
	 */
	public void setCreationBy(String creationBy) {
		this.creationBy = creationBy;
	}

	/**
	 * Is this blueprint active?
	 * 
	 * @return true if the blueprint is active.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Is this blueprint active.
	 * 
	 * @param active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Get the last edit date.
	 * 
	 * @return the edit date
	 */
	public Date getLastEditDate() {
		return lastEditDate;
	}

	/**
	 * Set the last edit date.
	 * @param lastEditDate
	 */
	public void setLastEditDate(Date lastEditDate) {
		this.lastEditDate = lastEditDate;
	}

	/**
	 * Get the last editor.
	 * @return
	 */
	public String getLastEditBy() {
		return lastEditBy;
	}

	/**
	 * Set the last editor.
	 * @param lastEditBy
	 */
	public void setLastEditBy(String lastEditBy) {
		this.lastEditBy = lastEditBy;
	}

	/**
	 * Are geodesks based on this blueprint limited to creator territory?
	 * @return
	 */
	public boolean isLimitToCreatorTerritory() {
		return limitToCreatorTerritory;
	}

	/**
	 * Set if geodesks based on this blueprint are limited to creator territory.
	 */
	public void setLimitToCreatorTerritory(boolean limitToCreatorTerritory) {
		this.limitToCreatorTerritory = limitToCreatorTerritory;
	}

	/**
	 * Are geodesks based on this blueprint limited to user territory?
	 * @return
	 */
	public boolean isLimitToUserTerritory() {
		return limitToUserTerritory;
	}

	/**
	 * Set if geodesks based on this blueprint are limited to user territory.
	 * @param limitToUserTerritory
	 */
	public void setLimitToUserTerritory(boolean limitToUserTerritory) {
		this.limitToUserTerritory = limitToUserTerritory;
	}

	/**
	 * Are the geodesks based on this blueprint public?
	 * @return
	 */
	public boolean isPublic() {
		return publicc;
	}

	/**
	 * Set if the geodesks based on this blueprint are public.
	 * @param publik
	 */
	public void setPublic(boolean publik) {
		this.publicc = publik;
	}

	/**
	 * Get territories that can use this blueprint (create a geodesk based on it).
	 * @return
	 */
	public List<Territory> getTerritories() {
		return groups;
	}

	/**
	 * Set the territories that can use this blueprint (create a geodesk based on it).
	 * @param territories
	 */
	public void setTerritories(List<Territory> territories) {
		this.groups = territories;
	}

	/**
	 * Is this blueprint deleted?
	 * @return
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * Set if this blueprint is deleted.
	 * @param deleted
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * Are geodesks based on this blueprint activated?
	 * @return
	 */
	public boolean isGeodesksActive() {
		return geodesksActive;
	}

	/**
	 * Set if geodesks based on this blueprint are activated.
	 * @param geodesksActive
	 */
	public void setGeodesksActive(boolean geodesksActive) {
		this.geodesksActive = geodesksActive;
	}

	@Override
	public String getUserApplicationKey() {
		return userApplicationKey;
	}

	@Override
	public void setUserApplicationKey(String userApplicationName) {
		this.userApplicationKey = userApplicationName;
	}

	@Override
	public void setApplicationClientWidgetInfos(Map<String, ClientWidgetInfo> applicationClientWidgetInfos) {
		this.applicationClientWidgetInfos = applicationClientWidgetInfos;
	}

	@Override
	public Map<String, ClientWidgetInfo> getApplicationClientWidgetInfos() {
		return applicationClientWidgetInfos;
	}

	@Override
	public void setOverviewMapClientWidgetInfos(Map<String, ClientWidgetInfo> overviewMapClientWidgetInfos) {
		this.overviewMapClientWidgetInfos = overviewMapClientWidgetInfos;
	}

	@Override
	public Map<String, ClientWidgetInfo> getOverviewMapClientWidgetInfos() {
		return overviewMapClientWidgetInfos;
	}

	@Override
	public void setMainMapClientWidgetInfos(Map<String, ClientWidgetInfo> mainMapClientWidgetInfos) {
		this.mainMapClientWidgetInfos = mainMapClientWidgetInfos;
	}

	@Override
	public Map<String, ClientWidgetInfo> getMainMapClientWidgetInfos() {
		return mainMapClientWidgetInfos;
	}

	@Override
	public void setMainMapLayers(List<ClientLayer> layers) {
		this.mainMapLayers = layers;
	}

	@Override
	public List<ClientLayer> getMainMapLayers() {
		return mainMapLayers;
	}

	@Override
	public void setOverviewMapLayers(List<ClientLayer> layers) {
		this.overviewMapLayers = layers;
	}

	@Override
	public List<ClientLayer> getOverviewMapLayers() {
		return overviewMapLayers;
	}
}
