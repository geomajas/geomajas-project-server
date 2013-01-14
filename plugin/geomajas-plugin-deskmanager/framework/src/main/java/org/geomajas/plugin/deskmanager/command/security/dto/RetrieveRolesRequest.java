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
package org.geomajas.plugin.deskmanager.command.security.dto;

import org.geomajas.command.CommandRequest;


/**
 * Command request for {@link org.geomajas.plugin.deskmanager.command.security.RetrieveRolesCommand}.
 * 
 * @author Oliver May
 */
public class RetrieveRolesRequest implements CommandRequest {

	private static final long serialVersionUID = 1L;

	public static final String MANAGER_ID = "manager";
	
	private String geodeskId;

	/**
	 * @param geodeskId the geodeskId to set
	 */
	public void setGeodeskId(String geodeskId) {
		this.geodeskId = geodeskId;
	}

	/**
	 * @return the geodeskId
	 */
	public String getGeodeskId() {
		return geodeskId;
	}

}
