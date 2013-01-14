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

package org.geomajas.geometry.conversion.jts;

import org.geomajas.annotation.Api;

/**
 * Exception that indicates something went wrong during conversion between the Geomajas and the JTS geometry model.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class JtsConversionException extends Exception {

	private static final long serialVersionUID = 100L;

	/**
	 * Create a new exception instance with the given error message.
	 * 
	 * @param message
	 *            The error message.
	 */
	public JtsConversionException(String message) {
		super(message);
	}
}