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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.command.Command;
import org.geomajas.command.dto.SearchByLocationRequest;
import org.geomajas.command.dto.SearchByLocationResponse;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.Feature;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.service.ApplicationService;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.FilterService;
import org.geomajas.service.VectorLayerService;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Geometry;

/**
 * <p>
 * Execute a search for features by location. The location can be any type of geometry. Although this will mostly be a
 * coordinate, it is possible to search using a Polygon or LineString as location. The command only expects the location
 * to be described using the map's coordinate system. Also, if a buffer is specified, this buffer will be added to the
 * location geometry before executing the search.
 * </p>
 * <p>
 * In addition to the location, it is also possible to describe what kind of geometric query to use (supported are:
 * intersects, contains, touches and within) and in case of the "intersects" query, it is even possible to describe what
 * ratio of intersection is required (number between 0 and 1). The default query will also be an intersection. For
 * example, if queryType = 1 (intersects) and ratio = 0.7, then only features whose geometry lies 70% within the given
 * location will be accepted.
 * </p>
 * <p>
 * Last but not least, it is required that at least one layer-ID is given to search in. If multiple layers are given, an
 * extra parameter comes into play: searchType. This searchType specifies whether to search features in all given
 * layers, to to start searching from the first layer, and stop once features are found (can be in the first layer, or
 * in the second, ...).
 * </p>
 * <p>
 * It will go over all given layers (provided they're vector layers), and fetch the features, using the location
 * geometry and the query type. In case the query type is "intersects", the overlapping ratio is also checked. The
 * resulting list of features is added to the command result so it can be send back to the client.
 * </p>
 * 
 * @author Pieter De Graef
 */
@Component
@Transactional(readOnly = true)
public class SearchByLocationCommand implements Command<SearchByLocationRequest, SearchByLocationResponse> {

	@Autowired
	private ApplicationService applicationService;

	@Autowired
	private DtoConverterService converter;

	@Autowired
	private FilterService filterCreator;

	@Autowired
	private VectorLayerService layerService;

	// -------------------------------------------------------------------------
	// Command implementation:
	// -------------------------------------------------------------------------

	/**
	 * The command's execution method. It will go over all given layers (provided they're vector layers), and fetch the
	 * features, using the location geometry and the query type. In case the query type is "intersects", the overlapping
	 * ratio is also checked. The resulting list of features is added to the command result so it can be send back to
	 * the client.
	 */
	public void execute(SearchByLocationRequest request, SearchByLocationResponse response) throws Exception {
		if (null == request.getLayerIds()) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "layerIds");
		}
		if (null == request.getCrs()) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "crs");
		}
		String[] layerIds = request.getLayerIds();
		Geometry location = converter.toInternal(request.getLocation());
		int queryType = request.getQueryType();
		double ratio = request.getRatio();
		int searchType = request.getSearchType();

		if (layerIds != null && layerIds.length > 0) {
			for (String layerId : layerIds) {
				VectorLayer vectorLayer = applicationService.getVectorLayer(layerId);
				if (vectorLayer != null) {
					String geomName = vectorLayer.getLayerInfo().getFeatureInfo().getGeometryType().getName();

					// Check if a buffer should be added around the location:
					Geometry geometry = location;
					if (request.getBuffer() > 0) {
						geometry = location.buffer(request.getBuffer());
					}

					// Create the correct Filter object:
					Filter f = null;
					switch (queryType) {
						case SearchByLocationRequest.QUERY_INTERSECTS:
							f = filterCreator.createIntersectsFilter(geometry, geomName);
							break;
						case SearchByLocationRequest.QUERY_CONTAINS:
							f = filterCreator.createContainsFilter(geometry, geomName);
							break;
						case SearchByLocationRequest.QUERY_TOUCHES:
							f = filterCreator.createTouchesFilter(geometry, geomName);
							break;
						case SearchByLocationRequest.QUERY_WITHIN:
							f = filterCreator.createWithinFilter(geometry, geomName);
							break;
					}

					// Gett the features
					List<InternalFeature> temp = layerService.getFeatures(layerId, applicationService.getCrs(request
							.getCrs()), f, null, VectorLayerService.FEATURE_INCLUDE_ALL);
					if (temp.size() > 0) {
						List<Feature> features = new ArrayList<Feature>();

						// Calculate overlap ratio in case of intersects:
						if (queryType == SearchByLocationRequest.QUERY_INTERSECTS && ratio >= 0 && ratio < 1) {
							for (InternalFeature feature : temp) {
								double minimalOverlap = feature.getGeometry().getArea() * ratio;
								Geometry overlap = location.intersection(feature.getGeometry());
								double effectiveOverlap = overlap.getArea();
								if (minimalOverlap <= effectiveOverlap) {
									features.add(converter.toDto(feature));
								}
							}
						} else {
							for (InternalFeature feature : temp) {
								features.add(converter.toDto(feature));
							}
						}

						// features.size can again be 0... so check:
						if (features.size() > 0) {
							// We have a response for this layer!
							response.addLayer(layerId, features);

							// If searchType == SEARCH_FIRST_LAYER, we should search no further:
							if (searchType == SearchByLocationRequest.SEARCH_FIRST_LAYER) {
								break;
							}
						}
					}
				}
			}
		}
	}

	public SearchByLocationResponse getEmptyCommandResponse() {
		return new SearchByLocationResponse();
	}
}
