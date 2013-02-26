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
package org.geomajas.plugin.deskmanager.command.manager.dto;

import org.geomajas.command.CommandRequest;

/**
 * Request object for {@link org.geomajas.plugin.deskmanager.command.manager.CreateBlueprintCommand}.
 * 
 * @author Oliver May
 */
public class CreateBlueprintRequest implements CommandRequest {

	private static final long serialVersionUID = 1L;

	public static final String COMMAND = "command.manager.CreateBlueprint";

	private String name;
	
	private String userApplicationName;

	public String getUserApplicationName() {
		return userApplicationName;
	}

	public void setUserApplicationName(String userApplicationName) {
		this.userApplicationName = userApplicationName;
	}

	/**
	 * The preferred name of the blueprint.
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
