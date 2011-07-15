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
package org.geomajas.command.dto;

import org.geomajas.command.CommandResponse;
import org.geomajas.configuration.client.ClientUserDataInfo;

/**
 * <p>
 * Response object for the {@link org.geomajas.command.configuration.GetClientUserDataCommand}. It return the requested
 * configuration object to the client.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.10.0
 */
public class GetClientUserDataResponse extends CommandResponse {

	private static final long serialVersionUID = 1100L;

	private ClientUserDataInfo information;

	// Constructors:

	public GetClientUserDataResponse() {
	}

	// Getters and setters:

	public ClientUserDataInfo getInformation() {
		return information;
	}

	public void setInformation(ClientUserDataInfo information) {
		this.information = information;
	}
}