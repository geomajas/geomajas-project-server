/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
