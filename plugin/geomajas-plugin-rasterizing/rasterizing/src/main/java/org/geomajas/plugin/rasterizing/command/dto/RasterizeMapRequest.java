/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.rasterizing.command.dto;

import org.geomajas.annotations.Api;
import org.geomajas.command.CommandRequest;
import org.geomajas.configuration.client.ClientMapInfo;

/**
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 *
 */
@Api(allMethods = true)
public class RasterizeMapRequest implements CommandRequest {

	private ClientMapInfo clientMapInfo;

	public ClientMapInfo getClientMapInfo() {
		return clientMapInfo;
	}

	public void setClientMapInfo(ClientMapInfo clientMapInfo) {
		this.clientMapInfo = clientMapInfo;
	}

}