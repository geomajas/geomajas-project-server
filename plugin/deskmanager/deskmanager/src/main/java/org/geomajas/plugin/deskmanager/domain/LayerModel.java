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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyClass;
import javax.persistence.Table;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.layer.LayerType;
import org.geomajas.plugin.deskmanager.domain.dto.DynamicLayerConfiguration;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.MapKey;
import org.hibernate.annotations.Type;

/**
 * Domain object for a LayerModel.
 * 
 * @author Oliver May
 * @since 1.0.0
 */
@Api (allMethods = true)
@Entity
@Table(name = "gdm_layermodel")
public class LayerModel implements Serializable, Comparable<LayerModel> {

	private static final long serialVersionUID = 1L;

	private static final String LAYER_TYPE_RASTER = "Raster";

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id; // UUID

	@Column(name = "active")
	private boolean active;

	/**
	 * May this layer be shown in a public geodesk?
	 */
	@Column(name = "public")
	private boolean publiek;

	/**
	 * A user friendly name for this layer.
	 */
	@Column(name = "name", nullable = false)
	private String name;

	@Index(name = "gdm_layermodel_clientlayer_idx")
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
	private DynamicLayerConfiguration dynamicLayerConfiguration;

	// -------------------------------------------------

	@Column(name = "minimum_scale")
	@Type(type = "org.geomajas.plugin.deskmanager.domain.types.XmlSerialisationType")
	private ScaleInfo minScale;

	@Column(name = "maximum_scale")
	@Type(type = "org.geomajas.plugin.deskmanager.domain.types.XmlSerialisationType")
	private ScaleInfo maxScale;

	@Column(name = "default_visible")
	private boolean defaultVisible;

	@ElementCollection()
	@MapKeyClass(String.class)
	@Type(type = "org.geomajas.plugin.deskmanager.domain.types.XmlSerialisationType")
	@MapKey(type = @Type(type = "org.hibernate.type.StringType"))
	@JoinTable(name = "gdm_layermodel_clientwidgetinfo")
	private Map<String, ClientWidgetInfo> widgetInfo = new HashMap<String, ClientWidgetInfo>();
	
	// -------------------------------------------------

	/**
	 * Get the id of this layer model.
	 * @return the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the id of this layer model.
	 * 
	 * @param id the id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Is this layermodel active?
	 * 
	 * @return true if active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Set if this layer model is active.
	 * 
	 * @param active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Is this layer model available in public geodesks?
	 * 
	 * @return true if public
	 */
	public boolean isPublic() {
		return publiek;
	}

	/**
	 * Set if this layer model is available in public geodesks. 
	 * 
	 * @param publiek
	 */
	public void setPublic(boolean publiek) {
		this.publiek = publiek;
	}

	/**
	 * Get the name of this layer model.
	 * 
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of this layer model.
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the client layer id of this model, the reference to a spring bean.
	 * 
	 * @return the client layer id.
	 */
	public String getClientLayerId() {
		return clientLayerId;
	}

	/**
	 * Set the client layer id of this model, the reference to a spring bean.
	 * 
	 * @param clientLayerId
	 */
	public void setClientLayerId(String clientLayerId) {
		this.clientLayerId = clientLayerId;
	}

	/**
	 * Compare the layer models for natural sorting on the name.
	 * @return order
	 */
	public int compareTo(LayerModel o) {
		return name.compareTo(o.name);
	}

	/**
	 * Get the minimum scale where this layer is available.
	 * @return the scale
	 */
	public ScaleInfo getMinScale() {
		return minScale;
	}

	/**
	 * Set the minimum scale where this layer is available.
	 * @param minScale the scale
	 */
	public void setMinScale(ScaleInfo minScale) {
		this.minScale = minScale;
	}

	/**
	 * Get the maximum scale where this layer is available.
	 * @return the maximum scale
	 */
	public ScaleInfo getMaxScale() {
		return maxScale;
	}

	/**
	 * Set the maximum scale where this layer is available.
	 * 
	 * @param maxScale the maximum scale
	 */
	public void setMaxScale(ScaleInfo maxScale) {
		this.maxScale = maxScale;
	}

	/**
	 * Is this layerModel deleted?
	 * 
	 * @return true if deleted
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * Set if this layermodel is deleted.
	 * 
	 * @param deleted
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * Set the owner of this layermodel. This owner can update the layermodel.
	 * @return the owner
	 */
	public Territory getOwner() {
		return owner;
	}

	/**
	 * Set the owner of this layermodel. This owner can update the layermodel.
	 * @param owner
	 */
	public void setOwner(Territory owner) {
		this.owner = owner;
	}

	/**
	 * Is this layer default visible?
	 * @return true if the layer is default visible
	 */
	public boolean isDefaultVisible() {
		return defaultVisible;
	}

	/**
	 * Set if the layer is default visible.
	 * 
	 * @param defaultVisible
	 */
	public void setDefaultVisible(boolean defaultVisible) {
		this.defaultVisible = defaultVisible;
	}

	/**
	 * Get the {@link DynamicLayerConfiguration}. Should only be used when initializing.
	 *  
	 * @return the dynamic layer configuration
	 */
	public DynamicLayerConfiguration getDynamicLayerConfiguration() {
		return dynamicLayerConfiguration;
	}

	/**
	 * Set the dynamic layer configuration. Should only be used when initializing.
	 * @param dynamicLayerConfiguration
	 */
	public void setDynamicLayerConfiguration(DynamicLayerConfiguration dynamicLayerConfiguration) {
		this.dynamicLayerConfiguration = dynamicLayerConfiguration;
	}

	/**
	 * Get the type of layer. 
	 * 
	 * @return
	 */
	public String getLayerType() {
		return layerType;
	}

	/**
	 * Set the type of layer.
	 * @param layerType
	 */
	public void setLayerType(String layerType) {
		this.layerType = layerType;
	}

	/**
	 * Set the type of layer as internal LayerType.
	 * @param layerType
	 */
	public void setLayerType(LayerType layerType) {
		switch (layerType) {
			case RASTER:
				setLayerType(LAYER_TYPE_RASTER);
				break;
			default:
				setLayerType(layerType.getGeometryType());
				break;
		}
	}

	/**
	 * If the layer is read only: system layers are read only, Dynamic layers are not.
	 * @return
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * Set if the layer is read only: system layers are read only, Dynamic layers are not.
	 * @param readOnly
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	/**
	 * Is this layer a system layer? Read only is always a system layer.
	 * @return
	 */
	public boolean isSystemLayer() {
		return !isReadOnly();
	}
	
	/**
	 * Set if this is a system layer, this will set read only.
	 * @param systemLayer
	 */
	public void setSystemLayer(boolean systemLayer) {
		setReadOnly(!systemLayer);
	}

	/**
	 * Set default widgetinfo on this layer.
	 * @param widgetInfo the widgetInfo to set
	 */
	public void setWidgetInfo(Map<String, ClientWidgetInfo> widgetInfo) {
		this.widgetInfo = widgetInfo;
	}

	/**
	 * Get default widgetinfo on this layer.
	 * @return the widgetInfo
	 */
	public Map<String, ClientWidgetInfo> getWidgetInfo() {
		return widgetInfo;
	}
}
