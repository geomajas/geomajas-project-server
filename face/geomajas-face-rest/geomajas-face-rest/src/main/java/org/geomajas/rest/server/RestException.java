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
