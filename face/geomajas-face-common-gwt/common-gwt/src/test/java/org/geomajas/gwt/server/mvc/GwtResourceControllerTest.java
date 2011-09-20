package org.geomajas.gwt.server.mvc;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
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

	public class NonExistingResourceLoader implements ResourceLoader {

		public Resource getResource(String location) {
			return new AbstractResource() {

				@Override
				public boolean exists() {
					return false;
				}

				public String getDescription() {
					return null;
				}

				public InputStream getInputStream() throws IOException {
					return null;
				}
			};
		}

		public ClassLoader getClassLoader() {
			return getClass().getClassLoader();
		}

	}

}
