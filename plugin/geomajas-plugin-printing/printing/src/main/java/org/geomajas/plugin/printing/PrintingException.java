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
package org.geomajas.plugin.printing;

import org.geomajas.global.GeomajasException;

/**
 * Common exception class for the printing plugin.
 * 
 * @author Jan De Moerloose
 * 
 */
public class PrintingException extends GeomajasException {

	private static final long serialVersionUID = -7531673605025800128L;

	public static final int DOCUMENT_NOT_FOUND = 0;

	public static final int PRINT_TEMPLATE_XML_PROBLEM = 1;

	public static final int PRINT_TEMPLATE_PERSIST_PROBLEM = 2;

	public static final int DOCUMENT_LAYOUT_PROBLEM = 3;

	public static final int DOCUMENT_RENDER_PROBLEM = 4;

	public static final int DTO_IMPLEMENTATION_NOT_FOUND = 5;

	/**
	 * Create new PrintingException.
	 * 
	 * @param ex
	 *            cause exception
	 * @param exceptionCode
	 *            code which points to the message
	 * @param parameters
	 *            possible extra parameters
	 */
	public PrintingException(Throwable ex, int exceptionCode, Object... parameters) {
		super(ex, exceptionCode, parameters);
	}

	/**
	 * Create new PrintingException.
	 * 
	 * @param exceptionCode
	 *            code which points to the message
	 * @param parameters
	 *            possible extra parameters
	 */
	public PrintingException(int exceptionCode, Object... parameters) {
		super(exceptionCode, parameters);
	}

	@Override
	public String getResourceBundleName() {
		return "org.geomajas.plugin.printing.PrintingException";
	}

}
