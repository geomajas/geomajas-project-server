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
package org.geomajas.plugin.deskmanager;

import org.geomajas.global.GeomajasException;

/**
 * Common exception class for the Deskmanager plugin.
 * 
 * @author An Buyle
 * 
 */
public class DeskmanagerException extends GeomajasException {

	private static final long serialVersionUID = -7531673605025800128L;

	public static final int NO_CONNECTION_TO_CAPABILITIES_SERVER = 1;

	public static final int LAYER_NOT_FOUND = 2;
	
	public static final int ERROR_CONSTRUCTING_RASTER_LAYER = 3;
	
	public static final int CLIENT_LAYERID_ALREADY_IN_USE = 4;

	public static final int SERVER_LAYERID_ALREADY_IN_USE = 5;

	public static final int CANNOT_CREATE_SHAPEFILE_FEATURESTORE = 6;

	public static final int DELETE_SEARCH_FAVORITE_FAILURE = 7;

	
	/**
	 * Constructor.
	 * 
	 * @param ex
	 *            cause exception
	 * @param exceptionCode
	 *            code which points to the message
	 * @param parameters
	 *            possible extra parameters
	 */
	public DeskmanagerException(Throwable ex, int exceptionCode, Object... parameters) {
		super(ex, exceptionCode, parameters);
	}

	/**
	 * Constructor.
	 * 
	 * @param exceptionCode
	 *            code which points to the message
	 * @param parameters
	 *            possible extra parameters
	 */
	public DeskmanagerException(int exceptionCode, Object... parameters) {
		super(exceptionCode, parameters);
	}

	@Override
	public String getResourceBundleName() {
		return "org.geomajas.plugin.deskmanager.DeskmanagerException";
	}

}
