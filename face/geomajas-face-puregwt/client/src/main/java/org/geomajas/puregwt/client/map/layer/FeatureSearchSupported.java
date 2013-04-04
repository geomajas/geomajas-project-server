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

package org.geomajas.puregwt.client.map.layer;

import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.puregwt.client.map.feature.Feature;

import com.google.gwt.core.client.Callback;

/**
 * Extension for the layer interface that adds support for searching for features.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface FeatureSearchSupported extends Layer {

	/**
	 * Search for features at a specific coordinate location.
	 * 
	 * @param location
	 *            The location to search at.
	 * @param callback
	 *            The callback that contains the list of features found.
	 */
	void searchFeatures(Coordinate location, Callback<List<Feature>, String> callback);

	/**
	 * Search for features at a specific location. The location can have any shape.
	 * 
	 * @param location
	 *            The location to search at.
	 * @param callback
	 *            The callback that contains the list of features found.
	 */
	void searchFeatures(Geometry location, Callback<List<Feature>, String> callback);
}