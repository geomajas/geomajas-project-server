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

import org.geomajas.command.CommandRequest;

/**
 * <p>
 * Request for getting the content of one or more resources on the class-path.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class GetResourceRequest implements CommandRequest {

	private static final long serialVersionUID = 153L;

	public static final String COMMAND = "command.resource.Get";

	private String[] resources;

	public GetResourceRequest() {
	}

	public GetResourceRequest(String[] resources) {
		this.resources = resources;
	}

	public String[] getResources() {
		return resources;
	}
}
