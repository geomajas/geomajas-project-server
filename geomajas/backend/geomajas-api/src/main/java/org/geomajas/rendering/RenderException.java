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

package org.geomajas.rendering;

import org.geomajas.global.GeomajasException;

/**
 * Exception indicating problems during rendering.
 *
 * @author Joachim Van der Auwera
 */
public class RenderException extends GeomajasException {

	private static final long serialVersionUID = 236362372152849178L;

	/**
	 * Create new RenderException.
	 */
	public RenderException() {
	}

	/**
	 * Create new RenderException.
	 *
	 * @param ex cause exception
	 */
	public RenderException(Exception ex) {
		super(ex);
	}

	/**
	 * Create new RenderException.
	 *
	 * @param ex cause exception
	 * @param exceptionCode code which points to the message
	 * @param parameters possible extra parameters
	 */
	public RenderException(Throwable ex, int exceptionCode, Object... parameters) {
		super(ex, exceptionCode, parameters);
	}

	/**
	 * Create new RenderException.
	 *
	 * @param ex cause exception
	 * @param exceptionCode code which points to the message
	 */
	public RenderException(Throwable ex, int exceptionCode) {
		super(ex, exceptionCode);
	}

	/**
	 * Create new RenderException.
	 *
	 * @param exceptionCode code which points to the message
	 * @param parameters possible extra parameters
	 */
	public RenderException(int exceptionCode, Object... parameters) {
		super(exceptionCode, parameters);
	}

	/**
	 * Create new RenderException.
	 *
	 * @param exceptionCode code which points to the message
	 */
	public RenderException(int exceptionCode) {
		super(exceptionCode);
	}
}
