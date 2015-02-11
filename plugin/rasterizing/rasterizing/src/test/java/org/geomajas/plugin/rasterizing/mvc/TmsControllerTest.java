package org.geomajas.plugin.rasterizing.mvc;

import java.io.OutputStream;

import org.geomajas.geometry.Coordinate;
import org.geomajas.security.SecurityManager;
import org.geomajas.spring.ThreadScopeContextHolder;
import org.geomajas.testdata.TestPathBinaryStreamAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/rasterizing/rasterizing-service.xml", "/org/geomajas/testdata/beanContext.xml",
		"/org/geomajas/testdata/layerBeans.xml", "/org/geomajas/testdata/layerBeansMultiLine.xml",
		"/org/geomajas/testdata/layerBeansMultiPolygon.xml", "/org/geomajas/testdata/layerBeansPoint.xml",
		"/org/geomajas/testdata/layerBluemarble.xml", "/org/geomajas/plugin/rasterizing/DefaultRasterizedPipelines.xml",
		"/META-INF/geomajasWebContextRasterizing.xml" })
public class TmsControllerTest {

	@Autowired
	private TmsController tmsController;

	@Qualifier("MvcTest.path")
	@Autowired
	private String imagePath;

	@Autowired
	private SecurityManager securityManager;

	private static final double DELTA = 0.07;
	
	// changing this to true and running the test from the base directory will generate the images !
	private boolean writeImages = false;
	
	@Before
	public void login() {
		// assure security context is set
		securityManager.createSecurityContext(null);
	}

	@After
	public void clearSecurityContext() {
		ThreadScopeContextHolder.clear();
	}

	@Test
	public void getVectorTile() throws Exception {
		MockHttpServletResponse response = new MockHttpServletResponse();
		tmsController.getVectorTile("layerBeansMultiPolygon", "layerBeansMultiPolygonStyleInfo", "EPSG:4326", 0, 0, 0,
				100.0 / 256.0, "-50,-50", 256, 256, true, false, null, response);
		response.flushBuffer();
		new ResponseAssert(response).assertEqualImage("layerBeansMultiPolygon-0-0-0.png", writeImages, DELTA);
	}

	private class ResponseAssert extends TestPathBinaryStreamAssert {

		private MockHttpServletResponse response;

		public ResponseAssert(MockHttpServletResponse response) {
			super(imagePath);
			this.response = response;
		}

		@Override
		public void generateActual(OutputStream out) throws Exception {
			out.write(response.getContentAsByteArray());
			out.flush();
		}

	}
}
