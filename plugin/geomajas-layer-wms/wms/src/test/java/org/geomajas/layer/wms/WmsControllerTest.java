/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.layer.wms;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Test for {@link WmsController}.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml", "/wmsContext.xml"})
public class WmsControllerTest {

	@Autowired
	private WmsController wmsController;

	@Test
	public void testDoSomething() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		wmsController.getWms();
		request.setRequestURI("d/wms/proxyblue/?SERVICE=WMS&layers=bluemarble&" +
				"WIDTH=512&HEIGHT=512&bbox=-52.01245495052001,-28.207099921352835,11.947593278789554," +
				"35.75294830795673&format=image/jpeg&version=1.1.1&srs=EPSG:4326&styles=&request=GetMap");
		request.setMethod("GET");
		final ModelAndView mav = handle(request, response);
		System.out.println(mav);
		//assertViewName(mav, "view");
		// assert something else
	}

}
