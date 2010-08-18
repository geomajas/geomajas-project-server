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
package org.geomajas.plugin.printing.mvc;

import java.io.ByteArrayOutputStream;
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
