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

package org.geomajas.gwt.client.command;

import org.geomajas.command.CommandRequest;

import java.io.Serializable;

/**
 * <p>
 * GWT implementation of an RPC request. On the server-side this request is transformed into a command object that is
 * then executed by a <code>CommandDispatcher</code> instance.
 * </p>
 *
 * @author Pieter De Graef
 */
public class GwtCommand implements Serializable {

	private static final long serialVersionUID = 5594368814162458480L;

	/**
	 * The Java class name of the actual command to be executed. Or rather the spring bean name.
	 */
	private String commandName;

	/**
	 * The specific request you wish to execute.
	 */
	private CommandRequest commandRequest;

	/**
	 * A user identification token.
	 */
	private String userToken;

	/**
	 * A string that identifies the preferred language in which to handle responses or errors.
	 */
	private String locale;

	// constructors:

	public GwtCommand() {
	}

	public GwtCommand(String commandName) {
		this.commandName = commandName;
	}

	// getters and setters:

	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	public CommandRequest getCommandRequest() {
		return commandRequest;
	}

	public void setCommandRequest(CommandRequest commandRequest) {
		this.commandRequest = commandRequest;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
}
