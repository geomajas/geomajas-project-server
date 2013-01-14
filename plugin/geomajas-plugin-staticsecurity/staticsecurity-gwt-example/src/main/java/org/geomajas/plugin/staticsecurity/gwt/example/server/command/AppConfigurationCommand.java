/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.staticsecurity.gwt.example.server.command;

import org.geomajas.command.Command;
import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.dto.GetConfigurationRequest;
import org.geomajas.command.dto.GetConfigurationResponse;
import org.geomajas.plugin.staticsecurity.gwt.example.server.command.dto.AppConfigurationRequest;
import org.geomajas.plugin.staticsecurity.gwt.example.server.command.dto.AppConfigurationResponse;
import org.geomajas.plugin.staticsecurity.gwt.example.server.security.AppSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Get the map configuration and some application specific information.
 *
 * @author Joachim Van der Auwera
 */
// @extract-start AppConfigurationCommand, Replacement command to read the configuration
@Component
public class AppConfigurationCommand implements Command<AppConfigurationRequest, AppConfigurationResponse> {

	@Autowired
	private CommandDispatcher commandDispatcher;

	@Autowired
	private AppSecurityContext securityContext;

	public AppConfigurationResponse getEmptyCommandResponse() {
		return new AppConfigurationResponse();
	}

	public void execute(AppConfigurationRequest request, AppConfigurationResponse response) throws Exception {
		GetConfigurationResponse original = (GetConfigurationResponse) commandDispatcher.execute(
				GetConfigurationRequest.COMMAND, request, securityContext.getToken(), null);
		response.setApplication(original.getApplication());

		// Add app specific configuration
		response.setBlablaButtonAllowed(securityContext.isBlablaButtonAllowed());
	}

}
// @extract-end
