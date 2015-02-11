/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.jasperreports.JasperReportsMultiFormatView;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * MVC controller to fetch a report on a layer of features.
 *
 * @author Jan De Moerloose
 * @author Joachim Van der Auwera
 */
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
	 * @param request request object for report parameters
	 * @param model mvc model
	 * @return name of the view
	 */
	@RequestMapping(value = "/reporting/f/{layerId}/{reportName}.{format}", method = RequestMethod.GET)
	public String reportFeatures(@PathVariable String reportName, @PathVariable String format,
			@PathVariable String layerId, @RequestParam(defaultValue = "EPSG:4326", required = false) String crs,
			@RequestParam(required = false) String filter, HttpServletRequest request, Model model) {
		List<InternalFeature> features;
		try {
			Filter filterObject = null;
			if (null != filter) {
				filterObject = filterService.parseFilter(filter);
			}
			features = vectorLayerService.getFeatures(layerId, geoService.getCrs2(crs), filterObject, null,
					VectorLayerService.FEATURE_INCLUDE_ALL);
			model.addAttribute(DATA_SOURCE, new InternalFeatureDataSource(features));
			addParameters(model, request);
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
	 * @param request request object for report parameters
	 * @param model mvc model
	 * @return name of the view
	 */
	@RequestMapping(value = "/reporting/c/{layerId}/{reportName}.{format}", method = RequestMethod.GET)
	public String reportFromCache(@PathVariable String reportName, @PathVariable String format,
			@PathVariable String layerId, @RequestParam(required = true) String key, HttpServletRequest request,
			Model model) {
		try {
			VectorLayer layer = configurationService.getVectorLayer(layerId);
			if (null != layer) {
				ReportingCacheContainer container =
						cacheManager.get(layer, CacheCategory.RASTER, key, ReportingCacheContainer.class);
				addParameters(model, request);
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

	/**
	 * Add the extra parameters which are passed as report parameters. The type of the parameter is "guessed" from the
	 * first letter of the parameter name.
	 *
	 * @param model view model
	 * @param request servlet request
	 */
	@SuppressWarnings("unchecked")
	private void addParameters(Model model, HttpServletRequest request) {
		for (Object objectEntry : request.getParameterMap().entrySet()) {
			Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) objectEntry;
			String key = entry.getKey();
			String[] values = entry.getValue();
			if (null != values && values.length > 0) {
				String value  = values[0];
				try {
					model.addAttribute(key, getParameter(key, value));
				} catch (ParseException pe) {
					log.error("Could not parse parameter value {} for {}, ignoring parameter.", key, value);
				} catch (NumberFormatException nfe) {
					log.error("Could not parse parameter value {} for {}, ignoring parameter.", key, value);
				}
			}
		}
	}

	/**
	 * Convert a query parameter to the correct object type based on the first letter of the name.
	 *
	 * @param name parameter name
	 * @param value parameter value
	 * @return parameter object as
	 * @throws ParseException value could not be parsed
	 * @throws NumberFormatException value could not be parsed
	 */
	private Object getParameter(String name, String value) throws ParseException, NumberFormatException {
		Object result = null;
		if (name.length() > 0) {

			switch (name.charAt(0)) {
				case 'i':
					if (null == value || value.length() == 0) {
						value = "0";
					}
					result = new Integer(value);
					break;
				case 'f':
					if (name.startsWith("form")) {
						result = value;
					} else {
						if (null == value || value.length() == 0) {
							value = "0.0";
						}
						result = new Double(value);
					}
					break;
				case 'd':
					if (null == value || value.length() == 0) {
						result = null;
					} else {
						SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
						result = dateParser.parse(value);
					}
					break;
				case 't':
					if (null == value || value.length() == 0) {
						result = null;
					} else {
						SimpleDateFormat timeParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						result = timeParser.parse(value);
					}
					break;
				case 'b':
					result = "true".equalsIgnoreCase(value) ? Boolean.TRUE : Boolean.FALSE;
					break;
				default:
					result = value;
			}
			if (log.isDebugEnabled()) {
				if (result != null) {
					log.debug(
							"parameter " + name + " value " + result + " class " + result.getClass().getName());
				} else {
					log.debug("parameter" + name + "is null");
				}
			}
		}
		return result;
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
