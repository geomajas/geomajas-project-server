/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.puregwt.client.command;

import java.io.Serializable;

import org.geomajas.command.CommandRequest;
import org.geomajas.global.Api;

/**
 * <p>
 * GWT implementation of an RPC request. On the server-side this request is transformed into a command object that is
 * then executed by a <code>CommandDispatcher</code> instance.
 * </p>
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