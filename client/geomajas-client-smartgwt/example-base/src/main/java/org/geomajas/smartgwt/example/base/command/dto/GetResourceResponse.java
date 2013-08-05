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

package org.geomajas.smartgwt.example.base.command.dto;

import org.geomajas.command.CommandResponse;

import java.util.Map;

/**
 * <p>
 * Response object for the get resources command. Contains contents of resources on the class-path.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class GetResourceResponse extends CommandResponse {

	private static final long serialVersionUID = 153L;

	private Map<String, String> resources;

	// Constructors:

	public GetResourceResponse() {
	}

	public GetResourceResponse(Map<String, String> resources) {
		this.resources = resources;
	}

	// Getters and setters:

	public Map<String, String> getResources() {
		return resources;
	}

	public void setResources(Map<String, String> javaSource) {
		this.resources = javaSource;
	}
}
