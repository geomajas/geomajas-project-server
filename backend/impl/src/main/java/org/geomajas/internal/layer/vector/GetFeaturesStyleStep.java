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

package org.geomajas.internal.layer.vector;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.internal.rendering.StyleFilterImpl;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.rendering.StyleFilter;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.geomajas.layer.VectorLayerService;

import java.util.ArrayList;
import java.util.List;

/**
 * Prepare the style filters for the {@link org.geomajas.layer.VectorLayerService} getFeatures.
 *
 * @author Joachim Van der Auwera
 */
public class GetFeaturesStyleStep implements PipelineStep<List<InternalFeature>> {

	public static final String STYLE_FILTERS_KEY = "styleFilters";
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(PipelineContext context, List<InternalFeature> response) throws GeomajasException {
		VectorLayer layer = context.get(PipelineCode.LAYER_KEY, VectorLayer.class);
		int featureIncludes = context.get(PipelineCode.FEATURE_INCLUDES_KEY, Integer.class);
		NamedStyleInfo style = context.getOptional(PipelineCode.STYLE_KEY, NamedStyleInfo.class);

		List<StyleFilter> styleFilters = null;
		if (style == null) {
			// no style specified, take the first
			style = layer.getLayerInfo().getNamedStyleInfos().get(0);
		} else if (style.getFeatureStyles().isEmpty()) {
			// only name specified, find it
			style = layer.getLayerInfo().getNamedStyleInfo(style.getName());
		}
		context.put(PipelineCode.STYLE_KEY, style);

		if ((featureIncludes & VectorLayerService.FEATURE_INCLUDE_STYLE) != 0) {
			if (style == null) {
				throw new GeomajasException(ExceptionCode.RENDER_FEATURE_MODEL_PROBLEM, "Style not found");
			}
			styleFilters = initStyleFilters(style.getFeatureStyles());
		}

		context.put(STYLE_FILTERS_KEY, styleFilters);
	}

	/**
	 * Build list of style filters from style definitions.
	 *
	 * @param styleDefinitions
	 *            list of style definitions
	 * @return list of style filters
	 */
	private List<StyleFilter> initStyleFilters(List<FeatureStyleInfo> styleDefinitions) {
		List<StyleFilter> styleFilters = new ArrayList<StyleFilter>();
		if (styleDefinitions == null || styleDefinitions.size() == 0) {
			styleFilters.add(new StyleFilterImpl()); // use default.
		} else {
			for (FeatureStyleInfo styleDef : styleDefinitions) {
				styleFilters.add(new StyleFilterImpl(styleDef));
			}
		}
		return styleFilters;
	}
	
}
