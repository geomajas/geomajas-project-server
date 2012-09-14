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
package org.geomajas.plugin.deskmanager.command.manager;

import org.geomajas.command.Command;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetGeodeskUrlBaseRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetGeodeskUrlBaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 * FIXME: this is specific for magdageo (vo, lo, public). Should be only one url!
 */
@Component(GetGeodeskUrlBaseRequest.COMMAND)
public class GetGeodeskUrlBaseCommand implements Command<GetGeodeskUrlBaseRequest, GetGeodeskUrlBaseResponse> {

	@Autowired(required = false)
	@Qualifier("loketUrlBasePublic")
	private String loketUrlBasePublic;

	@Autowired(required = false)
	@Qualifier("loketUrlBaseVO")
	private String loketUrlBaseVO;

	@Autowired(required = false)
	@Qualifier("loketUrlBaseLO")
	private String loketUrlBaseLO;

	public void execute(GetGeodeskUrlBaseRequest request, GetGeodeskUrlBaseResponse response) throws Exception {
		response.setLoketUrlBaseVO(loketUrlBaseVO);
		response.setLoketUrlBaseLO(loketUrlBaseLO);
		response.setLoketUrlBasePublic(loketUrlBasePublic);
	}

	public GetGeodeskUrlBaseResponse getEmptyCommandResponse() {
		return new GetGeodeskUrlBaseResponse();
	}

}
