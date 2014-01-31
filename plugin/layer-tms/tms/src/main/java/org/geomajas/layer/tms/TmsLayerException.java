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

package org.geomajas.layer.tms;

import org.geomajas.layer.LayerException;

/**
 * Exception thrown from within the {@link TmsConfigurationService}. It signals possible errors while retrieving or
 * parsing TMS capabilities files.
 * 
 * @author Pieter De Graef
 */
public class TmsLayerException extends LayerException {

	private static final long serialVersionUID = 100L;

	private static final String RESOURCE_BUNDLE_NAME = "org.geomajas.layer.tms.TmsLayerException";

	public static final int COULD_NOT_READ_FILE = 1;
	public static final int COULD_NOT_FIND_FILE = 2;

	/**
	 * Build TmsLayerException for given exception code.
	 *
	 * @param exceptionCode exception code
	 * @param parameters parameters for exception message
	 */
	public TmsLayerException(int exceptionCode, Object... parameters) {
		super(exceptionCode, parameters);
	}

	/**
	 * Build TmsLayerException for given cause and exception code.
	 *
	 * @param ex cause exception
	 * @param exceptionCode exception code
	 * @param parameters parameters for exception message
	 */
	public TmsLayerException(Throwable ex, int exceptionCode, Object... parameters) {
		super(ex, exceptionCode, parameters);
	}

	@Override
	public String getResourceBundleName() {
		return RESOURCE_BUNDLE_NAME;
	}
}