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

import org.geomajas.global.ExceptionCode;
import org.geomajas.layer.LayerException;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.IOException;

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
	private FeatureSource<SimpleFeatureType, SimpleFeature> featureSource;

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
	public FeatureSource<SimpleFeatureType, SimpleFeature> getFeatureSource() throws LayerException {
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