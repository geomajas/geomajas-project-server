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

import javax.servlet.ServletException;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;

public class GwtResourceControllerTest {

	@Test
	public void testResourceInClassPath() throws ServletException, IOException {
		// create mock context that loads from the classpath
		MockServletContext context = new MockServletContext();
		MockHttpServletRequest request = new MockHttpServletRequest(context);
		request.setPathInfo("/org/geomajas/gwt/server/mvc/geomajas_logo.png");
		request.setMethod("GET");
		MockHttpServletResponse response = new MockHttpServletResponse();
		GwtResourceController resourceController = new GwtResourceController();
		resourceController.setServletContext(context);
		resourceController.getResource(request, response);
		Resource resource = new ClassPathResource("/org/geomajas/gwt/server/mvc/geomajas_logo.png");
		Assert.assertArrayEquals(IOUtils.toByteArray(resource.getInputStream()), response.getContentAsByteArray());
	}

	@Test
	public void testResourceInContext() throws ServletException, IOException {
		// create mock context that loads from the classpath
		MockServletContext context = new MockServletContext();
		MockHttpServletRequest request = new MockHttpServletRequest(context);
		request.setServletPath("/org");
		request.setPathInfo("/org/geomajas/gwt/server/mvc/geomajas_logo.png");
		request.setMethod("GET");
		MockHttpServletResponse response = new MockHttpServletResponse();
		GwtResourceController resourceController = new GwtResourceController();
		resourceController.setServletContext(context);
		resourceController.getResource(request, response);
		Resource resource = new ClassPathResource("/org/geomajas/gwt/server/mvc/geomajas_logo.png");
		Assert.assertArrayEquals(IOUtils.toByteArray(resource.getInputStream()), response.getContentAsByteArray());
	}

}
