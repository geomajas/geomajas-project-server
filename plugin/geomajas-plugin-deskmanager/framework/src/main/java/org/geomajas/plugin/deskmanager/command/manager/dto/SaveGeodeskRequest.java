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
 * Command object for {@link org.geomajas.plugin.deskmanager.command.manager.SaveGeodeskCommand}.
 * 
 * @author Oliver May
 * @author Kristof Heirwegh
 */
public class SaveGeodeskRequest implements CommandRequest {

	private static final long serialVersionUID = 1L;

	/**
	 * Bitmask to save settings. 
	 */
	public static final int SAVE_SETTINGS = 1;

	/**
	 * Bitmask to save layers and layerstyles.
	 */
	public static final int SAVE_LAYERS = 2; // layers + layerstyles

	/**
	 * Bitmask to save security Territories.
	 */
	public static final int SAVE_TERRITORIES = 4; // security

	/**
	 * Bitmask to save notifications.
	 * @deprecated use clientwidgetinfo.
	 */
	@Deprecated
	public static final int SAVE_NOTIFICATIONS = 16; // mailnotifications

	/**
	 * Bitmask to save all clientwidgetinfo's (application, mainmap, overviewmap).
	 */
	public static final int SAVE_CLIENTWIDGETINFO = 32;

	public static final String COMMAND = "command.manager.SaveGeodesk";


	private int saveBitmask;

	private GeodeskDto loket;

	public int getSaveBitmask() {
		return saveBitmask;
	}

	public void setSaveBitmask(int saveBitmask) {
		this.saveBitmask = saveBitmask;
	}

	public GeodeskDto getLoket() {
		return loket;
	}

	public void setGeodesk(GeodeskDto loket) {
		this.loket = loket;
	}
}
