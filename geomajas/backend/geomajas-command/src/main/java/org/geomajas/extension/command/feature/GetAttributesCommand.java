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

import java.util.List;
import java.util.Map;

import org.geomajas.command.Command;
import org.geomajas.extension.command.dto.GetAttributesRequest;
import org.geomajas.extension.command.dto.GetAttributesResponse;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.service.FilterService;
import org.geomajas.service.VectorLayerService;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Get attributes for a feature. This is needed when features are loaded lazily.
 * 
 * @author Pieter De Graef
 */
@Component()
public class GetAttributesCommand implements Command<GetAttributesRequest, GetAttributesResponse> {

	@Autowired
	private FilterService filterCreator;

	@Autowired
	private VectorLayerService layerService;

	public GetAttributesResponse getEmptyCommandResponse() {
		return new GetAttributesResponse();
	}

	public void execute(GetAttributesRequest request, GetAttributesResponse response) throws Exception {
		if (null == request.getLayerId()) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "layerId");
		}
		if (null == request.getFeatureIds()) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "featureIds");
		}

		String layerId = request.getLayerId();
		String[] featureIds = request.getFeatureIds();
		if (featureIds.length > 0) {
			Filter filter = filterCreator.createFidFilter(featureIds);

			List<InternalFeature> features = layerService.getFeatures(layerId, null, filter, null,
					VectorLayerService.FEATURE_INCLUDE_ATTRIBUTES);

			Map<String, Object>[] attributes = new Map[featureIds.length];
			for (InternalFeature feature : features) {
				String id = feature.getId();
				int index = searchFeatureIndex(id, featureIds);
				if (index >= 0) {
					attributes[index] = feature.getAttributes();
				}
			}
			response.setAttributes(attributes);
		}
	}

	/**
	 * Searching for the index through the feature array, for each feature = x
	 * time :-(
	 *
	 * @param id id to find index of
	 * @param featureIds array in which to find the id
	 * @return index of id in array or -1 when not found
	 */
	private int searchFeatureIndex(String id, String[] featureIds) {
		if (id != null) {
			for (int i = 0; i < featureIds.length; i++) {
				if (id.equals(featureIds[i])) {
					return i;
				}
			}
		}
		return -1;
	}

}