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

package org.geomajas.layer.geotools;

import com.vividsolutions.jts.geom.Geometry;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.shapeinmem.FeatureSourceRetriever;
import org.geotools.data.DataStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;

import java.util.HashMap;
import java.util.Map;

/**
 * Geotools feature model. Should be able to use any geotools data source.
 *
 * @author check subversion
 */
public class GeotoolsFeatureModel extends FeatureSourceRetriever implements FeatureModel {

	private SimpleFeatureBuilder builder;

	private int srid;

	// Constructor:

	public GeotoolsFeatureModel(DataStore dataStore, String featureSourceName, int srid)
			throws LayerException {
		setDataStore(dataStore);
		setFeatureSourceName(featureSourceName);
		this.srid = srid;
		builder = new SimpleFeatureBuilder(getSchema());
	}

	public void setLayerInfo(VectorLayerInfo vectorLayerInfo) throws LayerException {
		//not used
	}

	// FeatureModel implementation:

	public Object getAttribute(Object feature, String name) throws LayerException {
		return asFeature(feature).getAttribute(name);
	}

	public Map<String, Object> getAttributes(Object feature) throws LayerException {
		SimpleFeature f = asFeature(feature);
		HashMap<String, Object> attribs = new HashMap<String, Object>();
		for (int i = 0; i < f.getAttributeCount(); i++) {
			String name = f.getFeatureType().getAttributeDescriptors().get(i).getLocalName();
			attribs.put(name, f.getAttribute(i));
		}
		return attribs;
	}

	public Geometry getGeometry(Object feature) throws LayerException {
		Geometry geometry = (Geometry) asFeature(feature).getDefaultGeometry();
		geometry.setSRID(srid);
		return (Geometry) geometry.clone();
	}

	public String getGeometryAttributeName() throws LayerException {
		return getSchema().getGeometryDescriptor().getLocalName();
	}

	public String getId(Object feature) throws LayerException {
		SimpleFeature featureAsFeature = asFeature(feature);
		return featureAsFeature.getID().substring(featureAsFeature.getID().lastIndexOf(".") + 1);
	}

	public int getSrid() throws LayerException {
		return srid;
	}

	/**
	 * TODO I have my doubts if this function actually works.
	 */
	public Object newInstance() throws LayerException {
		if (builder == null) {
			throw new LayerException(ExceptionCode.CREATE_FEATURE_NO_FEATURE_TYPE);
		}
		return builder.buildFeature(Integer.toString(nextId++));
	}

	public Object newInstance(String id) throws LayerException {
		if (builder == null) {
			throw new LayerException(ExceptionCode.CREATE_FEATURE_NO_FEATURE_TYPE);
		}
		return builder.buildFeature(id);
	}

	public void setAttributes(Object feature, Map<String, Object> attributes) throws LayerException {
		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			if (!entry.getKey().equals(getGeometryAttributeName())) {
				asFeature(feature).setAttribute(entry.getKey(), entry.getValue());
			}
		}
	}

	public void setGeometry(Object feature, Geometry geometry) throws LayerException {
		asFeature(feature).setDefaultGeometry(geometry);
	}

	public boolean canHandle(Object feature) {
		return feature instanceof SimpleFeature;
	}

	public Class<?> getSuperClass() throws LayerException {
		return null;
	}
}