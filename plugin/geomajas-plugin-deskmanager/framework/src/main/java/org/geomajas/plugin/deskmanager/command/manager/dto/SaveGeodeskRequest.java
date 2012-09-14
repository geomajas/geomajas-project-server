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
import org.geomajas.plugin.deskmanager.domain.dto.GeodeskDto;

/**
 * 
 * @author Oliver May
 *
 */
public class SaveGeodeskRequest implements CommandRequest {

	private static final long serialVersionUID = 1L;

	public static final int SAVE_SETTINGS = 1;

	public static final int SAVE_LAYERTREE = 2; // layers + layerstyles

	public static final int SAVE_GROUPS = 4; // security

	public static final int SAVE_NOTIFICATIONS = 16; // mailnotifications

	public static final int SAVE_CLIENTWIDGETINFO = 32;

	public static final String COMMAND = "command.deskmanager.beheer.SaveLoket";


	private int saveWhat;

	private GeodeskDto loket;

	public int getSaveWhat() {
		return saveWhat;
	}

	public void setSaveWhat(int saveWhat) {
		this.saveWhat = saveWhat;
	}

	public GeodeskDto getLoket() {
		return loket;
	}

	public void setGeodesk(GeodeskDto loket) {
		this.loket = loket;
	}
}
