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
package org.geomajas.extension.printing;

import org.geomajas.extension.printing.command.dto.PrintGetTemplateRequest;
import org.geomajas.extension.printing.document.AbstractDocument;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.NoSuchElementException;

/**
 * Servlet for serviing PDf documents.
 *
 * @author Jan De Moerloose
 */
public class PdfServlet extends HttpServlet {

	private static final long serialVersionUID = 151L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		try {

			int documentId = Integer.parseInt(request.getParameter("documentId"));

			AbstractDocument pdfDocument = PdfContainer.getInstance().getDocument(documentId);
			pdfDocument.render();
			OutputStream responseOutputStream = response.getOutputStream();

			if (pdfDocument.getDownloadMethod() == PrintGetTemplateRequest.DOWNLOAD_METHOD_SAVE) {
				response
						.setHeader("Content-Disposition",
								" attachment; filename=\"" + pdfDocument.getFilename() + "\"");
			} else {
				response.setHeader("Content-Disposition", " inline; filename=\"" + pdfDocument.getFilename() + "\"");
			}

			response.setContentType("application/pdf");
			response.setContentLength(pdfDocument.getOutputStream().size());

			pdfDocument.getOutputStream().writeTo(responseOutputStream);
			pdfDocument.getOutputStream().flush();

		} catch (NumberFormatException e) {
			response.setContentType("text/html");
			response.getWriter().println("Invalid document id!");
		} catch (NoSuchElementException e) {
			response.setContentType("text/html");
			response.getWriter().println("Document unavailable!");
		}
	}
}
