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

import net.sf.jasperreports.engine.JRImageRenderer;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.feature.Feature;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheManagerService;
import org.geomajas.plugin.reporting.data.InternalFeatureDataSource;
import org.geomajas.plugin.reporting.data.ReportingCacheContainer;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;
import org.opengis.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.jasperreports.JasperReportsMultiFormatView;

import java.util.ArrayList;
import java.util.List;

/**
 * MVC controller to fetch a report on a layer of features.
 *
 * @author Jan De Moerloose
 * @author Joachim Van der Auwera
 */
@Controller("/reporting/**")
public class ReportingController {

	private final Logger log = LoggerFactory.getLogger(ReportingController.class);
	private static final String DATA_SOURCE = "datasource";
	private static final String REPORT_DATA_PROBLEM = "Could not create report data";

	@Autowired
	private VectorLayerService vectorLayerService;

	@Autowired
	private CacheManagerService cacheManager;

	@Autowired
	private GeoService geoService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private DtoConverterService dtoConverterService;

	@Autowired
	private FilterService filterService;

	/**
	 * Report on the features of a layer.
	 *
	 * @param reportName name of the report
	 * @param format format for the report (eg "pdf")
	 * @param layerId id of the layer
	 * @param crs coordinate reference system
	 * @param filter filter to apply on layer
	 * @param model mvc model
	 * @return name of the view
	 */
	@RequestMapping(value = "/reporting/f/{layerId}/{reportName}.{format}", method = RequestMethod.GET)
	public String reportFeatures(@PathVariable String reportName, @PathVariable String format,
			@PathVariable String layerId, @RequestParam(defaultValue = "EPSG:4326", required = false) String crs,
			@RequestParam(required = false) String filter, Model model) {
		List<InternalFeature> features;
		try {
			Filter filterObject = null;
			if (null != filter) {
				filterObject = filterService.parseFilter(filter);
			}
			features = vectorLayerService.getFeatures(layerId, geoService.getCrs2(crs), filterObject, null,
					VectorLayerService.FEATURE_INCLUDE_ALL);
			model.addAttribute(DATA_SOURCE, new InternalFeatureDataSource(features));
		} catch (GeomajasException ge) {
			log.error(REPORT_DATA_PROBLEM, ge);
			model.addAttribute(DATA_SOURCE, new InternalFeatureDataSource(new ArrayList<InternalFeature>()));
		}
		model.addAttribute(JasperReportsMultiFormatView.DEFAULT_FORMAT_KEY, format);
		return getView(reportName);
	}

	/**
	 * Report on the features of a layer with map image and legend, data coming from the cache.
	 *
	 * @param reportName name of the report
	 * @param format format for the report (eg "pdf")
	 * @param layerId id of the layer
	 * @param key cache key for data
	 * @param model mvc model
	 * @return name of the view
	 */
	@RequestMapping(value = "/reporting/c/{layerId}/{reportName}.{format}", method = RequestMethod.GET)
	public String reportFromCache(@PathVariable String reportName, @PathVariable String format,
			@PathVariable String layerId, @RequestParam(required = true) String key, Model model) {
		try {
			VectorLayer layer = configurationService.getVectorLayer(layerId);
			if (null != layer) {
				ReportingCacheContainer container =
						cacheManager.get(layer, CacheCategory.RASTER, key, ReportingCacheContainer.class);
				if (null != container) {
					model.addAttribute("map", JRImageRenderer.getInstance(container.getMapImageData()));
					model.addAttribute("legend", JRImageRenderer.getInstance(container.getLegendImageData()));
					model.addAttribute(DATA_SOURCE, getDataSource(container));
				} else {
					model.addAttribute(DATA_SOURCE, new InternalFeatureDataSource(new ArrayList<InternalFeature>()));
				}
			}
		} catch (GeomajasException ge) {
			log.error(REPORT_DATA_PROBLEM, ge);
			model.addAttribute(DATA_SOURCE, new InternalFeatureDataSource(new ArrayList<InternalFeature>()));
		}
		model.addAttribute(JasperReportsMultiFormatView.DEFAULT_FORMAT_KEY, format);
		return getView(reportName);
	}

	private String getView(String reportName) {
		return reportName + "View";
	}

	private InternalFeatureDataSource getDataSource(ReportingCacheContainer container) throws GeomajasException {
		List<InternalFeature> features = new ArrayList<InternalFeature>();
		for (Feature feature : container.getFeatures()) {
			features.add(dtoConverterService.toInternal(feature));
		}
		return new InternalFeatureDataSource(features);
	}

}
