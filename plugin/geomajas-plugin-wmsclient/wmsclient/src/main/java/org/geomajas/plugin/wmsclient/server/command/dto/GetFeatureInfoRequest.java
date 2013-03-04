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

import org.geomajas.command.CommandRequest;

/**
 * Request for the GetFeatureInfoCommand. Should contain the actual request URL.
 * 
 * @author Pieter De Graef
 */
public class GetFeatureInfoRequest implements CommandRequest {

	private static final long serialVersionUID = 100L;
	
	public static final String COMMAND_NAME = "command.GetFeatureInfo";

	private String url;

	public GetFeatureInfoRequest() {
	}

	public GetFeatureInfoRequest(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}