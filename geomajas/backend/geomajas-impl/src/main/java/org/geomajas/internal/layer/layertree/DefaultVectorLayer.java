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
package org.geomajas.internal.layer.layertree;

import org.geomajas.configuration.EditPermissionType;
import org.geomajas.configuration.StyleInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.internal.rendering.DefaultLayerPaintContext;
import org.geomajas.layer.Layer;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.LayerModel;
import org.geomajas.layer.VectorLayer;
import org.geomajas.rendering.RenderException;
import org.geomajas.rendering.painter.LayerPaintContext;
import org.geomajas.rendering.painter.feature.FeaturePainter;
import org.geomajas.service.GeoService;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.referencing.CRS;
import org.opengis.filter.Filter;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Default vector layer implementation.
 *
 * @author check subversion
 */
public class DefaultVectorLayer implements Layer<VectorLayerInfo>, VectorLayer {

	@Autowired
	private GeoService geoService;

	/**
	 * Model for all the elements in this layer. The elements are effectively stored in this model.
	 */
	private LayerModel layerModel;

	/**
	 * Default filter, configured in a layer's XML. (is often null)
	 */
	private Filter filter;

	private VectorLayerInfo layerInfo;

	private CoordinateReferenceSystem crs;

	public VectorLayerInfo getLayerInfo() {
		return layerInfo;
	}

	public CoordinateReferenceSystem getCrs() {
		return crs;
	}

	private void initCrs() throws LayerException {
		try {
			crs = CRS.decode(layerInfo.getCrs());
		} catch (NoSuchAuthorityCodeException e) {
			throw new LayerException(ExceptionCode.LAYER_CRS_UNKNOWN_AUTHORITY, e, layerInfo.getId(),
					getLayerInfo().getCrs());
		} catch (FactoryException e) {
			throw new LayerException(ExceptionCode.LAYER_CRS_PROBLEMATIC, e, layerInfo.getId(),
					getLayerInfo().getCrs());
		}
	}

	// Layer implementation:

	/**
	 * <p>
	 * A layer's paint operation. This function will painter only features that accept the given filter. The result can
	 * be retrieved by calling the painter's "getFeatures" function.
	 * </p>
	 * <p>
	 * In essence this function will create a {@link LayerPaintContext} that the painter uses to correctly paint
	 * features. This function will also provide the accepted list of features to the painter.
	 * </p>
	 * 
	 * @param painter
	 *            The painter that will do the actual painting of the features.
	 * @param paintFilter
	 *            The paintFilter that determines what features should be painted.
	 * @param paintStyleDefs
	 *            An optional list of style definitions. These will overwrite the layer's original list of styles.
	 * @param targetCrs
	 *            The map's coordinate system. This may differ from the layer's coordinate system. This function will
	 *            create a transformer out of it, and add it to the
	 *            {@link org.geomajas.rendering.painter.LayerPaintContext} so that the painter knows how to transform
	 *            it's geometries or labels.
	 */
	public void paint(FeaturePainter painter, Filter paintFilter, StyleInfo[] paintStyleDefs,
			CoordinateReferenceSystem targetCrs) throws RenderException {
		StyleInfo[] styleDefs = paintStyleDefs;
		if (styleDefs == null) {
			List<StyleInfo> stylesList = getLayerInfo().getStyleDefinitions();
			styleDefs = stylesList.toArray(new StyleInfo[stylesList.size()]);
		}
		LayerPaintContext context = new DefaultLayerPaintContext(this, styleDefs);
		if (targetCrs != null && !targetCrs.equals(getCrs())) {
			try {
				context.setMathTransform(geoService.findMathTransform(getCrs(), targetCrs));
			} catch (FactoryException e) {
				throw new RenderException(ExceptionCode.LAYER_CRS_INIT_PROBLEM, e);
			}
		}
		try {
			Iterator<?> it = layerModel.getElements(paintFilter);
			while (it.hasNext()) {
				try {
					painter.paint(context, it.next());
				} catch (NoSuchElementException e) {
					throw new RenderException(ExceptionCode.UNEXPECTED_END_OF_FEATURES);
				} catch (Throwable t) {
					throw new RenderException(ExceptionCode.UNEXPECTED_FEATURE_WRITING_PROBLEM, t, getLayerInfo()
						.getLabel());
				}
			}
		} catch (LayerException lme) {
			throw new RenderException(ExceptionCode.RENDER_LAYER_MODEL_PROBLEM, lme);
		}
	}

	public void setLayerInfo(VectorLayerInfo info) throws LayerException {
		this.layerInfo = info;
		initCrs();
		String filterString = info.getFilter();
		if (null != filterString && filterString.length() > 0) {
			try {
				filter = CQL.toFilter(filterString);
			} catch (CQLException e) {
				filter = null; // @todo proper logging
			}
		}
		if (null != layerModel) {
			layerModel.setDefaultFilter(filter);
		}
	}

	public EditPermissionType getEditPermissions() {
		return getLayerInfo().getEditPermissions();
	}

	// Getters and setters:

	public LayerModel getLayerModel() {
		return layerModel;
	}

	public void setLayerModel(LayerModel layerModel) throws LayerException {
		this.layerModel = layerModel;
		layerModel.setLayerInfo(getLayerInfo());
	}

}
