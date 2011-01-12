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
package org.geomajas.plugin.printing.mvc;

import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geomajas.plugin.printing.document.Document;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

/**
 * 
 * @author Jan De Moerloose
 * 
 */
@Component("pdfView")
public class PdfView extends AbstractView {

	public PdfView() {
		setContentType("application/pdf");
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Document doc = (Document) model.get(PdfController.DOCUMENT_KEY);
		String download = (String) model.get(PdfController.DOWNLOAD_KEY);
		String fileName = (String) model.get(PdfController.FILENAME_KEY);
				
		// Write content type and also length (determined via byte array).
		response.setContentType(getContentType());
		response.setContentLength(doc.getContentLength());
		
		// check download method
		if (download.equals(PdfController.DOWNLOAD_METHOD_SAVE)) {
			response.setHeader("Content-Disposition", " attachment; filename=\"" + fileName + "\"");
		} else {
			response.setHeader("Content-Disposition", " inline; filename=\"" + fileName + "\"");
		}

		// Write the pdf
		ServletOutputStream out = response.getOutputStream();
		doc.render(out);
		out.flush();
	}

}
