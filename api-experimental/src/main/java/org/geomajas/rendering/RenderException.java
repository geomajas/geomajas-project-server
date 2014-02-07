/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.rendering;

import org.geomajas.annotation.Api;
import org.geomajas.global.GeomajasException;

/**
 * Exception indicating problems during rendering.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
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
