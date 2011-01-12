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

package org.geomajas.internal.layer.vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.geomajas.configuration.LabelStyleInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.geometry.CrsTransform;
import org.geomajas.global.GeomajasException;
import org.geomajas.internal.layer.feature.InternalFeatureImpl;
import org.geomajas.internal.rendering.StyleFilterImpl;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.pipeline.GetFeaturesContainer;
import org.geomajas.rendering.StyleFilter;
import org.geomajas.security.SecurityContext;
import org.geomajas.service.GeoService;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.opengis.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Get features from a vector layer.
 *
 * @author Joachim Van der Auwera
 * @author Kristof Heirwegh
 */
public class GetFeaturesEachStep implements PipelineStep<GetFeaturesContainer> {

	private final Logger log = LoggerFactory.getLogger(GetFeaturesEachStep.class);

	private String id;

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private GeoService geoService;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@SuppressWarnings("unchecked")
	public void execute(PipelineContext context, GetFeaturesContainer response) throws GeomajasException {
		List<InternalFeature> features = response.getFeatures();
		log.debug("Get features, was {}", features);
		if (null == features) {
			features = new ArrayList<InternalFeature>();
			response.setFeatures(features);
			VectorLayer layer = context.get(PipelineCode.LAYER_KEY, VectorLayer.class);
			Filter filter = context.get(PipelineCode.FILTER_KEY, Filter.class);
			int offset = context.get(PipelineCode.OFFSET_KEY, Integer.class);
			int maxResultSize = context.get(PipelineCode.MAX_RESULT_SIZE_KEY, Integer.class);
			int featureIncludes = context.get(PipelineCode.FEATURE_INCLUDES_KEY, Integer.class);
			String layerId = context.get(PipelineCode.LAYER_ID_KEY, String.class);
			NamedStyleInfo style = context.get(PipelineCode.STYLE_KEY, NamedStyleInfo.class);
			CrsTransform transformation = context.getOptional(PipelineCode.CRS_TRANSFORM_KEY, CrsTransform.class);
			List<StyleFilter> styleFilters = context.getOptional(GetFeaturesStyleStep.STYLE_FILTERS_KEY, List.class);

			if (log.isDebugEnabled()) {
				log.debug("getElements " + filter + ", offset = " + offset + ", maxResultSize= " + maxResultSize);
			}
			Envelope bounds = null;
			Iterator<?> it = layer.getElements(filter, 0, 0); // do not limit result here, security needs to be applied

			int count = 0;
			while (it.hasNext()) {
				Object featureObj = it.next();
				Geometry geometry = layer.getFeatureModel().getGeometry(featureObj);
				InternalFeature feature = convertFeature(featureObj, geometry, layerId, layer, transformation,
						styleFilters, style.getLabelStyle(), featureIncludes);
				log.debug("checking feature");
				if (securityContext.isFeatureVisible(layerId, feature)) {
					count++;
					if (count > offset) {
						feature.setEditable(securityContext.isFeatureUpdateAuthorized(layerId, feature));
						feature.setDeletable(securityContext.isFeatureDeleteAuthorized(layerId, feature));
						features.add(feature);

						if (null != geometry) {
							Envelope envelope = geometry.getEnvelopeInternal();
							if (null == bounds) {
								bounds = envelope;
							} else {
								bounds.expandToInclude(envelope);
							}
						}

						if (features.size() == maxResultSize) {
							break;
						}
					}
				} else {
					log.debug("feature not visible");
				}
			}
			response.setBounds(bounds);
		}
		log.debug("getElements done, features {}, bounds {}", response.getFeatures(), response.getBounds());
	}

	/**
	 * Convert the generic feature object (as obtained from the layer model) into a {@link InternalFeature}, with
	 * requested data. Part may be lazy loaded.
	 *
	 * @param feature
	 *            A feature object that comes directly from the {@link VectorLayer}
	 * @param geometry
	 *            geometry of the feature, passed in as needed in surrounding code to calc bounding box
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
	private InternalFeature convertFeature(Object feature, Geometry geometry, String layerId, VectorLayer layer,
										   CrsTransform transformation, List<StyleFilter> styles,
										   LabelStyleInfo labelStyle, int featureIncludes)
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
			Geometry transformed;
			if (null != transformation) {
				transformed = geoService.transform(geometry, transformation);
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
