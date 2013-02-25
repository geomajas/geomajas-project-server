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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
 * Representation of a geodesk.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
@Entity
@Table(name = "config_geodesks")
public class Geodesk implements BaseGeodesk {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id; // UUID

	@Column(name = "geodeskId", unique = true)
	private String geodeskId; // UUID

	@Column(name = "name", nullable = false)
	private String name;

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

	/**
	 * Enkel te wijzigen indien niet true in blueprint.
	 */
	@Column(name = "limit_to_creator_territory")
	private boolean limitToCreatorTerritory;

	/**
	 * Enkel te wijzigen indien niet true in blueprint.
	 */
	@Column(name = "limit_to_user_territory")
	private boolean limitToUserTerritory;

	@Column(name = "public")
	private boolean publicc;

	@Column(name = "deleted")
	private boolean deleted;

	@ManyToOne
	@JoinColumn(name = "blueprint_id")
	private Blueprint blueprint;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderColumn(name = "sortorder")
	@JoinTable(name = "config_geodesk_mainlayer")
	private List<ClientLayer> mainMapLayers = new ArrayList<ClientLayer>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderColumn(name = "sortorder")
	@JoinTable(name = "config_geodesk_overviewlayer")
	private List<ClientLayer> overviewMapLayers = new ArrayList<ClientLayer>();

	@ElementCollection()
	@MapKeyClass(String.class)
	@Type(type = "org.geomajas.plugin.deskmanager.domain.types.XmlSerialisationType")
	@MapKey(type = @Type(type = "org.hibernate.type.StringType"))
	@JoinTable(name = "config_geodesk_applicationclientwidgetinfos")
	private Map<String, ClientWidgetInfo> applicationClientWidgetInfos = new HashMap<String, ClientWidgetInfo>();

	@ElementCollection()
	@MapKeyClass(String.class)
	@Type(type = "org.geomajas.plugin.deskmanager.domain.types.XmlSerialisationType")
	@MapKey(type = @Type(type = "org.hibernate.type.StringType"))
	@JoinTable(name = "config_geodesk_overviewmapclientwidgetinfos")
	private Map<String, ClientWidgetInfo> overviewMapClientWidgetInfos = new HashMap<String, ClientWidgetInfo>();

	@ElementCollection()
	@MapKeyClass(String.class)
	@Type(type = "org.geomajas.plugin.deskmanager.domain.types.XmlSerialisationType")
	@MapKey(type = @Type(type = "org.hibernate.type.StringType"))
	@JoinTable(name = "config_geodesk_mainmapclientwidgetinfos")
	private Map<String, ClientWidgetInfo> mainMapClientWidgetInfos = new HashMap<String, ClientWidgetInfo>();

	// null == superuser
	@ManyToOne
	@JoinColumn(name = "owninggroup_id")
	private Territory owner;

	@ManyToMany(targetEntity = Territory.class, fetch = FetchType.LAZY)
	@JoinTable(name = "config_tt_geodesk_territory", joinColumns = @JoinColumn(name = "geodesk_id"), 
			inverseJoinColumns = { @JoinColumn(name = "group_id") })
	@OrderBy("name desc")
	private List<Territory> groups = new ArrayList<Territory>();

	/**
	 * Get the id.
	 * @return the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the id.
	 * @param id the id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name.
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the creation date.
	 * @return the creation date
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * Set the creation date.
	 * @param creationDate
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * Get the creator's name.
	 * @return the creator
	 */
	public String getCreationBy() {
		return creationBy;
	}

	/**
	 * Set the creator's name.
	 * @param creationBy the creator
	 */
	public void setCreationBy(String creationBy) {
		this.creationBy = creationBy;
	}

	/**
	 * Is this geodesk active?
	 * @return true if active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Set if this geodesk is active.
	 * @param active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Get the last edit date.
	 * 
	 * @return the edit date.
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
	 * Who edited the geodesk last.
	 * @return the last editor.
	 */
	public String getLastEditBy() {
		return lastEditBy;
	}

	/**
	 * Set who edited the geodesk last.
	 * @param lastEditBy
	 */
	public void setLastEditBy(String lastEditBy) {
		this.lastEditBy = lastEditBy;
	}

	/**
	 * Is this geodesk limited to creator territory?
	 */
	public boolean isLimitToCreatorTerritory() {
		return limitToCreatorTerritory;
	}

	/**
	 * Set if this geodesk is limited to the creator territory.
	 * @param limitToCreatorTerritory
	 */
	public void setLimitToCreatorTerritory(boolean limitToCreatorTerritory) {
		this.limitToCreatorTerritory = limitToCreatorTerritory;
	}

	/**
	 * Is this geodesk limited to the user's territory?
	 */
	public boolean isLimitToUserTerritory() {
		return limitToUserTerritory;
	}

	/**
	 * Set if this geodesk is limited to the user's territory.
	 * @param limitToUserTerritory
	 */
	public void setLimitToUserTerritory(boolean limitToUserTerritory) {
		this.limitToUserTerritory = limitToUserTerritory;
	}

	/**
	 * Is this geodesk public?
	 * @return
	 */
	public boolean isPublic() {
		return publicc;
	}

	/**
	 * Set if this geodesk is public.
	 * @param publik
	 */
	public void setPublic(boolean publik) {
		this.publicc = publik;
	}

	/**
	 * Get the blueprint on which this geodesk is based.
	 * @return
	 */
	public Blueprint getBlueprint() {
		return blueprint;
	}

	/**
	 * Set the blueprint on which this geodesk is based.
	 * @param blueprint
	 */
	public void setBlueprint(Blueprint blueprint) {
		this.blueprint = blueprint;
	}

	/**
	 * Get the geodesk id. This is the id that is used in the url.
	 * @return the geodesk id
	 */
	public String getGeodeskId() {
		return geodeskId;
	}

	/**
	 * Set the geodesk id. This is the id that is used in the url.
	 * @param geodeskId
	 */
	public void setGeodeskId(String geodeskId) {
		this.geodeskId = geodeskId;
	}

	/**
	 * Get territories that have access to this geodesk.
	 * @return
	 */
	public List<Territory> getTerritories() {
		return groups;
	}

	/**
	 * Set the territories that have access to this geodesk.
	 * @param groups
	 */
	public void setGroups(List<Territory> groups) {
		this.groups = groups;
	}

	/**
	 * Is this geodesk deleted.
	 * @return
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * Set if this geodesk is deleted.
	 * @param deleted
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * Get the owner of a geodesk, the owner can edit the geodesk.
	 * @return the owner
	 */
	public Territory getOwner() {
		return owner;
	}

	/**
	 * Set the owner of the geodesk.
	 */
	public void setOwner(Territory owner) {
		this.owner = owner;
	}

	/**
	 * Check if the geodesk must be filtered by creator territory.
	 * @return
	 */
	public boolean mustFilterByCreatorTerritory() {
		return (isPublic() && (isLimitToCreatorTerritory() || getBlueprint().isLimitToCreatorTerritory()));
	}

	/**
	 * Check if the geodesk must be filtered by user territory.
	 * @return
	 */
	public boolean mustFilterByUserTerritory() {
		return (!isPublic() && (isLimitToUserTerritory() || getBlueprint().isLimitToUserTerritory()));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((geodeskId == null) ? 0 : geodeskId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Geodesk other = (Geodesk) obj;
		if (geodeskId == null) {
			if (other.geodeskId != null) {
				return false;
			}
		} else if (!geodeskId.equals(other.geodeskId)) {
			return false;
		}
		return true;
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
	public void setMainMapClientWidgetInfos(Map<String, ClientWidgetInfo> mainMapClientWidgetInfos) {
		this.mainMapClientWidgetInfos = mainMapClientWidgetInfos;
	}

	@Override
	public Map<String, ClientWidgetInfo> getMainMapClientWidgetInfos() {
		return mainMapClientWidgetInfos;
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

	@Override
	public String getUserApplicationKey() {
		return getBlueprint().getUserApplicationKey();
	}

	@Override
	public void setUserApplicationKey(String userApplicationKey) {
		getBlueprint().setUserApplicationKey(userApplicationKey);
	}

}
