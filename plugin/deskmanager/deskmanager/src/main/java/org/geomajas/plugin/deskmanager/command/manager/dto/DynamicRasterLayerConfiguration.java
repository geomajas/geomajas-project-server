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
package org.geomajas.plugin.deskmanager.command.manager.dto;

import org.geomajas.configuration.LayerInfo;
import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.plugin.deskmanager.domain.dto.DynamicLayerConfiguration;

/**
 * Configuration object for dynamic raster layer configuration.
 *
 * @author Kristof Heirwegh
 * @author Oliver May
 */
public class DynamicRasterLayerConfiguration extends DynamicLayerConfiguration {

	private static final long serialVersionUID = 1L;

	private ClientRasterLayerInfo clientLayerInfo;

	private RasterLayerInfo layerInfo; // the serializable part of a serverlayer

	private boolean featureInfoEnabled;

	private String featureInfoFormat;

	public ClientRasterLayerInfo getClientRasterLayerInfo() {
		return clientLayerInfo;
	}

	public void setClientRasterLayerInfo(ClientRasterLayerInfo clientLayerInfo) {
		this.clientLayerInfo = clientLayerInfo;
	}

	public RasterLayerInfo getRasterLayerInfo() {
		return layerInfo;
	}

	public void setRasterLayerInfo(RasterLayerInfo layerInfo) {
		this.layerInfo = layerInfo;
	}

	// -------------------------------------------------

	public ClientLayerInfo getClientLayerInfo() {
		return clientLayerInfo;
	}

	public LayerInfo getServerLayerInfo() {
		return layerInfo;
	}

	/**
	 * Is feature info enabled on this configuration.
	 *
	 * @return true if feature info is enabled.
	 */
	public boolean isFeatureInfoEnabled() {
		return featureInfoEnabled;
	}

	/**
	 * Set if feature info should be enabled on this configuration.
	 *
	 * @param featureInfoEnabled
	 */
	public void setFeatureInfoEnabled(boolean featureInfoEnabled) {
		this.featureInfoEnabled = featureInfoEnabled;
	}

	/**
	 * Get the feature info format fetched from the WMS server.
	 *
	 * @return the feature info format.
	 */
	public String getFeatureInfoFormat() {
		return featureInfoFormat;
	}

	/**
	 * Set the feature info format fetched from the WMS server.
	 *
	 * @param featureInfoFormat the feature info format.
	 */
	public void setFeatureInfoFormat(String featureInfoFormat) {
		this.featureInfoFormat = featureInfoFormat;
	}
}
