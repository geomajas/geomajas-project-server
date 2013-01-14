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

package org.geomajas.layer.feature;

import org.geomajas.annotation.Api;

import java.io.Serializable;
import java.util.Arrays;

/**
 * DTO version of a {@link InternalFeature}. This object can be sent to the client.
 * 
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
public class FeatureTransaction implements Serializable {

	private static final long serialVersionUID = 151L;

	private String layerId;

	private Feature[] oldFeatures;

	private Feature[] newFeatures;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Default constructor - does nothing.
	 */
	public FeatureTransaction() {
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	/**
	 * Get the identifier of the vector layer wherein this transaction should take place.
	 *
	 * @return layer id
	 */
	public String getLayerId() {
		return layerId;
	}

	/**
	 * Set the identifier of the vector layer wherein this transaction should take place.
	 * 
	 * @param layerId
	 *            The layer's identifier.
	 */
	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	/**
	 * Returns the array of features with their original values. Can be empty or null when new features are to be
	 * created.
	 *
	 * @return the original values for each of the features
	 */
	public Feature[] getOldFeatures() {
		return oldFeatures;
	}

	/**
	 * Set the array of features with their original values. When creating new features, this array should be null.
	 * 
	 * @param oldFeatures
	 *            The original feature array.
	 */
	public void setOldFeatures(Feature[] oldFeatures) {
		this.oldFeatures = oldFeatures;
	}

	/**
	 * Return the list of features with the new values. Can be empty or null when features are to be deleted.
	 *
	 * @return the new values for each of the features
	 */
	public Feature[] getNewFeatures() {
		return newFeatures;
	}

	/**
	 * Set the list of features with the new values. When deleting existing features, this array should be null.
	 * 
	 * @param newFeatures the new values for each of the features
	 */
	public void setNewFeatures(Feature[] newFeatures) {
		this.newFeatures = newFeatures;
	}

	@Override
	public String toString() {
		return "FeatureTransaction{" +
				"layerId='" + layerId + '\'' +
				", oldFeatures=" + (oldFeatures == null ? null : Arrays.asList(oldFeatures)) +
				", newFeatures=" + (newFeatures == null ? null : Arrays.asList(newFeatures)) +
				'}';
	}
}
