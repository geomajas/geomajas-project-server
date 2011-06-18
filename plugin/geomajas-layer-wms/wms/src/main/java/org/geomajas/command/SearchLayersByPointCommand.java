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
package org.geomajas.command;

import java.util.List;

import org.geomajas.command.dto.SearchLayersByPointRequest;
import org.geomajas.command.dto.SearchLayersByPointResponse;
import org.geomajas.geometry.Crs;
import org.geomajas.geometry.CrsTransform;
import org.geomajas.global.Api;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.Layer;
import org.geomajas.layer.feature.Feature;
import org.geomajas.layer.wms.LayerFeatureInfoSupport;
import org.geomajas.security.SecurityContext;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.GeoService;
import org.opengis.geometry.MismatchedDimensionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;


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
 * It will go over all given layers (provided they're layers that support feature info by implementing 
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
@Component(SearchLayersByPointRequest.COMMAND)
public class SearchLayersByPointCommand 
	implements Command<SearchLayersByPointRequest, SearchLayersByPointResponse> {

	private final Logger log = LoggerFactory.getLogger(SearchLayersByPointCommand.class);

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private GeoService geoService;

	@Autowired
	private SecurityContext securityContext;

	public void execute(SearchLayersByPointRequest request, SearchLayersByPointResponse response) 
	throws Exception {
		if (null == request.getLayerIds()) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "layerIds");
		}
		String crsCode = request.getCrs();
		if (null == crsCode) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "crs");
		}
		String[] layerIds = request.getLayerIds();
		Coordinate coordinate = new Coordinate(request.getLocation().getX(), request.getLocation().getY());
		int searchType = request.getSearchType();
		Crs crs = geoService.getCrs2(request.getCrs());
		Envelope mapBounds = new Envelope(request.getBbox().getX(), request.getBbox().getMaxX(), 
				request.getBbox().getY(), request.getBbox().getMaxY());

		log.debug("search by location {}", coordinate);

		if (layerIds != null && layerIds.length > 0) {
			for (String layerId : layerIds) {
				if (securityContext.isLayerVisible(layerId)) {
					Layer<?> layer = configurationService.getLayer(layerId);
					if (layer != null && layer instanceof LayerFeatureInfoSupport && 
							((LayerFeatureInfoSupport) layer).isEnableFeatureInfoSupport()) {
						double layerScale = calculateLayerScale(crs, (Crs) layer.getCrs(), mapBounds, 
								request.getScale());
						Coordinate layerCoordinate = calculateLayerCoordinate(crs, (Crs) layer.getCrs(), coordinate);
						List<Feature> features = ((LayerFeatureInfoSupport) layer).getFeaturesByLocation(
								layerCoordinate, layerScale, request.getBuffer());
						if (features != null && features.size() > 0) {
							response.addLayer(layerId, features);
							if (searchType == SearchLayersByPointRequest.SEARCH_FIRST_LAYER) {
								break;
							}
						}
					}
				}
			}
		}
	}
	
	public SearchLayersByPointResponse getEmptyCommandResponse() {
		return new SearchLayersByPointResponse();
	}
	
	private Coordinate calculateLayerCoordinate(Crs mapCrs, Crs layerCrs, Coordinate mapCoordinate) 
	throws GeomajasException {
		Coordinate layerCoordinate = mapCoordinate;
		try {
			// We don't necessarily need to split into same CRS and different CRS cases, the latter implementation uses
			// identity transform if crs's are equal for map and layer but might introduce bugs in rounding and/or
			// conversions.
			if (!mapCrs.equals(layerCrs)) {
				CrsTransform mapToLayer = geoService.getCrsTransform(mapCrs, layerCrs);

				layerCoordinate = geoService.transform(new Envelope(mapCoordinate), mapToLayer).centre();
			}
		} catch (MismatchedDimensionException e) {
			throw new GeomajasException(e, ExceptionCode.RENDER_DIMENSION_MISMATCH);
		}
		return layerCoordinate;
	}
	
	private double calculateLayerScale(Crs mapCrs, Crs layerCrs, Envelope mapBounds, double mapScale) 
	throws GeomajasException {
		double layerScale = mapScale;

		try {
			// We don't necessarily need to split into same CRS and different CRS cases, the latter implementation uses
			// identity transform if crs's are equal for map and layer but might introduce bugs in rounding and/or
			// conversions.
			if (!mapCrs.equals(layerCrs)) {
				CrsTransform mapToLayer = geoService.getCrsTransform(mapCrs, layerCrs);

				// Translate the map coordinates to layer coordinates, assumes equal x-y orientation
				Envelope layerBounds = geoService.transform(mapBounds, mapToLayer);
				layerScale = mapBounds.getWidth() * mapScale / layerBounds.getWidth();
			}
		} catch (MismatchedDimensionException e) {
			throw new GeomajasException(e, ExceptionCode.RENDER_DIMENSION_MISMATCH);
		}
		return layerScale;
	}
}

