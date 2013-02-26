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
package org.geomajas.plugin.deskmanager.command.geodesk.dto;

import org.geomajas.command.CommandResponse;
import org.geomajas.configuration.client.ClientApplicationInfo;

/**
 * Response object for {@link org.geomajas.plugin.deskmanager.command.geodesk.InitializeGeodeskCommand}.
 * 
 * @author Jan De Moerloose
 * @author Oliver May
 */
public class InitializeGeodeskResponse extends CommandResponse {

	private static final long serialVersionUID = 100L;

	public static final String COMMAND = "command.geodesk.InitializeGeodesk";

	private String geodeskIdentifier;

	private String userApplicationKey;

	private String deskmanagerVersion;

	private String deskmanagerBuild;

	private ClientApplicationInfo clientApplicationInfo;

	
	/**
	 * Get the geodesk identifier (public id).
	 * 
	 * @return the identifier.
	 */
	public String getGeodeskIdentifier() {
		return geodeskIdentifier;
	}

	/**
	 * Set the geodesk identifier (public id).
	 * 
	 *  @param geodeskIdentifier the identifier
	 */
	public void setGeodeskIdentifier(String geodeskIdentifier) {
		this.geodeskIdentifier = geodeskIdentifier;
	}

	/**
	 * Get the client application info, this info does not contain maps.
	 * 
	 * @return the client application info.
	 */
	public ClientApplicationInfo getClientApplicationInfo() {
		return clientApplicationInfo;
	}

	/**
	 * Set the client application info, this info must not contain maps.
	 * 
	 * @param clientApplicationInfo the client application info.
	 */
	public void setClientApplicationInfo(ClientApplicationInfo clientApplicationInfo) {
		this.clientApplicationInfo = clientApplicationInfo;
	}

	/**
	 * Get the user application key for this geodesk.
	 * 
	 * @return the user application key.
	 */
	public String getUserApplicationKey() {
		return userApplicationKey;
	}

	/**
	 * Set the user application key for this geodesk.
	 * 
	 * @param userApplicationKey the user application key
	 */
	public void setUserApplicationKey(String userApplicationKey) {
		this.userApplicationKey = userApplicationKey;
	}

	/**
	 * Set the deskmanager application version.
	 * 
	 * @param deskmanagerVersion the deskmanagerVersion to set.
	 */
	public void setDeskmanagerVersion(String deskmanagerVersion) {
		this.deskmanagerVersion = deskmanagerVersion;
	}

	/**
	 * Get the deskmanager application version.
	 * 
	 * @return the deskmanager version string, injected by maven.
	 */
	public String getDeskmanagerVersion() {
		return deskmanagerVersion;
	}

	/**
	 * Set the deskmanager build version.
	 * 
	 * @param deskmanagerBuild the deskmanagerBuild to set.
	 */
	public void setDeskmanagerBuild(String deskmanagerBuild) {
		this.deskmanagerBuild = deskmanagerBuild;
	}

	/**
	 * Get the deskmanager build version.
	 * 
	 * @return the unique application build string, injected by maven.
	 */
	public String getDeskmanagerBuild() {
		return deskmanagerBuild;
	}
}
