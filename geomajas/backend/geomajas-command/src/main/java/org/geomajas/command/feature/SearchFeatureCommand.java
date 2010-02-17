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

import org.geomajas.command.Command;
import org.geomajas.command.dto.SearchFeatureRequest;
import org.geomajas.command.dto.SearchFeatureResponse;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.feature.Feature;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.feature.SearchCriterion;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.FilterService;
import org.geomajas.service.VectorLayerService;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Search features based on a seat of search criteria.
 *
 * @author Pieter De Graef
 */
@Component()
@Transactional(readOnly = true, rollbackFor = { Exception.class })
public class SearchFeatureCommand implements Command<SearchFeatureRequest, SearchFeatureResponse> {

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private DtoConverterService converter;

	@Autowired
	private FilterService filterCreator;

	@Autowired
	private VectorLayerService layerService;

	public SearchFeatureResponse getEmptyCommandResponse() {
		return new SearchFeatureResponse();
	}

	public void execute(SearchFeatureRequest request, SearchFeatureResponse response) throws Exception {
		if (null == request.getLayerId()) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "layer");
		}
		if (null == request.getCrs()) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "crs");
		}

		String layerId = request.getLayerId();

		Filter filter = createFilter(request);

		List<InternalFeature> features = layerService.getFeatures(layerId,
				configurationService.getCrs(request.getCrs()), filter, null, request.getFeatureInclude());
		response.setLayerId(layerId);
		int max = request.getMax();
		if (max == SearchFeatureRequest.MAX_UNLIMITED) {
			max = features.size();
		}
		if (max > features.size()) {
			max = features.size();
		}
		Feature[] maxList = new Feature[max];
		for (int i = 0; i < max; i++) {
			maxList[i] = converter.toDto(features.get(i));
		}
		response.setFeatures(maxList);
	}

	private Filter createFilter(SearchFeatureRequest request) throws GeomajasException {
		Filter f = null;
		for (SearchCriterion criterion : request.getCriteria()) {
			String c = criterion.toString();
			c = c.replace('*', '%');
			c = c.replace('?', '_');
			try {
				if (f == null) {
					f = CQL.toFilter(c);
				} else {
					f = filterCreator.createLogicFilter(f, request.getBooleanOperator(), CQL.toFilter(c));
				}
			} catch (CQLException e) {
				throw new GeomajasException(e, ExceptionCode.FILTER_APPLY_PROBLEM, c);
			}
		}

		// AND the layer filter
		String filter = request.getFilter();
		if (filter != null) {
			try {
				if (f == null) {
					f = CQL.toFilter(filter);
				} else {
					f = filterCreator.createLogicFilter(CQL.toFilter(filter), "and", f);
				}
			} catch (CQLException e) {
				throw new GeomajasException(e, ExceptionCode.FILTER_APPLY_PROBLEM, filter);
			}
		}

		// If f is still null:
		if (f == null) {
			f = filterCreator.createTrueFilter();
		}

		return f;
	}

}