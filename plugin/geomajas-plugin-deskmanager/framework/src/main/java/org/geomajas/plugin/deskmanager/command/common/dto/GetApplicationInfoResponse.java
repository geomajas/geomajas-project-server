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
package org.geomajas.plugin.deskmanager.command.common.dto;

import org.geomajas.command.CommandResponse;
import org.geomajas.configuration.client.ClientApplicationInfo;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
public class GetApplicationInfoResponse extends CommandResponse {

	private static final long serialVersionUID = 2L;

	public static final String COMMAND = "command.deskmanager.application.getApplicationInfoCommand";

	private String geodeskIdentifier;

	private String geodeskTypeIdentifier;

	private String deskmanagerVersion;

	private String deskmanagerBuild;

	// Does not contain ClientMapInfo's (are retrieved separately)
	private ClientApplicationInfo clientApplicationInfo;

	public String getGeodeskIdentifier() {
		return geodeskIdentifier;
	}

	public void setGeodeskIdentifier(String geodeskIdentifier) {
		this.geodeskIdentifier = geodeskIdentifier;
	}

	public ClientApplicationInfo getClientApplicationInfo() {
		return clientApplicationInfo;
	}

	public void setClientApplicationInfo(ClientApplicationInfo clientApplicationInfo) {
		this.clientApplicationInfo = clientApplicationInfo;
	}

	public String getGeodeskTypeIdentifier() {
		return geodeskTypeIdentifier;
	}

	public void setGeodeskTypeIdentifier(String geodeskTypeIdentifier) {
		this.geodeskTypeIdentifier = geodeskTypeIdentifier;
	}

	/**
	 * @param deskmanagerVersion the deskmanagerVersion to set.
	 */
	public void setDeskmanagerVersion(String deskmanagerVersion) {
		this.deskmanagerVersion = deskmanagerVersion;
	}

	/**
	 * @return the deskmanager version string, injected by maven.
	 */
	public String getDeskmanagerVersion() {
		return deskmanagerVersion;
	}

	/**
	 * @param deskmanagerBuild the deskmanagerBuild to set.
	 */
	public void setDeskmanagerBuild(String deskmanagerBuild) {
		this.deskmanagerBuild = deskmanagerBuild;
	}

	/**
	 * @return the unique application build string, injected by maven.
	 */
	public String getDeskmanagerBuild() {
		return deskmanagerBuild;
	}
}
