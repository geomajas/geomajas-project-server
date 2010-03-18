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
package org.geomajas.layer.shapeinmem;

import com.vividsolutions.jts.geom.Geometry;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.geotools.data.DataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * Feature model for handle shape files in memory.
 *
 * @author check subversion
 */
public class ShapeInMemFeatureModel extends FeatureSourceRetriever implements FeatureModel {

	private int srid;

	@Autowired
	private GeoService geoService;

	private DtoConverterService converterService;

	private Map<String, AttributeInfo> attributeInfoMap = new HashMap<String, AttributeInfo>();

	// Constructor:

	public ShapeInMemFeatureModel(DataStore dataStore, String featureSourceName, int srid,
			DtoConverterService converterService) throws LayerException {
		setDataStore(dataStore);
		setFeatureSourceName(featureSourceName);
		this.srid = srid;
		this.converterService = converterService;
	}

	public void setLayerInfo(VectorLayerInfo vectorLayerInfo) throws LayerException {
		FeatureInfo featureInfo = vectorLayerInfo.getFeatureInfo();
		for (AttributeInfo info : featureInfo.getAttributes()) {
			attributeInfoMap.put(info.getName(), info);
		}
	}

	// FeatureModel implementation:

	public Attribute getAttribute(Object feature, String name) throws LayerException {
		AttributeInfo attributeInfo = attributeInfoMap.get(name);
		if (null == attributeInfo) {
			throw new LayerException(ExceptionCode.ATTRIBUTE_UNKNOWN, name);
		}
		try {
			return converterService.toDto(asFeature(feature).getAttribute(name), attributeInfo);
		} catch (GeomajasException e) {
			throw new LayerException(e);
		}
	}

	public Map<String, Attribute> getAttributes(Object feature) throws LayerException {
		SimpleFeature f = asFeature(feature);
		HashMap<String, Attribute> attribs = new HashMap<String, Attribute>();
		for (Map.Entry<String, AttributeInfo> entry : attributeInfoMap.entrySet()) {
			String name = entry.getKey();
			try {
				attribs.put(name, converterService.toDto(f.getAttribute(name), entry.getValue()));
			} catch (GeomajasException e) {
				throw new LayerException(e);
			}
		}
		return attribs;
	}

	public int getSrid() throws LayerException {
		return srid;
	}

	public Geometry getGeometry(Object feature) throws LayerException {
		Geometry geom = (Geometry) asFeature(feature).getDefaultGeometry();
		return (Geometry) geom.clone();
	}

	public String getGeometryAttributeName() throws LayerException {
		return getSchema().getGeometryDescriptor().getLocalName();
	}

	public String getId(Object feature) throws LayerException {
		SimpleFeature realFeature = asFeature(feature);
		return realFeature.getID();
	}

	public Object newInstance() throws LayerException {
		String id = nextId + "";
		nextId++;
		return CommonFactoryFinder.getFeatureFactory(null).createSimpleFeature(
				new Object[getSchema().getAttributeCount()], getSchema(), id);
	}

	public Object newInstance(String id) throws LayerException {
		return CommonFactoryFinder.getFeatureFactory(null).createSimpleFeature(
				new Object[getSchema().getAttributeCount()], getSchema(), id);
	}

	public void setAttributes(Object feature, Map<String, Attribute> attributes) throws LayerException {
		for (Map.Entry<String, Attribute> entry : attributes.entrySet()) {
			if (!entry.getKey().equals(getGeometryAttributeName())) {
				asFeature(feature).setAttribute(entry.getKey(), entry.getValue().getValue());
			}
		}
	}

	public void setGeometry(Object feature, Geometry geometry) throws LayerException {
		asFeature(feature).setDefaultGeometry(geometry);
	}

	public boolean canHandle(Object feature) {
		return feature instanceof SimpleFeature;
	}

	// Private functions:

	void setNextId(long nextId) {
		this.nextId = nextId;
	}
}