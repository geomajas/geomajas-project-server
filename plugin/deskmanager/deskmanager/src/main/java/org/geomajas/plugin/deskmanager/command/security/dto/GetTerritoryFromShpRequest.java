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
package org.geomajas.plugin.deskmanager.command.security.dto;

import org.geomajas.command.CommandRequest;

/**
 * Request for {@link org.geomajas.plugin.deskmanager.command.security.GetTerritoryFromShpCommand}.
 * 
 * @author Jan Venstermans
 *
 */
public class GetTerritoryFromShpRequest implements CommandRequest {

	private static final long serialVersionUID = 115L;

	public static final String COMMAND = "GetTerritoryFromShp";

	/**
	 * The token for the uploaded file, as provided by
	 * {@link org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.panels.GenericUploadForm}.
	 */
	private String shpFileToken;

	private String toCrs;

	public String getShpFileToken() {
		return shpFileToken;
	}

	public void setShpFileToken(String shpFileToken) {
		this.shpFileToken = shpFileToken;
	}

	public String getToCrs() {
		return toCrs;
	}

	public void setToCrs(String toCrs) {
		this.toCrs = toCrs;
	}
}
