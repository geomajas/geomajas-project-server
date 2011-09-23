package org.geomajas.gwt.server.mvc;

import java.io.IOException;

import javax.servlet.ServletException;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/gwt/server/mvc/mockWebContext.xml", })
public class GeomajasControllerTest {

	@Autowired
	GeomajasController controller;

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
		MockHttpServletResponse response = new MockHttpServletResponse();
		controller.doPost(request, response);
	}
	
}
