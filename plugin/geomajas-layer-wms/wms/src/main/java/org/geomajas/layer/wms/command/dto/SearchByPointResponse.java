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
package org.geomajas.layer.wms.command.dto;

import org.geomajas.command.CommandResponse;
import org.geomajas.annotation.Api;
import org.geomajas.layer.feature.Feature;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Response object for {@link org.geomajas.layer.wms.command.wms.SearchByPointCommand}.
 *
 * @author Jan De Moerloose
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 * @since 1.8.0
 */
@Api(allMethods = true)
public class SearchByPointResponse extends CommandResponse {

	private static final long serialVersionUID = 180L;

	private Map<String, List<Feature>> featureMap; // NOSONAR GWT will not serialize when final

	/** No arguments constructor. */
	public SearchByPointResponse() {
		super();
		featureMap = new HashMap<String, List<Feature>>();
	}

	/**
	 * Add features for a layer. Only adds the features for a server id once.
	 *
	 * @param layerId layer id
	 * @param features features
	 * @return true when features were added
	 */
	public boolean addLayer(String layerId, List<Feature> features) {
		if (!featureMap.containsKey(layerId)) {
			featureMap.put(layerId, features);
			return true;
		}
		return false;
	}

	/**
	 * Get the features map.
	 *
	 * @return map with layer id as key and list of features as value
	 */
	public Map<String, List<Feature>> getFeatureMap() {
		return featureMap;
	}
}
