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

package org.geomajas.layer.geotools;

import com.vividsolutions.jts.geom.Geometry;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.shapeinmem.FeatureSourceRetriever;
import org.geomajas.service.DtoConverterService;
import org.geotools.data.DataStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;

import java.util.HashMap;
import java.util.Map;

/**
 * GeoTools feature model. Should be able to use any GeoTools data source.
 *
 * @author Jan De Moerloose
 * @author Pieter De Graef
 */
public class GeoToolsFeatureModel extends FeatureSourceRetriever implements FeatureModel {

	private SimpleFeatureBuilder builder;

	private int srid;

	private DtoConverterService converterService;

	private Map<String, AttributeInfo> attributeInfoMap = new HashMap<String, AttributeInfo>();

	// Constructor:

	public GeoToolsFeatureModel(DataStore dataStore, String featureSourceName, int srid,
			DtoConverterService converterService) throws LayerException {
		setDataStore(dataStore);
		setFeatureSourceName(featureSourceName);
		this.srid = srid;
		builder = new SimpleFeatureBuilder(getSchema());
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
		return convertAttribute(asFeature(feature).getAttribute(name), name);
	}

	public Map<String, Attribute> getAttributes(Object feature) throws LayerException {
		SimpleFeature f = asFeature(feature);
		HashMap<String, Attribute> attribs = new HashMap<String, Attribute>();
		for (AttributeInfo attributeInfo : attributeInfoMap.values()) {
			String name = attributeInfo.getName();
			attribs.put(name, convertAttribute(f.getAttribute(name), name));
		}
		return attribs;
	}

	private Attribute convertAttribute(Object object, String name) throws LayerException {
		AttributeInfo attributeInfo = attributeInfoMap.get(name);
		if (null == attributeInfo) {
			throw new LayerException(ExceptionCode.ATTRIBUTE_UNKNOWN, name, attributeInfoMap.keySet());
		}
		try {
			return converterService.toDto(object, attributeInfo);
		} catch (GeomajasException e) {
			throw new LayerException(e);
		}
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
		return featureAsFeature.getID();
	}

	public int getSrid() throws LayerException {
		return srid;
	}

	public Object newInstance() throws LayerException {
		if (builder == null) {
			throw new LayerException(ExceptionCode.CREATE_FEATURE_NO_FEATURE_TYPE);
		}
		return builder.buildFeature(Long.toString(nextId++));
	}

	public Object newInstance(String id) throws LayerException {
		if (builder == null) {
			throw new LayerException(ExceptionCode.CREATE_FEATURE_NO_FEATURE_TYPE);
		}
		return builder.buildFeature(id);
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
}