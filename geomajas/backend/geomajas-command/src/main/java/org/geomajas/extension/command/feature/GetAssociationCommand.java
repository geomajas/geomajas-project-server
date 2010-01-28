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
package org.geomajas.extension.command.feature;

import org.geomajas.command.Command;
import org.geomajas.extension.command.dto.GetAssociationRequest;
import org.geomajas.extension.command.dto.GetAssociationResponse;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.service.VectorLayerModelService;
import org.geotools.filter.text.cql2.CQL;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Get associated objects.
 *
 * @author Jan De Moerloose
 * @author Joachim Van der Auwera
 */
@Component()
public class GetAssociationCommand implements Command<GetAssociationRequest, GetAssociationResponse> {

	@Autowired
	private VectorLayerModelService layerModelService;

	public GetAssociationResponse getEmptyCommandResponse() {
		return new GetAssociationResponse();
	}

	public void execute(GetAssociationRequest request, GetAssociationResponse response) throws Exception {
		if (null == request.getLayerId()) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "layerId");
		}
		if (null == request.getAttributeName()) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "attributeName");
		}
		Filter filter;
		if (request.getFilter() != null) {
			filter = CQL.toFilter(request.getFilter());
		} else {
			filter = Filter.INCLUDE;
		}
		response.setObjects(layerModelService.getObjects(request.getLayerId(), request.getAttributeName(), filter));
	}

}
