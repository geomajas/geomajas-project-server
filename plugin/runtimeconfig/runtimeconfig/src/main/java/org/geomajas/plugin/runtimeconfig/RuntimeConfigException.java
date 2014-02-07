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
package org.geomajas.plugin.runtimeconfig;

import org.geomajas.global.GeomajasException;

/**
 * Exception in administration plugin.
 * 
 * @author Jan De Moerloose
 * 
 */
public class RuntimeConfigException extends GeomajasException {

	private static final long serialVersionUID = 100L;

	public static final int INVALID_BEAN_DEFINITION = 0;

	public static final int BEAN_CREATION_FAILED = 1;

	public static final int INVALID_BEAN_DEFINITION_LOCATION = 2;

	public static final int BEAN_CREATION_FAILED_LOCATION = 3;

	public static final int CONFIGURATION_NOT_SUPPORTED = 4;

	public static final int CONTEXT_RESTORE_FAILED = 5;

	public static final int BEAN_PERSIST_FAILED = 6;

	public static final int BEAN_DELETE_FAILED = 7;

	public static final int BAD_PARAMETER = 8;

	/**
	 * Create new ContextConfiguratorException.
	 * 
	 * @param ex cause exception
	 * @param exceptionCode code which points to the message
	 * @param parameters possible extra parameters
	 */
	public RuntimeConfigException(Throwable ex, int exceptionCode, Object... parameters) {
		super(ex, exceptionCode, parameters);
	}

	/**
	 * Create new ContextConfiguratorException.
	 * 
	 * @param exceptionCode code which points to the message
	 * @param parameters possible extra parameters
	 */
	public RuntimeConfigException(int exceptionCode, Object... parameters) {
		super(exceptionCode, parameters);
	}

	@Override
	public String getResourceBundleName() {
		return "org.geomajas.plugin.runtimeconfig.RuntimeConfigException";
	}

}
