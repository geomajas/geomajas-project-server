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
 * @author Kristof Heirwegh
 */
public class DynamicRasterLayerConfiguration extends DynamicLayerConfiguration {

	private static final long serialVersionUID = 1L;

	private ClientRasterLayerInfo clientLayerInfo;

	private RasterLayerInfo layerInfo; // the serializable part of a serverlayer

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
}
