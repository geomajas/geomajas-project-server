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
package org.geomajas.plugin.printing.mvc;

import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geomajas.plugin.printing.document.Document;
import org.geomajas.plugin.printing.document.Document.Format;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

/**
 * View to produce printed documents (PDF, PNG, ...).
 *
 * @author Jan De Moerloose
 * @author An Buyle
 */
@Component(PrintingController.DOCUMENT_VIEW_NAME)
public class DocumentView extends AbstractView {

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Document doc = (Document) model.get(PrintingController.DOCUMENT_KEY);
		String download = (String) model.get(PrintingController.DOWNLOAD_KEY);
		String fileName = (String) model.get(PrintingController.FILENAME_KEY);
		Format format = (Format) model.get(PrintingController.FORMAT_KEY);
				
		// Write content type and also length (determined via byte array).
		response.setContentType(format.getMimetype());
		
		
		// check download method
		if (download.equals(PrintingController.DOWNLOAD_METHOD_SAVE)) {
			response.setHeader("Content-Disposition", " attachment; filename=\"" + fileName + "\"");
		} else if (download.equals(PrintingController.DOWNLOAD_METHOD_BROWSER)) {
			response.setHeader("Content-Disposition", " inline; filename=\"" + fileName + "\"");
		} else {
			throw new IllegalArgumentException("invalid download method " + download);
		}

		// Write the docmuent
		ServletOutputStream out = response.getOutputStream();
		doc.render(out, format);
		response.setContentLength(doc.getContentLength());
		out.flush();
	}

}
