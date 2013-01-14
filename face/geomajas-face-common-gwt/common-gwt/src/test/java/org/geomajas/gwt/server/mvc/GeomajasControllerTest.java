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
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/gwt/server/mvc/mockWebContext.xml" })
public class GeomajasControllerTest {

	@Autowired
	@Qualifier("defaultController")
	GeomajasController defaultController;

	@Autowired
	@Qualifier("customController")
	GeomajasController customController;

	@Test
	public void testNonWebContext() throws ServletException, IOException {
		// create mock context that loads from the classpath
		MockServletConfig config = new MockServletConfig();
		MockHttpServletRequest request = new MockHttpServletRequest(config.getServletContext());
		MockHttpServletResponse response = new MockHttpServletResponse();
		GeomajasController c = new GeomajasController();
		c.init(config);
		try {
			c.doPost(request, response);
			Assert.fail("Should fail outside web context");
		} catch (RuntimeException e) {
		}
	}

	@Test
	public void testMockWebContext() throws ServletException, IOException {
		// create mock context that loads from the classpath
		MockServletConfig config = new MockServletConfig();
		MockHttpServletRequest request = new MockHttpServletRequest(config.getServletContext());
		request.setContentType("text/x-gwt-rpc");
		request.setCharacterEncoding("UTF-8");
		request.setContent(("6|0|10|http://apps.geomajas.org/explorer/be.geosparc.Explorer/"
				+ "|54044FB0C988344F1715C8B91330B0A2|org.geomajas.gwt.client.GeomajasService|"
				+ "execute|org.geomajas.gwt.client.command.GwtCommand/4093389776|command.configuration.GetMap|"
				+ "org.geomajas.command.dto.GetMapConfigurationRequest/104733661|explorer|mainMap|"
				+ "ss.TqRPfHFh24NVxB|1|2|3|4|1|5|5|6|7|8|9|0|10|").getBytes("UTF-8"));
		request.addHeader("X-GWT-Permutation", "54044FB0C988344F1715C8B91330B0A2");
		request.addHeader("X-GWT-Module-Base", "http://test/module/");
		MockHttpServletResponse response = new MockHttpServletResponse();
		defaultController.setServletConfig(config);
		defaultController.doPost(request, response);
		// expect the message of the out-dated 1.3 policy of GWT
		Assert.assertTrue(response.getContentAsString().contains(
				"Type 'org.geomajas.gwt.client.command.GwtCommand' was not assignable"
						+ " to 'com.google.gwt.user.client.rpc.IsSerializable'"));
	}

	@Test
	public void testSerializationPolicy() throws UnsupportedEncodingException, ServletException {
		// create mock context that loads from the classpath
		MockServletConfig config = new MockServletConfig();
		MockHttpServletRequest request = new MockHttpServletRequest(config.getServletContext());
		request.setContentType("text/x-gwt-rpc");
		request.setCharacterEncoding("UTF-8");
		request.setContent(("6|0|10|http://apps.geomajas.org/explorer/be.geosparc.Explorer/"
				+ "|54044FB0C988344F1715C8B91330B0A2|org.geomajas.gwt.client.GeomajasService|"
				+ "execute|org.geomajas.gwt.client.command.GwtCommand/4093389776|command.configuration.GetMap|"
				+ "org.geomajas.command.dto.GetMapConfigurationRequest/104733661|explorer|mainMap|"
				+ "ss.TqRPfHFh24NVxB|1|2|3|4|1|5|5|6|7|8|9|0|10|").getBytes("UTF-8"));
		request.addHeader("X-GWT-Permutation", "54044FB0C988344F1715C8B91330B0A2");
		request.addHeader("X-GWT-Module-Base", "http://test/module/");
		MockHttpServletResponse response = new MockHttpServletResponse();
		customController.setServletConfig(config);
		customController.doPost(request, response);
		// expect the message that the type is missing from our policy file
		Assert.assertTrue(response.getContentAsString().contains(
				"Type 'org.geomajas.gwt.client.command.GwtCommand' was not included in the set of types"));
	}

}
