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
package org.geomajas.widget.searchandfilter.command.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.command.CommandResponse;
import org.geomajas.layer.feature.Feature;

/**
 * Response for {@link org.geomajas.widget.searchandfilter.command.searchandfilter.FeatureSearchCommand}.
 * 
 * @author Kristof Heirwegh
 */
public class FeatureSearchResponse extends CommandResponse {

	private static final long serialVersionUID = 100L;

	private Map<String, List<Feature>> featureMap = new HashMap<String, List<Feature>>();

	public boolean addLayer(String layerId, List<Feature> features) {
		if (!featureMap.containsKey(layerId)) {
			featureMap.put(layerId, features);
		}
		return false;
	}

	public Map<String, List<Feature>> getFeatureMap() {
		return featureMap;
	}

	public void setFeatureMap(Map<String, List<Feature>> featureMap) {
		this.featureMap = featureMap;
	}

}
