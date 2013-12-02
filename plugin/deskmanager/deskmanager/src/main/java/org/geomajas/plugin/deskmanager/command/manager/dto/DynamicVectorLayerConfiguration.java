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
package org.geomajas.plugin.deskmanager.command.manager.dto;

import org.geomajas.configuration.LayerInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.plugin.deskmanager.domain.dto.DynamicLayerConfiguration;

/**
 * @author Kristof Heirwegh
 */
public class DynamicVectorLayerConfiguration extends DynamicLayerConfiguration {

	private static final long serialVersionUID = 1L;

	private ClientVectorLayerInfo clientVectorLayerInfo;

	private VectorLayerInfo vectorLayerInfo; // the serializable part of a serverlayer

	public ClientVectorLayerInfo getClientVectorLayerInfo() {
		return clientVectorLayerInfo;
	}

	public void setClientVectorLayerInfo(ClientVectorLayerInfo clientVectorLayerInfo) {
		this.clientVectorLayerInfo = clientVectorLayerInfo;
	}

	public VectorLayerInfo getVectorLayerInfo() {
		return vectorLayerInfo;
	}

	public void setVectorLayerInfo(VectorLayerInfo vectorLayerInfo) {
		this.vectorLayerInfo = vectorLayerInfo;
	}

	// -------------------------------------------------

	public ClientLayerInfo getClientLayerInfo() {
		return clientVectorLayerInfo;
	}

	public LayerInfo getServerLayerInfo() {
		return vectorLayerInfo;
	}
}
