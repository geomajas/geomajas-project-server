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
package org.geomajas.plugin.deskmanager.command.manager;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.command.Command;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetGeodesksRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetGeodesksResponse;
import org.geomajas.plugin.deskmanager.domain.Geodesk;
import org.geomajas.plugin.deskmanager.domain.dto.GeodeskDto;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
import org.geomajas.plugin.deskmanager.service.common.GeodeskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command to fetch a list of geodesks, accessible by the current logged in user.
 * 
 * @author Jan De Moerloose
 * @author Oliver May
 * @author Kristof Heirwegh
 */
@Component(GetGeodesksRequest.COMMAND)
@Transactional(readOnly = true, rollbackFor = { Exception.class })
public class GetGeodesksCommand implements Command<GetGeodesksRequest, GetGeodesksResponse> {

	@Autowired
	private GeodeskService geodeskService;

	@Autowired
	private DtoConverterService converterService;

	/** {@inheritDoc} */
	public void execute(GetGeodesksRequest request, GetGeodesksResponse response) throws Exception {

		List<GeodeskDto> geodesks = new ArrayList<GeodeskDto>();

		// no need to filter by group, this is done by security
		for (Geodesk l : geodeskService.getLoketten()) {
			geodesks.add(converterService.toDto(l, false));
		}

		response.setGeodesks(geodesks);
	}

	/** {@inheritDoc} */
	public GetGeodesksResponse getEmptyCommandResponse() {
		return new GetGeodesksResponse();
	}

}
