/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.printing.document;

import java.io.OutputStream;

import org.geomajas.plugin.printing.PrintingException;

/**
 * 
 * A renderable document.
 * 
 * @author Jan De Moerloose
 */

public interface Document {
	/**
	 * Renders the document to an output stream.
	 * 
	 * @param os
	 *            output stream
	 * @throws PrintingException
	 */
	void render(OutputStream os) throws PrintingException;
	
	/**
	 * Gets the content length of the document.
	 * 
	 * @return the content length in bytes of the document.
	 */
	int getContentLength();

}
