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
package org.geomajas.command.wms;

import com.vividsolutions.jts.geom.Coordinate;
import org.geomajas.command.Command;
import org.geomajas.command.dto.SearchLayersByPointRequest;
import org.geomajas.command.dto.SearchLayersByPointResponse;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Crs;
import org.geomajas.geometry.CrsTransform;
import org.geomajas.global.Api;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.Layer;
import org.geomajas.layer.LayerService;
import org.geomajas.layer.feature.Feature;
import org.geomajas.layer.wms.LayerFeatureInfoSupport;
import org.geomajas.security.SecurityContext;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.GeoService;
import org.opengis.geometry.MismatchedDimensionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * Execute a search for features by location/point. You can also specify a tolerance (in pixels) for the search.
 * This tolerance may or may not be supported (it is not for WMS GetFeatureInfo).
 * </p>
 * <p>
 * It is required that at least one layer ID is given to search in. If multiple layers are given, an extra parameter
 * comes into play: searchType. This searchType specifies whether to search features in all given layers, to start
 * searching from the first layer, and stop once features are found (can be in the first layer, or in the second, ...).
 * </p>
 * <p>
 * It will go over all given layers (provided they're layers that support feature info by implementing
 * {@link LayerFeatureInfoSupport}), and fetch the features, using the location and the query type. The
 * resulting list of features is added to the command result so it can be send back to the client.
 * </p>
 * <p>
 * This class is based on the {@link org.geomajas.command.feature.SearchByLocationCommand} class.
 * </p>
 *
 * @author Oliver May
 * @since 1.8.0
 */
@Api
@Component()
public class SearchLayersByPointCommand
		implements Command<SearchLayersByPointRequest, SearchLayersByPointResponse> {

	private final Logger log = LoggerFactory.getLogger(SearchLayersByPointCommand.class);

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private GeoService geoService;

	@Autowired
	@Qualifier("layer.LayerService")
	private LayerService layerService;

	@Autowired
	private SecurityContext securityContext;

	public void execute(SearchLayersByPointRequest request, SearchLayersByPointResponse response)
			throws Exception {
		String[] layerIds = request.getLayerIds();
		if (null == layerIds) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "layerIds");
		}
		String crsCode = request.getCrs();
		if (null == crsCode) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "crs");
		}
		org.geomajas.geometry.Coordinate requestLocation = request.getLocation();
		if (null == requestLocation) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "location");
		}
		Bbox mapBounds = request.getMapBounds();
		if (null == mapBounds) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "mapBounds");
		}

		Coordinate coordinate = new Coordinate(requestLocation.getX(), requestLocation.getY());
		Crs crs = geoService.getCrs2(request.getCrs());
		boolean searchFirstLayerOnly;
		switch (request.getSearchType()) {
			case SearchLayersByPointRequest.SEARCH_FIRST_LAYER:
				searchFirstLayerOnly = true;
				break;
			case SearchLayersByPointRequest.SEARCH_ALL_LAYERS:
				searchFirstLayerOnly = false;
				break;
			default:
				throw new IllegalArgumentException("Invalid value for searchType");
		}

		log.debug("search by location {}", coordinate);

		if (layerIds.length > 0) {
			for (String layerId : layerIds) {
				if (securityContext.isLayerVisible(layerId)) {
					Layer<?> layer = configurationService.getLayer(layerId);
					if (layer instanceof LayerFeatureInfoSupport &&
							((LayerFeatureInfoSupport) layer).isEnableFeatureInfoSupport()) {
						Crs layerCrs = layerService.getCrs(layer);
						double layerScale = calculateLayerScale(crs, layerCrs, mapBounds, request.getScale());
						Coordinate layerCoordinate = geoService.transform(coordinate, crs, layerCrs);
						List<Feature> features = ((LayerFeatureInfoSupport) layer).getFeaturesByLocation(
								layerCoordinate, layerScale, request.getPixelTolerance());
						if (features != null && features.size() > 0) {
							response.addLayer(layerId, features);
							if (searchFirstLayerOnly) {
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

	private double calculateLayerScale(Crs mapCrs, Crs layerCrs, Bbox mapBounds, double mapScale)
			throws GeomajasException {
		double layerScale = mapScale;

		try {
			// We don't necessarily need to split into same CRS and different CRS cases, the latter implementation uses
			// identity transform if CRSs are equal for map and layer but might introduce bugs in rounding and/or
			// conversions.
			if (!mapCrs.equals(layerCrs)) {
				CrsTransform mapToLayer = geoService.getCrsTransform(mapCrs, layerCrs);

				// Translate the map coordinates to layer coordinates, assumes equal x-y orientation
				Bbox layerBounds = geoService.transform(mapBounds, mapToLayer);
				layerScale = mapBounds.getWidth() * mapScale / layerBounds.getWidth();
			}
		} catch (MismatchedDimensionException e) {
			throw new GeomajasException(e, ExceptionCode.RENDER_DIMENSION_MISMATCH);
		}
		return layerScale;
	}
}

