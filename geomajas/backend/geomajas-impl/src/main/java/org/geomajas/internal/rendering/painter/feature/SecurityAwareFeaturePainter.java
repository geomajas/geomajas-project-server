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
import org.geomajas.internal.layer.feature.EditableFeature;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.FeatureFactory;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.feature.RenderedFeature;
import org.geomajas.rendering.RenderException;
import org.geomajas.rendering.painter.LayerPaintContext;
import org.geotools.geometry.jts.JTS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Basic implementation if the <code>FeaturePainter</code> interface. It does
 * exactly as the interface suggests: transform the <code>LayerModel</code>
 * objects into <code>RenderedFeature</code> objects.
 * </p>
 *
 * @author Pieter De Graef
 */
public class SecurityAwareFeaturePainter extends AbstractFeaturePainter {

	private final Logger log = LoggerFactory.getLogger(SecurityAwareFeaturePainter.class);

	/**
	 * Do we allow the 'editable' field to be added to the
	 * <code>RenderedFeature</code> objects or not? (speed issue)
	 */
	public static final int OPTION_PAINT_EDITABLE = 16;

	/**
	 * The full list of <code>RenderedFeature</code> objects.
	 */
	private List<RenderedFeature> features;

	private FeatureFactory featureFactory;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Default constructor. The default settings allow geometries, labels and
	 * style definitions to be added to the <code>RenderedFeature</code>
	 * objects, but not attributes.
	 */
	public SecurityAwareFeaturePainter(FeatureFactory featureFactory) {
		super();
		this.featureFactory = featureFactory;
		setOption(OPTION_PAINT_EDITABLE, true);
		features = new ArrayList<RenderedFeature>();
		//this.userContext = userContext;
		//this.sm = sm;
	}

	/**
	 * Constructor that determines what should be painted and what not.
	 *
	 * @param paintAttributes
	 *            Do we allow attributes to be added to the
	 *            <code>RenderedFeature</code> objects or not?
	 * @param paintGeometry
	 *            Do we allow geometries to be added to the
	 *            <code>RenderedFeature</code> objects or not?
	 * @param paintStyle
	 *            Do we allow style definitions to be added to the
	 *            <code>RenderedFeature</code> objects or not?
	 * @param paintLabel
	 *            Do we allow the label string to be added to the
	 *            <code>RenderedFeature</code> objects or not?
	 * @param paintEditable
	 *            Do we allow the 'editable' field to be added to the
	 *            <code>RenderedFeature</code> objects or not?
	 * @param userContext user context
	 * @param sm security manager
	 * @param application application id
	 */
	/*
	public SecurityAwareFeaturePainter(boolean paintAttributes, boolean paintGeometry, boolean paintStyle,
			boolean paintLabel, boolean paintEditable, UserContext userContext, GeoSecurityManager sm,
			String application) {
		super(paintAttributes, paintGeometry, paintStyle, paintLabel);
		setOption(OPTION_PAINT_EDITABLE, paintEditable);
		features = new ArrayList<RenderedFeature>();
		this.userContext = userContext;
		this.sm = sm;
		this.application = application;
	}
	*/

	// -------------------------------------------------------------------------
	// FeaturePainter implementation:
	// -------------------------------------------------------------------------

	/**
	 * Paint an individual feature. In other words transform the generic feature
	 * object into a <code>RenderedFeature</code>.
	 *
	 * @param paintContext
	 *            The provided painting context. It helps to determine what
	 *            style a feature should receive.
	 * @param feature
	 *            A feature object that comes directly from the
	 *            <code>LayerModel</code>.
	 * @throws RenderException
	 */
	public void paint(LayerPaintContext paintContext, Object feature) throws RenderException {
		try {
			FeatureModel featureModel = paintContext.getLayer().getLayerModel().getFeatureModel();

			EditableFeature f = (EditableFeature) featureFactory.createEditableFeature();
			f.setId(paintContext.getLayer().getLayerInfo().getId() + "." + featureModel.getId(feature));
			f.setLayer(paintContext.getLayer());

			// If allowed, add the label to the RenderedFeature:
			if (getOption(OPTION_PAINT_LABEL)) {
				String labelAttr = paintContext.getLayer().getLayerInfo().getLabelAttribute().getLabelAttributeName();
				Object attribute = featureModel.getAttribute(feature, labelAttr);
				if (attribute != null) {
					f.setLabel(attribute.toString());
				}
			}

			// If allowed, add the geometry (transformed!) to the RenderedFeature:
			if (getOption(OPTION_PAINT_GEOMETRY)) {
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
			if (getOption(OPTION_PAINT_STYLE)) {
				f.setStyleDefinition(paintContext.findStyleFilter(feature).getStyleDefinition());
			}

			// If allowed, add the attributes to the RenderedFeature:
			if (getOption(OPTION_PAINT_ATTRIBUTES)) {
				f.setAttributes(featureModel.getAttributes(feature));
			}

			if (getOption(OPTION_PAINT_EDITABLE)
					&& paintContext.getLayer().getEditPermissions().isUpdatingAllowed()) {
				f.setEditable(true); // to replace commented bit from below
				/*
				PermissionResult pr = sm.checkDataPermission(userContext, EnumSet.of(OperationType.UPDATE),
						new Resource(f.getLayer().getLayerInfo().getId()), application);
				if (pr.isAllowed()) {
					if (pr.getFilter() != null) {
						try {
							String securityFilter = StringUtil.substituteParameters(pr.getFilter(), userContext
									.getUser().getRoleProperties());
							securityFilter = StringUtil.substituteFormulas(securityFilter);
							Filter filter = CQL.toFilter(securityFilter);
							if (filter.evaluate(feature)) {
								f.setEditable(true);
							}
						} catch (CQLException e) {
							log.error(e.getMessage(), e);
							f.setEditable(false);
						}
					} else {
						f.setEditable(true);
					}
				}
				*/
			}
			features.add(f);
		} catch (LayerException fme) {
			throw new RenderException(ExceptionCode.RENDER_FEATURE_MODEL_PROBLEM, fme);
		}

	}

	public List<RenderedFeature> getFeatures() {
		return features;
	}
}
