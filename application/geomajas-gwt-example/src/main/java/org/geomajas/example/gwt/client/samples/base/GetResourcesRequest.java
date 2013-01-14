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

package org.geomajas.example.gwt.client.samples.base;

import org.geomajas.command.CommandRequest;

/**
 * <p>
 * Request for getting the content of one or more resources on the class-path.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class GetResourcesRequest implements CommandRequest {

	private static final long serialVersionUID = 153L;

	private String[] resources;

	// Constructors:

	public GetResourcesRequest() {
	}

	public GetResourcesRequest(String[] resources) {
		this.resources = resources;
	}

	// Getters and setters:

	public String[] getResources() {
		return resources;
	}

	public void setResources(String[] resources) {
		this.resources = resources;
	}
}
