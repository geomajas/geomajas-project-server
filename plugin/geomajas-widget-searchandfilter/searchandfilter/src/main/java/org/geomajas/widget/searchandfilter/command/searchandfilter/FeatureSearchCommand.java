/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.searchandfilter.command.searchandfilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.geomajas.command.Command;
import org.geomajas.geometry.Crs;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.feature.Feature;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.security.SecurityContext;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;
import org.geomajas.widget.searchandfilter.command.dto.FeatureSearchRequest;
import org.geomajas.widget.searchandfilter.command.dto.FeatureSearchResponse;
import org.geomajas.widget.searchandfilter.service.DtoSearchConverterService;
import org.opengis.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command to retrieve features using a FeatureSearch Criterion.
 * <p>
 * Criteria are combined to one filter (per layer).
 * 
 * @author Kristof Heirwegh
 */
@Component
@Transactional(readOnly = true, rollbackFor = { Exception.class })
public class FeatureSearchCommand implements Command<FeatureSearchRequest, FeatureSearchResponse> {

	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(FeatureSearchCommand.class);

	@Autowired
	private GeoService geoService;

	@Autowired
	private VectorLayerService layerService;

	@Autowired
	private DtoConverterService dtoConverterService;

	@Autowired
	private DtoSearchConverterService dtoSearchConverterService;

	@Autowired
	private FilterService filterService;

	@Autowired
	private SecurityContext securityContext;

	public void execute(final FeatureSearchRequest request, final FeatureSearchResponse response) throws Exception {
		if (request.getCriterion() == null) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "criterion");
		}
		if (null == request.getMapCrs()) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "mapCrs");
		}
		if (!request.getCriterion().isValid()) {
			throw new GeomajasException(ExceptionCode.UNEXPECTED_PROBLEM, "criterion is not valid");
		}

		String mapCrsCode = request.getMapCrs();
		Crs mapCrs = geoService.getCrs2(request.getMapCrs());

		Map<VectorLayer, Filter> filters = dtoSearchConverterService.dtoCriterionToFilters(request.getCriterion(),
				mapCrs);

		Map<String, String> layerFilters = request.getLayerFilters();

		for (Entry<VectorLayer, Filter> entry : filters.entrySet()) {
			String layerId = entry.getKey().getId();
			if (securityContext.isLayerVisible(layerId)) {
				Filter f = entry.getValue();
				if (layerFilters.containsKey(layerId)) {
					String layerFilter = layerFilters.get(layerId);
					f = filterService.createAndFilter(filterService.parseFilter(layerFilter), f);
				}

				List<InternalFeature> temp = layerService.getFeatures(layerId, mapCrs, f, null,
						request.getFeatureIncludes(), 0, request.getMax());
				if (temp.size() > 0) {
					List<Feature> features = new ArrayList<Feature>();
					for (InternalFeature feature : temp) {
						Feature dto = dtoConverterService.toDto(feature);
						dto.setCrs(mapCrsCode);
						features.add(dto);
					}
					response.addLayer(layerId, features);
				}
			}
		}
	}

	public FeatureSearchResponse getEmptyCommandResponse() {
		return new FeatureSearchResponse();
	}
}
