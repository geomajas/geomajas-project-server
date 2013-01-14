/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.geometry.service;

import org.geomajas.annotation.Api;

/**
 * Exception thrown from within the {@link WktService} in case of parsing or formatting errors.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api
public class WktException extends Exception {

	private static final long serialVersionUID = 100L;

	/**
	 * Initialize this exception with the given message.
	 * 
	 * @param message
	 *            The error message.
	 */
	public WktException(String message) {
		super(message);
	}

	/**
	 * Create WktException with given message and cause.
	 *
	 * @param message message
	 * @param cause cause
	 * @since 1.1.0
	 */
	public WktException(String message, Throwable cause) {
		super(message, cause);
	}
}