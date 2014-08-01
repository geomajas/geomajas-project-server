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
package org.geomajas.plugin.deskmanager.command.security;

import org.geomajas.command.Command;
import org.geomajas.plugin.deskmanager.command.security.dto.AuthenticateTokenRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.AuthenticationResponse;
import org.geomajas.plugin.deskmanager.security.AuthenticationService;
import org.geomajas.security.GeomajasSecurityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Command that will check whether an authentication token in active.
 *
 * @author Jan Venstermans
 */
@Component(AuthenticateTokenRequest.COMMAND)
public class AuthenticateTokenCommand implements Command<AuthenticateTokenRequest, AuthenticationResponse> {

	private final Logger log = LoggerFactory.getLogger(AuthenticateTokenCommand.class);

	@Autowired
	private AuthenticationService authenticationService;

	public AuthenticationResponse getEmptyCommandResponse() {
		return new AuthenticationResponse();
	}

	public void execute(AuthenticateTokenRequest request, AuthenticationResponse response) throws Exception {
		String authenticationToken = request.getAuthenticationSessionToken();
		try {
			String username = authenticationService.getUsernameFromToken(request.getAuthenticationSessionToken());
			if (username != null) {
				response.setUsername(username);
				response.setAuthenticationToken(authenticationToken);
			}
			log.info("Authentication Request (via token) for user " + username + " successful.");
		} catch (GeomajasSecurityException ex) {
			log.info("Authentication exception for token " + authenticationToken);
			// return an empty response, in compliance with static security
		}
	}
}