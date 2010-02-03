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
package org.geomajas.layermodel.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;
import org.geotools.referencing.CRS;
import org.opengis.filter.Filter;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * A simple Java beans based layer model.
 * 
 * @author Jan De Moerloose
 */
public class BeanLayer implements VectorLayer {

	private Map<String, Object> featuresById = new HashMap<String, Object>();

	/**
	 * The features (should be Java beans compliant)
	 */
	private List<Object> features = new ArrayList<Object>();

	private FeatureModel featureModel;

	private VectorLayerInfo layerInfo;

	@Autowired
	private FilterService filterService;

	@Autowired
	private GeoService geoService;

	private CoordinateReferenceSystem crs;

	public CoordinateReferenceSystem getCrs() {
		return crs;
	}

	private void initCrs() throws LayerException {
		try {
			crs = CRS.decode(layerInfo.getCrs());
		} catch (NoSuchAuthorityCodeException e) {
			throw new LayerException(ExceptionCode.LAYER_CRS_UNKNOWN_AUTHORITY, e, layerInfo.getId(), getLayerInfo()
					.getCrs());
		} catch (FactoryException exception) {
			throw new LayerException(ExceptionCode.LAYER_CRS_PROBLEMATIC, exception, layerInfo.getId(), getLayerInfo()
					.getCrs());
		}
	}

	public boolean isCreateCapable() {
		return true;
	}

	public boolean isUpdateCapable() {
		return true;
	}

	public boolean isDeleteCapable() {
		return true;
	}

	public Iterator<?> getElements(Filter queryFilter) throws LayerException {
		List<Object> filteredList = new ArrayList<Object>();
		for (Object feature : featuresById.values()) {
			if (queryFilter.evaluate(feature)) {
				filteredList.add(feature);
			}
		}
		return filteredList.iterator();
	}

	public Envelope getBounds() throws LayerException {
		return getBounds(Filter.INCLUDE);
	}

	/**
	 * Retrieve the bounds of the specified features.
	 * 
	 * @return the bounds of the specified features
	 */
	public Envelope getBounds(Filter queryFilter) throws LayerException {
		Iterator<?> it = getElements(queryFilter);
		// start with null envelope
		Envelope bounds = new Envelope();
		while (it.hasNext()) {
			Object o = it.next();
			Geometry g = featureModel.getGeometry(o);
			bounds.expandToInclude(g.getEnvelopeInternal());
		}
		return bounds;
	}

	public FeatureModel getFeatureModel() {
		return featureModel;
	}

	public void setLayerInfo(VectorLayerInfo layerInfo) throws LayerException {
		this.layerInfo = layerInfo;
		initCrs();
		initFeatureModel();
	}

	public VectorLayerInfo getLayerInfo() {
		return layerInfo;
	}

	public Object create(Object feature) throws LayerException {
		String id = featureModel.getId(feature);
		if (id != null && !featuresById.containsKey(id)) {
			return featuresById.put(id, feature);
		} else {
			return null;
		}
	}

	public Object read(String featureId) throws LayerException {
		return featuresById.get(featureId);
	}

	public Object saveOrUpdate(Object feature) throws LayerException {
		if (read(getFeatureModel().getId(feature)) == null) {
			return create(feature);
		} else {
			// Nothing to do
			return feature;
		}
	}

	public void delete(String featureId) throws LayerException {
		features.remove(featureId);
	}

	public Iterator<?> getObjects(String attributeName, Filter filter) throws LayerException {
		return Collections.EMPTY_LIST.iterator();
	}

	public List<Object> getFeatures() {
		return features;
	}

	public void setFeatures(List<Object> features) throws LayerException {
		if (null != features) {
			this.features.addAll(features);
			if (null != featureModel) {
				for (Object f : features) {
					featuresById.put(featureModel.getId(f), f);
				}
			}
		}
	}

	protected void initFeatureModel() throws LayerException {
		featureModel = new BeanFeatureModel(layerInfo, geoService.getSridFromCrs(layerInfo.getCrs()));
		for (Object f : features) {
			featuresById.put(featureModel.getId(f), f);
		}
	}
}
