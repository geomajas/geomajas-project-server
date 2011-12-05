/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.editing.client.merging;

/**
 * Exception that is thrown when something went wrong while merging geometries.
 * 
 * @author Pieter De Graef
 */
public class GeometryMergingException extends Exception {

	private static final long serialVersionUID = 100L;

	public GeometryMergingException() {
		super();
	}

	public GeometryMergingException(String message) {
		super(message);
	}

	public GeometryMergingException(String message, Throwable cause) {
		super(message, cause);
	}
}