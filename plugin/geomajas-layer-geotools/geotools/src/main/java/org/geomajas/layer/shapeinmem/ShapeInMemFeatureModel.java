/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer.shapeinmem;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.configuration.AbstractAttributeInfo;
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

	private final int srid;

	private VectorLayerInfo vectorLayerInfo;

	private final DtoConverterService converterService;

	protected long nextId;

	// Constructor:

	/**
	 * Create a {@link ShapeInMemFeatureModel}.
	 *
	 * @param dataStore data store
	 * @param featureSourceName feature source name
	 * @param srid srid
	 * @param converterService converter service
	 * @throws LayerException oops
	 */
	public ShapeInMemFeatureModel(DataStore dataStore, String featureSourceName, int srid,
			DtoConverterService converterService) throws LayerException {
		super();
		setDataStore(dataStore);
		setFeatureSourceName(featureSourceName);
		this.srid = srid;
		this.converterService = converterService;
	}

	// FeatureModel implementation:

	@Override
	public void setLayerInfo(VectorLayerInfo vectorLayerInfo) throws LayerException {
		super.setLayerInfo(vectorLayerInfo);
		this.vectorLayerInfo = vectorLayerInfo;
	}

	/** {@inheritDoc} */
	public Attribute getAttribute(Object feature, String name) throws LayerException {
		AbstractAttributeInfo attributeInfo = getAttributeInfoMap().get(name);
		if (null == attributeInfo) {
			throw new LayerException(ExceptionCode.ATTRIBUTE_UNKNOWN, name, getAttributeInfoMap().keySet());
		}
		try {
			return converterService.toDto(asFeature(feature).getAttribute(name), attributeInfo);
		} catch (GeomajasException e) {
			throw new LayerException(e);
		}
	}

	/** {@inheritDoc} */
	public Map<String, Attribute> getAttributes(Object feature) throws LayerException {
		SimpleFeature f = asFeature(feature);
		HashMap<String, Attribute> attribs = new HashMap<String, Attribute>();
		for (Map.Entry<String, AbstractAttributeInfo> entry : getAttributeInfoMap().entrySet()) {
			String name = entry.getKey();
			try {
				attribs.put(name, converterService.toDto(f.getAttribute(name), entry.getValue()));
			} catch (GeomajasException e) {
				throw new LayerException(e);
			}
		}
		return attribs;
	}

	/** {@inheritDoc} */
	public int getSrid() throws LayerException {
		return srid;
	}

	/** {@inheritDoc} */
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

	/** {@inheritDoc} */
	public String getId(Object feature) throws LayerException {
		SimpleFeature realFeature = asFeature(feature);
		return realFeature.getID();
	}

	/** {@inheritDoc} */
	public Object newInstance() throws LayerException {
		String id = Long.toString(nextId);
		nextId++;
		return CommonFactoryFinder.getFeatureFactory(null).createSimpleFeature(
				new Object[getSchema().getAttributeCount()], getSchema(), id);
	}

	/** {@inheritDoc} */
	public Object newInstance(String id) throws LayerException {
		return CommonFactoryFinder.getFeatureFactory(null).createSimpleFeature(
				new Object[getSchema().getAttributeCount()], getSchema(), id);
	}

	@Override
	public void setGeometry(Object feature, Geometry geometry) throws LayerException {
		asFeature(feature).setDefaultGeometry(geometry);
	}

	/** {@inheritDoc} */
	public boolean canHandle(Object feature) {
		return feature instanceof SimpleFeature;
	}

	/**
	 * Set the next id for attempts to set the feature id on new features.
	 *
	 * @param nextId next id
	 */
	void setNextId(long nextId) {
		this.nextId = nextId;
	}
}