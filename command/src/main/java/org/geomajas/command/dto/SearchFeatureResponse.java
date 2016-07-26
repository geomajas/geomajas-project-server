/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.command.dto;

import org.geomajas.annotation.Api;
import org.geomajas.command.CommandResponse;
import org.geomajas.layer.feature.Feature;

import java.util.Arrays;

/**
 * Response object for {@link org.geomajas.command.feature.SearchFeatureCommand}.
 *
 * @author Jan De Moerloose
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class SearchFeatureResponse extends CommandResponse {

	private static final long serialVersionUID = 151L;

	/**
	 * The layer in which the features lie.
	 */
	private String layerId;

	private Feature[] features;

	/**
	 * Get the layer id.
	 *
	 * @return layer id
	 */
	public String getLayerId() {
		return layerId;
	}

	/**
	 * Set the id of the layer for the response features.
	 *
	 * @param layerId layer id
	 */
	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	/**
	 * Get the result features.
	 *
	 * @return result features
	 */
	public Feature[] getFeatures() {
		return features;
	}

	/**
	 * Set the result features.
	 *
	 * @param features result features
	 */
	public void setFeatures(Feature[] features) {
		this.features = features;
	}

	@Override
	public String toString() {
		return "SearchFeatureResponse{" +
				"layerId='" + layerId + '\'' +
				", features=" + (features == null ? null : Arrays.asList(features)) +
				'}';
	}
}