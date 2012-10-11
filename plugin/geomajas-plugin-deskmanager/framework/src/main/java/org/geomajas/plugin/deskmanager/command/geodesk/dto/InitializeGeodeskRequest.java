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
package org.geomajas.plugin.deskmanager.command.geodesk.dto;

import org.geomajas.command.CommandRequest;


/**
 * Request object for {@link org.geomajas.plugin.deskmanager.command.common.InitializeGeodeskCommand}.
 * 
 * @author Oliver May
 */
public class InitializeGeodeskRequest implements CommandRequest {

	private static final long serialVersionUID = 1L;
	
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
