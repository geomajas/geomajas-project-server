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

package org.geomajas.global;

import java.io.Serializable;

/**
 * A Data Transfer Object (DTO) that is used when exceptions that occurred on the server need to be send back to the
 * client. Instead of send general exceptions we take the safe route and this type of object instead.
 * 
 * @author Pieter De Graef
 * @since 1.8.0
 */
@Api(allMethods = true)
public class ExceptionDto implements Serializable {

	private static final long serialVersionUID = 180L;

	private String message;

	private String className;

	private StackTraceElement[] stackTrace;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/** Empty constructor for serialization purposes. */
	public ExceptionDto() {
	}

	/**
	 * Constructor that takes all needed information on creation.
	 * 
	 * @param className
	 *            The class name of the original exception.
	 * @param message
	 *            The exception message.
	 * @param stackTrace
	 *            The full stack trace.
	 */
	public ExceptionDto(String className, String message, StackTraceElement[] stackTrace) {
		this.message = message;
		this.className = className;
		this.stackTrace = stackTrace;
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public StackTraceElement[] getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(StackTraceElement[] stackTrace) {
		this.stackTrace = stackTrace;
	}
}