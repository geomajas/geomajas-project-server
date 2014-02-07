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

package org.geomajas.plugin.staticsecurity.command.staticsecurity;

import org.geomajas.command.Command;
import org.geomajas.command.EmptyCommandRequest;
import org.geomajas.command.SuccessCommandResponse;
import org.geomajas.plugin.staticsecurity.security.AuthenticationTokenService;
import org.geomajas.plugin.staticsecurity.security.StaticSecurityService;
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

	@Override
	public SuccessCommandResponse getEmptyCommandResponse() {
		return new SuccessCommandResponse();
	}

	@Override
	public void execute(EmptyCommandRequest emptyCommandRequest, SuccessCommandResponse commandResponse)
			throws Exception {
		commandResponse.setSuccess(false);
		for (Authentication auth : securityContext.getSecurityServiceResults()) {
			if (StaticSecurityService.SECURITY_SERVICE_ID.equals(auth.getSecurityServiceId())) {
				tokenService.logout(securityContext.getToken());
				commandResponse.setSuccess(true);
			}
		}
	}
}
