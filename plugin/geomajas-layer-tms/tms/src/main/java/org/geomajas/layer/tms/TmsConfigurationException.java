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

package org.geomajas.layer.tms;

/**
 * Exception thrown from within the {@link TmsConfigurationService}. It signals possible errors while retrieving or
 * parsing TMS capabilities files.
 * 
 * @author Pieter De Graef
 */
public class TmsConfigurationException extends Exception {

	private static final long serialVersionUID = 100L;

	/**
	 * Create a new instance using another cause and adding an extra message.
	 * 
	 * @param message
	 *            The extra message to add.
	 * @param cause
	 *            The original cause of the problem.
	 */
	public TmsConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}
}