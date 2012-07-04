/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.layer.geotools;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.configuration.AbstractAttributeInfo;
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

import com.vividsolutions.jts.geom.Geometry;

/**
 * GeoTools feature model. Should be able to use any GeoTools data source.
 *
 * @author Jan De Moerloose
 * @author Pieter De Graef
 */
public class GeoToolsFeatureModel extends FeatureSourceRetriever implements FeatureModel {

	private final SimpleFeatureBuilder builder;

	private final int srid;

	private final DtoConverterService converterService;

	// Constructor:

	/**
	 * Create a GeoTools feature model.
	 *
	 * @param dataStore data store
	 * @param featureSourceName feature source name
	 * @param srid srid
	 * @param converterService converter service
	 * @throws LayerException feature model could not be constructed
	 */
	public GeoToolsFeatureModel(DataStore dataStore, String featureSourceName, int srid,
			DtoConverterService converterService) throws LayerException {
		super();
		setDataStore(dataStore);
		setFeatureSourceName(featureSourceName);
		this.srid = srid;
		builder = new SimpleFeatureBuilder(getSchema());
		this.converterService = converterService;
	}

	// FeatureModel implementation:

	/** {@inheritDoc} */
	public Attribute getAttribute(Object feature, String name) throws LayerException {
		return convertAttribute(asFeature(feature).getAttribute(name), name);
	}

	/** {@inheritDoc} */
	public Map<String, Attribute> getAttributes(Object feature) throws LayerException {
		SimpleFeature f = asFeature(feature);
		HashMap<String, Attribute> attribs = new HashMap<String, Attribute>();
		for (AbstractAttributeInfo attributeInfo : getAttributeInfoMap().values()) {
			String name = attributeInfo.getName();
			attribs.put(name, convertAttribute(f.getAttribute(name), name));
		}
		return attribs;
	}

	private Attribute convertAttribute(Object object, String name) throws LayerException {
		AbstractAttributeInfo attributeInfo = getAttributeInfoMap().get(name);
		if (null == attributeInfo) {
			throw new LayerException(ExceptionCode.ATTRIBUTE_UNKNOWN, name, getAttributeInfoMap().keySet());
		}
		try {
			return converterService.toDto(object, attributeInfo);
		} catch (GeomajasException e) {
			throw new LayerException(e);
		}
	}

	/** {@inheritDoc} */
	public Geometry getGeometry(Object feature) throws LayerException {
		Geometry geometry = (Geometry) asFeature(feature).getDefaultGeometry();
		geometry.setSRID(srid);
		return (Geometry) geometry.clone();
	}

	/** {@inheritDoc} */
	public String getId(Object feature) throws LayerException {
		SimpleFeature featureAsFeature = asFeature(feature);
		return featureAsFeature.getID();
	}

	/** {@inheritDoc} */
	public int getSrid() throws LayerException {
		return srid;
	}

	/** {@inheritDoc} */
	public Object newInstance() throws LayerException {
		if (builder == null) {
			throw new LayerException(ExceptionCode.CREATE_FEATURE_NO_FEATURE_TYPE);
		}
		// This creates a feature with a random id. This will probably not be used 
		return builder.buildFeature(null);
	}

	/** {@inheritDoc} */
	public Object newInstance(String id) throws LayerException {
		if (builder == null) {
			throw new LayerException(ExceptionCode.CREATE_FEATURE_NO_FEATURE_TYPE);
		}
		return builder.buildFeature(id);
	}

	/** {@inheritDoc} */
	public boolean canHandle(Object feature) {
		return feature instanceof SimpleFeature;
	}
}