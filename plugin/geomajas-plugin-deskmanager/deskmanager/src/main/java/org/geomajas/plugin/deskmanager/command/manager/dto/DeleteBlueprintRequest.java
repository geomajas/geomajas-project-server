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
 * Request object for {@link org.geomajas.plugin.deskmanager.command.manager.DeleteBlueprintCommand}.
 * 
 * @author Jan De Moerloose
 * @author Oliver May
 * @author Kristof Heirwegh
 *
 */
public class DeleteBlueprintRequest implements CommandRequest {

	private static final long serialVersionUID = 1L;

	public static final String COMMAND = "command.manager.DeleteBlueprint";

	private String blueprintId;

	/**
	 * Get the blueprintId of the blueprint to delete.
	 * @return the blueprintId
	 */
	public String getBlueprintId() {
		return blueprintId;
	}

	/**
	 * Set the blueprintId of the blueprint to delete.
	 * @param blueprintId the blueprintId
	 */
	public void setBlueprintId(String uuid) {
		this.blueprintId = uuid;
	}

}
