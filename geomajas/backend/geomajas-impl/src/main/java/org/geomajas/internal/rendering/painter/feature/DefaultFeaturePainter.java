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

package org.geomajas.internal.rendering.painter.feature;

import com.vividsolutions.jts.geom.Geometry;
import org.geomajas.global.ExceptionCode;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.FeatureFactory;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.rendering.RenderException;
import org.geomajas.rendering.painter.LayerPaintContext;
import org.geotools.geometry.jts.JTS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.TransformException;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Basic implementation if the <code>FeaturePainter</code> interface. It does exactly as the interface suggests:
 * transform the <code>LayerModel</code> objects into <code>RenderedFeature</code> objects.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class DefaultFeaturePainter extends AbstractFeaturePainter {

	/**
	 * The full list of <code>RenderedFeature</code> objects.
	 */
	private List<InternalFeature> features;

	private FeatureFactory featureFactory;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Default constructor. The default settings allow geometries, labels and style definitions to be added to the
	 * <code>RenderedFeature</code> objects, but not attributes.
	 */
	public DefaultFeaturePainter(FeatureFactory featureFactory) {
		super();
		this.featureFactory = featureFactory;
		features = new ArrayList<InternalFeature>();
	}

	/**
	 * Constructor that determines what should be painted and what not.
	 * 
	 * @param renderingAttributes
	 *            Do we allow attributes to be added to the <code>RenderedFeature</code> objects or not?
	 * @param renderingGeometry
	 *            Do we allow geometries to be added to the <code>RenderedFeature</code> objects or not?
	 * @param renderingStyle
	 *            Do we allow style definitions to be added to the <code>RenderedFeature</code> objects or not?
	 * @param renderingLabels
	 *            Do we allow the label string to be added to the <code>RenderedFeature</code> objects or not?
	 */
	public DefaultFeaturePainter(boolean renderingAttributes, boolean renderingGeometry, boolean renderingStyle,
			boolean renderingLabels) {
		super(renderingAttributes, renderingGeometry, renderingStyle, renderingLabels);
		features = new ArrayList<InternalFeature>();
	}

	// -------------------------------------------------------------------------
	// FeaturePainter implementation:
	// -------------------------------------------------------------------------

	/**
	 * Paint an individual feature. In other words transform the generic feature object into a
	 * <code>RenderedFeature</code>.
	 * 
	 * @param paintContext
	 *            The provided painting context. It helps to determine what style a feature should receive.
	 * @param feature
	 *            A feature object that comes directly from the <code>LayerModel</code>.
	 * @throws RenderException
	 */
	public void paint(LayerPaintContext paintContext, Object feature) throws RenderException {
		try {
			FeatureModel featureModel = paintContext.getLayer().getLayerModel().getFeatureModel();

			InternalFeature f = featureFactory.createRenderedFeature();
			f.setId(paintContext.getLayer().getLayerInfo().getId() + "." + featureModel.getId(feature));
			f.setLayer(paintContext.getLayer());

			// If allowed, add the label to the RenderedFeature:
			if (isRenderingLabels()) {
				String labelAttr = paintContext.getLayer().getLayerInfo().getLabelAttribute().getLabelAttributeName();
				Object attribute = featureModel.getAttribute(feature, labelAttr);
				if (attribute != null) {
					f.setLabel(attribute.toString());
				}
			}

			// If allowed, add the geometry (transformed!) to the RenderedFeature:
			if (isRenderingGeometry()) {
				Geometry geometry = featureModel.getGeometry(feature);
				Geometry transformed;
				if (paintContext.getMathTransform() != null) {
					try {
						transformed = JTS.transform(geometry, paintContext.getMathTransform());
					} catch (MismatchedDimensionException e) {
						throw new RenderException(ExceptionCode.RENDER_TRANSFORMATION_FAILED, e);
					} catch (TransformException e) {
						throw new RenderException(ExceptionCode.RENDER_TRANSFORMATION_FAILED, e);
					}
				} else {
					transformed = geometry;
				}
				f.setGeometry(transformed);
			}

			// If allowed, add the style definition to the RenderedFeature:
			if (isRenderingStyle()) {
				f.setStyleDefinition(paintContext.findStyleFilter(feature).getStyleDefinition());
			}

			// If allowed, add the attributes to the RenderedFeature:
			if (isRenderingAttributes()) {
				f.setAttributes(featureModel.getAttributes(feature));
			}

			features.add(f);
		} catch (LayerException fme) {
			throw new RenderException(ExceptionCode.RENDER_FEATURE_MODEL_PROBLEM, fme);
		}
	}

	/**
	 * The full list of <code>RenderedFeature</code> objects.
	 * 
	 * @return features
	 */
	public List<org.geomajas.layer.feature.InternalFeature> getFeatures() {
		return features;
	}
}