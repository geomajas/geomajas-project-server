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
import org.geomajas.command.CommandRequest;

/**
 * Request object for {@link org.geomajas.command.configuration.GetMapConfigurationCommand}.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class GetMapConfigurationRequest implements CommandRequest {

	private static final long serialVersionUID = 151L;

	/**
	 * Command name for this request.
	 *
	 * @since 1.9.0
	 * */
	public static final String COMMAND = "command.configuration.GetMap";

	private String applicationId;

	private String mapId;

	/** No-arguments constructor. */
	public GetMapConfigurationRequest() {
	}

	/**
	 * Constructor which immediately initializes some fields.
	 *
	 * @param mapId map id
	 * @param applicationId application id
	 */
	public GetMapConfigurationRequest(String mapId, String applicationId) {
		this.mapId = mapId;
		this.applicationId = applicationId;
	}

	/**
	 * Get the application id.
	 *
	 * @return application id
	 */
	public String getApplicationId() {
		return applicationId;
	}

	/**
	 * Set the application id.
	 *
	 * @param applicationId application id
	 */
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	/**
	 * Get the map id.
	 *
	 * @return map id
	 */
	public String getMapId() {
		return mapId;
	}

	/**
	 * Set the map id.
	 *
	 * @param mapId map id
	 */
	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	@Override
	public String toString() {
		return "GetMapConfigurationRequest{" +
				"applicationId='" + applicationId + '\'' +
				", mapId='" + mapId + '\'' +
				'}';
	}
}
