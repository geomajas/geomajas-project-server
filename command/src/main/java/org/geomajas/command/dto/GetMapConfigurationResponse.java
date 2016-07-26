/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.command.dto;

import org.geomajas.annotation.Api;
import org.geomajas.command.CommandResponse;
import org.geomajas.configuration.client.ClientMapInfo;

/**
 * <p>
 * This result contains the configuration metadata for a specific layertree, together with the tools definitions.
 * </p>
 *
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
public class GetMapConfigurationResponse extends CommandResponse {

	private static final long serialVersionUID = 151L;

	private ClientMapInfo mapInfo;

	/** No-arguments constructor. */
	public GetMapConfigurationResponse() {
	}

	/** Constructor with map info. */
	public GetMapConfigurationResponse(ClientMapInfo mapInfo) {
		this.mapInfo = mapInfo;
	}

	/**
	 * Get map info.
	 *
	 * @return map info
	 */
	public ClientMapInfo getMapInfo() {
		return mapInfo;
	}

	/**
	 * Set map info.
	 *
	 * @param mapInfo map info
	 */
	public void setMapInfo(ClientMapInfo mapInfo) {
		this.mapInfo = mapInfo;
	}

	@Override
	public String toString() {
		return "GetMapConfigurationResponse{" +
				"mapInfo=" + mapInfo +
				'}';
	}
}