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

import java.util.HashMap;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyClass;
import javax.persistence.Table;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientWidgetInfo;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.MapKey;
import org.hibernate.annotations.Type;


/**
 * Domain object for a (configured) layer in a geodesk or blueprint.
 * Contains a clientLayerInfo configuration and a reference to the layerModel.
 * 
 * @author Oliver May
 * @since 1.0.0
 */
@Api (allMethods = true)
@Entity
@Table(name = "gdm_clientlayer")
public class ClientLayer {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id; // UUID

	//FIXME: we should do some pre and postprocessing on ClientLayerInfo. Now we persist to much information.
	@Type(type = "org.geomajas.plugin.deskmanager.domain.types.XmlSerialisationType")
	private ClientLayerInfo clientLayerInfo;

	@ManyToOne(fetch = FetchType.LAZY)
	private LayerModel layerModel;
	
	@ElementCollection()
	@MapKeyClass(String.class)
	@Type(type = "org.geomajas.plugin.deskmanager.domain.types.XmlSerialisationType")
	@MapKey(type = @Type(type = "org.hibernate.type.StringType"))
	@JoinTable(name = "gdm_clientlayer_clientwidgetinfo")
	private Map<String, ClientWidgetInfo> widgetInfo = new HashMap<String, ClientWidgetInfo>();
	
	/**
	 * Set the client layer info.
	 * 
	 * @param clientLayerInfo the clientLayerInfo to set
	 */
	public void setClientLayerInfo(ClientLayerInfo clientLayerInfo) {
		this.clientLayerInfo = clientLayerInfo;
	}

	/**
	 * Get the client Layer Info.
	 * 
	 * @return the clientLayerInfo
	 */
	public ClientLayerInfo getClientLayerInfo() {
		return clientLayerInfo;
	}

	/**
	 * Set the layer model.
	 * 
	 * @param layerModel the layerModel to set
	 */
	public void setLayerModel(LayerModel layerModel) {
		this.layerModel = layerModel;
	}

	/**
	 * Get the layer model.
	 * 
	 * @return the layerModel
	 */
	public LayerModel getLayerModel() {
		return layerModel;
	}

	/**
	 * Set the id.
	 * 
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the widget info's for this client layer.
	 * 
	 * @param widgetInfo the widgetInfo to set
	 */
	public void setWidgetInfo(Map<String, ClientWidgetInfo> widgetInfo) {
		this.widgetInfo = widgetInfo;
	}

	/**
	 * Get the widget info's for this layer.
	 * 
	 * @return the widgetInfo
	 */
	public Map<String, ClientWidgetInfo> getWidgetInfo() {
		return widgetInfo;
	}
}
