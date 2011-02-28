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

import java.io.IOException;

import org.geomajas.global.ExceptionCode;
import org.geomajas.layer.LayerException;
import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Both the GeotoolsFeatureModel and the GeotoolsLayer use the same FeatureSource a their way of dealing with geotools
 * persistence. So instead of each of these classes having the same functions, we created this little class.
 * 
 * @author Pieter De Graef
 */
public class FeatureSourceRetriever {

	/**
	 * Reference to a geotools datastore.
	 */
	private DataStore dataStore;

	/**
	 * The name of the feature source in the geotools datastore.
	 */
	private String featureSourceName;

	/**
	 * The FeatureSource object with which we have access to the geotools persistence layer.
	 */
	private SimpleFeatureSource featureSource;

	protected long nextId;

	public void setDataStore(DataStore dataStore) throws LayerException {
		this.dataStore = dataStore;
	}

	public DataStore getDataStore() {
		return dataStore;
	}

	public void setFeatureSourceName(String featureSourceName) throws LayerException {
		this.featureSourceName = featureSourceName;
	}

	// Class specific functions:

	/**
	 * Retrieve the geotools featuretype for the 'featureSourceName'.
	 * 
	 * @return feature type
	 */
	public SimpleFeatureType getSchema() throws LayerException {
		return getFeatureSource().getSchema();
	}

	/**
	 * Retrieve the FeatureSource object from the datastore.
	 * 
	 * @return An opengis FeatureSource object;
	 * @throws LayerException
	 *             oops
	 */
	public SimpleFeatureSource getFeatureSource() throws LayerException {
		if (featureSource == null) {
			try {
				featureSource = dataStore.getFeatureSource(featureSourceName);
			} catch (IOException e) {
				throw new LayerException(e, ExceptionCode.FEATURE_MODEL_PROBLEM, "Cannot find feature source "
						+ featureSourceName);
			} catch (NullPointerException e) {
				throw new LayerException(e, ExceptionCode.FEATURE_MODEL_PROBLEM, "Cannot find feature source "
						+ featureSourceName);
			}
		}
		return featureSource;
	}

	/**
	 * Get the name of the feature source.
	 * 
	 * @return name of feature source
	 */
	public String getFeatureSourceName() {
		return featureSourceName;
	}

	protected SimpleFeature asFeature(Object possibleFeature) throws LayerException {
		if (possibleFeature instanceof SimpleFeature) {
			return (SimpleFeature) possibleFeature;
		} else {
			throw new LayerException(ExceptionCode.INVALID_FEATURE_OBJECT, possibleFeature.getClass().getName());
		}
	}

	protected String getNextId() {
		String id = nextId + "";
		nextId++;
		return id;
	}
}