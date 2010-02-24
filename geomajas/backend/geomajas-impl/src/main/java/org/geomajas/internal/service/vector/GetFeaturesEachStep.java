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

package org.geomajas.internal.service.vector;

import com.vividsolutions.jts.geom.Geometry;
import org.geomajas.configuration.LabelStyleInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.internal.layer.feature.InternalFeatureImpl;
import org.geomajas.internal.rendering.StyleFilterImpl;
import org.geomajas.internal.service.VectorLayerServiceImpl;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.rendering.StyleFilter;
import org.geomajas.rendering.pipeline.PipelineContext;
import org.geomajas.rendering.pipeline.PipelineStep;
import org.geomajas.security.SecurityContext;
import org.geomajas.service.VectorLayerService;
import org.geotools.geometry.jts.JTS;
import org.opengis.filter.Filter;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * ...
 *
 * @author Joachim Van der Auwera
 */
public class GetFeaturesEachStep implements PipelineStep<GetFeaturesRequest, List<InternalFeature>> {

	private final Logger log = LoggerFactory.getLogger(GetFeaturesEachStep.class);

	private String id;

	@Autowired
	private SecurityContext securityContext;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(GetFeaturesRequest request, PipelineContext context,
			List<InternalFeature> response) throws GeomajasException {
		VectorLayer layer = request.getLayer();
		Filter filter = context.get(VectorLayerServiceImpl.FILTER_KEY, Filter.class);
		int offset = request.getOffset();
		int maxResultSize = request.getMaxResultSize();
		int featureIncludes = request.getFeatureIncludes();
		String layerId = request.getLayerId();
		NamedStyleInfo style = request.getStyle();
		MathTransform transformation = request.getCrsTransform();
		List<StyleFilter> styleFilters = context.getOptional(GetFeaturesStyleStep.STYLE_FILTERS_KEY, List.class);

		if (log.isDebugEnabled()) {
			log.debug("getElements " + filter + ", offset = " + offset + ", maxResultSize= " + maxResultSize);
		}
		Iterator<?> it = layer.getElements(filter, offset, maxResultSize);
		while (it.hasNext()) {
			InternalFeature feature = convertFeature(it.next(), layerId, layer, transformation, styleFilters, style
					.getLabelStyle(), featureIncludes);
			log.debug("checking feature");
			if (securityContext.isFeatureVisible(layerId, feature)) {
				feature.setEditable(securityContext.isFeatureUpdateAuthorized(layerId, feature));
				feature.setDeletable(securityContext.isFeatureDeleteAuthorized(layerId, feature));
				response.add(feature);
			} else {
				log.debug("feature not visible");
			}
		}
		log.debug("getElements done");
	}

	/**
	 * Convert the generic feature object (as obtained from te layer model) into a {@link InternalFeature}, with
	 * requested data. Part may be lazy loaded.
	 *
	 * @param feature
	 *            A feature object that comes directly from the {@link VectorLayer}
	 * @param layerId
	 *            layer id
	 * @param layer
	 *            vector layer for the feature
	 * @param transformation
	 *            transformation to apply to the geometry
	 * @param styles
	 *            style filters to apply
	 * @param labelStyle
	 *            label style
	 * @param featureIncludes
	 *            aspects to include in features
	 * @return actual feature
	 * @throws GeomajasException
	 *             oops
	 */
	private InternalFeature convertFeature(Object feature, String layerId, VectorLayer layer,
			MathTransform transformation, List<StyleFilter> styles, LabelStyleInfo labelStyle, int featureIncludes)
			throws GeomajasException {
		FeatureModel featureModel = layer.getFeatureModel();
		InternalFeatureImpl res = new InternalFeatureImpl();
		res.setId(featureModel.getId(feature));
		res.setLayer(layer);

		// If allowed, add the label to the InternalFeature:
		if ((featureIncludes & VectorLayerService.FEATURE_INCLUDE_LABEL) != 0) {
			String labelAttr = labelStyle.getLabelAttributeName();
			Attribute attribute = featureModel.getAttribute(feature, labelAttr);
			if (null != attribute && null != attribute.getValue()) {
				res.setLabel(attribute.getValue().toString());
			}
		}

		// If allowed, add the geometry (transformed!) to the InternalFeature:
		if ((featureIncludes & VectorLayerService.FEATURE_INCLUDE_GEOMETRY) != 0) {
			Geometry geometry = featureModel.getGeometry(feature);
			Geometry transformed;
			if (null != transformation) {
				try {
					transformed = JTS.transform(geometry, transformation);
				} catch (TransformException te) {
					throw new GeomajasException(te, ExceptionCode.GEOMETRY_TRANSFORMATION_FAILED);
				}
			} else {
				transformed = geometry;
			}
			res.setGeometry(transformed);
		}

		// If allowed, add the style definition to the InternalFeature:
		if ((featureIncludes & VectorLayerService.FEATURE_INCLUDE_STYLE) != 0) {
			res.setStyleDefinition(findStyleFilter(feature, styles).getStyleDefinition());
		}

		// If allowed, add the attributes to the InternalFeature:
		if ((featureIncludes & VectorLayerService.FEATURE_INCLUDE_ATTRIBUTES) != 0) {
			filterAttributes(layerId, res, featureModel.getAttributes(feature));
		}

		return res;
	}

	private Map<String, Attribute> filterAttributes(String layerId, InternalFeature feature,
													Map<String, Attribute> featureAttributes) {
		feature.setAttributes(featureAttributes); // to allow isAttributeReadable to see full object
		Map<String, Attribute> filteredAttributes = new HashMap<String, Attribute>();
		for (String key : featureAttributes.keySet()) {
			if (securityContext.isAttributeReadable(layerId, feature, key)) {
				Attribute attribute = featureAttributes.get(key);
				attribute.setEditable(securityContext.isAttributeWritable(layerId, feature, key));
				filteredAttributes.put(key, featureAttributes.get(key));
			}
		}
		feature.setAttributes(filteredAttributes);
		return filteredAttributes;
	}

	/**
	 * Find the style filter that must be applied to this feature.
	 *
	 * @param feature
	 *            feature to find the style for
	 * @param styles
	 *            style filters to select from
	 * @return a style filter
	 */
	private StyleFilter findStyleFilter(Object feature, List<StyleFilter> styles) {
		for (StyleFilter styleFilter : styles) {
			if (styleFilter.getFilter().evaluate(feature)) {
				return styleFilter;
			}
		}
		return new StyleFilterImpl();
	}

}
