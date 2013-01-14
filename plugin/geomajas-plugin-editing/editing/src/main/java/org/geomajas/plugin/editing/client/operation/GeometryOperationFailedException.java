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

package org.geomajas.plugin.editing.client.operation;

/**
 * Exception that is thrown when an operation onto a geometry has failed.
 * 
 * @author Pieter De Graef
 */
public class GeometryOperationFailedException extends Exception {

	private static final long serialVersionUID = 100L;

	public GeometryOperationFailedException() {
		super();
	}

	public GeometryOperationFailedException(String message) {
		super(message);
	}

	public GeometryOperationFailedException(Throwable throwable) {
		super(throwable);
	}
}