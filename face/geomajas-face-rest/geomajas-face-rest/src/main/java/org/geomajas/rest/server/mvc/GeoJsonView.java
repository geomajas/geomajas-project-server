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

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.rest.server.GeoJsonParser;
import org.geomajas.rest.server.GeotoolsConvertorService;
import org.geotools.data.memory.MemoryFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

/**
 * 
 * @author Oliver May
 * 
 */

@Component("GeoJsonView")
public class GeoJsonView extends AbstractView {

	@Autowired
	private GeotoolsConvertorService convertorService;

	private GeoJsonParser parser = new GeoJsonParser();

	public GeoJsonView() {
		setContentType("application/json");
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Object ff = model.get(RestController.FEATURE_COLLECTION);
		VectorLayerInfo info = (VectorLayerInfo) model.get(RestController.VECTOR_LAYER_INFO);
		List<String> attrs = (List<String>) model.get(RestController.ATTRIBUTES);
		SimpleFeatureType sft = convertorService.toSimpleFeatureType(info, attrs);
		response.setContentType(getContentType());
		if (Collection.class.isAssignableFrom(ff.getClass())) {
			Collection<InternalFeature> features = (Collection<InternalFeature>) ff;
			FeatureCollection<SimpleFeatureType, SimpleFeature> coll = new MemoryFeatureCollection(sft);
			for (InternalFeature feature : features) {
				coll.add(convertorService.toSimpleFeature(feature, sft));
			}
			parser.write(coll, response.getOutputStream());
		} else {
			InternalFeature feature = (InternalFeature) ff;
			parser.write(convertorService.toSimpleFeature(feature, sft), response.getOutputStream());
		}
		response.getOutputStream().flush();
	}

}
