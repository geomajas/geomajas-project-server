/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.command;

import org.geomajas.annotation.Api;

/**
 * Command request object which contains a layer id. Should be extended when you need a layer id as request
 * parameter.
 * <p/>
 * It is intended that this may be used for transaction support to assure transactions are started for relevant layers
 * only.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class LayerIdCommandRequest implements CommandRequest {

	private static final long serialVersionUID = 151L;
	private String layerId;

	/**
	 * Get the layer id.
	 *
	 * @return layer id
	 */
	public String getLayerId() {
		return layerId;
	}

	/**
	 * Set the layer id.
	 *
	 * @param layerId layer id
	 */
	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

}
