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
import org.geomajas.plugin.deskmanager.domain.dto.BlueprintDto;

/**
 * Request object for {@link org.geomajas.plugin.deskmanager.command.manager.SaveBlueprintCommand}.
 * 
 * @author Oliver May
 * @author Kristof Heirwegh
 *
 */
public class SaveBlueprintRequest implements CommandRequest {

	private static final long serialVersionUID = 1L;

	/**
	 * Bitmask to save settings. 
	 */
	public static final int SAVE_SETTINGS = SaveGeodeskRequest.SAVE_SETTINGS;

	/**
	 * Bitmask to save layers and layerstyles. 
	 */
	public static final int SAVE_LAYERS = SaveGeodeskRequest.SAVE_LAYERS;

	/**
	 * Bitmask to save Territories. 
	 */
	public static final int SAVE_TERRITORIES = SaveGeodeskRequest.SAVE_TERRITORIES;

	/**
	 * Bitmask to save clientwidgetinfo's (application, mainmap, overviewmap). 
	 */
	public static final int SAVE_CLIENTWIDGETINFO = SaveGeodeskRequest.SAVE_CLIENTWIDGETINFO;
	
	public static final String COMMAND = "command.deskmanager.manager.SaveBlueprint";

	private int saveBitmask;

	private BlueprintDto blueprint;

	public int getSaveBitmask() {
		return saveBitmask;
	}

	public void setSaveBitmask(int saveBitmask) {
		this.saveBitmask = saveBitmask;
	}

	public BlueprintDto getBlueprint() {
		return blueprint;
	}

	public void setBlueprint(BlueprintDto blueprint) {
		this.blueprint = blueprint;
	}
}
