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
package org.geomajas.layer.wms.command.dto;

import org.geomajas.command.CommandResponse;
import org.geomajas.global.Api;
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
 * @since 1.6.0
 */
@Api(allMethods = true)
public class SearchByPointResponse extends CommandResponse {

	private static final long serialVersionUID = 180L;

	private final Map<String, List<Feature>> featureMap = new HashMap<String, List<Feature>>();

	public SearchByPointResponse() {
		super();
	}

	public boolean addLayer(String layerId, List<Feature> features) {
		if (!featureMap.containsKey(layerId)) {
			featureMap.put(layerId, features);
		}
		return false;
	}

	public Map<String, List<Feature>> getFeatureMap() {
		return featureMap;
	}
}
