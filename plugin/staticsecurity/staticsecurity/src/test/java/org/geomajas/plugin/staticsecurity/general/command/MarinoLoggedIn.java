/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.staticsecurity.general.command;

import org.geomajas.command.Command;
import org.geomajas.command.CommandResponse;
import org.geomajas.command.EmptyCommandRequest;
import org.geomajas.global.ExceptionCode;
import org.geomajas.security.GeomajasSecurityException;
import org.geomajas.security.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Command which verifies that the correct user is logged in (checking security context setup).
 *
 * @author Joachim Van der Auwera
 */
@Component
public class MarinoLoggedIn implements Command<EmptyCommandRequest, CommandResponse> {

	@Autowired
	private SecurityContext securityContext;

	public CommandResponse getEmptyCommandResponse() {
		return new CommandResponse();
	}

	public void execute(EmptyCommandRequest emptyCommandRequest, CommandResponse commandResponse) throws Exception {
		if(securityContext.getToken() == null){
			throw new GeomajasSecurityException(ExceptionCode.TEST);
		}
		if(!"marino".equals(securityContext.getUserId())){
			throw new GeomajasSecurityException(ExceptionCode.TEST);
		}
	}
}
