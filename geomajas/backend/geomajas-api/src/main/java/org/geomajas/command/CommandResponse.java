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

package org.geomajas.command;

import org.geomajas.global.Json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Base result object from executing a command. This can contain actual data or hold error-messages.
 *
 * @author Jan De Moerloose
 * @author Joachim Van der Auwera
 */
public class CommandResponse implements Serializable {

	private static final long serialVersionUID = 151L;
	private String id;
	private long executionTime;
	private List<String> errorMessages = new ArrayList<String>();
	private transient List<Throwable> errors = new ArrayList<Throwable>();

	// Class specific functions:

	/**
	 * Determine whether an error occurred while dispatching the command.
	 * @return
	 */
	public boolean isError() {
		return (null != errorMessages && errorMessages.size() > 0) || (null != errors && errors.size() > 0);
	}

	// Getters and setters:

	/**
	 * Get the id assigned to this command. The id can help you to match client and server side logs about a command.
	 *
	 * @return id
	 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get the list of error messages which occurred when dispatching the command.
	 * These messages are localised.
	 *
	 * @return list of error messages
	 */
	public List<String> getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}

	/**
	 * Get the list of exceptions which are thrown while dispatching the command.
	 * This list is used (by the dispatcher) to fill the errorMessages list.
	 *
	 * @return list of {@link Throwable}s
	 */
	@Json(serialize = false)
	public List<Throwable> getErrors() {
		return errors;
	}

	public void setErrors(List<Throwable> errors) {
		this.errors = errors;
	}

	/**
	 * Get execution time in milliseconds.
	 *
	 * @return command execution time in milliseconds
	 */
	public long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}
}
