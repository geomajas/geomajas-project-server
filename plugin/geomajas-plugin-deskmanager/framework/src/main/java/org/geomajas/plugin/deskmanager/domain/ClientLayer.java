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

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;


/**
 * Domain object for a (configured) layer in a geodesk or blueprint.
 * Contains a clientLayerInfo configuration and a reference to the layerModel.
 * 
 * @author Oliver May
 *
 */
@Entity
@Table(name = "config_clientlayers")
public class ClientLayer {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id; // UUID

	@Type(type = "org.geomajas.plugin.deskmanager.domain.types.XmlSerialisationType")
	private ClientLayerInfo clientLayerInfo;

	@ManyToOne(fetch = FetchType.LAZY)
	private LayerModel layerModel;
	
	/**
	 * @param clientLayerInfo the clientLayerInfo to set
	 */
	public void setClientLayerInfo(ClientLayerInfo clientLayerInfo) {
		this.clientLayerInfo = clientLayerInfo;
	}

	/**
	 * Returns the clientLayerInfo. This DeskmanagerClientLayerInfoI does not contain the featureinfo.
	 * 
	 * @return the clientLayerInfo
	 */
	public ClientLayerInfo getClientLayerInfo() {
		return clientLayerInfo;
	}

	/**
	 * @param layerModel the layerModel to set
	 */
	public void setLayerModel(LayerModel layerModel) {
		this.layerModel = layerModel;
	}

	/**
	 * @return the layerModel
	 */
	public LayerModel getLayerModel() {
		return layerModel;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

}
