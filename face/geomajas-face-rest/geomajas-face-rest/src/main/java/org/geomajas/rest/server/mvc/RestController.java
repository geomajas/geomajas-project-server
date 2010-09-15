/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.rest.server.mvc;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.rest.server.RestException;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.FilterService;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.vividsolutions.jts.geom.Envelope;

/**
 * 
 * Spring MVC controller that maps a REST request to vectorlayers.
 * 
 * @author Oliver May
 * @author Jan De Moerlose
 * 
 */

@Controller("restController")
public class RestController {

	@Autowired
	private VectorLayerService vectorLayerService;

	@Autowired
	private FilterService filterService;

	@Autowired
	private ConfigurationService configurationService;

	static final String VIEW = "GeoJSONView";

	static final String FEATURE_COLLECTION = "FeatureCollection";

	static final String FEATURE_INFO = "FeatureInfo";

	static final String ATTRIBUTES = "Attrs";

	@RequestMapping(value = "/rest/{layerId}/{featureId}.json", method = RequestMethod.GET)
	public String readOneFeature(@PathVariable String layerId, @PathVariable String featureId,
			@RequestParam(value = "no_geom", required = false) boolean noGeom,
			@RequestParam(value = "attrs", required = false) List<String> attrs, Model model) throws RestException {
		List<InternalFeature> features;
		try {
			int featureIncludes = getIncludes(noGeom);
			features = vectorLayerService.getFeatures(layerId, null, filterService
					.createFidFilter(new String[] { featureId }), null, featureIncludes);

		} catch (GeomajasException e) {
			throw new RestException(e, RestException.PROBLEM_READING_LAYERSERVICE, layerId);
		}
		if (features.size() != 1) {
			throw new RestException(RestException.FEATURE_NOT_FOUND, featureId, layerId);
		}
		model.addAttribute(FEATURE_COLLECTION, features.get(0));
		model.addAttribute(FEATURE_INFO, features.get(0).getLayer().getLayerInfo());
		model.addAttribute(ATTRIBUTES, attrs);
		return VIEW;
	}

	private int getIncludes(Boolean noGeom) {
		int featureIncludes = VectorLayerService.FEATURE_INCLUDE_ALL;
		if (noGeom) {
			featureIncludes = VectorLayerService.FEATURE_INCLUDE_ATTRIBUTES;
		}
		return featureIncludes;
	}

	@RequestMapping(value = "/rest/{layerId}", method = RequestMethod.GET)
	public String readFeatures(@PathVariable String layerId,
			@RequestParam(value = "no_geom", required = false) boolean noGeom,
			@RequestParam(value = "attrs", required = false) List<String> attrs,
			@RequestParam(value = "box", required = false) Envelope box,
			@RequestParam(value = "bbox", required = false) Envelope bbox, Model model) throws RestException {

		List<Filter> filters = new ArrayList<Filter>();
		try {
			filters.add(createBBoxFilter(layerId, box, bbox));
			List<InternalFeature> features = vectorLayerService.getFeatures(layerId, null, and(filters), null,
					getIncludes(noGeom));
			if (features.size() > 0) {
				model.addAttribute(FEATURE_INFO, features.get(0).getLayer().getLayerInfo());
			}
			model.addAttribute(FEATURE_COLLECTION, features);
			model.addAttribute(ATTRIBUTES, attrs);
		} catch (LayerException e1) {
			throw new RestException(RestException.PROBLEM_READING_LAYERSERVICE, layerId);
		} catch (GeomajasException e) {
			throw new RestException(e, RestException.PROBLEM_READING_LAYERSERVICE, layerId);
		}
		return VIEW;
	}

	public ModelAndView createUpdateFeatures() {
		ModelAndView mav = new ModelAndView();

		return mav;
	}

	public ModelAndView updateFeature() {
		ModelAndView mav = new ModelAndView();

		return mav;
	}

	public ModelAndView deleteFeature() {
		ModelAndView mav = new ModelAndView();

		return mav;
	}

	public ModelAndView countFeatures() {
		ModelAndView mav = new ModelAndView();

		return mav;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setConversionService(new RestParameterConversionService());
	}

	private Filter and(List<Filter> filters) {
		if (filters.size() == 0) {
			return null;
		} else {
			Filter result = filters.get(0);
			for (int i = 1; i < filters.size(); i++) {
				result = filterService.createAndFilter(result, filters.get(i));
			}
			return result;
		}
	}

	private Filter createBBoxFilter(String layerId, Envelope... bbox) throws LayerException {
		VectorLayer layer = configurationService.getVectorLayer(layerId);
		for (Envelope envelope : bbox) {
			if (envelope != null) {
				return filterService.createBboxFilter(layer.getLayerInfo().getCrs(), envelope, layer.getFeatureModel()
						.getGeometryAttributeName());
			}
		}
		return filterService.createTrueFilter();

	}

}
