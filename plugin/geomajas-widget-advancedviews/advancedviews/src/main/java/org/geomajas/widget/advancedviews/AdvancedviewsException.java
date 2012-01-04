/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.advancedviews;

import org.geomajas.global.GeomajasException;

/**
 * Common exception class for the advanced views plugin.
 * 
 * @author Kristof Heirwegh
 * 
 */
public class AdvancedviewsException extends GeomajasException {

	private static final long serialVersionUID = -7531673605025800128L;

	public static final int REQUIRED_PARAMETER_MISSING = 0;

	public static final int NO_SUCH_LAYER = 1;
	
	public static final int NO_SUCH_NAMEDSTYLE = 2;

	public static final int NO_SUCH_FEATURESTYLE = 3;

	public static final int PARSING_DASHARRAY_FAILED = 4;

	public static final int IMAGE_NOT_FOUND = 5;

	public static final int FAILED_CREATING_IMAGEICON = 6;

	/**
	 * Create new AdvancedviewsException.
	 * 
	 * @param ex
	 *            cause exception
	 * @param exceptionCode
	 *            code which points to the message
	 * @param parameters
	 *            possible extra parameters
	 */
	public AdvancedviewsException(Throwable ex, int exceptionCode, Object... parameters) {
		super(ex, exceptionCode, parameters);
	}

	/**
	 * Create new AdvancedviewsException.
	 * 
	 * @param exceptionCode
	 *            code which points to the message
	 * @param parameters
	 *            possible extra parameters
	 */
	public AdvancedviewsException(int exceptionCode, Object... parameters) {
		super(exceptionCode, parameters);
	}

	@Override
	public String getResourceBundleName() {
		return "org.geomajas.widget.advancedviews.AdvancedviewsException";
	}

}
