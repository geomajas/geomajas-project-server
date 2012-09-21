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

	private ClientLayerInfo refLayerInfo;
	
	private String clientLayerIdReference;

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
	 * @param clientLayerIdReference the clientLayerIdReference to set
	 */
	public void setClientLayerIdReference(String clientLayerId) {
		this.clientLayerIdReference = clientLayerId;
	}

	/**
	 * @return the clientLayerIdReference
	 */
	public String getClientLayerIdReference() {
		return clientLayerIdReference;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clientLayerIdReference == null) ? 0 : clientLayerIdReference.hashCode());
		return result;
	}

	/* (non-Javadoc)
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
		LayerDto other = (LayerDto) obj;
		if (clientLayerIdReference == null) {
			if (other.clientLayerIdReference != null) {
				return false;
			}
		} else if (!clientLayerIdReference.equals(other.clientLayerIdReference)) {
			return false;
		}
		return true;
	}

	/**
	 * @param refLayerInfo the refLayerInfo to set
	 */
	public void setReferencedLayerInfo(ClientLayerInfo refLayerInfo) {
		this.refLayerInfo = refLayerInfo;
	}

	/**
	 * @return the refLayerInfo
	 */
	public ClientLayerInfo getReferencedLayerInfo() {
		return refLayerInfo;
	}
	
	
}
