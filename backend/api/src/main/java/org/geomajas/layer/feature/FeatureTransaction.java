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

package org.geomajas.layer.feature;

import org.geomajas.global.Api;

import java.io.Serializable;

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
}
