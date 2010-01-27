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

import com.vividsolutions.jts.geom.Geometry;
import org.geomajas.geometry.Bbox;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerModel;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.feature.RenderedFeature;
import org.geomajas.rendering.painter.LayerPaintContext;
import org.geomajas.rendering.painter.PaintFactory;
import org.geomajas.service.ApplicationService;
import org.geomajas.service.GeoService;
import org.geomajas.service.VectorLayerModelService;
import org.geotools.geometry.jts.JTS;
import org.opengis.filter.Filter;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Implementation of {@link VectorLayerModelService}, a service which allows accessing data from a vector layer model.
 * <p/>
 * All access to layer models should be done through this service, not by accessing the layer models directly as this
 * adds possible caching, security etc. These services are implemented using pipelines
 * (see {@link org.geomajas.rendering.pipeline.PipelineService}) to make them configurable.
 *
 * @author Joachim Van der Auwera
 */
@Component
public class VectorLayerModelServiceImpl implements VectorLayerModelService {

	@Autowired
	private ApplicationService applicationService;

	@Autowired
	private GeoService geoService;

	@Autowired
	private PaintFactory paintFactory;

	public void saveOrUpdate(String layerId, CoordinateReferenceSystem crs,
			List<RenderedFeature> oldFeatures, List<RenderedFeature> newFeatures) throws GeomajasException {
		VectorLayer layer = applicationService.getVectorLayer(layerId);
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
			RenderedFeature oldFeature = oldFeatures.get(i);
			RenderedFeature newFeature = newFeatures.get(i);
			if (null == newFeature) {
				// delete ?
				if (null != oldFeature) {
					layerModel.delete(oldFeature.getLocalId());
				}
			} else {
				// create or update
				Object feature = null;
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

	public Iterable<RenderedFeature> getElements(String layerId, CoordinateReferenceSystem crs, Filter filter)
			throws GeomajasException {
		throw new GeomajasException(ExceptionCode.NOT_IMPLEMENTED);
	}

	public Bbox getBounds(String layerId, CoordinateReferenceSystem crs, Filter filter) throws GeomajasException {
		throw new GeomajasException(ExceptionCode.NOT_IMPLEMENTED);
	}

	public Iterable<?> getObjects(String layerId, String attributeName, Filter filter) throws GeomajasException {
		throw new GeomajasException(ExceptionCode.NOT_IMPLEMENTED);
	}
}
