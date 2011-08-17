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
package org.geomajas.plugin.reporting.mvc;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.plugin.reporting.data.InternalFeatureDataSource;
import org.geomajas.service.GeoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * MVC controller to fetch a report on a layer of features.
 * 
 * @author Jan De Moerloose
 * 
 */
@Controller("/reporting/**")
public class ReportController {

	private final Logger log = LoggerFactory.getLogger(ReportController.class);

	@Autowired
	private VectorLayerService vectorLayerService;

	@Autowired
	private GeoService geoService;

	/**
	 * Report on the features of a layer.
	 * 
	 * @param reportName name of the report
	 * @param crs coordinate reference sysytem
	 * @param layerId id of the layer
	 * @param model mvc model
	 * @return name of the view
	 */
	@RequestMapping(value = "/reporting/{reportName}.pdf", method = RequestMethod.GET)
	public String reportFeatures(@PathVariable String reportName,
			@RequestParam(defaultValue = "EPSG:4326", required = false) String crs,
			@RequestParam(defaultValue = "unknown", required = false) String layerId, Model model) {
		List<InternalFeature> features;
		try {
			features = vectorLayerService.getFeatures(layerId, geoService.getCrs2(crs), null, null,
					VectorLayerService.FEATURE_INCLUDE_ALL);
			model.addAttribute("datasource", new InternalFeatureDataSource(features));
		} catch (LayerException e) {
			log.error("Could not create report data", e);
			model.addAttribute("datasource", new InternalFeatureDataSource(new ArrayList<InternalFeature>()));
		} catch (GeomajasException e) {
			log.error("Could not create report data", e);
			model.addAttribute("datasource", new InternalFeatureDataSource(new ArrayList<InternalFeature>()));
		}
		return reportName + "View";
	}

}
