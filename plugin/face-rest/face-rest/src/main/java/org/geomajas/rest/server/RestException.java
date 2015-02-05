/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.rest.server;

import org.geomajas.global.GeomajasException;

/**
 * 
 * Exception thrown when there is an error in the rest face.
 * 
 * @author Oliver May
 * @author Jan De Moerlose
 * 
 */

public class RestException extends GeomajasException {

	private static final long serialVersionUID = 100L;

	public static final int FEATURE_NOT_FOUND = 0;
	public static final int PROBLEM_READING_LAYERSERVICE = 1;
	public static final int NO_SUCH_ATTRIBUTE = 2;
	public static final int UNSUPPORTED_QUERY_OPERATION = 3;
	public static final int NO_ELEMENTS = 4;


	/**
	 * Create new RestException.
	 * 
	 * @param ex
	 *            cause exception
	 * @param exceptionCode
	 *            code which points to the message
	 * @param parameters
	 *            possible extra parameters
	 */
	public RestException(Throwable ex, int exceptionCode, Object... parameters) {
		super(ex, exceptionCode, parameters);
	}

	/**
	 * Create new RestException.
	 * 
	 * @param exceptionCode
	 *            code which points to the message
	 * @param parameters
	 *            possible extra parameters
	 */
	public RestException(int exceptionCode, Object... parameters) {
		super(exceptionCode, parameters);
	}

	@Override
	public String getResourceBundleName() {
		return "org.geomajas.rest.server.RestException";
	}

}
