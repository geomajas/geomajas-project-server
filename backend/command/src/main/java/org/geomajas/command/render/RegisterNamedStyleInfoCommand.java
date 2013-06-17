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
package org.geomajas.command.render;

import org.geomajas.annotation.Api;
import org.geomajas.command.Command;
import org.geomajas.command.dto.RegisterNamedStyleInfoRequest;
import org.geomajas.command.dto.RegisterNamedStyleInfoResponse;
import org.geomajas.service.StyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Command to register a style to the backend, so that it can be used in rendering of layers and legends.
 * 
 * @author Oliver May
 * 
 * @since 1.13.0
 */
@Api
@Component
public class RegisterNamedStyleInfoCommand implements
		Command<RegisterNamedStyleInfoRequest, RegisterNamedStyleInfoResponse> {

	@Autowired
	private StyleService styleService;

	@Override
	public RegisterNamedStyleInfoResponse getEmptyCommandResponse() {
		return new RegisterNamedStyleInfoResponse();
	}

	@Override
	public void execute(RegisterNamedStyleInfoRequest request, RegisterNamedStyleInfoResponse response)
			throws Exception {
		response.setStyleName(styleService.registerStyle(request.getLayerId(), request.getNamedStyleInfo()));
	}

}
