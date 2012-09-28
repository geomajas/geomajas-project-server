/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.command.common;

import org.geomajas.command.Command;
import org.geomajas.command.CommandRequest;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.deskmanager.command.common.dto.GetApplicationInfoResponse;
import org.geomajas.plugin.deskmanager.domain.Geodesk;
import org.geomajas.plugin.deskmanager.service.common.GeodeskConfigurationService;
import org.geomajas.plugin.deskmanager.service.common.GeodeskIdService;
import org.geomajas.plugin.deskmanager.service.common.GeodeskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Command that reads the application info. This checks security. The command also returns the version and build of this
 * application instance.
 * 
 * @author Oliver May
 * 
 */
@Component(GetApplicationInfoResponse.COMMAND)
public class GetApplicationInfoCommand implements Command<CommandRequest, GetApplicationInfoResponse> {

	@Autowired
	private GeodeskIdService geodeskIdService;

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

	public GetApplicationInfoResponse getEmptyCommandResponse() {
		return new GetApplicationInfoResponse();
	}

	public void execute(CommandRequest request, GetApplicationInfoResponse response) throws Exception {
		response.setDeskmanagerBuild(deskmanagerBuild);
		response.setDeskmanagerVersion(deskmanagerVersion);

		String id = geodeskIdService.getGeodeskIdentifier();
		Geodesk loket = null;
		loket = geodeskService.getGeodeskByPublicId(id); // this checks if loket is allowed
		if (loket != null) {
			response.setGeodeskIdentifier(id);
			response.setGeodeskTypeIdentifier(loket.getBlueprint().getUserApplicationKey());

			ClientApplicationInfo loketConfig = configurationService.createGeodeskConfiguration(loket, false);
			response.setClientApplicationInfo(loketConfig);

		} else {
			throw new GeomajasException(ExceptionCode.APPLICATION_NOT_FOUND, id);
		}
	}
}
