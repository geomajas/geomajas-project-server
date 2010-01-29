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
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.layer.Layer;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.LayerModel;
import org.geomajas.layer.VectorLayer;
import org.geomajas.service.GeoService;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.referencing.CRS;
import org.opengis.filter.Filter;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Default vector layer implementation.
 *
 * @author Jan De Moerloose
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
