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
package org.geomajas.plugin.rasterizing.command.dto;

import org.geomajas.annotation.Api;
import org.geomajas.command.CommandRequest;
import org.geomajas.configuration.client.ClientMapInfo;

/**
 * Request object for the {@link org.geomajas.plugin.rasterizing.command.rasterizing.RasterizeMapCommand}.
 *
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public class RasterizeMapRequest implements CommandRequest {

	private static final long serialVersionUID = 100L;

	/**
	 * Command name to handle the request.
	 */
	public static final String COMMAND = "command.rasterizing.RasterizeMap";

	private ClientMapInfo clientMapInfo;

	/**
	 * Get the info for the map to be rasterized.
	 *
	 * @return info for map to be rasterized
	 */
	public ClientMapInfo getClientMapInfo() {
		return clientMapInfo;
	}

	/**
	 * Set the info for the map to be rasterized.
	 *
	 * @param clientMapInfo info for map to be rasterized
	 */
	public void setClientMapInfo(ClientMapInfo clientMapInfo) {
		this.clientMapInfo = clientMapInfo;
	}

}