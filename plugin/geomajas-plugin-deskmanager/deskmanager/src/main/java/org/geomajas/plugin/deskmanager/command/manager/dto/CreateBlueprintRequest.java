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
	
	private String userApplicationKey;

	/**
	 * Get the user application key.
	 * @return the user application key.
	 */
	public String getUserApplicationKey() {
		return userApplicationKey;
	}

	/**
	 * Set the user application key.
	 * 
	 * @param userApplicationKey the key to set.
	 */
	public void setUserApplicationKey(String userApplicationKey) {
		this.userApplicationKey = userApplicationKey;
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
