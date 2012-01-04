/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
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
}