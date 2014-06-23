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
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.deskmanager.command.security.dto.GetTerritoryFromShpRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.GetTerritoryFromShpResponse;
import org.geomajas.plugin.deskmanager.service.security.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command for getting the {@link org.geomajas.geometry.Geometry} object in an uploaded shp file.
 * 
 * @author Jan Venstermans
 * 
 */
@Transactional
@Component(GetTerritoryFromShpRequest.COMMAND)
public class GetTerritoryFromShpCommand implements Command<GetTerritoryFromShpRequest, GetTerritoryFromShpResponse> {

	@Autowired
	private GroupService groupService;

	@Autowired
	private org.geomajas.service.DtoConverterService geomajasConverterService;

	private final Logger log = LoggerFactory.getLogger(GetTerritoryFromShpCommand.class);

	@Override
	public GetTerritoryFromShpResponse getEmptyCommandResponse() {
		return new GetTerritoryFromShpResponse();
	}

	@Override
	public void execute(GetTerritoryFromShpRequest request, GetTerritoryFromShpResponse response) throws Exception {
		Geometry geometry = geomajasConverterService.
				toDto(groupService.getGeometryOfShpFile(request.getShpFileToken(), request.getToCrs()));
		response.setGeometry(geometry);
		log.info("shapefile contains geometry: " + geometry);
	}

}
