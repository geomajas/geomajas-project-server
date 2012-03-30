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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.command.Command;
import org.geomajas.command.dto.SearchByLocationRequest;
import org.geomajas.command.dto.SearchByLocationResponse;
import org.geomajas.geometry.Crs;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.feature.Feature;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.security.SecurityContext;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;
import org.opengis.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @author An Buyle
 * @since 1.6.0
 */
@Api
@Component()
@Transactional(readOnly = true, rollbackFor = { Exception.class })
public class SearchByLocationCommand implements Command<SearchByLocationRequest, SearchByLocationResponse> {

	private final Logger log = LoggerFactory.getLogger(SearchByLocationCommand.class);

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private GeoService geoService;

	@Autowired
	private DtoConverterService converter;

	@Autowired
	private FilterService filterCreator;

	@Autowired
	private VectorLayerService layerService;

	@Autowired
	private SecurityContext securityContext;

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
		String crsCode = request.getCrs();
		if (null == crsCode) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "crs");
		}
		Geometry location = converter.toInternal(request.getLocation());
		int queryType = request.getQueryType();
		double ratio = request.getRatio();
		int searchType = request.getSearchType();
		Crs crs = geoService.getCrs2(request.getCrs());

		// Check if a buffer should be added around the location:
		Geometry geometry = location;
		if (request.getBuffer() > 0) {
			geometry = location.buffer(request.getBuffer());
		}
		log.debug("search by location " + geometry);

		for (String clientLayerId : request.getLayerIds()) {
			String serverLayerId = request.getServerLayerId(clientLayerId);
			if (null == serverLayerId) {
				throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, 
								"serverLayerId for clientLayerId " + clientLayerId);
			}
			if (securityContext.isLayerVisible(serverLayerId)) {
				VectorLayer vectorLayer = configurationService.getVectorLayer(serverLayerId);
				if (vectorLayer != null) {
					String geomName = vectorLayer.getLayerInfo().getFeatureInfo().getGeometryType().getName();

					// Transform geometry to layer CRS:
					Geometry layerGeometry = geoService.transform(geometry, crs, layerService.getCrs(vectorLayer));
					log.trace("on layer " + serverLayerId + " use " + layerGeometry);

					// Create the correct Filter object:
					Filter f = null;
					switch (queryType) {
						case SearchByLocationRequest.QUERY_INTERSECTS:
							f = filterCreator.createIntersectsFilter(layerGeometry, geomName);
							break;
						case SearchByLocationRequest.QUERY_CONTAINS:
							f = filterCreator.createContainsFilter(layerGeometry, geomName);
							break;
						case SearchByLocationRequest.QUERY_TOUCHES:
							f = filterCreator.createTouchesFilter(layerGeometry, geomName);
							break;
						case SearchByLocationRequest.QUERY_WITHIN:
							f = filterCreator.createWithinFilter(layerGeometry, geomName);
							break;
						default:
							throw new IllegalArgumentException("Unknown query type " + queryType);
					}
					//Set the per layer filter
					if (null != request.getFilter(clientLayerId)) {
						if (null == f) {
							f = filterCreator.parseFilter(request.getFilter(clientLayerId));
						} else {
							f = filterCreator.createAndFilter(
									filterCreator.parseFilter(request.getFilter(clientLayerId)), f);
						}
					}
					//Set the global filter
					if (null != request.getFilter()) {
						if (null == f) {
							f = filterCreator.parseFilter(request.getFilter());
						} else {
							f = filterCreator.createAndFilter(filterCreator.parseFilter(request.getFilter()), f);
						}
					}

					// Get the features:
					List<InternalFeature> temp = layerService.getFeatures(serverLayerId, crs, f, null, request
							.getFeatureIncludes());
					if (temp.size() > 0) {
						List<Feature> features = new ArrayList<Feature>();

						// Calculate overlap ratio in case of intersects:
						if (queryType == SearchByLocationRequest.QUERY_INTERSECTS && ratio >= 0 && ratio < 1) {
							for (InternalFeature feature : temp) {
								double minimalOverlap = feature.getGeometry().getArea() * ratio;
								Geometry overlap = location.intersection(feature.getGeometry());
								double effectiveOverlap = overlap.getArea();
								if (minimalOverlap <= effectiveOverlap) {
									log.trace("found " + feature);
									Feature dto = converter.toDto(feature);
									dto.setCrs(crsCode);
									features.add(dto);
								}
							}
						} else {
							for (InternalFeature feature : temp) {
								log.trace("found " + feature);
								Feature dto = converter.toDto(feature);
								dto.setCrs(crsCode);
								features.add(dto);
							}
						}

						// features.size can again be 0... so check:
						if (features.size() > 0) {
							// We have a response for this layer!
							response.addLayer(clientLayerId, features);

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

	/** {@inheritDoc} */
	public SearchByLocationResponse getEmptyCommandResponse() {
		return new SearchByLocationResponse();
	}
}
