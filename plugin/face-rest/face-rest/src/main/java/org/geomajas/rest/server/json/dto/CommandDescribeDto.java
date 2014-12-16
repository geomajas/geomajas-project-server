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

package org.geomajas.rest.server.json.dto;

import org.geomajas.command.CommandRequest;
import org.geomajas.command.CommandResponse;

import java.io.Serializable;

/**
 * Dto object than encapsulates empty {@link CommandRequest} and empty {@link CommandResponse} objects.
 *
 * @author Dosi Bingov
 */
public class CommandDescribeDto implements Serializable {
	private CommandRequest jsonRequest;

	private CommandResponse jsonResponse;

	public CommandDescribeDto() {
	}

	public CommandDescribeDto(CommandRequest jsonRequest, CommandResponse jsonResponse) {
		this.jsonRequest = jsonRequest;
		this.jsonResponse = jsonResponse;
	}


	public CommandRequest getJsonRequest() {
		return jsonRequest;
	}

	public void setJsonRequest(CommandRequest jsonRequest) {
		this.jsonRequest = jsonRequest;
	}

	public CommandResponse getJsonResponse() {
		return jsonResponse;
	}

	public void setJsonResponse(CommandResponse jsonResponse) {
		this.jsonResponse = jsonResponse;
	}
}
