/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.command.dto;

import org.geomajas.command.CommandRequest;

/**
 * Request object for {@link org.geomajas.command.configuration.GetClientUserDataCommand}. It requests a specific
 * configuration object to be returned to the client. The object must implement the
 * {@link org.geomajas.configuration.client.ClientUserDataInfo} interface though.
 * 
 * @author Pieter De Graef
 * @since 1.10.0
 */
public class GetClientUserDataRequest implements CommandRequest {

	private static final long serialVersionUID = 1100L;

	/** Command name for this request. */
	public static final String COMMAND = "command.configuration.GetClientUserData";

	private String className;

	private String identifier;

	/**
	 * Get the class name of the object to return to the client. This object must be an implementation of the
	 * {@link org.geomajas.configuration.client.ClientUserDataInfo} interface.
	 * 
	 * @return Return the class name of the object to fetch from the server.
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Set the class name of the object to return to the client. This object must be an implementation of the
	 * {@link org.geomajas.configuration.client.ClientUserDataInfo} interface.
	 * 
	 * @param className
	 *            The class name of the object to fetch from the server.
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * Get the unique identifier of the configuration object to return to the client. This must be a uniquely
	 * identifiable Spring bean.
	 * 
	 * @return Return the unique identifier of the configuration object to return to the client.
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Set the unique identifier of the configuration object to return to the client. This must be a uniquely
	 * identifiable Spring bean.
	 * 
	 * @param identifier
	 *            The unique identifier of the configuration object to return to the client.
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
}