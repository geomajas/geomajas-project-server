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

import java.util.List;

import org.geomajas.command.Command;
import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.dto.SearchByLocationRequest;
import org.geomajas.command.dto.SearchByLocationResponse;
import org.geomajas.command.dto.SearchFeatureRequest;
import org.geomajas.command.dto.SearchFeatureResponse;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.Feature;
import org.geomajas.security.SecurityContext;
import org.geomajas.service.ConfigurationService;
import org.geomajas.widget.searchandfilter.command.dto.ExportToCsvRequest;
import org.geomajas.widget.searchandfilter.command.dto.ExportToCsvResponse;
import org.geomajas.widget.searchandfilter.command.dto.FeatureSearchRequest;
import org.geomajas.widget.searchandfilter.command.dto.FeatureSearchResponse;
import org.geomajas.widget.searchandfilter.service.csv.CsvExportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ExportToCsvCommand.
 * <p>
 * You can use both a searchFeatureRequest or a searchByLocationRequest to
 * retrieve features.
 * 
 * @author Kristof Heirwegh
 */
@Component
public class ExportToCsvCommand implements Command<ExportToCsvRequest, ExportToCsvResponse> {

	private final Logger log = LoggerFactory.getLogger(ExportToCsvCommand.class);

	private static final String EXTENSION = ".csv";

	@Autowired
	private CommandDispatcher dispatch;
	
	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private CsvExportService csvService;

	public ExportToCsvResponse getEmptyCommandResponse() {
		return new ExportToCsvResponse();
	}

	public void execute(ExportToCsvRequest request, ExportToCsvResponse response) throws Exception {
		Feature[] features = null;
		String token = securityContext.getToken();
		String locale = request.getLocale();
		if (request.getSearchFeatureRequest() != null) {
			log.debug("CSV export using FeatureRequest");
			SearchFeatureResponse result = (SearchFeatureResponse) dispatch.execute(SearchFeatureRequest.COMMAND,
					request.getSearchFeatureRequest(), token, locale);
			if (result.isError()) {
				response.getErrorMessages().addAll(result.getErrorMessages());
				response.getErrors().addAll(result.getErrors());
			} else {
				features = result.getFeatures();
			}
		} else if (request.getSearchByLocationRequest() != null) {
			log.debug("CSV export using LocationRequest");
			SearchByLocationResponse result = (SearchByLocationResponse) dispatch.execute(
					SearchByLocationRequest.COMMAND, request.getSearchByLocationRequest(), token, locale);
			if (result.isError()) {
				response.getErrorMessages().addAll(result.getErrorMessages());
				response.getErrors().addAll(result.getErrors());
			} else {
				List<Feature> res = result.getFeatureMap().get(request.getLayerId());
				if (res != null) {
					features = res.toArray(new Feature[res.size()]);
				} else {
					features = new Feature[0];
				}
			}
		} else if (request.getSearchByCriterionRequest() != null) {
			log.debug("CSV export using CriterionRequest");
			FeatureSearchResponse result = (FeatureSearchResponse) dispatch.execute(FeatureSearchRequest.COMMAND,
					request.getSearchByCriterionRequest(), token, locale);
			if (result.isError()) {
				response.getErrorMessages().addAll(result.getErrorMessages());
				response.getErrors().addAll(result.getErrors());
			} else {
				List<Feature> res = result.getFeatureMap().get(request.getLayerId());
				if (res != null) {
					features = res.toArray(new Feature[res.size()]);
				} else {
					features = new Feature[0];
				}
			}

		} else {
			throw new IllegalArgumentException("You must provide a feature or location search request.");
		}

		if (features != null) {
			VectorLayer layer = configurationService.getVectorLayer(request.getLayerId());
			String fileName = (request.getFilename() == null || "".equals(request.getFilename()) ? request.getLayerId()
					+ EXTENSION : request.getFilename());
			if (!fileName.endsWith(EXTENSION) && !fileName.endsWith(".CSV")) {
				fileName += EXTENSION;
			}
			request.setFilename(fileName);
			response.setDocumentId(csvService.buildCsvDocument(features, layer, request));
		}
	}
}