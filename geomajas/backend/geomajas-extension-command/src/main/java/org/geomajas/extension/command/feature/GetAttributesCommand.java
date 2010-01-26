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
import org.geomajas.extension.command.dto.GetAttributesRequest;
import org.geomajas.extension.command.dto.GetAttributesResponse;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.Layer;
import org.geomajas.layer.VectorLayer;
import org.geomajas.service.ApplicationService;
import org.geomajas.service.FilterCreator;
import org.opengis.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;

/**
 * ???
 * 
 * @author Pieter De Graef
 */
@Component()
public class GetAttributesCommand implements Command<GetAttributesRequest, GetAttributesResponse> {

	private final Logger log = LoggerFactory.getLogger(GetAttributesCommand.class);

	@Autowired
	private ApplicationService runtimeParameters;

	@Autowired
	private FilterCreator filterCreator;

	public GetAttributesResponse getEmptyCommandResponse() {
		return new GetAttributesResponse();
	}

	public void execute(GetAttributesRequest request, GetAttributesResponse response) throws Exception {
		if (null == request.getLayerId()) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "layerId");
		}

		String layerId = request.getLayerId();
		String[] featureIds = request.getFeatureIds();
		if (layerId != null && featureIds != null && featureIds.length > 0) {
			Layer layer;
			layer = runtimeParameters.getLayer(layerId);
			if (layer != null && layer instanceof VectorLayer) {
				VectorLayer vectorLayer = (VectorLayer) layer;

				// Prepare filtering:
				Filter filter = filterCreator.createFidFilter(featureIds);

				// Retrieve the features:
				Iterator<?> iterator;
				try {
					iterator = vectorLayer.getLayerModel().getElements(filter);
				} catch (GeomajasException ge) {
					response.getErrors().add(ge);
					return;
				}

				Map<String, Object>[] attributes = new Map[featureIds.length];

				// Get the attributes:
				while (iterator.hasNext()) {
					Object feature = iterator.next();
					if (feature != null) {
						try {
							String id = vectorLayer.getLayerModel().getFeatureModel().getId(feature);
							int index = searchFeatureIndex(id, featureIds);
							if (index >= 0) {
								attributes[index] = vectorLayer.getLayerModel().getFeatureModel()
										.getAttributes(feature);
							}
						} catch (GeomajasException e) {
							log.error("execute() problem", e);
						}
					}
				}

				response.setAttributes(attributes);
			}
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