/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.command.manager.dto;

import org.geomajas.command.CommandRequest;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
public class CreateBlueprintRequest implements CommandRequest {

	private static final long serialVersionUID = 1L;

	public static final String COMMAND = "command.deskmanager.beheer.CreateBlueprint";

	private String userApplicationName;

	public String getUserApplicationName() {
		return userApplicationName;
	}

	public void setUserApplicationName(String userApplicationName) {
		this.userApplicationName = userApplicationName;
	}

}
