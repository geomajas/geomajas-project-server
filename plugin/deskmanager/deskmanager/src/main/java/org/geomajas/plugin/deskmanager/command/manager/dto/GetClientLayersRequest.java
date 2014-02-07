/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.command.manager.dto;

import org.geomajas.command.CommandRequest;


/**
 * Request object for {@link org.geomajas.plugin.deskmanager.command.manager.GetClientLayersCommand}.
 * 
 * @author Oliver May
 *
 */
public class GetClientLayersRequest implements CommandRequest {

	public static final String COMMAND = "command.deskmanager.manager.GetLayers";
	
	private static final long serialVersionUID = 100L;

	public String toString() {
		return COMMAND;
	}

}
