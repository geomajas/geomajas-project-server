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

package org.geomajas.global;

import java.io.Serializable;

import org.geomajas.annotation.Api;

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

	private int exceptionCode;

	private StackTraceElement[] stackTrace;

	private ExceptionDto cause;

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

	/**
	 * Returns the class name of the original exception.
	 * 
	 * @return The real exceptions class name.
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Set the class name of the original exception.
	 * 
	 * @param className
	 *            The exception class name.
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * Returns the message of the original exception.
	 * 
	 * @return The real exceptions message.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Set the message of the original exception.
	 * 
	 * @param message
	 *            The exception message.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Returns the stack trace of the original exception.
	 * 
	 * @return The real exceptions stack trace.
	 */
	public StackTraceElement[] getStackTrace() {
		return stackTrace;
	}

	/**
	 * Set the stack trace of the original exception.
	 * 
	 * @param stackTrace
	 *            The exception stack trace.
	 */
	public void setStackTrace(StackTraceElement[] stackTrace) {
		this.stackTrace = stackTrace;
	}

	/**
	 * Set the cause exception to allow inclusion in the display.
	 *
	 * @return cause exception DTO
	 * @since 1.10.0
	 */
	public ExceptionDto getCause() {
		return cause;
	}

	/**
	 * Get the cause exception to allow inclusion in the error display.
	 *
	 * @param cause cause exception DTO
	 * @since 1.10.0
	 */
	public void setCause(ExceptionDto cause) {
		this.cause = cause;
	}

	/**
	 * Get the exception code (when it is a {@link GeomajasException}).
	 *
	 * @return GeomajasException exception code
	 * @since 1.10.0
	 */
	public int getExceptionCode() {
		return exceptionCode;
	}

	/**
	 * Set the exception code (when it is a {@link GeomajasException}.
	 *
	 * @param exceptionCode GeomajasException exception code
	 * @since 1.10.0
	 */
	public void setExceptionCode(int exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
}