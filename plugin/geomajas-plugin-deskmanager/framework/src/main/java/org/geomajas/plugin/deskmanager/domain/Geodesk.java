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
import javax.persistence.MapKeyClass;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.OrderColumn;

import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
@Entity
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

	@OneToMany(cascade = CascadeType.ALL)
	@OrderColumn(name = "sortorder")
	@JoinTable(name = "geodesk_mainlayer")
	private List<Layer> mainMapLayers = new ArrayList<Layer>();

	@OneToMany(cascade = CascadeType.ALL)
	@OrderColumn(name = "sortorder")
	@JoinTable(name = "geodesk_overviewlayer")
	private List<Layer> overviewMapLayers = new ArrayList<Layer>();

	@ElementCollection(fetch = FetchType.EAGER)
	@MapKeyClass(String.class)
	@Type(type = "org.geomajas.plugin.deskmanager.domain.types.XmlSerialisationType")
	private Map<String, ClientWidgetInfo> applicationClientWidgetInfos = new HashMap<String, ClientWidgetInfo>();

	@ElementCollection(fetch = FetchType.EAGER)
	@MapKeyClass(String.class)
	@Type(type = "org.geomajas.plugin.deskmanager.domain.types.XmlSerialisationType")
	private Map<String, ClientWidgetInfo> overviewMapClientWidgetInfos = new HashMap<String, ClientWidgetInfo>();
	
	@ElementCollection(fetch = FetchType.EAGER)
	@MapKeyClass(String.class)
	@Type(type = "org.geomajas.plugin.deskmanager.domain.types.XmlSerialisationType")
	private Map<String, ClientWidgetInfo> mainMapClientWidgetInfos = new HashMap<String, ClientWidgetInfo>();

	// null == superuser
	@ManyToOne
	@JoinColumn(name = "owninggroup_id")
	private Territory owner;

	@ManyToMany(cascade = { CascadeType.ALL }, targetEntity = Territory.class, fetch = FetchType.LAZY)
	@JoinTable(name = "tt_groups_geodesks", 
		joinColumns = @JoinColumn(name = "geodesk_id"), inverseJoinColumns = { @JoinColumn(name = "group_id") })
	// @Fetch(FetchMode.JOIN) -- cannot use join because of the ManyToOne field 'owner' of the same type
	@OrderBy("name desc")
	private List<Territory> groups = new ArrayList<Territory>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinTable(name = "tt_mails_geodesks")
	private List<MailAddress> mailAddresses = new ArrayList<MailAddress>();

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

	/**
	 * This is just the property getter, do not use to test if filter should be set, use mustFilterByGeodeskTerritory().
	 */
	public boolean isLimitToCreatorTerritory() {
		return limitToCreatorTerritory;
	}

	public void setLimitToCreatorTerritory(boolean limitToCreatorTerritory) {
		this.limitToCreatorTerritory = limitToCreatorTerritory;
	}

	/**
	 * This is just the property getter, do not use to test if filter should be set, use mustFilterByUserTerritory().
	 */
	public boolean isLimitToUserTerritory() {
		return limitToUserTerritory;
	}

	public void setLimitToUserTerritory(boolean limitToUserTerritory) {
		this.limitToUserTerritory = limitToUserTerritory;
	}

	public boolean isPublic() {
		return publicc;
	}

	public void setPublic(boolean publik) {
		this.publicc = publik;
	}

	public Blueprint getBlueprint() {
		return blueprint;
	}

	public void setBlueprint(Blueprint blueprint) {
		this.blueprint = blueprint;
	}

	public String getGeodeskId() {
		return geodeskId;
	}

	public void setGeodeskId(String geodeskId) {
		this.geodeskId = geodeskId;
	}

	public List<Territory> getTerritories() {
		return groups;
	}

	public void setGroups(List<Territory> groups) {
		this.groups = groups;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Territory getOwner() {
		return owner;
	}

	public void setOwner(Territory owner) {
		this.owner = owner;
	}

	public List<MailAddress> getMailAddresses() {
		return mailAddresses;
	}

	public void setMailAddresses(List<MailAddress> mailAddresses) {
		this.mailAddresses = mailAddresses;
	}

	// -------------------------------------------------

	public boolean mustFilterByCreatorTerritory() {
		return (isPublic() && (isLimitToCreatorTerritory() || getBlueprint().isLimitToCreatorTerritory()));
	}

	public boolean mustFilterByUserTerritory() {
		return (!isPublic() && (isLimitToUserTerritory() || getBlueprint().isLimitToUserTerritory()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((geodeskId == null) ? 0 : geodeskId.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
	 * @param mainMapLayers the mainMapLayers to set
	 */
	public void setMainMapLayers(List<Layer> layers) {
		this.mainMapLayers = layers;
	}

	/**
	 * @return the mainMapLayers
	 */
	public List<Layer> getMainMapLayers() {
		return mainMapLayers;
	}
	/**
	 * @param mainMapLayers the mainMapLayers to set
	 */

	public void setOverviewMapLayers(List<Layer> layers) {
		this.overviewMapLayers = layers;
	}

	/**
	 * @return the mainMapLayers
	 */
	public List<Layer> getOverviewMapLayers() {
		return overviewMapLayers;
	}

	public String getUserApplicationKey() {
		return getBlueprint().getUserApplicationKey();
	}

	public void setUserApplicationKey(String userApplicationKey) {
		getBlueprint().setUserApplicationKey(userApplicationKey);
	}

}
