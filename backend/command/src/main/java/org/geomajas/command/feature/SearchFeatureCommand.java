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

import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.command.Command;
import org.geomajas.command.dto.SearchFeatureRequest;
import org.geomajas.command.dto.SearchFeatureResponse;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.feature.Feature;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.feature.SearchCriterion;
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

/**
 * Search features based on a seat of search criteria.
 * 
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api
@Component()
@Transactional(readOnly = true, rollbackFor = { Exception.class })
public class SearchFeatureCommand implements Command<SearchFeatureRequest, SearchFeatureResponse> {

	private final Logger log  = LoggerFactory.getLogger(SearchFeatureCommand.class);

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private GeoService geoService;

	@Autowired
	private DtoConverterService converter;

	@Autowired
	private FilterService filterService;

	@Autowired
	private VectorLayerService layerService;

	/** {@inheritDoc} */
	public SearchFeatureResponse getEmptyCommandResponse() {
		return new SearchFeatureResponse();
	}

	/** {@inheritDoc} */
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
		log.debug("filter to apply : {}", filter);

		List<InternalFeature> features = layerService.getFeatures(layerId, geoService.getCrs(request.getCrs()), filter,
				null, request.getFeatureIncludes(), request.getOffSet(), request.getMax());
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

	Filter createFilter(SearchFeatureRequest request, String layerId) throws GeomajasException {
		Filter f = null;
		VectorLayer layer = configurationService.getVectorLayer(layerId);
		if (layer == null) {
			throw new  GeomajasException(ExceptionCode.LAYER_NOT_FOUND, layerId);
		}
		String idName = layer.getLayerInfo().getFeatureInfo().getIdentifier().getName();
		if (null != request.getCriteria()) {
			for (SearchCriterion criterion : request.getCriteria()) {
				Filter temp;
				String attributeName = criterion.getAttributeName();
				String operator = criterion.getOperator();
				if ((SearchFeatureRequest.ID_ATTRIBUTE.equals(attributeName) || attributeName.equals(idName)) &&
						(null == operator || "=".equals(operator))) {
					temp = filterService.createFidFilter(new String[]{criterion.getValue()});
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