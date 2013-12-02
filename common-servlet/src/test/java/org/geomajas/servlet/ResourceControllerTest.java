package org.geomajas.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import javax.servlet.ServletException;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;

public class ResourceControllerTest {

	@Test
	public void testResourceInContext() throws ServletException, IOException {
		// create a default mock context (will load from classpath)
		MockServletContext context = new TestServletContext();
		MockHttpServletRequest request = new MockHttpServletRequest(context);
		request.setPathInfo("/org/geomajas/servlet/geomajas_logo.png");
		request.setMethod("GET");
		MockHttpServletResponse response = new MockHttpServletResponse();
		ResourceController resourceController = new ResourceController();
		resourceController.setServletContext(context);
		resourceController.getResource(request, response);
		Resource resource = new ClassPathResource("/org/geomajas/servlet/geomajas_logo.png");
		Assert.assertArrayEquals(IOUtils.toByteArray(resource.getInputStream()), response.getContentAsByteArray());
	}

	@Test
	public void testResourceInClassPath() throws ServletException, IOException {
		// create an empty mock context
		MockServletContext context = new TestServletContext(true);
		MockHttpServletRequest request = new MockHttpServletRequest(context);
		request.setPathInfo("/org/geomajas/servlet/geomajas_logo.png");
		request.setMethod("GET");
		MockHttpServletResponse response = new MockHttpServletResponse();
		ResourceController resourceController = new ResourceController();
		resourceController.setServletContext(context);
		resourceController.getResource(request, response);
		Resource resource = new ClassPathResource("/org/geomajas/servlet/geomajas_logo.png");
		Assert.assertArrayEquals(IOUtils.toByteArray(resource.getInputStream()), response.getContentAsByteArray());
	}

	@Test
	public void testIncludeServletPath() throws ServletException, IOException {
		// create an empty mock context
		MockServletContext context = new TestServletContext(true);
		MockHttpServletRequest request = new MockHttpServletRequest(context);
		request.setServletPath("/org");
		request.setPathInfo("/geomajas/servlet/test.js");
		request.setMethod("GET");
		MockHttpServletResponse response = new MockHttpServletResponse();
		ResourceController resourceController = new ResourceController();
		resourceController.setIncludeServletPath(true);
		resourceController.setServletContext(context);
		resourceController.getResource(request, response);
		Resource resource = new ClassPathResource("/org/geomajas/servlet/test.js");
		Assert.assertArrayEquals(IOUtils.toByteArray(resource.getInputStream()), response.getContentAsByteArray());
	}
	
	@Test
	public void testCompression() throws ServletException, IOException {
		// create an empty mock context
		MockServletContext context = new TestServletContext(true);
		MockHttpServletRequest request = new MockHttpServletRequest(context);
		request.addHeader("Accept-Encoding", "gzip");
		request.setPathInfo("/org/geomajas/servlet/test.js");
		request.setMethod("GET");
		MockHttpServletResponse response = new MockHttpServletResponse();
		ResourceController resourceController = new ResourceController();
		resourceController.setServletContext(context);
		resourceController.getResource(request, response);
		Resource resource = new ClassPathResource("/org/geomajas/servlet/test.js");
		GZIPInputStream gzipInputStream = new GZIPInputStream(
				new ByteArrayInputStream(response.getContentAsByteArray()));
		Assert.assertArrayEquals(IOUtils.toByteArray(resource.getInputStream()), IOUtils.toByteArray(gzipInputStream));
	}
	
	
	@Test
	public void testCompressionDisabled() throws ServletException, IOException {
		// create an empty mock context
		MockServletContext context = new TestServletContext(true);
		MockHttpServletRequest request = new MockHttpServletRequest(context);
		request.addHeader("Accept-Encoding", "gzip");
		request.setPathInfo("/org/geomajas/servlet/test.js");
		request.setMethod("GET");
		MockHttpServletResponse response = new MockHttpServletResponse();
		ResourceController resourceController = new ResourceController();
		resourceController.setCompressionAllowed(false);
		resourceController.setServletContext(context);
		resourceController.getResource(request, response);
		Resource resource = new ClassPathResource("/org/geomajas/servlet/test.js");
		Assert.assertArrayEquals(IOUtils.toByteArray(resource.getInputStream()), response.getContentAsByteArray());
	}

	class TestServletContext extends MockServletContext {
		
		public TestServletContext(){
			this(false);
		}
		
		public TestServletContext(boolean empty) {
			super(empty ? "non-existing-path" : null );
		}

		/**
		 * No configured mime-types to test the mime-types of the controller
		 */
		@Override
		public String getMimeType(String filePath) {
			return null;
		}
				
	}

}
