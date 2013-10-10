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
package org.geomajas.plugin.rasterizing.api;

import org.geomajas.annotation.Api;
import org.geomajas.global.GeomajasException;

/**
 * Exception class for the rasterizing plugin.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 * 
 */
@Api(allMethods = true)
public class RasterException extends GeomajasException {

	/** Image writing failed. */
	public static final int IMAGE_WRITING_FAILED = 1;
	/** Missing layer factory. */
	public static final int MISSING_LAYER_FACTORY = 2;
	/**
	 * Bad Svg content.
	 * @since 1.3.0
	 */	
	public static final int BAD_SVG = 3;

	/**
	 * Create new RasterException.
	 *
	 * @param exceptionCode code which points to the message
	 * @param parameters possible extra parameters
	 */
	public RasterException(int exceptionCode, Object... parameters) {
		super(exceptionCode, parameters);
	}

	/**
	 * Create new RasterException.
	 *
	 * @param exceptionCode code which points to the message
	 */
	public RasterException(int exceptionCode) {
		super(exceptionCode);
	}

	/**
	 * Create new RasterException.
	 *
	 * @param ex cause exception
	 * @param exceptionCode code which points to the message
	 * @param parameters possible extra parameters
	 */
	public RasterException(Throwable ex, int exceptionCode, Object... parameters) {
		super(ex, exceptionCode, parameters);
	}

	/**
	 * Create new RasterException.
	 *
	 * @param ex cause exception
	 * @param exceptionCode code which points to the message
	 */
	public RasterException(Throwable ex, int exceptionCode) {
		super(ex, exceptionCode);
	}

}
