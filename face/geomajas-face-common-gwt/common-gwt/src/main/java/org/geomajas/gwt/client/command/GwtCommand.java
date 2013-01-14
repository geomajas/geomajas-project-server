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

package org.geomajas.gwt.client.command;

import org.geomajas.annotation.Api;
import org.geomajas.command.CommandRequest;

import java.io.Serializable;

/**
 * <p>
 * GWT implementation of an RPC request. On the server-side this request is transformed into a command object that is
 * then executed by a <code>CommandDispatcher</code> instance.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 0.0.0
 */
@Api(allMethods = true)
public class GwtCommand implements Serializable {

	private static final long serialVersionUID = 5594368814162458480L;

	private String commandName;

	private CommandRequest commandRequest;

	private String userToken;

	private String locale;

	// constructors:

	/** Default constructor. Does nothing. */
	public GwtCommand() {
	}

	/**
	 * Constructor setting the command spring bean name.
	 * 
	 * @param commandName name of command to execute
	 */
	public GwtCommand(String commandName) {
		this.commandName = commandName;
	}

	// getters and setters:

	/**
	 * The name of the command to be executed (matches the spring bean name).
	 *
	 * @return command name
	 */
	public String getCommandName() {
		return commandName;
	}

	/**
	 * The name of the command to be executed (matches the spring bean name).
	 * 
	 * @param commandName command name
	 */
	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	/**
	 * Get the specific request for the command you wish to execute.
	 *
	 * @return request object for command
	 */
	public CommandRequest getCommandRequest() {
		return commandRequest;
	}

	/**
	 * Set the specific request for the command you wish to execute.
	 * 
	 * @param commandRequest request object
	 */
	public void setCommandRequest(CommandRequest commandRequest) {
		this.commandRequest = commandRequest;
	}

	/**
	 * Get the user's identification token.
	 *
	 * @return current user token
	 */
	public String getUserToken() {
		return userToken;
	}

	/**
	 * A user identification token.
	 * 
	 * @param userToken user token (acquired from the server at login).
	 */
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	/**
	 * A string that identifies the preferred language in which to handle responses or errors.
	 *
	 * @return locale
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * A string that identifies the preferred language in which to handle responses or errors.
	 * 
	 * @param locale The new language to use.
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}
}
