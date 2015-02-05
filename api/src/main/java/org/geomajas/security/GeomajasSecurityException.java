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

package org.geomajas.security;

import org.geomajas.annotation.Api;
import org.geomajas.global.GeomajasException;

/**
 * Geomajas security exception.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class GeomajasSecurityException extends GeomajasException {

	private static final long serialVersionUID = 153L;

	/**
	 * Create new GeomajasException.
	 */
	public GeomajasSecurityException() {
		super();
	}

	/**
	 * Create new GeomajasException.
	 *
	 * @param ex cause exception
	 */
	public GeomajasSecurityException(Throwable ex) {
		super(ex);
	}

	/**
	 * Create new GeomajasException.
	 *
	 * @param ex cause exception
	 * @param exceptionCode code which points to the message
	 * @param parameters possible extra parameters
	 */
	public GeomajasSecurityException(Throwable ex, int exceptionCode, Object... parameters) {
		super(ex, exceptionCode, parameters);
	}

	/**
	 * Create new GeomajasException.
	 *
	 * @param ex cause exception
	 * @param exceptionCode code which points to the message
	 */
	public GeomajasSecurityException(Throwable ex, int exceptionCode) {
		super(ex, exceptionCode);
	}

	/**
	 * Create new GeomajasException.
	 *
	 * @param exceptionCode code which points to the message
	 * @param parameters possible extra parameters
	 */
	public GeomajasSecurityException(int exceptionCode, Object... parameters) {
		super(exceptionCode, parameters);
	}

	/**
	 * Create new GeomajasException.
	 *
	 * @param exceptionCode code which points to the message
	 */
	public GeomajasSecurityException(int exceptionCode) {
		super(exceptionCode);
	}
}
