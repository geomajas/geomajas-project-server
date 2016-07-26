/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.command;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.global.ExceptionDto;
import org.geomajas.global.Json;

/**
 * Base result object from executing a command. This can contain actual data or hold error-messages.
 * 
 * @author Jan De Moerloose
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class CommandResponse implements Serializable {

	private static final long serialVersionUID = 151L;

	private String id;

	private long executionTime;

	private List<String> errorMessages = new ArrayList<String>(); // NOSONAR final makes it non-serializable

	private List<ExceptionDto> exceptions;

	private final transient List<Throwable> errors = new ArrayList<Throwable>();

	// Class specific functions:

	/**
	 * Determine whether an error occurred while dispatching the command.
	 * 
	 * @return true when an exception occurred during command processing
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

	/**
	 * Set the id assigned to this command. The id can help you to match client and server side logs about a command.
	 * 
	 * @param id
	 *            id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get the list of error messages which occurred when dispatching the command. These messages are localised.
	 * 
	 * @return list of error messages
	 */
	public List<String> getErrorMessages() {
		return errorMessages;
	}

	/**
	 * Get the list of exceptions which are thrown while dispatching the command. This list is used (by the dispatcher)
	 * to fill the errorMessages list.<br/>
	 * Serializable since 1.8.0.
	 * 
	 * @return list of {@link Throwable}s
	 */
	@Json(serialize = false)
	public List<Throwable> getErrors() {
		return errors;
	}

	/**
	 * Get execution time in milliseconds.
	 * 
	 * @return command execution time in milliseconds
	 */
	public long getExecutionTime() {
		return executionTime;
	}

	/**
	 * Set the execution time in milliseconds for the command.
	 * 
	 * @param executionTime
	 *            command execution time in milliseconds
	 */
	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}

	/**
	 * Returns a list of serializable exceptions. These exceptions are actually send to the client.
	 * 
	 * @return The exception list.
	 * @since 1.8.0
	 */
	public List<ExceptionDto> getExceptions() {
		if (exceptions == null) {
			exceptions = new ArrayList<ExceptionDto>();
		}
		return exceptions;
	}

	/**
	 * String to display as command dispatcher trace.
	 *
	 * @return string representation of respone
	 * @since 1.10.0
	 */
	@Override
	public String toString() {
		return "CommandResponse{" +
				"id='" + id + '\'' +
				", executionTime=" + executionTime +
				", errorMessages=" + errorMessages +
				", exceptions=" + exceptions +
				", errors=" + errors +
				'}';
	}
}
