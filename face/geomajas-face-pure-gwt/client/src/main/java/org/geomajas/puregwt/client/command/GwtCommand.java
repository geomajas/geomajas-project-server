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

package org.geomajas.puregwt.client.command;

import java.io.Serializable;

import org.geomajas.command.CommandRequest;
import org.geomajas.global.Api;

/**
 * GWT implementation of an RPC request. On the server-side this request is transformed into a command object that is
 * then executed by a {@link CommandDispatcher} instance.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class GwtCommand implements Serializable {

	private static final long serialVersionUID = 100L;

	private String commandName;

	private CommandRequest commandRequest;

	private String userToken;

	private String locale;

	// ------------------------------------------------------------------------
	// constructors:
	// ------------------------------------------------------------------------

	/** Default constructor. Does nothing. */
	public GwtCommand() {
	}

	/**
	 * Constructor setting the command spring bean name.
	 * 
	 * @param commandName
	 *            The Java class name of the actual command to be executed. Or rather the spring bean name.
	 */
	public GwtCommand(String commandName) {
		this.commandName = commandName;
	}

	// ------------------------------------------------------------------------
	// getters and setters:
	// ------------------------------------------------------------------------

	/** The Java class name of the actual command to be executed. Or rather the spring bean name. */
	public String getCommandName() {
		return commandName;
	}

	/**
	 * The Java class name of the actual command to be executed. Or rather the spring bean name.
	 * 
	 * @param commandName
	 *            Set the command bean name.
	 */
	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	/** Get the specific request for the command you wish to execute. */
	public CommandRequest getCommandRequest() {
		return commandRequest;
	}

	/**
	 * Set the specific request for the command you wish to execute.
	 * 
	 * @param commandRequest
	 *            The request object.
	 */
	public void setCommandRequest(CommandRequest commandRequest) {
		this.commandRequest = commandRequest;
	}

	/** Get the user's identification token. */
	public String getUserToken() {
		return userToken;
	}

	/**
	 * A user identification token.
	 * 
	 * @param userToken
	 *            The user's unique token (acquired from the server at login).
	 */
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	/** A string that identifies the preferred language in which to handle responses or errors. */
	public String getLocale() {
		return locale;
	}

	/**
	 * A string that identifies the preferred language in which to handle responses or errors.
	 * 
	 * @param locale
	 *            The new language to use.
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}
}