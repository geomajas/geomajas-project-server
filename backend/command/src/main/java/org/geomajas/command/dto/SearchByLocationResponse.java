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
package org.geomajas.command.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.annotation.Api;
import org.geomajas.command.CommandResponse;
import org.geomajas.layer.feature.Feature;

/**
 * Response object for {@link org.geomajas.command.feature.SearchByLocationCommand}.
 *
 * @author Jan De Moerloose
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class SearchByLocationResponse extends CommandResponse {

	private static final long serialVersionUID = 151L;

	private Map<String, List<Feature>> featureMap = new HashMap<String, List<Feature>>();

	/**
	 * Add a features for a layer in the response. Only adds features for a layer once.
	 *
	 * @param layerId layer id
	 * @param features features
	 * @return true when features were added, false when response already contained features for the layer
	 */
	public boolean addLayer(String layerId, List<Feature> features) {
		if (!featureMap.containsKey(layerId)) {
			featureMap.put(layerId, features);
		}
		return false;
	}

	/**
	 * Get map with features. The map key is the layer id, the value are the found features.
	 *
	 * @return features map
	 */
	public Map<String, List<Feature>> getFeatureMap() {
		return featureMap;
	}

	/**
	 * Set map with features. The map key is the layer id, the value are the found features.
	 *
	 * @param featureMap features map
	 */
	public void setFeatureMap(Map<String, List<Feature>> featureMap) {
		this.featureMap = featureMap;
	}

	@Override
	public String toString() {
		return "SearchByLocationResponse{" +
				"featureMap=" + featureMap +
				'}';
	}
}
