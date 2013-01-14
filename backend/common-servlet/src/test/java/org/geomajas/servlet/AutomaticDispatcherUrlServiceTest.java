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

package org.geomajas.servlet;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Test to verify the functioning of the {@link AutomaticDispatcherUrlService}.
 *
 * @author Joachim Van der Auwera
 */
public class AutomaticDispatcherUrlServiceTest {
	private static final String X_FORWARD_HOST_HEADER = "X-Forwarded-Host";
	private static final String X_GWT_MODULE_HEADER = "X-GWT-Module-Base";

	@Test
	public void testBuildUrl() throws Exception {
		AutomaticDispatcherUrlService adus = new AutomaticDispatcherUrlService();

		// set mock request in context holder
		MockHttpServletRequest mockRequest = new MockHttpServletRequest();
		mockRequest.setScheme("http");
		mockRequest.setServerName("geomajas.org");
		mockRequest.setServerPort(80);
		mockRequest.setContextPath("/test");
		ServletRequestAttributes attributes = new ServletRequestAttributes(mockRequest);
		RequestContextHolder.setRequestAttributes(attributes);

		Assert.assertEquals("http://geomajas.org/test/d/", adus.getDispatcherUrl());


		mockRequest.setScheme("https");
		mockRequest.setServerName("secure.geomajas.org");
		mockRequest.setServerPort(8443);
		mockRequest.setContextPath(null);
		Assert.assertEquals("https://secure.geomajas.org:8443/d/", adus.getDispatcherUrl());

		// clean up
		RequestContextHolder.setRequestAttributes(null);
	}
	
	@Test
	public void testReverseProxyWithModuleBase() {
		AutomaticDispatcherUrlService adus = new AutomaticDispatcherUrlService();

		// set mock request in context holder
		MockHttpServletRequest mockRequest = new MockHttpServletRequest();
		mockRequest.setScheme("http");
		mockRequest.setServerName("myhost");
		mockRequest.setServerPort(80);
		mockRequest.setContextPath("/test");
		mockRequest.addHeader(X_FORWARD_HOST_HEADER, "geomajas.org");
		mockRequest.addHeader(X_GWT_MODULE_HEADER, "http://geomajas.org/app/Module");
		ServletRequestAttributes attributes = new ServletRequestAttributes(mockRequest);
		RequestContextHolder.setRequestAttributes(attributes);
		Assert.assertEquals("http://geomajas.org/app/d/", adus.getDispatcherUrl());

		// clean up
		RequestContextHolder.setRequestAttributes(null);
	}
	
	@Test
	public void testReverseProxyWithoutModuleBase() {
		AutomaticDispatcherUrlService adus = new AutomaticDispatcherUrlService();

		// set mock request in context holder
		MockHttpServletRequest mockRequest = new MockHttpServletRequest();
		mockRequest.setScheme("http");
		mockRequest.setServerName("myhost");
		mockRequest.setServerPort(80);
		mockRequest.setContextPath("/test");
		mockRequest.addHeader(X_FORWARD_HOST_HEADER, "geomajas.org");
		ServletRequestAttributes attributes = new ServletRequestAttributes(mockRequest);
		RequestContextHolder.setRequestAttributes(attributes);
		Assert.assertEquals("http://geomajas.org/test/d/", adus.getDispatcherUrl());

		// clean up
		RequestContextHolder.setRequestAttributes(null);
	}
	
	@Test
	public void testLocalizeAutomatic() {
		AutomaticDispatcherUrlService adus = new AutomaticDispatcherUrlService();

		// set mock request in context holder
		MockHttpServletRequest mockRequest = new MockHttpServletRequest();
		mockRequest.setScheme("http");
		mockRequest.setServerName("myhost");
		mockRequest.setServerPort(80);
		mockRequest.setLocalName("localhost");
		mockRequest.setLocalPort(8080);
		mockRequest.setContextPath("/test");
		mockRequest.addHeader(X_FORWARD_HOST_HEADER, "geomajas.org");
		ServletRequestAttributes attributes = new ServletRequestAttributes(mockRequest);
		RequestContextHolder.setRequestAttributes(attributes);
		Assert.assertEquals("http://localhost:8080/test/d/something", adus.localize("http://geomajas.org/test/d/something"));

		// clean up
		RequestContextHolder.setRequestAttributes(null);
	}
	
	@Test
	public void testLocalizeConfigured() {
		AutomaticDispatcherUrlService adus = new AutomaticDispatcherUrlService();
		adus.setLocalDispatcherUrl("http://my:8080/local/dispatcher/");

		// set mock request in context holder
		MockHttpServletRequest mockRequest = new MockHttpServletRequest();
		mockRequest.setScheme("http");
		mockRequest.setServerName("myhost");
		mockRequest.setServerPort(80);
		mockRequest.setLocalName("localhost");
		mockRequest.setLocalPort(8080);
		mockRequest.setContextPath("/test");
		mockRequest.addHeader(X_FORWARD_HOST_HEADER, "geomajas.org");
		ServletRequestAttributes attributes = new ServletRequestAttributes(mockRequest);
		RequestContextHolder.setRequestAttributes(attributes);
		Assert.assertEquals("http://my:8080/local/dispatcher/something", adus.localize("http://geomajas.org/test/d/something"));

		// clean up
		RequestContextHolder.setRequestAttributes(null);
	}
}
