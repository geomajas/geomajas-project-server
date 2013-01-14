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

package org.geomajas.internal.layer.vector;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.internal.rendering.StyleFilterImpl;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.pipeline.GetFeaturesContainer;
import org.geomajas.rendering.StyleFilter;
import org.geomajas.service.FilterService;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Prepare the style filters for the {@link org.geomajas.layer.VectorLayerService} getFeatures.
 *
 * @author Joachim Van der Auwera
 */
public class GetFeaturesStyleStep implements PipelineStep<GetFeaturesContainer> {

	public static final String STYLE_FILTERS_KEY = "styleFilters";
	private String id;
	
	@Autowired
	private FilterService filterService;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(PipelineContext context, GetFeaturesContainer response) throws GeomajasException {
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
	 * @throws GeomajasException 
	 */
	private List<StyleFilter> initStyleFilters(List<FeatureStyleInfo> styleDefinitions) throws GeomajasException {
		List<StyleFilter> styleFilters = new ArrayList<StyleFilter>();
		if (styleDefinitions == null || styleDefinitions.size() == 0) {
			styleFilters.add(new StyleFilterImpl()); // use default.
		} else {
			for (FeatureStyleInfo styleDef : styleDefinitions) {
				StyleFilterImpl styleFilterImpl = null;
				String formula = styleDef.getFormula();
				if (null != formula && formula.length() > 0) {
					styleFilterImpl = new StyleFilterImpl(filterService.parseFilter(formula), styleDef);
				} else {
					styleFilterImpl = new StyleFilterImpl(Filter.INCLUDE, styleDef);
				}
				styleFilters.add(styleFilterImpl);
			}
		}
		return styleFilters;
	}
	
}
