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
import org.geomajas.plugin.deskmanager.domain.dto.BlueprintDto;

/**
 * 
 * @author Oliver May
 *
 */
public class SaveBlueprintRequest implements CommandRequest {

	private static final long serialVersionUID = 1L;

	public static final int SAVE_SETTINGS = SaveGeodeskRequest.SAVE_SETTINGS;

	public static final int SAVE_LAYERS = SaveGeodeskRequest.SAVE_LAYERS; // layers + layerstyles

	public static final int SAVE_GROUPS = SaveGeodeskRequest.SAVE_TERRITORIES; // security

	public static final int SAVE_CLIENTWIDGETINFO = SaveGeodeskRequest.SAVE_CLIENTWIDGETINFO;
	
	public static final String COMMAND = "command.deskmanager.beheer.SaveBlueprint";

	private int saveWhat;

	private BlueprintDto blueprint;

	public int getSaveWhat() {
		return saveWhat;
	}

	public void setSaveWhat(int saveWhat) {
		this.saveWhat = saveWhat;
	}

	public BlueprintDto getBlueprint() {
		return blueprint;
	}

	public void setBlueprint(BlueprintDto blueprint) {
		this.blueprint = blueprint;
	}
}
