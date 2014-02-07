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
package org.geomajas.plugin.printing.document;

import java.io.OutputStream;

import org.geomajas.plugin.printing.PrintingException;
import org.springframework.util.StringUtils;

/**
 * 
 * A renderable document.
 * 
 * @author Jan De Moerloose
 */

public interface Document {

	/**
	 * Format of the document.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	enum Format {
		PDF("pdf", "application/pdf"), JPG("jpg", "image/jpg"), PNG("png", "image/png");

		private String extension;

		private String mimetype;

		private Format(String extension, String mimetype) {
			this.extension = extension;
			this.mimetype = mimetype;
		}

		public String getExtension() {
			return extension;
		}

		public String getMimetype() {
			return mimetype;
		}

		public static Format decode(String path) throws PrintingException {
			String extension = StringUtils.getFilenameExtension(path).toLowerCase();
			if (JPG.getExtension().equals(extension)) {
				return JPG;
			} else if (PNG.getExtension().equals(extension)) {
				return PNG;
			} else {
				return PDF;
			}
		}
	}

	/**
	 * Renders the document to an output stream.
	 * 
	 * @param os
	 *            output stream
	 * @throws PrintingException
	 */
	void render(OutputStream os, Format format) throws PrintingException;

	/**
	 * Gets the content length of the document.
	 * 
	 * @return the content length in bytes of the document.
	 */
	int getContentLength();

}
