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
package org.geomajas.command.feature;

import java.util.List;

import org.geomajas.command.Command;
import org.geomajas.command.dto.SearchAttributesRequest;
import org.geomajas.command.dto.SearchAttributesResponse;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.service.FilterService;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * Search for attribute values of a certain attribute within a certain vector layer. The response holds all possible
 * attribute values.
 * </p>
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

	/** {@inheritDoc} */
	public SearchAttributesResponse getEmptyCommandResponse() {
		return new SearchAttributesResponse();
	}

	/** {@inheritDoc} */
	public void execute(SearchAttributesRequest request, SearchAttributesResponse response) throws Exception {
		String layerId = request.getLayerId();
		if (null == layerId) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "layer");
		}

		Filter filter = Filter.INCLUDE;
		if (request.getFilter() != null) {
			filter = filterService.parseFilter(request.getFilter());
		}

		List<Attribute<?>> attributes = layerService.getAttributes(layerId, request.getAttributePath(), filter);
		response.setAttributes(attributes);
	}
}