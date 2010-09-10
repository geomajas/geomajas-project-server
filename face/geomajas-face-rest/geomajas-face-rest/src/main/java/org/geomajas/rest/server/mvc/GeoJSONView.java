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

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.rest.server.GeotoolsConvertorService;
import org.geotools.geojson.GeoJSON;
import org.opengis.feature.simple.SimpleFeatureType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

/**
 * 
 * @author Oliver May
 * 
 */

@Component("GeoJSONView")
public class GeoJSONView extends AbstractView {

	@Autowired
	private GeotoolsConvertorService convertorService;

	public GeoJSONView() {
		setContentType("application/json");
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<InternalFeature> features = (List<InternalFeature>) model.get(RestContoller.FEATURE_COLLECTION);

		if (features.size() > 0) {
			InternalFeature f = features.get(0);
			SimpleFeatureType sft = convertorService.toSimpleFeatureType(f.getLayer().getLayerInfo());

			response.setContentType(getContentType());

			for (InternalFeature feature : features) {
				GeoJSON.write(convertorService.toSimpleFeature(feature, sft), response.getOutputStream());
			}

			response.getOutputStream().flush();

		}
	}
}
