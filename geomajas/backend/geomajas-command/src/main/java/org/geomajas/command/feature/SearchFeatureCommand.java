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
import org.geomajas.command.dto.SearchFeatureRequest;
import org.geomajas.command.dto.SearchFeatureResponse;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.Feature;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.feature.SearchCriterion;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.FilterService;
import org.geomajas.service.VectorLayerService;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
	private FilterService filterService;

	@Autowired
	private VectorLayerService layerService;

	public SearchFeatureResponse getEmptyCommandResponse() {
		return new SearchFeatureResponse();
	}

	public void execute(SearchFeatureRequest request, SearchFeatureResponse response) throws Exception {
		String layerId = request.getLayerId();
		if (null == layerId) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "layer");
		}
		String crs = request.getCrs();
		if (null == crs) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "crs");
		}

		Filter filter = createFilter(request, layerId);

		List<InternalFeature> features = layerService.getFeatures(layerId, configurationService
				.getCrs(request.getCrs()), filter, null, request.getFeatureIncludes(), 0, request.getMax());
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
			Feature feature = converter.toDto(features.get(i));
			feature.setCrs(crs);
			maxList[i] = feature;
		}
		response.setFeatures(maxList);
	}

	private Filter createFilter(SearchFeatureRequest request, String layerId) throws GeomajasException {
		Filter f = null;
		VectorLayer layer = configurationService.getVectorLayer(layerId);
		String idName = layer.getLayerInfo().getFeatureInfo().getIdentifier().getName();
		for (SearchCriterion criterion : request.getCriteria()) {
			Filter temp;
			String attributeName = criterion.getAttributeName();
			if (SearchFeatureRequest.ID_ATTRIBUTE.equals(attributeName) || attributeName.equals(idName)) {
				temp = filterService.createFidFilter(new String[] { criterion.getValue() });
			} else {
				String c = criterion.toString().replace('*', '%').replace('?', '_');
				temp = filterService.parseFilter(c);
			}
			if (f == null) {
				f = temp;
			} else {
				f = filterService.createLogicFilter(f, request.getBooleanOperator(), temp);
			}
		}

		// AND the layer filter
		String filter = request.getFilter();
		if (filter != null) {
			if (f == null) {
				f = filterService.parseFilter(filter);
			} else {
				f = filterService.createAndFilter(filterService.parseFilter(filter), f);
			}
		}

		// If f is still null:
		if (f == null) {
			f = filterService.createTrueFilter();
		}

		return f;
	}
}