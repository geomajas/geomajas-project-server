/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.plugin.springsecurity.command;

import org.geomajas.command.Command;
import org.geomajas.command.EmptyCommandRequest;
import org.geomajas.command.SuccessCommandResponse;
import org.geomajas.plugin.springsecurity.security.AuthenticationTokenService;
import org.geomajas.plugin.springsecurity.security.SpringSecurityService;
import org.geomajas.security.Authentication;
import org.geomajas.security.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Command which allows logging out.
 *
 * @author Joachim Van der Auwera
 */
@Component
public class LogoutCommand implements Command<EmptyCommandRequest, SuccessCommandResponse> {

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private AuthenticationTokenService tokenService;

	public SuccessCommandResponse getEmptyCommandResponse() {
		return new SuccessCommandResponse();
	}

	public void execute(EmptyCommandRequest emptyCommandRequest, SuccessCommandResponse commandResponse)
			throws Exception {
		commandResponse.setSucces(false);
		for (Authentication auth : securityContext.getSecurityServiceResults()) {
			if (SpringSecurityService.SECURITY_SERVICE_ID.equals(auth.getSecurityServiceId())) {
				tokenService.logout(securityContext.getToken());
				commandResponse.setSucces(true);
			}
		}
	}
}
