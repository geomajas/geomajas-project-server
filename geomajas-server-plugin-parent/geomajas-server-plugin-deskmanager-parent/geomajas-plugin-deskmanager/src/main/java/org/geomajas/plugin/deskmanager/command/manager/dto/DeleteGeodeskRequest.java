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
 * Request object for {@link org.geomajas.plugin.deskmanager.command.manager.DeleteGeodeskCommand}.
 * 
 * @author Jan De Moerloose
 * @author Oliver May
 * @author Kristof Heirwegh
 */
public class DeleteGeodeskRequest implements CommandRequest {

	private static final long serialVersionUID = 1L;

	public static final String COMMAND = "command.deskmanager.manager.DeleteLoket";

	private String geodeskId;

	public String getGeodeskId() {
		return geodeskId;
	}

	public void setGeodeskId(String geodeskId) {
		this.geodeskId = geodeskId;
	}

}
