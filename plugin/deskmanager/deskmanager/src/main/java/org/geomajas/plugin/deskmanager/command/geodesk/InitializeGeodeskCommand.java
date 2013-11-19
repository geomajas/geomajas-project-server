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
package org.geomajas.plugin.deskmanager.command.geodesk;

import org.geomajas.command.Command;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.deskmanager.command.geodesk.dto.InitializeGeodeskRequest;
import org.geomajas.plugin.deskmanager.command.geodesk.dto.InitializeGeodeskResponse;
import org.geomajas.plugin.deskmanager.domain.Geodesk;
import org.geomajas.plugin.deskmanager.service.common.GeodeskConfigurationService;
import org.geomajas.plugin.deskmanager.service.common.GeodeskService;
import org.geomajas.security.GeomajasSecurityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Command that reads the application info. This checks security. The command also returns the version and build of this
 * application instance. This command is called when the geodesk is first loaded, so it also checks security and may 
 * trigger a login screen.
 * 
 * @author Oliver May
 * 
 */
@Component(InitializeGeodeskResponse.COMMAND)
public class InitializeGeodeskCommand implements Command<InitializeGeodeskRequest, InitializeGeodeskResponse> {

	@Autowired
	private GeodeskService geodeskService;

	@Autowired
	private GeodeskConfigurationService configurationService;

	@Autowired
	@Qualifier("deskmanager.version")
	private String deskmanagerVersion;

	@Autowired
	@Qualifier("deskmanager.build")
	private String deskmanagerBuild;

	public InitializeGeodeskResponse getEmptyCommandResponse() {
		return new InitializeGeodeskResponse();
	}

	public void execute(InitializeGeodeskRequest request, InitializeGeodeskResponse response) throws Exception {
		response.setDeskmanagerBuild(deskmanagerBuild);
		response.setDeskmanagerVersion(deskmanagerVersion);
		
		Geodesk loket = null;
		try {
			loket = geodeskService.getGeodeskByPublicId(request.getGeodeskId());
		} catch (GeomajasSecurityException e) {
			throw new GeomajasSecurityException(ExceptionCode.CREDENTIALS_MISSING_OR_INVALID);
		}
		if (loket != null) {
			response.setGeodeskIdentifier(request.getGeodeskId());
			response.setUserApplicationKey(loket.getBlueprint().getUserApplicationKey());

			ClientApplicationInfo loketConfig = configurationService.createGeodeskConfiguration(loket, false);
			response.setClientApplicationInfo(loketConfig);

		} else {
			throw new GeomajasException(ExceptionCode.APPLICATION_NOT_FOUND, request.getGeodeskId());
		}
	}
}
