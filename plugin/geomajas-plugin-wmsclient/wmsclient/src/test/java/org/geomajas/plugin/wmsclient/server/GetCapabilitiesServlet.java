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

package org.geomajas.plugin.wmsclient.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that returns WMS Capabilities files.
 * 
 * @author Pieter De Graef
 */
public class GetCapabilitiesServlet extends HttpServlet {

	private static final long serialVersionUID = 100L;

	private static final String CAPA_111_FILE = "GetCapabilities111.xml";

	private static final String CAPA_130_FILE = "GetCapabilities130.xml";

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String version = (String) req.getParameter("version");
		InputStream in;
		if ("1.1.1".equals(version)) {
			in = getClass().getResourceAsStream(CAPA_111_FILE);
		} else if ("1.3.0".equals(version)) {
			in = getClass().getResourceAsStream(CAPA_130_FILE);
		} else {
			return;
		}
		resp.setContentType("application/xml");
		copyStream(in, resp.getOutputStream());
		resp.getOutputStream().flush();
	}

	private void copyStream(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
	}
}