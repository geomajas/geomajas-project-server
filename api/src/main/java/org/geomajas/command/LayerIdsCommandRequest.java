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

package org.geomajas.command;

import org.geomajas.annotation.Api;

/**
 * Command request object which contains a list of layer ids. Should be extended when you need multiple layer ids as
 * request parameter.
 * <p/>
 * It is intended that this may be used for transaction support to assure transactions are started for relevant layers
 * only.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class LayerIdsCommandRequest implements CommandRequest {

	private static final long serialVersionUID = 160L;
	private String[] layerIds;

	/**
	 * Get the layer ids.
	 *
	 * @return layer ids
	 */
	public String[] getLayerIds() {
		return layerIds;
	}

	/**
	 * Set the layer ids.
	 *
	 * @param layerIds layer ids
	 */
	public void setLayerIds(String[] layerIds) {
		this.layerIds = layerIds;
	}
}
