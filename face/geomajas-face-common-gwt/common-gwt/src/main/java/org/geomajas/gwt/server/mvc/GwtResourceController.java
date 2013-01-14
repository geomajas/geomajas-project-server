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
package org.geomajas.gwt.server.mvc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geomajas.servlet.ResourceController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * A resource controller that searches both classpath and web context. This is a fallback controller for resources that
 * that are located in the GWT module. It allows direct fetching of those resources or getting them from the classpath.
 * This allows the configuration of GWT accessible images by their classpath location: 
 * <p/>
 * <code>
 * 	String href = GWT.getModuleBaseURL() + "&lt;configured classpath location&gt;";<p>
 * 	Image.setUrl(href);
 * </code>
 * 
 * @author Jan De Moerloose
 */
public class GwtResourceController extends ResourceController {
	
	public GwtResourceController() {
		setCompressionAllowed(false);
	}
	
	@RequestMapping(value = "/**/*", method = RequestMethod.GET)
	public void getResource(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		super.getResource(request, response);
	}

	protected URL[] getRequestResourceUrls(String rawResourcePath, HttpServletRequest request)
			throws MalformedURLException {
		URL[] resources = super.getRequestResourceUrls(rawResourcePath, request);
		// try web context (prepending servlet path)
		if (resources == null || resources.length == 0) {
			rawResourcePath = request.getServletPath() + request.getPathInfo();
			resources = super.getRequestResourceUrls(rawResourcePath, request);
		}
		return resources;
	}
}
