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
package org.geomajas.plugin.deskmanager.domain.dto;

import java.io.Serializable;

import org.geomajas.configuration.client.ClientLayerInfo;


/**
 * @author Oliver May
 *
 */
public class LayerDto implements Serializable {

	private static final long serialVersionUID = 100L;

	private LayerModelDto layerModel;
	
	private ClientLayerInfo layerInfo;

	private String clientLayerId;

	/**
	 * @param layerModel the layerModel to set
	 */
	public void setLayerModel(LayerModelDto layerModel) {
		this.layerModel = layerModel;
	}

	/**
	 * @return the layerModel
	 */
	public LayerModelDto getLayerModel() {
		return layerModel;
	}

	/**
	 * @param layerInfo the layerInfo to set
	 */
	public void setCLientLayerInfo(ClientLayerInfo layerInfo) {
		this.layerInfo = layerInfo;
	}

	/**
	 * @return the layerInfo
	 */
	public ClientLayerInfo getClientLayerInfo() {
		return layerInfo;
	}

	/**
	 * @param clientLayerId the clientLayerId to set
	 */
	public void setClientLayerId(String clientLayerId) {
		this.clientLayerId = clientLayerId;
	}

	/**
	 * @return the clientLayerId
	 */
	public String getClientLayerId() {
		return clientLayerId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clientLayerId == null) ? 0 : clientLayerId.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LayerDto other = (LayerDto) obj;
		if (clientLayerId == null) {
			if (other.clientLayerId != null)
				return false;
		} else if (!clientLayerId.equals(other.clientLayerId))
			return false;
		return true;
	}
	
	
}
