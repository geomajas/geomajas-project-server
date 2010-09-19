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

package org.geomajas.layer;

import org.geomajas.global.Api;
import org.geomajas.global.GeomajasException;

/**
 * Exception indicating problems during the processing of a layer.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class LayerException extends GeomajasException {

	private static final long serialVersionUID = 7528326795118453722L;

	/**
	 * Create LayerException.
	 */
	public LayerException() {
	}

	/**
	 * Create LayerException.
	 *
	 * @param ex cause exception
	 */
	public LayerException(Throwable ex) {
		super(ex);
	}

	/**
	 * Create LayerException.
	 *
	 * @param ex cause exception
	 * @param exceptionCode code which points to the message
	 * @param parameters possible extra parameters
	 */
	public LayerException(Throwable ex, int exceptionCode, Object... parameters) {
		super(ex, exceptionCode, parameters);
	}

	/**
	 * Create LayerException.
	 *
	 * @param ex cause exception
	 * @param exceptionCode code which points to the message
	 */
	public LayerException(Throwable ex, int exceptionCode) {
		super(ex, exceptionCode);
	}

	/**
	 * Create LayerException.
	 *
	 * @param exceptionCode code which points to the message
	 * @param parameters possible extra parameters
	 */
	public LayerException(int exceptionCode, Object... parameters) {
		super(exceptionCode, parameters);
	}

	/**
	 * Create LayerException.
	 *
	 * @param exceptionCode code which points to the message
	 */
	public LayerException(int exceptionCode) {
		super(exceptionCode);
	}

}
