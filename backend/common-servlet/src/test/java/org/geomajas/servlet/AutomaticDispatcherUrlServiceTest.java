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

	@Test
	public void testBuildUrl() throws Exception {
		AutomaticDispatcherUrlService adus = new AutomaticDispatcherUrlService();

		// set mock request in context holder
		MockHttpServletRequest mockRequest = new MockHttpServletRequest();
		mockRequest.setScheme("http");
		mockRequest.setServerName("geomajas.org");
		mockRequest.setServerPort(80);
		mockRequest.setContextPath("test");
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
}
