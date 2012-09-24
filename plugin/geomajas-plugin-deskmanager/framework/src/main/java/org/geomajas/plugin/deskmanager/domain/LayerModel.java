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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.plugin.deskmanager.domain.dto.LayerConfiguration;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
@Entity
@Table(name = "config_layermodels")
public class LayerModel implements Serializable, Comparable<LayerModel> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "active")
	private boolean active;

	/**
	 * Mag deze laag getoond worden in een publiek loket?
	 */
	@Column(name = "public")
	private boolean publiek;

	/**
	 * Een meer userfriendly name than clientLayerId, default = clientlayer.label
	 */
	@Column(name = "name", nullable = false)
	private String name;

	@Index(name = "config_layermodels_clientlayer_idx")
	@Column(name = "clientlayerid", nullable = false)
	private String clientLayerId; // referenced Bean name

	@Column(name = "layertype")
	private String layerType;

	// null == system layer
	@ManyToOne
	@JoinColumn(name = "owninggroup_id")
	private Territory owner;

	@Column(name = "readonly")
	private boolean readOnly;

	@Column(name = "deleted")
	private boolean deleted;

	@Basic(fetch = FetchType.LAZY)
	@Column(name = "layerConfiguration", nullable = true)
	@Type(type = "org.geomajas.plugin.deskmanager.domain.types.XmlSerialisationType")
	private LayerConfiguration layerConfiguration;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "tt_mails_layermodels")
	private List<MailAddress> mailAddresses = new ArrayList<MailAddress>();

	// -------------------------------------------------

	@Column(name = "minimum_scale")
	@Type(type = "org.geomajas.plugin.deskmanager.domain.types.XmlSerialisationType")
	private ScaleInfo minScale;

	@Column(name = "maximum_scale")
	@Type(type = "org.geomajas.plugin.deskmanager.domain.types.XmlSerialisationType")
	private ScaleInfo maxScale;

	@Column(name = "default_visible")
	private boolean defaultVisible;

	@Column(name = "show_in_legend")
	private boolean showInLegend;

	// -------------------------------------------------

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isPublic() {
		return publiek;
	}

	public void setPublic(boolean publiek) {
		this.publiek = publiek;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClientLayerId() {
		return clientLayerId;
	}

	public void setClientLayerId(String clientLayerId) {
		this.clientLayerId = clientLayerId;
	}

	public int compareTo(LayerModel o) {
		return name.compareTo(o.name);
	}

	public ScaleInfo getMinScale() {
		return minScale;
	}

	public void setMinScale(ScaleInfo minScale) {
		this.minScale = minScale;
	}

	public ScaleInfo getMaxScale() {
		return maxScale;
	}

	public void setMaxScale(ScaleInfo maxScale) {
		this.maxScale = maxScale;
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

	public boolean isDefaultVisible() {
		return defaultVisible;
	}

	public void setDefaultVisible(boolean defaultVisible) {
		this.defaultVisible = defaultVisible;
	}

	public boolean isShowInLegend() {
		return showInLegend;
	}

	public void setShowInLegend(boolean showInLegend) {
		this.showInLegend = showInLegend;
	}

	public LayerConfiguration getLayerConfiguration() {
		return layerConfiguration;
	}

	public void setLayerConfiguration(LayerConfiguration layerConfiguration) {
		this.layerConfiguration = layerConfiguration;
	}

	public String getLayerType() {
		return layerType;
	}

	public void setLayerType(String layerType) {
		this.layerType = layerType;
	}

	public List<MailAddress> getMailAddresses() {
		return mailAddresses;
	}

	public void setMailAddresses(List<MailAddress> mailAddresses) {
		this.mailAddresses = mailAddresses;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
}
