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

package org.geomajas.plugin.wmsclient.server.command.dto;

import java.util.List;

import org.geomajas.command.CommandResponse;
import org.geomajas.layer.feature.Feature;

/**
 * Response for the GetFeatureInfo command. It returns the features found.
 * 
 * @author Pieter De Graef
 */
public class GetFeatureInfoResponse extends CommandResponse {

	private static final long serialVersionUID = 1L;

	private List<Feature> features;

	private String wmsResponse;

	public GetFeatureInfoResponse() {
	}

	public GetFeatureInfoResponse(List<Feature> features) {
		this.features = features;
	}

	public List<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(List<Feature> features) {
		this.features = features;
	}

	public String getWmsResponse() {
		return wmsResponse;
	}

	public void setWmsResponse(String wmsResponse) {
		this.wmsResponse = wmsResponse;
	}
}