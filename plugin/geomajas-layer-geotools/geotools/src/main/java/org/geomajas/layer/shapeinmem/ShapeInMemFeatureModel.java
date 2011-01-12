/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer.shapeinmem;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.LayerType;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.service.DtoConverterService;
import org.geotools.data.DataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;

/**
 * Feature model for handle shape files in memory.
 * 
 * @author Jan De Moerloose
 * @author Pieter De Graef
 */
public class ShapeInMemFeatureModel extends FeatureSourceRetriever implements FeatureModel {

	private int srid;

	private VectorLayerInfo vectorLayerInfo;

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
		this.vectorLayerInfo = vectorLayerInfo;
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
		if (geom instanceof MultiLineString && vectorLayerInfo.getLayerType() == LayerType.LINESTRING) {
			return (Geometry) geom.getGeometryN(0).clone();
		} else if (geom instanceof MultiPolygon && vectorLayerInfo.getLayerType() == LayerType.POLYGON) {
			return (Geometry) geom.getGeometryN(0).clone();
		} else if (geom instanceof MultiPoint && vectorLayerInfo.getLayerType() == LayerType.POINT) {
			return (Geometry) geom.getGeometryN(0).clone();
		}
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