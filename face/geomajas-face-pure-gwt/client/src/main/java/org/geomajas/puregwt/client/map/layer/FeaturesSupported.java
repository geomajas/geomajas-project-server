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

package org.geomajas.puregwt.client.map.layer;

import java.util.Collection;

import org.geomajas.layer.feature.Feature;

/**
 * Extension for the layer interface which signifies that this particular layer has support for features. This means
 * that the individual objects that make up a layer are available.
 * 
 * @author Pieter De Graef
 */
public interface FeaturesSupported {

	/**
	 * Apply a filter on the layer. Such a filter specifies which features are to be shown on the map, and which aren't.
	 * This filter is actually used on the back-end and therefore follows the CQL standards.
	 * 
	 * @param filter
	 *            The CQL filter, based upon the layer attribute definitions. Use null to disable filtering.
	 */
	void setFilter(String filter);

	/**
	 * Returns the filter that has been set onto this layer.
	 * 
	 * @return Returns the CQL filter.
	 */
	String getFilter();

	/**
	 * Is a certain feature selected or not?
	 * 
	 * @param featureId
	 *            The unique identifier of the feature within this layer.
	 * @return Returns true if the feature has been selected, false otherwise.
	 */
	boolean isFeatureSelected(String featureId);

	/**
	 * Select the given feature.
	 * 
	 * @param feature
	 *            The feature to select. Must be part of this layer.
	 * @return Return true if the selection was successful.
	 */
	boolean selectFeature(Feature feature);

	/**
	 * Deselect the given feature.
	 * 
	 * @param feature
	 *            The feature to deselect. Must be part of this layer.
	 * @return Return true if the deselection was successful.
	 */
	boolean deselectFeature(Feature feature);

	/** Deselect all features within this layer. */
	void clearSelectedFeatures();

	/**
	 * Return a collection of all selected features within this layer.
	 * 
	 * @return Returns the identifiers, not the actual features.
	 */
	Collection<String> getSelectedFeatureIds();

	// VectorLayerStore getFeatureStore();
}