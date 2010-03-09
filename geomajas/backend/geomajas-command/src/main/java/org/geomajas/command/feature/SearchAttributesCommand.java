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
package org.geomajas.command.feature;

import java.util.List;

import org.geomajas.command.Command;
import org.geomajas.command.dto.SearchAttributesRequest;
import org.geomajas.command.dto.SearchAttributesResponse;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.service.FilterService;
import org.geomajas.service.VectorLayerService;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Search for attribute values of a certain attribute within a certain vector layer. The response holds all possible
 * attribute values.
 * 
 * @author Pieter De Graef
 */
@Component()
@Transactional(readOnly = true, rollbackFor = { Exception.class })
public class SearchAttributesCommand implements Command<SearchAttributesRequest, SearchAttributesResponse> {

	@Autowired
	private FilterService filterService;

	@Autowired
	private VectorLayerService layerService;

	public SearchAttributesResponse getEmptyCommandResponse() {
		return new SearchAttributesResponse();
	}

	public void execute(SearchAttributesRequest request, SearchAttributesResponse response) throws Exception {
		String layerId = request.getLayerId();
		if (null == layerId) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "layer");
		}

		Filter filter = Filter.INCLUDE;
		if (request.getFilter() != null) {
			filter = filterService.parseFilter(request.getFilter());
		}

		List<Attribute<?>> attributes = layerService.getAttributes(layerId, request.getAttributeName(), filter);
		response.setAttributes(attributes);
	}
}