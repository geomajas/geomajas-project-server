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

package org.geomajas.internal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.LabelStyleInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.global.GeomajasSecurityException;
import org.geomajas.internal.layer.feature.InternalFeatureImpl;
import org.geomajas.internal.rendering.StyleFilterImpl;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerAssociationSupport;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.rendering.StyleFilter;
import org.geomajas.rendering.painter.PaintFactory;
import org.geomajas.security.SecurityContext;
import org.geomajas.service.ApplicationService;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;
import org.geomajas.service.VectorLayerService;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.geometry.jts.JTS;
import org.opengis.filter.Filter;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Implementation of {@link org.geomajas.service.VectorLayerService}, a service which allows accessing data from a
 * vector layer model.
 * <p/>
 * All access to layer models should be done through this service, not by accessing the layer models directly as this
 * adds possible caching, security etc. These services are implemented using pipelines (see
 * {@link org.geomajas.rendering.pipeline.PipelineService}) to make them configurable.
 * 
 * @author Joachim Van der Auwera
 */
@Component
public class VectorLayerServiceImpl implements VectorLayerService {

	private final Logger log = LoggerFactory.getLogger(VectorLayerServiceImpl.class);

	@Autowired
	private ApplicationService applicationService;

	@Autowired
	private GeoService geoService;

	@Autowired
	private PaintFactory paintFactory;

	@Autowired
	private FilterService filterService;

	@Autowired
	private SecurityContext securityContext;

	private VectorLayer getVectorLayer(String layerId) throws GeomajasException {
		if (!securityContext.isLayerVisible(layerId)) {
			throw new GeomajasSecurityException(ExceptionCode.LAYER_NOT_VISIBLE, layerId, securityContext.getUserId());
		}
		VectorLayer layer = applicationService.getVectorLayer(layerId);
		if (null == layer) {
			throw new GeomajasException(ExceptionCode.VECTOR_LAYER_NOT_FOUND, layerId);
		}
		return layer;
	}

	public void saveOrUpdate(String layerId, CoordinateReferenceSystem crs, List<InternalFeature> oldFeatures,
			List<InternalFeature> newFeatures) throws GeomajasException {
		VectorLayer layer = getVectorLayer(layerId);
		FeatureModel featureModel = layer.getFeatureModel();

		MathTransform mapToLayer;
		try {
			mapToLayer = geoService.findMathTransform(crs, layer.getCrs());
		} catch (FactoryException fe) {
			throw new GeomajasException(fe, ExceptionCode.CRS_TRANSFORMATION_NOT_POSSIBLE, crs, layer.getCrs());
		}

		int count = Math.max(oldFeatures.size(), newFeatures.size());
		while (oldFeatures.size() < count) {
			oldFeatures.add(null);
		}
		while (newFeatures.size() < count) {
			newFeatures.add(null);
		}
		for (int i = 0; i < count; i++) {
			InternalFeature oldFeature = oldFeatures.get(i);
			InternalFeature newFeature = newFeatures.get(i);
			if (null == newFeature) {
				// delete ?
				if (null != oldFeature) {
					if (securityContext.isFeatureDeleteAuthorized(layerId, oldFeature)) {
						layer.delete(oldFeature.getLocalId());
					} else {
						throw new GeomajasSecurityException(ExceptionCode.FEATURE_DELETE_PROHIBITED,
								oldFeature.getId(), securityContext.getUserId());
					}
				}
			} else {
				// create or update
				Object feature;
				if (null == oldFeature) {
					// create new feature
					transformGeometry(newFeature, mapToLayer);
					if (securityContext.isFeatureCreateAuthorized(layerId, oldFeature)) {
						if (newFeature.getLocalId() == null) {
							feature = featureModel.newInstance();
						} else {
							feature = featureModel.newInstance(newFeature.getLocalId());
						}
					} else {
						throw new GeomajasSecurityException(ExceptionCode.FEATURE_CREATE_PROHIBITED, securityContext
								.getUserId());
					}
				} else {
					if (null == oldFeature.getId() || !oldFeature.getId().equals(newFeature.getId())) {
						throw new GeomajasException(ExceptionCode.FEATURE_ID_MISMATCH);
					}
					transformGeometry(newFeature, mapToLayer);
					if (securityContext.isFeatureUpdateAuthorized(layerId, oldFeature, newFeature)) {
						feature = layer.read(newFeature.getLocalId());
					} else {
						throw new GeomajasSecurityException(ExceptionCode.FEATURE_UPDATE_PROHIBITED,
								oldFeature.getId(), securityContext.getUserId());
					}
				}

				// Assure only writable attributes are set
				Map<String, Attribute> requestAttributes = newFeature.getAttributes();
				Map<String, Attribute> filteredAttributes = new HashMap<String, Attribute>();
				if (null != requestAttributes) {
					for (String key : requestAttributes.keySet()) {
						if (securityContext.isAttributeWritable(layerId, newFeature, key)) {
							filteredAttributes.put(key, requestAttributes.get(key));
						}
					}
				}
				featureModel.setAttributes(feature, filteredAttributes);

				if (newFeature.getGeometry() != null) {
					featureModel.setGeometry(feature, newFeature.getGeometry());
				}
				feature = layer.saveOrUpdate(feature);

				// Not needed for existing features, but no problem to re-set feature id
				String id = featureModel.getId(feature);
				newFeature.setId(layerId + "." + id);

				newFeature.setAttributes(featureModel.getAttributes(feature));

				newFeature.setEditable(securityContext.isFeatureUpdateAuthorized(layerId, newFeature));
				newFeature.setDeletable(securityContext.isFeatureDeleteAuthorized(layerId, newFeature));
			}
		}
	}

	/**
	 * Convert the geometry (if any) in the passed feature to layer crs.
	 * 
	 * @param feature
	 *            feature in which the geometry should be updated
	 * @param mapToLayer
	 *            transformation to apply
	 * @throws GeomajasException
	 *             oops
	 */
	private void transformGeometry(InternalFeature feature, MathTransform mapToLayer) throws GeomajasException {
		if (feature.getGeometry() != null) {
			try {
				feature.setGeometry(JTS.transform(feature.getGeometry(), mapToLayer));
			} catch (TransformException te) {
				throw new GeomajasException(te, ExceptionCode.GEOMETRY_TRANSFORMATION_FAILED);
			}
		}
	}

	public List<InternalFeature> getFeatures(String layerId, CoordinateReferenceSystem crs, Filter queryFilter,
			NamedStyleInfo style, int featureIncludes, int offset, int maxResultSize) throws GeomajasException {
		VectorLayer layer = getVectorLayer(layerId);
		Filter filter = getLayerFilter(layer.getLayerInfo(), queryFilter);

		List<StyleFilter> styleFilters = null;
		if (style == null) {
			// no style specified, take the first
			style = layer.getLayerInfo().getNamedStyleInfos().get(0);
		} else if (style.getFeatureStyles().isEmpty()) {
			// only name specified, find it
			style = layer.getLayerInfo().getNamedStyleInfo(style.getName());
		}

		if ((featureIncludes & FEATURE_INCLUDE_STYLE) != 0) {
			if (style == null) {
				throw new GeomajasException(ExceptionCode.RENDER_FEATURE_MODEL_PROBLEM, "Style not found");
			}
			styleFilters = initStyleFilters(style.getFeatureStyles());
		}

		MathTransform transformation = null;
		if ((featureIncludes & FEATURE_INCLUDE_GEOMETRY) != 0 && crs != null && !crs.equals(layer.getCrs())) {
			try {
				transformation = geoService.findMathTransform(layer.getCrs(), crs);
			} catch (FactoryException fe) {
				throw new GeomajasException(fe, ExceptionCode.CRS_TRANSFORMATION_NOT_POSSIBLE, crs, layer.getCrs());
			}
		}

		List<InternalFeature> res = new ArrayList<InternalFeature>();
		log.debug("getElements " + filter + ",offset = " + offset + ",maxResultSize= " + maxResultSize);
		Iterator<?> it = layer.getElements(filter, offset, maxResultSize);
		while (it.hasNext()) {
			InternalFeature feature = convertFeature(it.next(), layerId, layer, transformation, styleFilters, style
					.getLabelStyle(), featureIncludes);
			log.debug("checking feature");
			if (securityContext.isFeatureVisible(layerId, feature)) {
				feature.setEditable(securityContext.isFeatureUpdateAuthorized(layerId, feature));
				feature.setDeletable(securityContext.isFeatureDeleteAuthorized(layerId, feature));
				res.add(feature);
			} else {
				log.debug("feature not visible");
			}
		}
		log.debug("getElements done");
		return res;
	}

	public List<InternalFeature> getFeatures(String layerId, CoordinateReferenceSystem crs, Filter filter,
			NamedStyleInfo style, int featureIncludes) throws GeomajasException {
		return getFeatures(layerId, crs, filter, style, featureIncludes, 0, 0);
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
		res.setId(layerId + "." + featureModel.getId(feature));
		res.setLayer(layer);

		// If allowed, add the label to the InternalFeature:
		if ((featureIncludes & FEATURE_INCLUDE_LABEL) != 0) {
			String labelAttr = labelStyle.getLabelAttributeName();
			Attribute attribute = featureModel.getAttribute(feature, labelAttr);
			if (null != attribute && null != attribute.getValue()) {
				res.setLabel(attribute.getValue().toString());
			}
		}

		// If allowed, add the geometry (transformed!) to the InternalFeature:
		if ((featureIncludes & FEATURE_INCLUDE_GEOMETRY) != 0) {
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
		if ((featureIncludes & FEATURE_INCLUDE_STYLE) != 0) {
			res.setStyleDefinition(findStyleFilter(feature, styles).getStyleDefinition());
		}

		// If allowed, add the attributes to the InternalFeature:
		if ((featureIncludes & FEATURE_INCLUDE_ATTRIBUTES) != 0) {
			// Assure only readable attributes are set
			Map<String, Attribute> featureAttributes = featureModel.getAttributes(feature);
			res.setAttributes(featureAttributes); // to allow isAttributeReadable to see full object
			Map<String, Attribute> filteredAttributes = new HashMap<String, Attribute>();
			for (String key : featureAttributes.keySet()) {
				if (securityContext.isAttributeReadable(layerId, res, key)) {
					Attribute attribute = featureAttributes.get(key);
					attribute.setEditable(securityContext.isAttributeWritable(layerId, res, key));
					filteredAttributes.put(key, featureAttributes.get(key));
				}
			}
			res.setAttributes(filteredAttributes); // overwrite with filtered attributes
		}

		return res;
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

	public Envelope getBounds(String layerId, CoordinateReferenceSystem crs, Filter queryFilter)
			throws GeomajasException {
		VectorLayer layer = getVectorLayer(layerId);
		Filter filter = getLayerFilter(layer.getLayerInfo(), queryFilter);

		MathTransform layerToTarget;
		try {
			layerToTarget = geoService.findMathTransform(layer.getCrs(), crs);
		} catch (FactoryException fe) {
			throw new GeomajasException(fe, ExceptionCode.CRS_TRANSFORMATION_NOT_POSSIBLE, crs, layer.getCrs());
		}
		Envelope bounds = layer.getBounds(filter);
		try {
			bounds = JTS.transform(bounds, layerToTarget);
		} catch (TransformException te) {
			throw new GeomajasException(te, ExceptionCode.GEOMETRY_TRANSFORMATION_FAILED);
		}
		return bounds;
	}

	/**
	 * Get the (combined) filter to apply. Always returns a filter, never null.
	 * <p/>
	 * This combines the visible area, the security filter for the layer, the default filter for the layer,
	 * 
	 * @param layerInfo
	 *            layer info (matching the layer id)
	 * @param queryFilter
	 *            base query filter if any
	 * @return filter to apply
	 * @throws GeomajasException
	 *             oops
	 */
	private Filter getLayerFilter(VectorLayerInfo layerInfo, Filter queryFilter) throws GeomajasException {
		Filter filter = queryFilter;
		String layerId = layerInfo.getId();

		// apply generic security filter
		Filter layerFeatureFilter = securityContext.getFeatureFilter(layerId);
		if (null != layerFeatureFilter) {
			filter = and(filter, layerFeatureFilter);
		}

		// apply default filter
		String defaultFilter = layerInfo.getFilter();
		if (null != defaultFilter) {
			try {
				filter = and(filter, CQL.toFilter(defaultFilter));
			} catch (CQLException ce) {
				throw new GeomajasException(ce, ExceptionCode.FILTER_APPLY_PROBLEM, defaultFilter);
			}
		}

		// apply visible area filter
		Geometry visibleArea = securityContext.getVisibleArea(layerId);
		String geometryName = layerInfo.getFeatureInfo().getGeometryType().getName();
		if (securityContext.isPartlyVisibleSufficient(layerId)) {
			filter = and(filter, filterService.createIntersectsFilter(visibleArea, geometryName));
		} else {
			filter = and(filter, filterService.createWithinFilter(visibleArea, geometryName));
		}

		if (null == filter) {
			filter = filterService.createTrueFilter();
		}
		return filter;
	}

	private Filter and(Filter f1, Filter f2) {
		if (null == f1) {
			return f2;
		} else if (null == f2) {
			return f1;
		} else {
			return filterService.createAndFilter(f1, f2);
		}
	}

	public List<Object> getObjects(String layerId, String attributeName, Filter queryFilter) throws GeomajasException {
		VectorLayer layer = getVectorLayer(layerId);
		Filter filter = getLayerFilter(layer.getLayerInfo(), queryFilter);
		// @todo security ??

		List<Object> list = new ArrayList<Object>();
		if (layer instanceof VectorLayerAssociationSupport) {
			Iterator<?> it = ((VectorLayerAssociationSupport) layer).getObjects(attributeName, filter);
			while (it.hasNext()) {
				list.add(it.next());
			}
		}
		return list;
	}
}
