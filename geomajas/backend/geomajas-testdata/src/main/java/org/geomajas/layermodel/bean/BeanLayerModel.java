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

import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.LayerModel;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.service.BboxService;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * A simple Java beans based layer model.
 * 
 * @author Jan De Moerloose
 */
@Component
@Scope("prototype")
public class BeanLayerModel implements LayerModel {

	private Map<String, Object> featuresById = new HashMap<String, Object>();

	/**
	 * The features (should be Java beans compliant)
	 */
	private List<Object> features = new ArrayList<Object>();

	private FeatureModel featureModel;

	private VectorLayerInfo layerInfo;

	private Filter defaultFilter;

	@Autowired
	private BboxService bboxService;

	@Autowired
	private FilterService filterService;

	@Autowired
	private GeoService geoService;

	public Iterator<?> getElements(Filter queryFilter) throws LayerException {
		Filter filter = queryFilter;
		if (defaultFilter != null) {
			filter = filterService.createLogicFilter(filter, "AND", defaultFilter);
		}
		Filter realFilter = filter;
		List<Object> filteredList = new ArrayList<Object>();
		for (Object feature : featuresById.values()) {
			if (realFilter.evaluate(feature)) {
				filteredList.add(feature);
			}
		}
		return filteredList.iterator();
	}

	public Bbox getBounds() throws LayerException {
		return getBounds(Filter.INCLUDE);
	}

	/**
	 * Retrieve the bounds of the specified features.
	 * 
	 * @return the bounds of the specified features
	 */
	public Bbox getBounds(Filter queryFilter) throws LayerException {
		Filter filter = queryFilter;
		if (defaultFilter != null) {
			filter = filterService.createLogicFilter(filter, "AND", defaultFilter);
		}
		Iterator<?> it = getElements(filter);
		// start with null envelope
		Envelope bounds = new Envelope();
		while (it.hasNext()) {
			Object o = it.next();
			Geometry g = featureModel.getGeometry(o);
			bounds.expandToInclude(g.getEnvelopeInternal());
		}
		return bboxService.fromEnvelope(bounds);
	}

	public FeatureModel getFeatureModel() {
		return featureModel;
	}

	public void setLayerInfo(VectorLayerInfo layerInfo) throws LayerException {
		this.layerInfo = layerInfo;
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

	public Filter getDefaultFilter() {
		return defaultFilter;
	}

	public void setDefaultFilter(Filter defaultFilter) {
		this.defaultFilter = defaultFilter;
	}

	public List<Object> getFeatures() {
		return features;
	}

	public void setFeatures(List<Object> features) throws LayerException {
		this.features = features;
	}

	protected void initFeatureModel() throws LayerException {
		AssociationAttributeInfo info = new AssociationAttributeInfo();
		featureModel = new BeanFeatureModel(layerInfo, geoService.getSridFromCrs(layerInfo.getCrs()));
		for (Object f : features) {
			featuresById.put(featureModel.getId(f), f);
		}
	}
}
