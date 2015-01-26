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

package org.geomajas.plugin.reporting;

import org.geomajas.annotation.Api;
import org.geomajas.global.GeomajasException;

/**
 * Custom exception for reporting errors.
 *
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public class ReportingException extends GeomajasException {

	/** Feature code is missing. */
	public static final int MISSING_FEATURE_CODE = 1;

	private static final String RESOURCE_BUNDLE_NAME = "org.geomajas.plugin.reporting.ReportingException";

	/**
	 * Construct reporting exception.
	 *
	 * @param exceptionCode exception code
	 * @param parameters parameters
	 */
	public ReportingException(int exceptionCode, Object... parameters) {
		super(exceptionCode, parameters);
	}

	/**
	 * Construct reporting exception.
	 *
	 * @param exceptionCode exception code
	 */
	public ReportingException(int exceptionCode) {
		super(exceptionCode);
	}

	/**
	 * Construct reporting exception.
	 *
	 * @param ex cause exception
	 * @param exceptionCode exception code
	 * @param parameters parameters
	 */
	public ReportingException(Throwable ex, int exceptionCode, Object... parameters) {
		super(ex, exceptionCode, parameters);
	}

	/**
	 * Construct reporting exception.
	 *
	 * @param ex cause exception
	 * @param exceptionCode exception code
	 */
	public ReportingException(Throwable ex, int exceptionCode) {
		super(ex, exceptionCode);
	}

	@Override
	public String getResourceBundleName() {
		return RESOURCE_BUNDLE_NAME;
	}

}
