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
import java.util.Iterator;
import java.util.List;

import org.geomajas.configuration.LayerInfo;
import org.geomajas.configuration.StyleInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.internal.application.style.StyleFilterImpl;
import org.geomajas.internal.layer.feature.ClippedInternalFeature;
import org.geomajas.internal.layer.feature.RenderedFeatureImpl;
import org.geomajas.layer.LayerModel;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.rendering.painter.LayerPaintContext;
import org.geomajas.rendering.painter.PaintFactory;
import org.geomajas.rendering.style.StyleFilter;
import org.geomajas.service.ApplicationService;
import org.geomajas.service.BboxService;
import org.geomajas.service.GeoService;
import org.geomajas.service.VectorLayerService;
import org.geotools.geometry.jts.JTS;
import org.opengis.filter.Filter;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Implementation of {@link org.geomajas.service.VectorLayerService}, a service which allows accessing data from a
 * vector layer model.
 * <p/>
 * All access to layer models should be done through this service, not by accessing the layer models directly as this
 * adds possible caching, security etc. These services are implemented using pipelines
 * (see {@link org.geomajas.rendering.pipeline.PipelineService}) to make them configurable.
 *
 * @author Joachim Van der Auwera
 */
@Component
public class VectorLayerServiceImpl implements VectorLayerService {

	@Autowired
	private ApplicationService applicationService;

	@Autowired
	private GeoService geoService;

	@Autowired
	private BboxService bboxService;

	@Autowired
	private PaintFactory paintFactory;

	public void saveOrUpdate(String layerId, CoordinateReferenceSystem crs,
			List<InternalFeature> oldFeatures, List<InternalFeature> newFeatures) throws GeomajasException {
		VectorLayer layer = applicationService.getVectorLayer(layerId);
		if (null == layer) {
			throw new GeomajasException(ExceptionCode.VECTOR_LAYER_NOT_FOUND, layerId);
		}
		LayerModel layerModel = layer.getLayerModel();
		FeatureModel featureModel = layerModel.getFeatureModel();

		MathTransform mapToLayer;
		try {
			mapToLayer = geoService.findMathTransform(crs, layer.getCrs());
		} catch (FactoryException fe) {
			throw new GeomajasException(fe, ExceptionCode.CRS_TRANSFORMATION_NOT_POSSIBLE, crs, layer.getCrs());
		}

		LayerPaintContext context = paintFactory.createLayerPaintContext(layer);

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
					layerModel.delete(oldFeature.getLocalId());
				}
			} else {
				// create or update
				Object feature;
				if (null == oldFeature) {
					// create new feature
					feature = featureModel.newInstance(newFeature.getLocalId());
				} else {
					feature = layerModel.read(newFeature.getLocalId());
				}
				featureModel.setAttributes(feature, newFeature.getAttributes());

				if (newFeature.getGeometry() != null) {
					Geometry transformed;
					try {
						transformed = JTS.transform(newFeature.getGeometry(), mapToLayer);
					} catch (TransformException te) {
						throw new GeomajasException(te, ExceptionCode.GEOMETRY_TRANSFORMATION_FAILED);
					}
					featureModel.setGeometry(feature, transformed);
				}
				feature = layerModel.saveOrUpdate(feature);

				// Not needed for existing features, but no problem to re-set feature id
				String id = featureModel.getId(feature);
				newFeature.setId(layerId + "." + id);

				newFeature.setStyleDefinition(context.findStyleFilter(newFeature).getStyleDefinition());
				newFeature.setAttributes(featureModel.getAttributes(feature));
			}
		}
	}

	public List<InternalFeature> getFeatures(String layerId, CoordinateReferenceSystem crs, Filter filter,
			List<StyleInfo> styleDefinitions, int featureIncludes)
			throws GeomajasException {
		VectorLayer layer = applicationService.getVectorLayer(layerId);
		if (null == layer) {
			throw new GeomajasException(ExceptionCode.VECTOR_LAYER_NOT_FOUND, layerId);
		}
		LayerModel layerModel = layer.getLayerModel();

		List<StyleFilter> styleFilters = null;
		if ((featureIncludes & FEATURE_INCLUDE_STYLE) != 0) {
			if (null != styleDefinitions) {
				styleFilters = initStyleFilters(styleDefinitions);
			} else {
				styleFilters = initStyleFilters(layer.getLayerInfo().getStyleDefinitions());
			}
		}

		//LayerPaintContext context = new DefaultLayerPaintContext(this, styleDefs);
		MathTransform transformation = null;
		if ((featureIncludes & FEATURE_INCLUDE_GEOMETRY) != 0 && crs != null && !crs.equals(layer.getCrs())) {
			try {
				transformation = geoService.findMathTransform(layer.getCrs(), crs);
			} catch (FactoryException fe) {
				throw new GeomajasException(fe, ExceptionCode.CRS_TRANSFORMATION_NOT_POSSIBLE, crs, layer.getCrs());
			}
		}

		List<InternalFeature> res = new ArrayList<InternalFeature>();
		Iterator<?> it = layerModel.getElements(filter);
		while (it.hasNext()) {
			res.add(convertFeature(it.next(), layer, transformation, styleFilters, featureIncludes));
		}
		return res;
	}

	/**
	 * Convert the generic feature object (as obtained from te layer model) into a {@link RenderedFeature}, with
	 * requested data. Part may be lazy loaded.
	 *
	 * @param feature A feature object that comes directly from the {@link LayerModel}
	 * @param layer vector layer for the feature
	 * @param transformation transformation to apply to the geometry
	 * @param styles style filters to apply
	 * @param featureIncludes aspects to include in features
	 * @throws GeomajasException oops
	 */
	private InternalFeature convertFeature(Object feature, VectorLayer layer, MathTransform transformation,
			List<StyleFilter> styles, int featureIncludes) throws GeomajasException {
		LayerInfo layerInfo = layer.getLayerInfo();
		FeatureModel featureModel = layer.getLayerModel().getFeatureModel();
		ClippedInternalFeature res = new ClippedInternalFeature(new RenderedFeatureImpl(bboxService));
		res.setId(layerInfo.getId() + "." + featureModel.getId(feature));
		res.setLayer(layer);

		// If allowed, add the label to the RenderedFeature:
		if ((featureIncludes & FEATURE_INCLUDE_LABEL) != 0) {
			String labelAttr = layer.getLayerInfo().getLabelAttribute().getLabelAttributeName();
			Object attribute = featureModel.getAttribute(feature, labelAttr);
			if (attribute != null) {
				res.setLabel(attribute.toString());
			}
		}

		// If allowed, add the geometry (transformed!) to the RenderedFeature:
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

		// If allowed, add the style definition to the RenderedFeature:
		if ((featureIncludes & FEATURE_INCLUDE_STYLE) != 0) {
			res.setStyleDefinition(findStyleFilter(feature, styles).getStyleDefinition());
		}

		// If allowed, add the attributes to the RenderedFeature:
		if ((featureIncludes & FEATURE_INCLUDE_ATTRIBUTES) != 0) {
			res.setAttributes(featureModel.getAttributes(feature));
		}

		return res;
	}

	/**
	 * Find the style filter that must be applied to this feature.
	 *
	 * @param feature feature to find the style for
	 * @param styles style filters to select from
	 * @return a style filter
	 */
	public StyleFilter findStyleFilter(Object feature, List<StyleFilter> styles) {
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
	 * @param styleDefinitions list of style definitions
	 * @return list of style filters
	 */
	private List<StyleFilter> initStyleFilters(List<StyleInfo> styleDefinitions) {
		List<StyleFilter> styleFilters = new ArrayList<StyleFilter>();
		if (styleDefinitions == null || styleDefinitions.size() == 0) {
			styleFilters.add(new StyleFilterImpl()); // use default.
		} else {
			for (StyleInfo styleDef : styleDefinitions) {
				styleFilters.add(new StyleFilterImpl(styleDef));
			}
		}
		return styleFilters;
	}

	public Bbox getBounds(String layerId, CoordinateReferenceSystem crs, Filter filter) throws GeomajasException {
		VectorLayer layer = applicationService.getVectorLayer(layerId);
		if (null == layer) {
			throw new GeomajasException(ExceptionCode.VECTOR_LAYER_NOT_FOUND, layerId);
		}
		LayerModel layerModel = layer.getLayerModel();

		MathTransform layerToTarget;
		try {
			layerToTarget = geoService.findMathTransform(layer.getCrs(), crs);
		} catch (FactoryException fe) {
			throw new GeomajasException(fe, ExceptionCode.CRS_TRANSFORMATION_NOT_POSSIBLE, crs, layer.getCrs());
		}
		Bbox bounds = layerModel.getBounds(filter);
		try {
			bounds = bboxService.fromEnvelope(JTS.transform(bboxService.toEnvelope(bounds), layerToTarget));
		} catch (TransformException te) {
			throw new GeomajasException(te, ExceptionCode.GEOMETRY_TRANSFORMATION_FAILED);
		}
		return bounds;
	}

	public List<Object> getObjects(String layerId, String attributeName, Filter filter) throws GeomajasException {
		VectorLayer layer = applicationService.getVectorLayer(layerId);
		if (null == layer) {
			throw new GeomajasException(ExceptionCode.VECTOR_LAYER_NOT_FOUND, layerId);
		}
		LayerModel layerModel = layer.getLayerModel();

		List<Object> list = new ArrayList<Object>();
		Iterator<?> it = layerModel.getObjects(attributeName, filter);
		while (it.hasNext()) {
			list.add(it.next());
		}
		return list;
	}
}
