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
package org.geomajas.plugin.deskmanager.command.common;

import java.util.Iterator;

import org.geomajas.command.Command;
import org.geomajas.command.dto.SearchFeatureRequest;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.SearchCriterion;
import org.geomajas.plugin.deskmanager.command.common.dto.SearchFeatureToCsvRequest;
import org.geomajas.plugin.deskmanager.command.common.dto.ToCsvResponse;
import org.geomajas.plugin.deskmanager.reporting.csv.CsvExport;
import org.geomajas.plugin.deskmanager.reporting.csv.CsvExportService;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.FilterService;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.opengis.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Oliver May
 *
 */
@Component(SearchFeatureToCsvRequest.COMMAND)
@Transactional(readOnly = true, rollbackFor = { Exception.class })
public class SearchFeatureToCsvCommand implements Command<SearchFeatureToCsvRequest, ToCsvResponse> {

	private final Logger log = LoggerFactory.getLogger(SearchFeatureToCsvCommand.class);

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private FilterService filterService;

	@Autowired
	private CsvExportService ces;

	public ToCsvResponse getEmptyCommandResponse() {
		return new ToCsvResponse();
	}

	@SuppressWarnings("unchecked")
	public void execute(SearchFeatureToCsvRequest request, ToCsvResponse response) throws Exception {
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
		VectorLayer vectorLayer = configurationService.getVectorLayer(layerId);

		if (vectorLayer.toString().startsWith("org.geomajas.layer.hibernate.HibernateLayer")) { // Spring aop is messing
																								// with "instanceof"...
			Iterator<CsvExport> it = (Iterator<CsvExport>) vectorLayer.getElements(filter, 0, Integer.MAX_VALUE);
			response.setDocumentId(ces.buildCsvDocument(it, vectorLayer.getId()));
		} else {
			Iterator<SimpleFeatureImpl> it = (Iterator<SimpleFeatureImpl>) vectorLayer.getElements(filter, 0,
					Integer.MAX_VALUE);
			response.setDocumentId(ces.buildCsvDocument(it, vectorLayer));
		}
	}

	@SuppressWarnings("deprecation")  // see GBE-321
	Filter createFilter(SearchFeatureRequest request, String layerId) throws GeomajasException {
		Filter f = null;
		VectorLayer layer = configurationService.getVectorLayer(layerId);
		String idName = layer.getLayerInfo().getFeatureInfo().getIdentifier().getName();
		for (SearchCriterion criterion : request.getCriteria()) {
			Filter temp;
			String attributeName = criterion.getAttributeName();
			String operator = criterion.getOperator();
			if ((SearchFeatureRequest.ID_ATTRIBUTE.equals(attributeName) || attributeName.equals(idName))
					&& (null == operator || "=".equals(operator))) {
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