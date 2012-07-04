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

package org.geomajas.layer.shapeinmem;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.geomajas.configuration.AbstractAttributeInfo;
import org.geomajas.configuration.AbstractEditableAttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.GeometryAttributeInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.Attribute;
import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Both the {@link org.geomajas.layer.geotools.GeoToolsFeatureModel} and the
 * {@link org.geomajas.layer.geotools.GeoToolsLayer} use the same FeatureSource a their way of dealing with GeoTools
 * persistence. So instead of each of these classes having the same functions, we created this little class.
 * 
 * @author Pieter De Graef
 */
public class FeatureSourceRetriever {

	/**
	 * Reference to a GeoTools data store.
	 */
	private DataStore dataStore;

	/**
	 * The name of the feature source in the GeoTools data store.
	 */
	private String featureSourceName;

	private final Map<String, AbstractAttributeInfo> attributeInfoMap = new HashMap<String, AbstractAttributeInfo>();
	
	private GeometryAttributeInfo geometryInfo;

	/**
	 * Set the data store.
	 *
	 * @param dataStore data store
	 * @throws LayerException in case something goes wrong
	 */
	public void setDataStore(DataStore dataStore) throws LayerException {
		this.dataStore = dataStore;
	}

	/**
	 * Get the current data store.
	 *
	 * @return data store
	 */
	public DataStore getDataStore() {
		return dataStore;
	}

	/**
	 * Set feature source name.
	 *
	 * @param featureSourceName feature source name
	 * @throws LayerException oops
	 */
	public void setFeatureSourceName(String featureSourceName) throws LayerException {
		this.featureSourceName = featureSourceName;
	}

	// Class specific functions:

	/**
	 * Retrieve the GeoTools feature type for the 'featureSourceName'.
	 * 
	 * @return feature type
	 * @throws LayerException problem when retrieving feature source
	 */
	public SimpleFeatureType getSchema() throws LayerException {
		return getFeatureSource().getSchema();
	}

	/**
	 * Retrieve the FeatureSource object from the data store.
	 * 
	 * @return An OpenGIS FeatureSource object;
	 * @throws LayerException
	 *             oops
	 */
	public SimpleFeatureSource getFeatureSource() throws LayerException {
		try {
			return dataStore.getFeatureSource(featureSourceName);
		} catch (IOException e) {
			throw new LayerException(e, ExceptionCode.FEATURE_MODEL_PROBLEM, "Cannot find feature source "
					+ featureSourceName);
		} catch (NullPointerException e) {
			throw new LayerException(e, ExceptionCode.FEATURE_MODEL_PROBLEM, "Cannot find feature source "
					+ featureSourceName);
		}
	}
	
	/**
	 * Get the name of the feature source.
	 * 
	 * @return name of feature source
	 */
	public String getFeatureSourceName() {
		return featureSourceName;
	}

	/**
	 * Create a GeoTools feature model.
	 *
	 * @param vectorLayerInfo vector layer info
	 * @throws LayerException feature model could not be constructed
	 */
	public void setLayerInfo(VectorLayerInfo vectorLayerInfo) throws LayerException {
		FeatureInfo featureInfo = vectorLayerInfo.getFeatureInfo();
		for (AbstractAttributeInfo info : featureInfo.getAttributes()) {
			attributeInfoMap.put(info.getName(), info);
		}
		geometryInfo = featureInfo.getGeometryType();
	}

	/**
	 * Convert the given feature object to a {@link SimpleFeature}.
	 *
	 * @param possibleFeature feature object to convert
	 * @return converted feature
	 * @throws LayerException object could not be converted
	 */
	protected SimpleFeature asFeature(Object possibleFeature) throws LayerException {
		if (possibleFeature instanceof SimpleFeature) {
			return (SimpleFeature) possibleFeature;
		} else {
			throw new LayerException(ExceptionCode.INVALID_FEATURE_OBJECT, possibleFeature.getClass().getName());
		}
	}

	/** {@inheritDoc} */
	public void setAttributes(Object feature, Map<String, Attribute> attributes) throws LayerException {
		for (Map.Entry<String, Attribute> entry : attributes.entrySet()) {
			String name = entry.getKey();
			if (!name.equals(getGeometryAttributeName())) {
				AbstractAttributeInfo attributeInfo = attributeInfoMap.get(name);
				if (attributeInfo instanceof AbstractEditableAttributeInfo &&
						((AbstractEditableAttributeInfo) attributeInfo).isEditable()) {
					asFeature(feature).setAttribute(name, entry.getValue());
				}
			}
		}
	}
	
	/** {@inheritDoc} */
	public void setGeometry(Object feature, Geometry geometry) throws LayerException {
		if (geometryInfo.isEditable()) {
			asFeature(feature).setDefaultGeometry(geometry);
		}
	}

	/** {@inheritDoc} */
	public String getGeometryAttributeName() throws LayerException {
		return getSchema().getGeometryDescriptor().getLocalName();
	}

	protected Map<String, AbstractAttributeInfo> getAttributeInfoMap() {
		return attributeInfoMap;
	}
	
	public GeometryAttributeInfo getGeometryInfo() {
		return geometryInfo;
	}

}