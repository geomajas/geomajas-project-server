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
package org.geomajas.widget.layer.configuration.client;

import javax.validation.constraints.NotNull;

import org.geomajas.annotation.Api;


/**
 * Representation of a layer leaf node in the layer tree widget.
 * 
 * @author Oliver May
 * @since 1.0.0
 */
@Api
public class ClientLayerNodeInfo extends ClientAbstractNodeInfo {
	
	private static final long serialVersionUID = 100L;

	@NotNull
	private String layerId;

	/**
	 * The client layer id of the layer represented by this node.
	 * 
	 * @param layerId the client layer Id to set
	 */
	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	/**
	 * The client layer id of the layer represented by this node.
	 * 
	 * @return the client layer Id
	 */
	public String getLayerId() {
		return layerId;
	}
	
	
}
