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
package org.geomajas.command.dto;

import org.geomajas.command.CommandResponse;
import org.geomajas.configuration.client.ClientApplicationInfo;

/**
 * <p>
 * This result contains the needed configuration metadata for setting up the client.
 * </p>
 *
 * @author Pieter De Graef
 */
public class GetConfigurationResponse extends CommandResponse {

	private static final long serialVersionUID = 151L;

	private ClientApplicationInfo application;

	// Constructors:

	public GetConfigurationResponse() {
	}

	public GetConfigurationResponse(ClientApplicationInfo application) {
		this.application = application;
	}

	// Getters and setters:

	public ClientApplicationInfo getApplication() {
		return application;
	}

	public void setApplication(ClientApplicationInfo application) {
		this.application = application;
	}

	@Override
	public String toString() {
		return "GetConfigurationResponse{" +
				"application=" + application +
				'}';
	}
}
