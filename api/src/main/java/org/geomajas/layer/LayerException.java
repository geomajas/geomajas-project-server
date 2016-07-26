/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.layer;

import org.geomajas.annotation.Api;
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
		super();
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
