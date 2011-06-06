/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.command.feature;

import java.util.List;

import org.geomajas.command.Command;
import org.geomajas.command.dto.SearchByLocationResponse;
import org.geomajas.command.dto.SearchRasterLayersByLocationRequest;
import org.geomajas.geometry.Crs;
import org.geomajas.global.Api;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerFeatureInfoSupport;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.feature.Feature;
import org.geomajas.security.SecurityContext;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;


/**
 * <p>
 * Execute a search for features by location. The location can be any type of geometry, but it will be converted to a 
 * coordinate. In the case the location is not a single point, the center will be used. 
 * The command only expects the location to be described using the map's coordinate system. 
 * </p>
 * <p>
 * It is required that at least one layer-ID is given to search in. If multiple layers are given, an extra parameter 
 * comes into play: searchType. This searchType specifies whether to search features in all given layers, to start 
 * searching from the first layer, and stop once features are found (can be in the first layer, or in the second, ...).
 * </p>
 * <p>
 * It will go over all given layers (provided they're raster layers that support feature info by implementing 
 * {@link LayerFeatureInfoSupport}), and fetch the features, using the location geometry and the query type. The 
 * resulting list of features is added to the command result so it can be send back to the client.
 * </p>
 * <p>
 * This class is based on the {@link SearchByLocationCommand} class.
 * </p>
 * @author Oliver May
 * @since 1.9.0
 */
@Api
@Component()
public class SearchRasterLayersByLocationCommand 
	implements Command<SearchRasterLayersByLocationRequest, SearchByLocationResponse> {

	private final Logger log = LoggerFactory.getLogger(SearchRasterLayersByLocationCommand.class);

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private GeoService geoService;

	@Autowired
	private DtoConverterService converter;

	@Autowired
	private SecurityContext securityContext;

	public void execute(SearchRasterLayersByLocationRequest request, SearchByLocationResponse response) 
	throws Exception {
		if (null == request.getLayerIds()) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "layerIds");
		}
		String crsCode = request.getCrs();
		if (null == crsCode) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "crs");
		}
		String[] layerIds = request.getLayerIds();
		Geometry location = converter.toInternal(request.getLocation());
		Coordinate coordinate = new Coordinate(location.getCentroid().getX(), location.getCentroid().getY());
		int searchType = request.getSearchType();
		Crs crs = geoService.getCrs2(request.getCrs());
		Envelope env = new Envelope(request.getBbox().getX(), request.getBbox().getMaxX(), 
				request.getBbox().getY(), request.getBbox().getMaxY());

		// Check if a buffer should be added around the location:
		Geometry geometry = location;
		if (request.getBuffer() > 0) {
			geometry = location.buffer(request.getBuffer());
		}
		log.debug("search by location " + geometry);

		if (layerIds != null && layerIds.length > 0) {
			for (String layerId : layerIds) {
				if (securityContext.isLayerVisible(layerId)) {
					RasterLayer rasterLayer = configurationService.getRasterLayer(layerId);
					if (rasterLayer != null && rasterLayer instanceof LayerFeatureInfoSupport) {
						List<Feature> features = ((LayerFeatureInfoSupport) rasterLayer).getFeaturseByLocation(
								coordinate, crs, env, request.getScale());
						if (features != null && features.size() > 0) {
							response.addLayer(layerId, features);
							if (searchType == SearchRasterLayersByLocationRequest.SEARCH_FIRST_LAYER) {
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

